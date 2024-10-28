package com.juaracoding.dto.response;

public class MenuResponseDTO {
    private Long idMenu;
    private String namaMenu;
    private String pathMenu;
    private String endPoint;
    private String menuHeaderName;

    // Getters and Setters
    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getPathMenu() {
        return null;
    }

    public void setPathMenu(String pathMenu) {
        this.pathMenu = pathMenu;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
    }

    public void setMenuHeaderName(String s) {
    }
}