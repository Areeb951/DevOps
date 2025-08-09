package com.leavemanagement.controller;

import com.leavemanagement.entity.LeaveBalance;
import com.leavemanagement.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveBalance>> getLeaveBalancesByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalancesByEmployee(employeeId));
    }

    @GetMapping("/employee/{employeeId}/type/{leaveTypeId}")
    public ResponseEntity<LeaveBalance> getLeaveBalanceByEmployeeAndType(
            @PathVariable Long employeeId, 
            @PathVariable Long leaveTypeId) {
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalanceByEmployeeAndType(employeeId, leaveTypeId));
    }

    @PostMapping("/reset-annual")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetAnnualLeaveBalances() {
        leaveBalanceService.resetAnnualLeaveBalances();
        return ResponseEntity.ok("Annual leave balances reset successfully");
    }
}
