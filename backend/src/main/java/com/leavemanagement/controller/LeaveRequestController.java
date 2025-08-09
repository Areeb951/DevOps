package com.leavemanagement.controller;

import com.leavemanagement.entity.LeaveRequest;
import com.leavemanagement.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        return leaveRequestService.getLeaveRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByEmployeeId(employeeId));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByManagerId(@PathVariable Long managerId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByManagerId(managerId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequest>> getPendingLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getPendingLeaveRequests());
    }

    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(leaveRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequest leaveRequestDetails) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveRequest(id, leaveRequestDetails));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeaveRequest(@PathVariable Long id, @RequestParam String managerComment) {
        return ResponseEntity.ok(leaveRequestService.approveLeaveRequest(id, managerComment));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LeaveRequest> rejectLeaveRequest(@PathVariable Long id, @RequestParam String managerComment) {
        return ResponseEntity.ok(leaveRequestService.rejectLeaveRequest(id, managerComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
}
