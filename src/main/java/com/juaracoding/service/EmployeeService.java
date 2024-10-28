package com.juaracoding.service;

import com.juaracoding.dto.validasi.EmployeeRequestDTO;
import com.juaracoding.dto.response.EmployeeResponseDTO;
import com.juaracoding.dto.report.EmployeeReportDTO;
import com.juaracoding.model.Employee;
import com.juaracoding.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee();
        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setEmail(requestDTO.getEmail());

        Employee savedEmployee = employeeRepository.save(employee);
        return new EmployeeResponseDTO(savedEmployee.getIdEmployee(), savedEmployee.getFirstName(), savedEmployee.getLastName(), savedEmployee.getEmail());
    }

    public EmployeeResponseDTO getEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return new EmployeeResponseDTO(employee.getIdEmployee(), employee.getFirstName(), employee.getLastName(), employee.getEmail());
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> new EmployeeResponseDTO(emp.getIdEmployee(), emp.getFirstName(), emp.getLastName(), emp.getEmail()))
                .collect(Collectors.toList());
    }

    public EmployeeReportDTO generateEmployeeReport(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        String fullName = employee.getFirstName() + " " + employee.getLastName();
        return new EmployeeReportDTO(employee.getIdEmployee(), fullName, employee.getEmail());
    }

    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
