package com.juaracoding.controller;

import com.juaracoding.dto.validasi.AksesValidasiDTO;
import com.juaracoding.service.AksesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/akses")
public class AksesController {

    private final AksesService aksesService;

    @Autowired
    public AksesController(AksesService aksesService) {
        this.aksesService = aksesService;
    }

    @PostMapping
    public ResponseEntity<Object> createAkses(@Valid @RequestBody AksesValidasiDTO aksesDTO, HttpServletRequest request) {
        return aksesService.createAkses(aksesDTO, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAkses(@PathVariable Long id, @Valid @RequestBody AksesValidasiDTO aksesDTO, HttpServletRequest request) {
        return aksesService.updateAkses(id, aksesDTO, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAkses(@PathVariable Long id, HttpServletRequest request) {
        return aksesService.deleteAkses(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAksesById(@PathVariable Long id, HttpServletRequest request) {
        return aksesService.getAksesById(id, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllAkses(Pageable pageable, HttpServletRequest request) {
        return aksesService.getAllAkses(pageable, request);
    }
}
