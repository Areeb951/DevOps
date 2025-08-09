import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';
import './Dashboard.css';

interface LeaveBalance {
  id: number;
  leaveType: {
    name: string;
    description: string;
  };
  totalDays: number;
  daysUsed: number;
  daysRemaining: number;
}

interface LeaveRequest {
  id: number;
  startDate: string;
  endDate: string;
  reason: string;
  status: string;
  leaveType: {
    name: string;
  };
}

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [leaveBalances, setLeaveBalances] = useState<LeaveBalance[]>([]);
  const [recentRequests, setRecentRequests] = useState<LeaveRequest[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [balancesResponse, requestsResponse] = await Promise.all([
          axios.get('/api/leave-balances/employee/1'), // Replace with actual employee ID
          axios.get('/api/leave-requests/employee/1')  // Replace with actual employee ID
        ]);
        
        setLeaveBalances(balancesResponse.data);
        setRecentRequests(requestsResponse.data.slice(0, 5)); // Show only recent 5
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return <div className="dashboard-loading">Loading dashboard...</div>;
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>Welcome back, {user?.firstName}!</h1>
        <p>Here's your leave management overview</p>
      </div>

      <div className="dashboard-grid">
        <div className="dashboard-card leave-balances">
          <h2>Leave Balances</h2>
          <div className="balance-list">
            {leaveBalances.map((balance) => (
              <div key={balance.id} className="balance-item">
                <div className="balance-type">{balance.leaveType.name}</div>
                <div className="balance-details">
                  <span className="remaining">{balance.daysRemaining} days remaining</span>
                  <span className="used">{balance.daysUsed} days used</span>
                </div>
                <div className="balance-bar">
                  <div 
                    className="balance-progress" 
                    style={{ 
                      width: `${(balance.daysUsed / balance.totalDays) * 100}%` 
                    }}
                  ></div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="dashboard-card quick-actions">
          <h2>Quick Actions</h2>
          <div className="action-buttons">
            <button className="btn btn-primary">Request Leave</button>
            <button className="btn btn-secondary">View Requests</button>
            <button className="btn btn-outline">Update Profile</button>
          </div>
        </div>

        <div className="dashboard-card recent-requests">
          <h2>Recent Leave Requests</h2>
          {recentRequests.length > 0 ? (
            <div className="request-list">
              {recentRequests.map((request) => (
                <div key={request.id} className="request-item">
                  <div className="request-type">{request.leaveType.name}</div>
                  <div className="request-dates">
                    {new Date(request.startDate).toLocaleDateString()} - {new Date(request.endDate).toLocaleDateString()}
                  </div>
                  <div className={`request-status status-${request.status.toLowerCase()}`}>
                    {request.status}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="no-requests">No recent leave requests</p>
          )}
        </div>

        <div className="dashboard-card stats">
          <h2>Statistics</h2>
          <div className="stats-grid">
            <div className="stat-item">
              <div className="stat-number">{leaveBalances.reduce((sum, b) => sum + b.daysRemaining, 0)}</div>
              <div className="stat-label">Total Days Available</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">{recentRequests.filter(r => r.status === 'APPROVED').length}</div>
              <div className="stat-label">Approved Requests</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">{recentRequests.filter(r => r.status === 'PENDING').length}</div>
              <div className="stat-label">Pending Requests</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
