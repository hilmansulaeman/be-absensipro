package com.juaracoding.dto.validasi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RegisDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[\\w\\s\\.]{20,100}$",
            message = "Format Alamat Tidak Valid , contoh : Jln. Kenari 2B jakbar 11480")
    private String alamat;

    @NotNull
    @NotBlank(message = "NAMA TIDAK BOLEH NULL !!")
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s\\.,]{3,50}$",
            message = "Format Nama Tidak Valid , contoh : Paul Christian")
    @JsonProperty("nama_lengkap")
    private String namaLengkap;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,11}$",
            message = "Format No HP Tidak Valid , min 9 max 11 setelah angka 8, contoh : (0/62/+62)81111111")
    @JsonProperty("no_hp")
    private String noHp;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$])[\\w].{8,16}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\") setelah 4 kondisi min 8 max 16 alfanumerik, contoh : aB4$12345")
    private String password;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^([a-z0-9]{8,25})$",
            message = "Format Huruf kecil dan numeric saja min 8 max 25 karakter, contoh : paulchihuy123")
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tanggalLahir;

    public @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com") String email) {
        this.email = email;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^[\\w\\s\\.]{20,100}$",
            message = "Format Alamat Tidak Valid , contoh : Jln. Kenari 2B jakbar 11480") String getAlamat() {
        return alamat;
    }

    public void setAlamat(@NotNull @NotBlank @NotEmpty @Pattern(regexp = "^[\\w\\s\\.]{20,100}$",
            message = "Format Alamat Tidak Valid , contoh : Jln. Kenari 2B jakbar 11480") String alamat) {
        this.alamat = alamat;
    }

    public @NotNull @NotBlank(message = "NAMA TIDAK BOLEH NULL !!") @NotEmpty @Pattern(regexp = "^[a-zA-Z\\s\\.,]{3,50}$",
            message = "Format Nama Tidak Valid , contoh : Paul Christian") String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(@NotNull @NotBlank(message = "NAMA TIDAK BOLEH NULL !!") @NotEmpty @Pattern(regexp = "^[a-zA-Z\\s\\.,]{3,50}$",
            message = "Format Nama Tidak Valid , contoh : Paul Christian") String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,11}$",
            message = "Format No HP Tidak Valid , min 9 max 11 setelah angka 8, contoh : (0/62/+62)81111111") String getNoHp() {
        return noHp;
    }

    public void setNoHp(@NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,11}$",
            message = "Format No HP Tidak Valid , min 9 max 11 setelah angka 8, contoh : (0/62/+62)81111111") String noHp) {
        this.noHp = noHp;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$])[\\w].{8,16}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\") setelah 4 kondisi min 8 max 16 alfanumerik, contoh : aB4$12345") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$])[\\w].{8,16}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\") setelah 4 kondisi min 8 max 16 alfanumerik, contoh : aB4$12345") String password) {
        this.password = password;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^([a-z0-9]{8,25})$",
            message = "Format Huruf kecil dan numeric saja min 8 max 25 karakter, contoh : paulchihuy123") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull @NotBlank @NotEmpty @Pattern(regexp = "^([a-z0-9]{8,25})$",
            message = "Format Huruf kecil dan numeric saja min 8 max 25 karakter, contoh : paulchihuy123") String username) {
        this.username = username;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}