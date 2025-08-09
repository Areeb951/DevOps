package com.leavemanagement.service;

import com.leavemanagement.entity.LeaveRequest;
import com.leavemanagement.entity.LeaveBalance;
import com.leavemanagement.repository.LeaveRequestRepository;
import com.leavemanagement.repository.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }
    
    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id);
    }
    
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }
    
    public List<LeaveRequest> getLeaveRequestsByManagerId(Long managerId) {
        return leaveRequestRepository.findByManagerId(managerId);
    }
    
    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus(LeaveRequest.Status.PENDING);
    }
    
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        // Validate leave request
        validateLeaveRequest(leaveRequest);
        
        // Check leave balance
        checkLeaveBalance(leaveRequest);
        
        leaveRequest.setStatus(LeaveRequest.Status.PENDING);
        return leaveRequestRepository.save(leaveRequest);
    }
    
    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequestDetails) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        // Only allow updates if status is PENDING
        if (leaveRequest.getStatus() != LeaveRequest.Status.PENDING) {
            throw new RuntimeException("Cannot update leave request that is not pending");
        }
        
        leaveRequest.setStartDate(leaveRequestDetails.getStartDate());
        leaveRequest.setEndDate(leaveRequestDetails.getEndDate());
        leaveRequest.setReason(leaveRequestDetails.getReason());
        leaveRequest.setLeaveTypeId(leaveRequestDetails.getLeaveTypeId());
        
        return leaveRequestRepository.save(leaveRequest);
    }
    
    public LeaveRequest approveLeaveRequest(Long id, String managerComment) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        leaveRequest.setStatus(LeaveRequest.Status.APPROVED);
        leaveRequest.setManagerComment(managerComment);
        leaveRequest.setApprovedAt(java.time.LocalDateTime.now());
        
        // Deduct from leave balance
        deductLeaveBalance(leaveRequest);
        
        return leaveRequestRepository.save(leaveRequest);
    }
    
    public LeaveRequest rejectLeaveRequest(Long id, String managerComment) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        leaveRequest.setStatus(LeaveRequest.Status.REJECTED);
        leaveRequest.setManagerComment(managerComment);
        leaveRequest.setRejectedAt(java.time.LocalDateTime.now());
        
        return leaveRequestRepository.save(leaveRequest);
    }
    
    public void deleteLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        if (leaveRequest.getStatus() != LeaveRequest.Status.PENDING) {
            throw new RuntimeException("Cannot delete leave request that is not pending");
        }
        
        leaveRequestRepository.deleteById(id);
    }
    
    private void validateLeaveRequest(LeaveRequest leaveRequest) {
        if (leaveRequest.getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Start date cannot be in the past");
        }
        
        if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }
        
        if (leaveRequest.getStartDate().equals(leaveRequest.getEndDate()) && leaveRequest.getIsHalfDay()) {
            throw new RuntimeException("Half day leave cannot span multiple days");
        }
    }
    
    private void checkLeaveBalance(LeaveRequest leaveRequest) {
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeId(leaveRequest.getEmployeeId(), leaveRequest.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        
        long requestedDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
        if (leaveRequest.getIsHalfDay()) {
            requestedDays = 0.5;
        }
        
        if (balance.getAvailableDays() < requestedDays) {
            throw new RuntimeException("Insufficient leave balance");
        }
    }
    
    private void deductLeaveBalance(LeaveRequest leaveRequest) {
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeId(leaveRequest.getEmployeeId(), leaveRequest.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        
        long requestedDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
        if (leaveRequest.getIsHalfDay()) {
            requestedDays = 0.5;
        }
        
        balance.setAvailableDays(balance.getAvailableDays() - requestedDays);
        balance.setUsedDays(balance.getUsedDays() + requestedDays);
        
        leaveBalanceRepository.save(balance);
    }
}
