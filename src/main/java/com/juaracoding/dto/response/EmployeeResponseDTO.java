package com.juaracoding.dto.response;

public class EmployeeResponseDTO {
    private long idEmployee;
    private String firstName;
    private String lastName;
    private String email;

    public EmployeeResponseDTO(long idEmployee, String firstName, String lastName, String email) {
        this.idEmployee = idEmployee;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public EmployeeResponseDTO() {

    }

    public long getIdEmployee() {
        return idEmployee;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
}
