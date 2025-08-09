package com.leavemanagement.repository;

import com.leavemanagement.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByManagerId(Long managerId);
    List<LeaveRequest> findByStatus(LeaveRequest.Status status);
    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveRequest.Status status);
    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByEmployeeIdAndStartDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
