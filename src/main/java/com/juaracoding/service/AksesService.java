package com.juaracoding.service;

import com.juaracoding.dto.validasi.AksesValidasiDTO;
import com.juaracoding.model.Akses;
import com.juaracoding.model.Divisi;
import com.juaracoding.repo.AksesRepository;
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
public class AksesService {

    private final AksesRepository aksesRepository;
    private final DivisiRepository divisiRepository;

    @Autowired
    public AksesService(AksesRepository aksesRepository, DivisiRepository divisiRepository) {
        this.aksesRepository = aksesRepository;
        this.divisiRepository = divisiRepository;
    }

    public ResponseEntity<Object> createAkses(AksesValidasiDTO aksesDTO, HttpServletRequest request) {
        System.out.println("Nama Akses: " + aksesDTO.getNamaAkses());
        System.out.println("Divisi ID: " + aksesDTO.getDivisiId());

        // Periksa apakah akses dengan nama yang sama sudah ada
        if (aksesRepository.findByNamaAkses(aksesDTO.getNamaAkses()).isPresent()) {
            return GlobalFunction.validasiGagal("Nama akses sudah ada", "AKSES_DUPLICATE", request);
        }

        // Periksa apakah divisi ID yang diberikan ada di database
        Optional<Divisi> divisiOptional = divisiRepository.findById(aksesDTO.getDivisiId());
        if (divisiOptional.isEmpty()) {
            System.out.println("Divisi ID tidak ditemukan di database.");
            return GlobalFunction.validasiGagal("Divisi tidak ditemukan", "DIVISI_NOT_FOUND", request);
        }

        // Buat entitas akses baru dan simpan ke database
        Akses akses = new Akses();
        akses.setNamaAkses(aksesDTO.getNamaAkses());
        akses.setDivisi(divisiOptional.get());

        aksesRepository.save(akses);
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    public ResponseEntity<Object> updateAkses(Long id, AksesValidasiDTO aksesDTO, HttpServletRequest request) {
        Optional<Akses> aksesOptional = aksesRepository.findById(id);
        if (aksesOptional.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        Akses akses = aksesOptional.get();
        akses.setNamaAkses(aksesDTO.getNamaAkses());

        // Update Divisi jika diberikan
        if (aksesDTO.getDivisiId() != null) {
            Optional<Divisi> divisiOptional = divisiRepository.findById(aksesDTO.getDivisiId());
            if (divisiOptional.isEmpty()) {
                System.out.println("Divisi ID tidak ditemukan di database.");
                return GlobalFunction.validasiGagal("Divisi tidak ditemukan", "DIVISI_NOT_FOUND", request);
            }
            akses.setDivisi(divisiOptional.get());
        }

        aksesRepository.save(akses);
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    public ResponseEntity<Object> deleteAkses(Long id, HttpServletRequest request) {
        Optional<Akses> aksesOptional = aksesRepository.findById(id);
        if (aksesOptional.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        aksesRepository.deleteById(id);
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    public ResponseEntity<Object> getAksesById(Long id, HttpServletRequest request) {
        Optional<Akses> akses = aksesRepository.findById(id);
        if (akses.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.dataByIdDitemukan(akses.get(), request);
    }

    public ResponseEntity<Object> getAllAkses(Pageable pageable, HttpServletRequest request) {
        Page<Akses> aksesPage = aksesRepository.findAll(pageable);
        if (aksesPage.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.customDataDitemukan("Akses data found", aksesPage.getContent(), request);
    }
}
