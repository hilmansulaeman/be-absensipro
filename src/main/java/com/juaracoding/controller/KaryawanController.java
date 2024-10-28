package com.juaracoding.controller;

import com.juaracoding.dto.response.KaryawanResponseDTO;
import com.juaracoding.dto.validasi.KaryawanValidasiDTO;
import com.juaracoding.service.KaryawanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/karyawan")
public class KaryawanController {

    private final KaryawanService karyawanService;

    @Autowired
    public KaryawanController(KaryawanService karyawanService) {
        this.karyawanService = karyawanService;
    }

    @PostMapping
    public ResponseEntity<Object> createKaryawan(@RequestBody KaryawanValidasiDTO karyawanDTO, HttpServletRequest request) {
        return karyawanService.createKaryawan(karyawanDTO, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateKaryawan(@PathVariable Long id, @RequestBody KaryawanValidasiDTO karyawanDTO, HttpServletRequest request) {
        return karyawanService.updateKaryawan(id, karyawanDTO, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKaryawan(@PathVariable Long id, HttpServletRequest request) {
        return karyawanService.deleteKaryawan(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getKaryawanById(@PathVariable Long id, HttpServletRequest request) {
        return karyawanService.getKaryawanById(id, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllKaryawan(HttpServletRequest request) {
        return karyawanService.getAllKaryawan(request);
    }
}
