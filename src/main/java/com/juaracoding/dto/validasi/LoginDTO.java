package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LoginDTO {

    @NotNull(message = "NULL!!!")
    @NotBlank(message = "BLANK!!!")
    @NotEmpty(message = "EMPTY!!!")
    private String username;

    @NotNull
    @NotBlank
    @NotEmpty
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
