import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navigation: React.FC = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!isAuthenticated) {
    return null;
  }

  return (
    <nav className="nav">
      <ul>
        <li><Link to="/">Dashboard</Link></li>
        <li><Link to="/leave-request">Request Leave</Link></li>
        <li><Link to="/leave-requests">My Leave Requests</Link></li>
        <div className="user-info">
          <span>Welcome, {user?.firstName || user?.username}</span>
          <button onClick={handleLogout} className="btn btn-secondary">Logout</button>
        </div>
      </ul>
    </nav>
  );
};

export default Navigation;
