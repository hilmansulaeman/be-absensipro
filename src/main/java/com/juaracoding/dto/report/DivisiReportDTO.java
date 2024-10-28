package com.juaracoding.dto.report;

import java.util.Date;

public class DivisiReportDTO {
    private Long idDivisi;
    private String namaDivisi;
    private String kodeDivisi;
    private String deskripsiDivisi;
    private Date createdDate;
    private Integer createdBy;
    private Date modifiedDate;
    private Integer modifiedBy;
    private Byte isDelete;

    // Getters and Setters
    public Long getIdDivisi() {
        return idDivisi;
    }

    public void setIdDivisi(Long idDivisi) {
        this.idDivisi = idDivisi;
    }

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
