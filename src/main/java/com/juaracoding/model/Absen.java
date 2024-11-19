package com.juaracoding.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TrxAbsen")
public class Absen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAbsen")
    private Long idAbsen;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AbsenIn")
    private Date absenIn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AbsenOut")
    private Date absenOut;

    @ManyToOne
    @JoinColumn(name = "IDUser", referencedColumnName = "IDUser")
    private Userz userz;

    @Column(name = "IsDelete", nullable = false)
    private Byte isDelete = 0; // Default 0 untuk menandai absen aktif

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
