import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI } from '../utils/api';
import '../styles/UserDetails.css';

const UserDetails = ({ userId, onLogout }) => {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({
    name: '',
    dateCreated: '',
    email: '',
    contacts: '',
    birthDate: '',
    age: '',
    familyStatus: '',
    favoriteThings: '',
    goals: ''
  });
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    if (userId) {
      fetchUserDetails();
    }
  }, [userId]);

  const fetchUserDetails = async () => {
    try {
      setLoading(true);
      const response = await userAPI.getUserById(userId);
      setUserDetails({
        name: response.name || '',
        dateCreated: response.dateCreated || '',
        email: response.email || '',
        contacts: response.contacts || '',
        birthDate: response.birthDate || '',
        age: response.age || '',
        familyStatus: response.familyStatus || '',
        favoriteThings: response.favoriteThings || '',
        goals: response.goals || ''
      });
    } catch (error) {
      console.error('Error fetching user details:', error);
      setError('Failed to load user details');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    setUserDetails({
      ...userDetails,
      [e.target.name]: e.target.value
    });
    // Clear messages when user starts typing
    if (error) setError('');
    if (success) setSuccess('');
  };

  const handleUpdate = async () => {
    try {
      setError('');
      setSuccess('');
      
      await userAPI.updateUser(userId, userDetails);
      setIsEditing(false);
      setSuccess('User details updated successfully!');
      
      // Refresh data
      await fetchUserDetails();
    } catch (error) {
      console.error('Error updating user details:', error);
      setError(error.response?.data?.message || 'Error updating user details');
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      try {
        await userAPI.deleteUser(userId);
        onLogout();
        navigate('/');
      } catch (error) {
        console.error('Error deleting user:', error);
        setError(error.response?.data?.message || 'Error deleting account');
      }
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
      <div className="user-details-page">
        <div className="loading">Loading user details...</div>
      </div>
    );
  }

  return (
    <div className="user-details-page fade-in">
      {/* Sidebar Navigation */}
      <div className="sidebar slide-in-left">
        
        <nav className="sidebar-nav">
          <div 
            className="nav-item" 
            onClick={() => handleNavigation('/dashboard')}
          >
            Dash Board
          </div>
          <div className="nav-item active">
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
          <div className="nav-item logout" onClick={handleLogout}>
            Log Out
          </div>
        </nav>
      </div>

      {/* Main Content */}
      <div className="main-content">
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        
        <div className="user-form">
          {/* Form Section */}
          <div className="form-left">
            <div className="form-group">
              <label>Name</label>
              <input
                type="text"
                name="name"
                value={userDetails.name}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your name"
              />
            </div>
            
            <div className="form-group">
              <label>Date</label>
              <input
                type="date"
                name="dateCreated"
                value={userDetails.dateCreated}
                onChange={handleInputChange}
                disabled={!isEditing}
              />
            </div>
            
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                name="email"
                value={userDetails.email}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your email"
              />
            </div>
            
            <div className="form-group">
              <label>Contacts</label>
              <input
                type="text"
                name="contacts"
                value={userDetails.contacts}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your contact number"
              />
            </div>
            
            <div className="form-group">
              <label>Birth of Date</label>
              <input
                type="date"
                name="birthDate"
                value={userDetails.birthDate}
                onChange={handleInputChange}
                disabled={!isEditing}
              />
            </div>
            
            <div className="form-group">
              <label>Age</label>
              <input
                type="number"
                name="age"
                value={userDetails.age}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your age"
                min="1"
                max="120"
              />
            </div>
            
            <div className="form-group">
              <label>Family Status</label>
              <select
                name="familyStatus"
                value={userDetails.familyStatus}
                onChange={handleInputChange}
                disabled={!isEditing}
              >
                <option value="">Select status</option>
                <option value="Single">Single</option>
                <option value="Married">Married</option>
                <option value="Divorced">Divorced</option>
                <option value="Widowed">Widowed</option>
              </select>
            </div>
            
            <div className="form-group">
              <label>Favourite Things</label>
              <input
                type="text"
                name="favoriteThings"
                value={userDetails.favoriteThings}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your favorite things"
              />
            </div>
            
            <div className="form-group">
              <label>Goals</label>
              <textarea
                name="goals"
                value={userDetails.goals}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your fitness goals"
                rows="3"
              />
            </div>
          </div>

          {/* Display Section */}
          <div className="form-right">
            <div className="user-info-display">
              <h3>{userDetails.name || 'Name not set'}</h3>
              <div className="info-item">
                <span className="info-label">Date:</span>
                <span className="info-value">{userDetails.dateCreated || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Email:</span>
                <span className="info-value">{userDetails.email || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Contact:</span>
                <span className="info-value">{userDetails.contacts || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Birth Date:</span>
                <span className="info-value">{userDetails.birthDate || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Age:</span>
                <span className="info-value">{userDetails.age || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Status:</span>
                <span className="info-value">{userDetails.familyStatus || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Favorites:</span>
                <span className="info-value">{userDetails.favoriteThings || 'Not set'}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Goals:</span>
                <span className="info-value">{userDetails.goals || 'Not set'}</span>
              </div>
            </div>
          </div>
        </div>

        
        <div className="form-actions">
          {!isEditing ? (
            <>
              <button 
                className="update-btn" 
                onClick={() => setIsEditing(true)}
              >
                UPDATE
              </button>
              <button 
                className="create-btn" 
                onClick={() => setIsEditing(true)}
              >
                CREATE
              </button>
              <button 
                className="delete-btn" 
                onClick={handleDelete}
              >
                DELETE
              </button>
            </>
          ) : (
            <>
              <button 
                className="save-btn" 
                onClick={handleUpdate}
              >
                SAVE
              </button>
              <button 
                className="cancel-btn" 
                onClick={() => {
                  setIsEditing(false);
                  fetchUserDetails(); // Reset form
                }}
              >
                CANCEL
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserDetails;