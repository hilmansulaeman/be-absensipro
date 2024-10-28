package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VerifikasiTokenDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$",message = "TOKEN TIDAK SESUAI")
    private String token;

    @NotNull
    @NotBlank
    @NotEmpty
    private String email;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
