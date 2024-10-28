package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.NotEmpty;

public class AksesValidasiDTO {

    @NotEmpty(message = "Nama akses cannot be empty.")
    private String namaAkses;

    private Long divisiId;

    // Getters and Setters
    public String getNamaAkses() {
        return namaAkses;
    }

    public void setNamaAkses(String namaAkses) {
        this.namaAkses = namaAkses;
    }

    public Long getDivisiId() {
        return divisiId;
    }

    public void setDivisiId(Long divisiId) {
        this.divisiId = divisiId;
    }
}
