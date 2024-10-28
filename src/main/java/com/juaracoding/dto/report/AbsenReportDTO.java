package com.juaracoding.dto.report;

import java.util.Date;

public class AbsenReportDTO {

    private Long idAbsen;
    private Date absenIn;
    private Date absenOut;
    private Long userId;
    private String userName;
    private Long totalHoursWorked; // Calculated as (absenOut - absenIn) if needed

    // Getters and Setters

    public Long getIdAbsen() {
        return idAbsen;
    }

    public void setIdAbsen(Long idAbsen) {
        this.idAbsen = idAbsen;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(Long totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }
}
