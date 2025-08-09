package com.leavemanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Data
public class CreateLeaveRequestDto {
    @NotNull(message = "Leave type is required")
    private Long leaveTypeId;
    
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;
    
    @NotNull(message = "Reason is required")
    private String reason;
}
