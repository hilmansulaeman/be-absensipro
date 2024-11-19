package com.juaracoding.controller;

import com.juaracoding.dto.response.UserResponseDTO;
import com.juaracoding.dto.validasi.UserValidasiDTO;
import com.juaracoding.dto.validasi.VerifikasiForgotPasswordDTO;
import com.juaracoding.model.Userz;
import com.juaracoding.service.UserService;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.MappingAttribute;
import com.juaracoding.handler.FormatValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/authz")
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MappingAttribute mappingAttribute = new MappingAttribute();
    private Map<String, Object> objectMapper = new HashMap<>();

    @Autowired
    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /*
     * Validasi Form Registrasi
     */
    @PostMapping("/v1/register")
    public String registerUser(@ModelAttribute("usr") @Valid UserValidasiDTO userValidasiDTO,
                               BindingResult bindingResult, Model model, WebRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usr", userValidasiDTO);
            return "auth/register";
        }

        if (!FormatValidation.phoneNumberFormatValidation(userValidasiDTO.getNoHP(), null)) {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_PHONE_NUMBER_FORMAT_INVALID);
            model.addAttribute("usr", userValidasiDTO);
            return "auth/register";
        }

        // Map dari DTO ke entity sebelum diteruskan ke userService
        Userz user = modelMapper.map(userValidasiDTO, Userz.class);
        Map<String, Object> response = userService.checkRegis(user, request);

        if (ConstantMessage.ERROR_FLOW_INVALID.equals(response.get("message"))) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) response.get("success")) {
            mappingAttribute.setAttribute(model, response);
            model.addAttribute("verifyEmail", userValidasiDTO.getEmail());
            model.addAttribute("usr", new UserValidasiDTO());
            return "auth/verifikasi";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, response.get("message").toString());
            model.addAttribute("usr", userValidasiDTO);
            return "auth/register";
        }
    }

    /*
     * Verifikasi Token untuk Pendaftaran
     */
    @PostMapping("/v1/verify")
    public String verifyRegistrationToken(@ModelAttribute("usr") @Valid UserValidasiDTO userValidasiDTO,
                                          BindingResult bindingResult, Model model,
                                          @RequestParam("email") String email, WebRequest request) {
        if (email.isEmpty() || !FormatValidation.emailFormatValidation(email, null)) {
            return "redirect:/api/check/logout";
        }

        String token = userValidasiDTO.getPassword(); // Asumsikan token disimpan di password
        if (token.isEmpty() || token.length() != 6) {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_INVALID);
            model.addAttribute("verifyEmail", email);
            return "auth/verifikasi";
        }

        // Map dari DTO ke entity sebelum diteruskan ke userService
        Userz user = modelMapper.map(userValidasiDTO, Userz.class);
        Map<String, Object> response = userService.confirmRegis(user, email, request);

        if (ConstantMessage.ERROR_FLOW_INVALID.equals(response.get("message"))) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) response.get("success")) {
            mappingAttribute.setErrorMessage(bindingResult, "REGISTRASI BERHASIL SILAHKAN LOGIN");
            model.addAttribute("usr", new UserValidasiDTO());
            return "auth/login";
        } else {
            model.addAttribute("verifyEmail", email);
            mappingAttribute.setErrorMessage(bindingResult, response.get("message").toString());
            return "auth/verifikasi";
        }
    }

    /*
     * Login User
     */
    @PostMapping("/v1/login")
    public String loginUser(@ModelAttribute("usr") @Valid UserValidasiDTO userValidasiDTO,
                            BindingResult bindingResult, Model model, WebRequest request) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        // Map dari DTO ke entity sebelum diteruskan ke userService
        Userz user = modelMapper.map(userValidasiDTO, Userz.class);
        Map<String, Object> response = userService.doLogin(user, request);

        if (!(Boolean) response.get("success")) {
            mappingAttribute.setErrorMessage(bindingResult, response.get("message").toString());
            return "auth/login";
        }

        Userz loggedInUser = (Userz) response.get("data");
        request.setAttribute("USR_ID", loggedInUser.getIdUser(), WebRequest.SCOPE_SESSION);
        request.setAttribute("EMAIL", loggedInUser.getEmail(), WebRequest.SCOPE_SESSION);
        mappingAttribute.setAttribute(model, response, request);

        return "admin/dashboard";
    }

    /*
     * Lupa Password - Kirim Email
     */
    @PostMapping("/v1/forgetpwd")
    public String forgotPassword(@ModelAttribute("forgetpwd") @Valid VerifikasiForgotPasswordDTO forgotPasswordDTO,
                                 BindingResult bindingResult, Model model, WebRequest request) {
        String email = forgotPasswordDTO.getEmail();
        if (email.isEmpty() || !FormatValidation.emailFormatValidation(email, null)) {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_EMAIL_FORMAT_INVALID);
            return "auth/forget_pwd_email";
        }

        Map<String, Object> response = userService.sendMailForgetPwd(email, request);
        if (ConstantMessage.ERROR_FLOW_INVALID.equals(response.get("message"))) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) response.get("success")) {
            model.addAttribute("forgetpwd", new VerifikasiForgotPasswordDTO());
            return "auth/forget_pwd_verifikasi";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, response.get("message").toString());
            return "auth/forget_pwd_email";
        }
    }

    /*
     * Verifikasi Token untuk Lupa Password
     */
    @PostMapping("/v1/vertokenfp")
    public String verifyForgotPasswordToken(@ModelAttribute("forgetpwd") @Valid VerifikasiForgotPasswordDTO forgotPasswordDTO,
                                            BindingResult bindingResult, Model model, WebRequest request) {
        String email = forgotPasswordDTO.getEmail();
        String token = forgotPasswordDTO.getToken();

        if (email.isEmpty() || !FormatValidation.emailFormatValidation(email, null) || token.isEmpty() || token.length() != 6) {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_INVALID);
            model.addAttribute("forgetpwd", forgotPasswordDTO);
            return "auth/forget_pwd_verifikasi";
        }

        Map<String, Object> response = userService.confirmTokenForgotPwd(forgotPasswordDTO, request);
        if (ConstantMessage.ERROR_FLOW_INVALID.equals(response.get("message"))) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) response.get("success")) {
            forgotPasswordDTO.setToken("");
            model.addAttribute("forgetpwd", forgotPasswordDTO);
            return "auth/forget_password";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, response.get("message").toString());
            return "auth/forget_pwd_verifikasi";
        }
    }

    /*
     * Reset Password
     */
    @PostMapping("/v1/cfpwd")
    public String resetPassword(@ModelAttribute("forgetpwd") @Valid VerifikasiForgotPasswordDTO forgotPasswordDTO,
                                BindingResult bindingResult, Model model, WebRequest request) {
        if (bindingResult.hasErrors()) {
            return "auth/forget_password";
        }

        String email = forgotPasswordDTO.getEmail();
        if (email.isEmpty() || !FormatValidation.emailFormatValidation(email, null)) {
            return "redirect:/api/check/logout";
        }

        Map<String, Object> response = userService.confirmPasswordChange(forgotPasswordDTO, request);
        if (ConstantMessage.ERROR_FLOW_INVALID.equals(response.get("message"))) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) response.get("success")) {
            model.addAttribute("usr", new UserValidasiDTO());
            return "auth/login";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, response.get("message").toString());
            return "auth/forget_password";
        }
    }

    /*
     * API Generate Token Baru untuk Lupa Password
     */
    @GetMapping("/v1/ntverfp")
    public String requestNewTokenForForgetPassword(@ModelAttribute("forgetPwd") @Valid VerifikasiForgotPasswordDTO forgetPasswordDTO,
                                                   BindingResult bindingResult, Model model,
                                                   @RequestParam("emailz") String email, WebRequest request) {
        forgetPasswordDTO.setToken(""); // Kosongkan token untuk menghapus inputan sebelumnya
        forgetPasswordDTO.setEmail(email);

        if (email == null || email.isEmpty() || !FormatValidation.emailFormatValidation(email, null)) {
            return "redirect:/api/check/logout";
        }

        objectMapper = userService.getNewToken(email, request);

        if (ConstantMessage.ERROR_FLOW_INVALID.equals(objectMapper.get("message"))) {
            return "redirect:/api/check/logout";
        }

        Boolean isSuccess = (Boolean) objectMapper.get("success");
        if (isSuccess) {
            model.addAttribute("forgetPwd", forgetPasswordDTO);
            mappingAttribute.setAttribute(model, objectMapper);
            return "auth/forget_pwd_verifikasi";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("forgetPwd", forgetPasswordDTO);
            return "auth/login";
        }
    }
}
