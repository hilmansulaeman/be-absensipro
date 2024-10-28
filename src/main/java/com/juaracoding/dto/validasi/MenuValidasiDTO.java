package com.juaracoding.dto.validasi;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MenuValidasiDTO {

    @NotEmpty(message = "Nama menu cannot be empty.")
    @Size(max = 25, message = "Nama menu must be at most 25 characters.")
    private String namaMenu;

    @NotEmpty(message = "Path menu cannot be empty.")
    @Size(max = 50, message = "Path menu must be at most 50 characters.")
    private String pathMenu;

    @NotEmpty(message = "End point cannot be empty.")
    @Size(max = 30, message = "End point must be at most 30 characters.")
    private String endPoint;

    private Long menuHeaderId;

    // Getters and Setters

    public @NotEmpty(message = "Nama menu cannot be empty.") @Size(max = 25, message = "Nama menu must be at most 25 characters.") String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(@NotEmpty(message = "Nama menu cannot be empty.") @Size(max = 25, message = "Nama menu must be at most 25 characters.") String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public @NotEmpty(message = "Path menu cannot be empty.") @Size(max = 50, message = "Path menu must be at most 50 characters.") String getPathMenu() {
        return pathMenu;
    }

    public void setPathMenu(@NotEmpty(message = "Path menu cannot be empty.") @Size(max = 50, message = "Path menu must be at most 50 characters.") String pathMenu) {
        this.pathMenu = pathMenu;
    }

    public @NotEmpty(message = "End point cannot be empty.") @Size(max = 30, message = "End point must be at most 30 characters.") String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(@NotEmpty(message = "End point cannot be empty.") @Size(max = 30, message = "End point must be at most 30 characters.") String endPoint) {
        this.endPoint = endPoint;
    }

    public Long getMenuHeaderId() {
        return menuHeaderId;
    }

    public void setMenuHeaderId(Long menuHeaderId) {
        this.menuHeaderId = menuHeaderId;
    }
}
