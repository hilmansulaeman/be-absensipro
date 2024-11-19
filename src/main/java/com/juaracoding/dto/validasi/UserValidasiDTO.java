package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserValidasiDTO {

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotEmpty(message = "Username cannot be empty.")
    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    private String password;

    @Pattern(regexp = "^\\+?\\d{10,13}$", message = "Invalid phone number.")
    private String noHP;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    @NotNull(message = "Akses is required.")
    private AksesValidasiDTO akses;

    // Getter and Setter for Akses
    public AksesValidasiDTO getAkses() {
        return akses;
    }

    public void setAkses(AksesValidasiDTO akses) {
        this.akses = akses;
    }

    public void setIdUser(Long id) {

    }
}
