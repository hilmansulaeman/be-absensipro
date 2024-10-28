package com.juaracoding.dto.report;

public class EmployeeReportDTO {
    private long idEmployee;
    private String fullName;
    private String email;

    public EmployeeReportDTO(long idEmployee, String fullName, String email) {
        this.idEmployee = idEmployee;
        this.fullName = fullName;
        this.email = email;
    }

    public long getIdEmployee() {
        return idEmployee;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }
}
