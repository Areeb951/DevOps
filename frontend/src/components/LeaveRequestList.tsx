import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface LeaveRequest {
  id: number;
  startDate: string;
  endDate: string;
  reason: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  leaveTypeId: number;
  isHalfDay: boolean;
  managerComment?: string;
  createdAt: string;
}

const LeaveRequestList: React.FC = () => {
  const [leaveRequests, setLeaveRequests] = useState<LeaveRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchLeaveRequests();
  }, []);

  const fetchLeaveRequests = async () => {
    try {
      // In a real app, you'd fetch from your API
      // For now, we'll use mock data
      const mockData: LeaveRequest[] = [
        {
          id: 1,
          startDate: '2024-01-15',
          endDate: '2024-01-17',
          reason: 'Family vacation',
          status: 'APPROVED',
          leaveTypeId: 1,
          isHalfDay: false,
          managerComment: 'Approved - Enjoy your vacation!',
          createdAt: '2024-01-10T10:00:00Z'
        },
        {
          id: 2,
          startDate: '2024-01-20',
          endDate: '2024-01-20',
          reason: 'Doctor appointment',
          status: 'PENDING',
          leaveTypeId: 2,
          isHalfDay: true,
          createdAt: '2024-01-12T14:30:00Z'
        },
        {
          id: 3,
          startDate: '2024-01-25',
          endDate: '2024-01-26',
          reason: 'Personal matters',
          status: 'REJECTED',
          leaveTypeId: 3,
          isHalfDay: false,
          managerComment: 'Rejected - Critical project deadline',
          createdAt: '2024-01-14T09:15:00Z'
        }
      ];
      
      setLeaveRequests(mockData);
    } catch (error) {
      setError('Failed to fetch leave requests');
    } finally {
      setLoading(false);
    }
  };

  const getStatusClass = (status: string) => {
    switch (status) {
      case 'PENDING':
        return 'status-pending';
      case 'APPROVED':
        return 'status-approved';
      case 'REJECTED':
        return 'status-rejected';
      default:
        return '';
    }
  };

  const getLeaveTypeName = (typeId: number) => {
    switch (typeId) {
      case 1:
        return 'Annual Leave';
      case 2:
        return 'Sick Leave';
      case 3:
        return 'Personal Leave';
      case 4:
        return 'Maternity/Paternity Leave';
      default:
        return 'Unknown';
    }
  };

  if (loading) {
    return <div className="table-container">Loading...</div>;
  }

  if (error) {
    return <div className="table-container">
      <div className="error-message">{error}</div>
    </div>;
  }

  return (
    <div className="table-container">
      <h2>My Leave Requests</h2>
      
      {leaveRequests.length === 0 ? (
        <p>No leave requests found.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Type</th>
              <th>Reason</th>
              <th>Status</th>
              <th>Manager Comment</th>
              <th>Created</th>
            </tr>
          </thead>
          <tbody>
            {leaveRequests.map((request) => (
              <tr key={request.id}>
                <td>{new Date(request.startDate).toLocaleDateString()}</td>
                <td>{new Date(request.endDate).toLocaleDateString()}</td>
                <td>
                  {getLeaveTypeName(request.leaveTypeId)}
                  {request.isHalfDay && ' (Half Day)'}
                </td>
                <td>{request.reason}</td>
                <td className={getStatusClass(request.status)}>
                  {request.status}
                </td>
                <td>{request.managerComment || '-'}</td>
                <td>{new Date(request.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default LeaveRequestList;
