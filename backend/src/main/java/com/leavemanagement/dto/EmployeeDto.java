package com.leavemanagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String position;
    private LocalDate hireDate;
    private String managerName;
    private Boolean isActive;
}
