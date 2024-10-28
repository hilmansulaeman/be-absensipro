package com.juaracoding.controller;

import com.juaracoding.dto.validasi.EmployeeRequestDTO;
import com.juaracoding.dto.response.EmployeeResponseDTO;
import com.juaracoding.dto.report.EmployeeReportDTO;
import com.juaracoding.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO responseDTO = employeeService.createEmployee(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable long id) {
        EmployeeResponseDTO responseDTO = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<EmployeeReportDTO> generateEmployeeReport(@PathVariable long id) {
        EmployeeReportDTO reportDTO = employeeService.generateEmployeeReport(id);
        return ResponseEntity.ok(reportDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
