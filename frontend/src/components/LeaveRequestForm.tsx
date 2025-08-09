import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const LeaveRequestForm: React.FC = () => {
  const [formData, setFormData] = useState({
    startDate: '',
    endDate: '',
    leaveTypeId: '1',
    reason: '',
    isHalfDay: false
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.startDate || !formData.endDate || !formData.reason) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      setError('');
      setLoading(true);
      
      // In a real app, you'd get the employee ID from the authenticated user
      const requestData = {
        ...formData,
        employeeId: 1, // This should come from the authenticated user
        managerId: 1,  // This should be determined based on the employee's manager
        status: 'PENDING'
      };

      await axios.post('/api/leave-requests', requestData);
      
      setSuccess('Leave request submitted successfully!');
      setTimeout(() => {
        navigate('/leave-requests');
      }, 2000);
      
    } catch (error) {
      setError('Failed to submit leave request. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <h2>Submit Leave Request</h2>
      
      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="startDate">Start Date *</label>
          <input
            type="date"
            id="startDate"
            name="startDate"
            value={formData.startDate}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="endDate">End Date *</label>
          <input
            type="date"
            id="endDate"
            name="endDate"
            value={formData.endDate}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="leaveTypeId">Leave Type</label>
          <select
            id="leaveTypeId"
            name="leaveTypeId"
            value={formData.leaveTypeId}
            onChange={handleChange}
          >
            <option value="1">Annual Leave</option>
            <option value="2">Sick Leave</option>
            <option value="3">Personal Leave</option>
            <option value="4">Maternity/Paternity Leave</option>
          </select>
        </div>
        
        <div className="form-group">
          <label htmlFor="reason">Reason *</label>
          <textarea
            id="reason"
            name="reason"
            value={formData.reason}
            onChange={handleChange}
            rows={4}
            required
          />
        </div>
        
        <div className="form-group">
          <label>
            <input
              type="checkbox"
              name="isHalfDay"
              checked={formData.isHalfDay}
              onChange={handleChange}
            />
            Half Day Leave
          </label>
        </div>
        
        <div style={{ textAlign: 'center' }}>
          <button type="submit" disabled={loading} className="btn">
            {loading ? 'Submitting...' : 'Submit Request'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default LeaveRequestForm;
