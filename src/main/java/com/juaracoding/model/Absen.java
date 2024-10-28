package com.juaracoding.model;

import jakarta.persistence.*;

import java.util.*;

/*
    KODE MODUL 03
 */
@Entity
@Table(name = "TrxAbsen")
public class Absen {

    @Id
    @Column(name = "IDAbsen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbsen;

    @Column(name = "AbsenIn")
    private Date absenIn;
    @Column(name = "AbsenOut")
    private Date absenOut;

    @ManyToOne
    @JoinColumn(name = "IDUser")
    private Userz userz;

    @Column(name = "IsDelete", nullable = false)
    private Byte isDelete = 1;//khusus disini default 0 karena setelah verifikasi baru di update menjadi 1


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

    public Userz getUserz() {
        return userz;
    }

    public void setUserz(Userz userz) {
        this.userz = userz;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
