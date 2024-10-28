package com.juaracoding.service;

import com.juaracoding.dto.validasi.DivisiValidasiDTO;
import com.juaracoding.model.Divisi;
import com.juaracoding.repo.DivisiRepository;
import com.juaracoding.utils.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DivisiService {

    private final DivisiRepository divisiRepository;

    @Autowired
    public DivisiService(DivisiRepository divisiRepository) {
        this.divisiRepository = divisiRepository;
    }

    public ResponseEntity<Object> createDivisi(DivisiValidasiDTO divisiDTO, HttpServletRequest request) {
        if (divisiRepository.existsByNamaDivisi(divisiDTO.getNamaDivisi())) {
            return GlobalFunction.validasiGagal("Nama divisi sudah ada", "DIVISI_DUPLICATE", request);
        }

        if (divisiRepository.existsByKodeDivisi(divisiDTO.getKodeDivisi())) {
            return GlobalFunction.validasiGagal("Kode divisi sudah ada", "KODE_DUPLICATE", request);
        }

        Divisi divisi = new Divisi();
        divisi.setNamaDivisi(divisiDTO.getNamaDivisi());
        divisi.setKodeDivisi(divisiDTO.getKodeDivisi());
        divisi.setDeskripsiDivisi(divisiDTO.getDeskripsiDivisi());

        divisiRepository.save(divisi);
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    public ResponseEntity<Object> updateDivisi(Long id, DivisiValidasiDTO divisiDTO, HttpServletRequest request) {
        Optional<Divisi> divisiOptional = divisiRepository.findById(id);
        if (divisiOptional.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        Divisi divisi = divisiOptional.get();
        divisi.setNamaDivisi(divisiDTO.getNamaDivisi());
        divisi.setKodeDivisi(divisiDTO.getKodeDivisi());
        divisi.setDeskripsiDivisi(divisiDTO.getDeskripsiDivisi());

        divisiRepository.save(divisi);
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    public ResponseEntity<Object> deleteDivisi(Long id, HttpServletRequest request) {
        if (!divisiRepository.existsById(id)) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        divisiRepository.deleteById(id);
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    public ResponseEntity<Object> getDivisiById(Long id, HttpServletRequest request) {
        Optional<Divisi> divisi = divisiRepository.findById(id);
        if (divisi.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.dataByIdDitemukan(divisi.get(), request);
    }

    public ResponseEntity<Object> getAllDivisi(Pageable pageable, HttpServletRequest request) {
        Page<Divisi> divisiPage = divisiRepository.findAll(pageable);
        if (divisiPage.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.customDataDitemukan("Data divisi ditemukan", divisiPage.getContent(), request);
    }
}
