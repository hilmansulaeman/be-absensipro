package com.juaracoding.dto.validasi;

import com.juaracoding.utils.ConstantMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

public class KaryawanValidasiDTO {

    @NotEmpty(message = ConstantMessage.ERROR_NAMALENGKAP_IS_EMPTY)
    @NotNull(message = ConstantMessage.ERROR_NAMALENGKAP_IS_NULL)
    private String namaLengkap;

    @Length(message = ConstantMessage.ERROR_GENDER_CONFIRM_LENGTH, max = 1)
    private String jenisKelamin;

    @Length(message = ConstantMessage.ERROR_MARTIALSTATUS_CONFIRM_LENGTH, max = 1)
    private String statusPernikahan;

    private Date tanggalLahir;

    @NotEmpty(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY)
    @Email(message = ConstantMessage.ERROR_EMAIL_INVALID)
    @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50)
    private String email;

    private String noHP;
    private String jabatan;
    private Date hireDate;

    // Getters and Setters

    public @NotEmpty(message = ConstantMessage.ERROR_NAMALENGKAP_IS_EMPTY) @NotNull(message = ConstantMessage.ERROR_NAMALENGKAP_IS_NULL) String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(@NotEmpty(message = ConstantMessage.ERROR_NAMALENGKAP_IS_EMPTY) @NotNull(message = ConstantMessage.ERROR_NAMALENGKAP_IS_NULL) String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public @Length(message = ConstantMessage.ERROR_GENDER_CONFIRM_LENGTH, max = 1) String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(@Length(message = ConstantMessage.ERROR_GENDER_CONFIRM_LENGTH, max = 1) String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public @Length(message = ConstantMessage.ERROR_MARTIALSTATUS_CONFIRM_LENGTH, max = 1) String getStatusPernikahan() {
        return statusPernikahan;
    }

    public void setStatusPernikahan(@Length(message = ConstantMessage.ERROR_MARTIALSTATUS_CONFIRM_LENGTH, max = 1) String statusPernikahan) {
        this.statusPernikahan = statusPernikahan;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public @NotEmpty(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY) @Email(message = ConstantMessage.ERROR_EMAIL_INVALID) @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50) String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY) @Email(message = ConstantMessage.ERROR_EMAIL_INVALID) @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50) String email) {
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
}
