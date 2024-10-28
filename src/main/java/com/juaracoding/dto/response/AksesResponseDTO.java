package com.juaracoding.dto.response;

import java.util.Date;
import java.util.List;

public class AksesResponseDTO {
    private Long idAkses;
    private String namaAkses;
    private List<String> listMenuNames;
    private String divisiName;

    // Getters and Setters
    public Long getIdAkses() {
        return idAkses;
    }

    public void setIdAkses(Long idAkses) {
        this.idAkses = idAkses;
    }

    public String getNamaAkses() {
        return namaAkses;
    }

    public void setNamaAkses(String namaAkses) {
        this.namaAkses = namaAkses;
    }

    public List<String> getListMenuNames() {
        return listMenuNames;
    }

    public void setListMenuNames(List<String> listMenuNames) {
        this.listMenuNames = listMenuNames;
    }

    public String getDivisiName() {
        return divisiName;
    }

    public void setDivisiName(String divisiName) {
        this.divisiName = divisiName;
    }
}