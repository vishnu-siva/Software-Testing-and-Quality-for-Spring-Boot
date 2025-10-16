import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI } from '../utils/api';
import '../styles/Dashboard.css';

const Dashboard = ({ userId, onLogout }) => {
  const navigate = useNavigate();
  const [counts, setCounts] = useState({
    userDetails: 0,
    bmiResults: 0,
    workoutPlans: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (userId) {
      fetchCounts();
    }
  }, [userId]);

  const fetchCounts = async () => {
    try {
      setLoading(true);
      const response = await userAPI.getDashboardCounts(userId);
      setCounts(response);
    } catch (error) {
      console.error('Error fetching counts:', error);
      setError('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    onLogout();
    navigate('/');
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  if (loading) {
    return (
      <div className="dashboard-page">
        <div className="loading">Loading dashboard...</div>
      </div>
    );
  }

  return (
    <div className="dashboard-page fade-in">
      <div className="sidebar slide-in-left">
        <nav className="sidebar-nav">
          <div className="nav-item active">Dash Board</div>
          <div 
            className="nav-item" 
            onClick={() => handleNavigation('/user-details')}
          >
            User Details
          </div>
          <div 
            className="nav-item" 
            onClick={() => handleNavigation('/workout-plan')}
          >
            Work Out Plan
          </div>
          <div 
            className="nav-item" 
            onClick={() => handleNavigation('/helping-tools')}
          >
            Helping tools
          </div>
          <div 
           className="nav-item logout" onClick={handleLogout}>
            Log Out
          </div>
        </nav>
      </div>

      
      <div className="main-content">
        {error && <div className="error-message">{error}</div>}
        
        <div className="dashboard-header">
          <h1>Fiteasy Usage Overview</h1>
        </div>

        <div className="dashboardd">
          <div className="recent-activity">
          <h3>Welcome to FitEasy!</h3>
          <p>Start your fitness journey by:</p>
          <ul>
            <li>Completing your profile in User Details</li>
            <li>Calculating your BMI in Workout Plan</li>
            <li>Adding helpful fitness resources in Helping Tools</li>
          </ul>
        </div>

        <div className="dashboard-cards">
          <div 
            className="dashboard-card"
            onClick={() => handleNavigation('/user-details')}
          >
            <div className="card-content">
              <h3>USER DETAILS</h3>
            </div>
            <div className="card-count">
              <span>added {counts.userDetails > 0 ? '9th' : 'no'}</span>
              <span>details</span>
            </div>
          </div>

          
          <div 
            className="dashboard-card"
            onClick={() => handleNavigation('/workout-plan')}
          >
            <div className="card-content">
              <h3>BMI</h3>
            </div>
            <div className="card-count">
              <span>{counts.bmiResults}</span>
              <span>results</span>
            </div>
          </div>

         
          <div 
            className="dashboard-card"
            onClick={() => handleNavigation('/workout-plan')}
          >
            <div className="card-content">
              <h3>WORK OUT</h3>
            </div>
            <div className="card-count">
              <span>{counts.workoutPlans} plans</span>
            </div>
          </div>
        </div>

        
        <div className="quick-actions">
          <h3>Quick Actions</h3>
          <div className="action-buttons">
            <button 
              className="action-btn"
              onClick={() => handleNavigation('/workout-plan')}
            >
              Calculate BMI
            </button>
            <button 
              className="action-btn"
              onClick={() => handleNavigation('/helping-tools')}
            >
              Add YouTube Link
            </button>
            <button 
              className="action-btn"
              onClick={() => handleNavigation('/user-details')}
            >
              Update Profile
            </button>
          </div>
        </div>

      </div>
    </div>
    </div>
  );
};

export default Dashboard;