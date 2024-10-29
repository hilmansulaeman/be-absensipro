package com.juaracoding.controller;

import com.juaracoding.dto.validasi.UserValidasiDTO;
import com.juaracoding.dto.validasi.VerifikasiForgotPasswordDTO;
import com.juaracoding.model.Userz;
import com.juaracoding.security.JwtUtility;
import com.juaracoding.service.UserService;
import com.juaracoding.utils.GlobalFunction;
import com.juaracoding.utils.ConstantMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtility jwtUtility) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtility = jwtUtility;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserValidasiDTO userDTO, HttpServletRequest request) {
        if (userService.existsByEmail(userDTO.getEmail())) {
            return GlobalFunction.validasiGagal(ConstantMessage.ERROR_EMAIL_ISEXIST, "USER_ALREADY_EXISTS", request);
        }
        if (userService.existsByUsername(userDTO.getUsername())) {
            return GlobalFunction.validasiGagal(ConstantMessage.ERROR_USERNAME_ISEXIST, "USER_ALREADY_EXISTS", request);
        }

        Userz user = new Userz();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setNoHP(userDTO.getNoHP());
        userService.save(user, request);

        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserValidasiDTO userDTO, HttpServletRequest request) {
        Optional<Userz> userOptional = userService.findByEmail(userDTO.getEmail());

        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(userDTO.getPassword(), userOptional.get().getPassword())) {
            return GlobalFunction.validasiGagal(ConstantMessage.ERROR_LOGIN_FAILED, "LOGIN_FAILED", request);
        }

        Userz user = userOptional.get();
        String token = jwtUtility.generateToken(user);

        return GlobalFunction.customDataDitemukan(ConstantMessage.SUCCESS_LOGIN, token, request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email, HttpServletRequest request) {
        Optional<Userz> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        String token = jwtUtility.generateTokenForPasswordReset(userOptional.get());
        return GlobalFunction.customDataDitemukan(ConstantMessage.SUCCESS_SEND_EMAIL, token, request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody VerifikasiForgotPasswordDTO resetDTO, HttpServletRequest request) {
        Optional<Userz> userOptional = userService.findByEmail(resetDTO.getEmail());

        if (userOptional.isEmpty() || !jwtUtility.validatePasswordResetToken(resetDTO.getToken(), resetDTO.getEmail())) {
            return GlobalFunction.validasiGagal(ConstantMessage.ERROR_TOKEN_INVALID, "TOKEN_INVALID", request);
        }

        Userz user = userOptional.get();

        if (resetDTO.getPasswordBaru().equals(resetDTO.getPassword())) {
            return GlobalFunction.validasiGagal(ConstantMessage.ERROR_PASSWORD_IS_SAME, "PASSWORD_SAME", request);
        }

        user.setPassword(passwordEncoder.encode(resetDTO.getPasswordBaru()));
        userService.update(user.getIdUser(), user, request);

        return GlobalFunction.dataBerhasilDiubah(request);
    }
}
