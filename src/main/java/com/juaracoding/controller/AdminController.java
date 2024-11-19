package com.juaracoding.controller;

import com.juaracoding.dto.response.UserResponseDTO;
import com.juaracoding.dto.validasi.UserValidasiDTO;
import com.juaracoding.model.Userz;
import com.juaracoding.service.UserService;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.GenerateMenuString;
import com.juaracoding.utils.MappingAttribute;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MappingAttribute mappingAttribute;
    private final ModelMapper modelMapper;
    private final String[] strExceptionArr = new String[]{"UserController"};

    private final Map<String, Object> objectMapper = new HashMap<>();

    @Autowired
    public AdminController(UserService userService, MappingAttribute mappingAttribute, ModelMapper modelMapper) {
        this.userService = userService;
        this.mappingAttribute = mappingAttribute;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/v1/dashboard")
    public String login(@ModelAttribute("usr") @Valid UserValidasiDTO userValidasiDTO,
                        BindingResult bindingResult,
                        Model model,
                        WebRequest request) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("usr", new UserValidasiDTO());
            return "auth/login";
        }

        // Konversi UserValidasiDTO ke entitas Userz
        Userz userz = modelMapper.map(userValidasiDTO, Userz.class);

        // Panggil metode login di UserService
        objectMapper.clear();
        objectMapper.putAll(userService.doLogin(userz, request));

        Boolean isSuccess = (Boolean) objectMapper.get("success");
        Object userData = objectMapper.get("data");

        if (userData == null) {
            mappingAttribute.setErrorMessage(bindingResult, (String) objectMapper.get("message"));
            model.addAttribute("usr", new UserValidasiDTO());
            return "auth/login";
        }

        if (isSuccess) {
            Userz nextUser = (Userz) userData;

            // Set session attributes
            setSessionAttributes(request, nextUser);

            // Map attributes to model
            mappingAttribute.setAttribute(model, objectMapper, request);
            return "admin/dashboard";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, (String) objectMapper.get("message"));
            return "auth/login";
        }
    }

    private void setSessionAttributes(WebRequest request, Userz user) {
        request.setAttribute("USR_ID", user.getIdUser(), WebRequest.SCOPE_SESSION);
        request.setAttribute("EMAIL", user.getEmail(), WebRequest.SCOPE_SESSION);
        request.setAttribute("NO_HP", user.getNoHP(), WebRequest.SCOPE_SESSION);
        request.setAttribute("USR_NAME", user.getUsername(), WebRequest.SCOPE_SESSION);
        request.setAttribute("HTML_MENU", new GenerateMenuString().menuInnerHtml(user.getAkses()), WebRequest.SCOPE_SESSION);
    }

    @GetMapping("/form")
    public String form(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model, objectMapper, request);
        return "admin/form-elements";
    }

    @GetMapping("/tabel")
    public String tabel() {
        return "admin/table-elements";
    }

    @GetMapping("/karyawan")
    public String karyawan() {
        return "admin/master-karyawan";
    }

    @GetMapping("/absen")
    public String absen() {
        return "admin/master-absen";
    }

    @GetMapping("/report")
    public String report(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model, objectMapper, request);
        return "admin/report";
    }

    @GetMapping("/student")
    public String student(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model, objectMapper, request);
        return "admin/student";
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
}
