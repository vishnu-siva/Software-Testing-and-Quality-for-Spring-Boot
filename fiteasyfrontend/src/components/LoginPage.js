import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { userAPI } from '../utils/api';
import '../styles/LoginPage.css';
import apple from '../images/apple.gif';  

const LoginPage = ({ onLogin }) => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    // Clear error when user starts typing
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation
    if (!formData.username || !formData.password) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await userAPI.login(formData);
      
      if (response.userId && response.token) {
        onLogin(response.userId, response.token);
        navigate('/dashboard');
      } else {
        setError('Login failed. Please try again.');
      }
    } catch (error) {
      console.error('Login error:', error);
      setError(error.response?.data?.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    
    <div className="login-page">
      <div className="login-container fade-in">
        <div className="login-header">
          <h1>WELCOME BACK</h1>
          <h2>FitEase</h2>
        </div>
      
        <div className="form-subtitle">Log in to your account</div>

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <input
              type="text"
              name="username"
              placeholder="username"
              value={formData.username}
              onChange={handleInputChange}
              disabled={loading}
              required
            />
          </div>
          
          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder="password"
              value={formData.password}
              onChange={handleInputChange}
              disabled={loading}
              required
            />
          </div>
          
          {error && <div className="error-message">{error}</div>}
          
          <button 
            type="submit" 
            className="login-button"
            disabled={loading}
          >
            {loading ? 'Logging in...' : 'LOG IN'}
          </button>
          
          <div className="signup-link">
            <span>Don't have account? </span>
            <Link to="/signup" className="signup-link-btn">
              SIGN UP
            </Link>
          </div>
        </form>

        <div className="welcome"> 
            <img src={apple} alt="apple"  />
        </div>

      </div>
    </div>
  );
};

export default LoginPage;