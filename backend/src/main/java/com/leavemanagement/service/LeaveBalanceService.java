package com.leavemanagement.service;

import com.leavemanagement.entity.LeaveBalance;
import com.leavemanagement.entity.LeaveType;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.repository.LeaveBalanceRepository;
import com.leavemanagement.repository.LeaveTypeRepository;
import com.leavemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;

    public List<LeaveBalance> getLeaveBalancesByEmployee(Long employeeId) {
        return leaveBalanceRepository.findByEmployeeId(employeeId);
    }

    public LeaveBalance getLeaveBalanceByEmployeeAndType(Long employeeId, Long leaveTypeId) {
        return leaveBalanceRepository.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
    }

    public void updateLeaveBalance(Long employeeId, Long leaveTypeId, int daysUsed) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId)
                .orElseGet(() -> createNewLeaveBalance(employeeId, leaveTypeId));
        
        balance.setDaysUsed(balance.getDaysUsed() + daysUsed);
        balance.setDaysRemaining(balance.getTotalDays() - balance.getDaysUsed());
        
        leaveBalanceRepository.save(balance);
    }

    public void resetAnnualLeaveBalances() {
        List<LeaveBalance> annualBalances = leaveBalanceRepository.findByLeaveTypeName("Annual Leave");
        
        for (LeaveBalance balance : annualBalances) {
            Employee employee = balance.getEmployee();
            int yearsOfService = Period.between(employee.getHireDate(), LocalDate.now()).getYears();
            
            // Calculate annual leave based on years of service
            int totalDays = calculateAnnualLeaveDays(yearsOfService);
            
            balance.setTotalDays(totalDays);
            balance.setDaysUsed(0);
            balance.setDaysRemaining(totalDays);
            
            leaveBalanceRepository.save(balance);
        }
    }

    private LeaveBalance createNewLeaveBalance(Long employeeId, Long leaveTypeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
        
        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setTotalDays(leaveType.getDefaultDays());
        balance.setDaysUsed(0);
        balance.setDaysRemaining(leaveType.getDefaultDays());
        
        return balance;
    }

    private int calculateAnnualLeaveDays(int yearsOfService) {
        if (yearsOfService < 1) return 20;
        if (yearsOfService < 3) return 22;
        if (yearsOfService < 5) return 25;
        if (yearsOfService < 10) return 28;
        return 30;
    }
}
