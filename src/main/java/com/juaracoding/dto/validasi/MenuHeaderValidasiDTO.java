package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.NotEmpty;

public class MenuHeaderValidasiDTO {

    @NotEmpty(message = "Nama menu header cannot be empty.")
    private String namaMenuHeader;

    private String deskripsiMenuHeader;

    // Getters and Setters
    public String getNamaMenuHeader() {
        return namaMenuHeader;
    }

    public void setNamaMenuHeader(String namaMenuHeader) {
        this.namaMenuHeader = namaMenuHeader;
    }

    public String getDeskripsiMenuHeader() {
        return deskripsiMenuHeader;
    }

    public void setDeskripsiMenuHeader(String deskripsiMenuHeader) {
        this.deskripsiMenuHeader = deskripsiMenuHeader;
    }
}
