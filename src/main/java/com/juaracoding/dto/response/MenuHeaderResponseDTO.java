package com.juaracoding.dto.response;

public class MenuHeaderResponseDTO {
    private Long idMenuHeader;
    private String namaMenuHeader;
    private String deskripsiMenuHeader;

    // Getters and Setters
    public Long getIdMenuHeader() {
        return idMenuHeader;
    }

    public void setIdMenuHeader(Long idMenuHeader) {
        this.idMenuHeader = idMenuHeader;
    }

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
