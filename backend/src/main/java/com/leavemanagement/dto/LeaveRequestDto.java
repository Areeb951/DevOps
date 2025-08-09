package com.leavemanagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private Long id;
    private Long employeeId;
    private Long leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    private String employeeName;
    private String leaveTypeName;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
