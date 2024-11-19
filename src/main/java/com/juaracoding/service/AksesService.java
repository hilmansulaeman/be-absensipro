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
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
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

    // Method to save a new Akses
    public Map<String, Object> saveAkses(Akses akses, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Akses savedAkses = aksesRepository.save(akses);
            response.put("success", true);
            response.put("idDataSave", savedAkses.getIdAkses());
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save Akses");
            return response;
        }
    }

    // Method to update an existing Akses
    public Map<String, Object> updateAkses(Long id, Akses akses, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Akses> existingAkses = aksesRepository.findById(id);
            if (existingAkses.isPresent()) {
                Akses updatedAkses = existingAkses.get();
                updatedAkses.setNamaAkses(akses.getNamaAkses());
                updatedAkses.setDivisi(akses.getDivisi()); // assuming the divisi relationship exists
                aksesRepository.save(updatedAkses);
                response.put("success", true);
                return response;
            } else {
                response.put("success", false);
                response.put("message", "Akses not found");
                return response;
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update Akses");
            return response;
        }
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

    public Map<String, Object> findAllAkses(Pageable pageable, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<Akses> aksesPage = aksesRepository.findAll(pageable);
            if (aksesPage.isEmpty()) {
                response.put("message", "Data kosong");
                response.put("success", false);
                return response;
            }
            response.put("data", aksesPage.getContent());
            response.put("success", true);
            return response;
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan");
            response.put("success", false);
            return response;
        }
    }

    public Map<String, ?> findById(Long id, WebRequest request) {
        return null;
    }
}
