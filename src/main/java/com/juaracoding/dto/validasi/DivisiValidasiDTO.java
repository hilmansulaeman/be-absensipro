package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.NotEmpty;

public class DivisiValidasiDTO {

    @NotEmpty(message = "Nama divisi tidak boleh kosong.")
    private String namaDivisi;

    @NotEmpty(message = "Kode divisi tidak boleh kosong.")
    private String kodeDivisi;

    private String deskripsiDivisi;

    // Getters and Setters
    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }

    public String getKodeDivisi() {
        return kodeDivisi;
    }

    public void setKodeDivisi(String kodeDivisi) {
        this.kodeDivisi = kodeDivisi;
    }

    public String getDeskripsiDivisi() {
        return deskripsiDivisi;
    }

    public void setDeskripsiDivisi(String deskripsiDivisi) {
        this.deskripsiDivisi = deskripsiDivisi;
    }
}
