package com.juaracoding.dto.validasi;

import java.util.Date;

public class AbsenValidationDTO {private Long userId;
    private Date absenIn;
    private Date absenOut;

    // Getters and Setter


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getAbsenIn() {
        return absenIn;
    }

    public void setAbsenIn(Date absenIn) {
        this.absenIn = absenIn;
    }

    public Date getAbsenOut() {
        return absenOut;
    }

    public void setAbsenOut(Date absenOut) {
        this.absenOut = absenOut;
    }
}
