package com.juaracoding.controller;

import com.juaracoding.dto.response.AbsenResponseDTO;
import com.juaracoding.dto.validasi.AbsenValidationDTO;
import com.juaracoding.dto.report.AbsenReportDTO;
import com.juaracoding.service.AbsenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absen")
public class AbsenController {

    private final AbsenService absenService;

    @Autowired
    public AbsenController(AbsenService absenService) {
        this.absenService = absenService;
    }

    @PostMapping("/save")
    public ResponseEntity<AbsenResponseDTO> saveAbsen(@RequestBody AbsenValidationDTO absenValidationDTO) {
        AbsenResponseDTO responseDTO = absenService.saveAbsen(absenValidationDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AbsenResponseDTO>> getAllAbsens() {
        List<AbsenResponseDTO> allAbsens = absenService.getAllAbsens();
        return ResponseEntity.ok(allAbsens);
    }

    @GetMapping("/report/{userId}")
    public ResponseEntity<List<AbsenReportDTO>> getReportByUserId(@PathVariable Long userId) {
        List<AbsenReportDTO> report = absenService.getReportByUserId(userId);
        return ResponseEntity.ok(report);
    }
}
