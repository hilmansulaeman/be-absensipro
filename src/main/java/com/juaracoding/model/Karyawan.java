package com.juaracoding.model;

import com.juaracoding.utils.ConstantMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MstKaryawan")
public class Karyawan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDKaryawan", nullable = false)
    private Long idKaryawan;

    @NotEmpty(message = ConstantMessage.ERROR_NAMALENGKAP_IS_EMPTY)
    @NotNull(message = ConstantMessage.ERROR_NAMALENGKAP_IS_NULL)
    @Column(name = "NamaLengkap", length = 50)
    private String namaLengkap;

    @Length(message = ConstantMessage.ERROR_GENDER_CONFIRM_LENGTH, max = 1)
    @Column(name = "JenisKelamin")
    private String jenisKelamin;

    @Length(message = ConstantMessage.ERROR_MARTIALSTATUS_CONFIRM_LENGTH, max = 1)
    @Column(name = "StatusPernikahan", length = 1)
    private String statusPernikahan;

    @Column(name = "TanggalLahir")
    private Date tanggalLahir;

    @NotEmpty(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50)
    @NotNull(message = ConstantMessage.ERROR_EMAIL_IS_NULL)
    @Column(name = "Email", unique = true, length = 50)
    private String email;

    @Column(name = "NomorHandPhone")
    private String noHP;

    @ManyToMany
    @JoinTable(name = "NamaDivisi",
            joinColumns = {@JoinColumn(name = "IDDivisi", referencedColumnName = "IDKaryawan")})
    private List<Karyawan> karyawanList;

    @Column(name = "Jabatan")
    private String jabatan;

    @Column(name = "HireDate", nullable = false)
    private Date hireDate;

    @Column(name = "CurrentFlag")
    private Byte isCurrentFlag = 0;

    /*
       start audit trails
    */
    @Column(name = "CreatedDate", nullable = false)
    private Date createdDate = new Date();

    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy = 1;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;
    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "IsDelete", nullable = false)
    private Byte isDelete = 0;//khusus disini default 0 karena setelah verifikasi baru di update menjadi 1
    /*
        end audit trails
     */

    public Long getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(Long idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

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

    public @NotEmpty(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY) @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50) @NotNull(message = ConstantMessage.ERROR_EMAIL_IS_NULL) String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY) @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50) @NotNull(message = ConstantMessage.ERROR_EMAIL_IS_NULL) String email) {
        this.email = email;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public List<Karyawan> getKaryawanList() {
        return karyawanList;
    }

    public void setKaryawanList(List<Karyawan> karyawanList) {
        this.karyawanList = karyawanList;
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