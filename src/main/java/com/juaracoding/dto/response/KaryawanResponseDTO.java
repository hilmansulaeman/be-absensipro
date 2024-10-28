package com.juaracoding.dto.response;

import java.util.Date;

public class KaryawanResponseDTO {
    private Long idKaryawan;
    private String namaLengkap;
    private String jenisKelamin;
    private String statusPernikahan;
    private Date tanggalLahir;
    private String email;
    private String noHP;
    private String jabatan;
    private Date hireDate;
    private Byte isCurrentFlag;

    // Getters and Setters
    public Long getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(Long idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getStatusPernikahan() {
        return statusPernikahan;
    }

    public void setStatusPernikahan(String statusPernikahan) {
        this.statusPernikahan = statusPernikahan;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Byte getIsCurrentFlag() {
        return isCurrentFlag;
    }

    public void setIsCurrentFlag(Byte isCurrentFlag) {
        this.isCurrentFlag = isCurrentFlag;
    }
}
