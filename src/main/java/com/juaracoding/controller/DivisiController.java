package com.juaracoding.controller;

import com.juaracoding.dto.response.DivisiResponseDTO;
import com.juaracoding.dto.validasi.DivisiValidasiDTO;
import com.juaracoding.service.DivisiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/divisi")
public class DivisiController {

    private final DivisiService divisiService;

    @Autowired
    public DivisiController(DivisiService divisiService) {
        this.divisiService = divisiService;
    }

    @PostMapping
    public ResponseEntity<Object> createDivisi(@RequestBody DivisiValidasiDTO divisiDTO, HttpServletRequest request) {
        return divisiService.createDivisi(divisiDTO, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDivisi(@PathVariable Long id, @RequestBody DivisiValidasiDTO divisiDTO, HttpServletRequest request) {
        return divisiService.updateDivisi(id, divisiDTO, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDivisi(@PathVariable Long id, HttpServletRequest request) {
        return divisiService.deleteDivisi(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDivisiById(@PathVariable Long id, HttpServletRequest request) {
        return divisiService.getDivisiById(id, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllDivisi(Pageable pageable, HttpServletRequest request) {
        return divisiService.getAllDivisi(pageable, request);
    }
}
