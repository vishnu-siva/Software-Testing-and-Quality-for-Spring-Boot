import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import HomePage from './components/HomePage';
import LoginPage from './components/LoginPage';
import SignUpPage from './components/SignUpPage';
import Dashboard from './components/Dashboard';
import UserDetails from './components/UserDetails';
import WorkoutPlan from './components/WorkoutPlan';
import HelpingTools from './components/HelpingTools';
import './App.css';
import './styles/HomePage.css';


function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    // Check if user is already logged in
    const token = localStorage.getItem('fiteasy_token');
    const storedUserId = localStorage.getItem('fiteasy_userId');
    
    if (token && storedUserId) {
      setIsAuthenticated(true);
      setUserId(parseInt(storedUserId));
    }
  }, []);

  const handleLogin = (userId, token) => {
    localStorage.setItem('fiteasy_token', token);
    localStorage.setItem('fiteasy_userId', userId.toString());
    setIsAuthenticated(true);
    setUserId(userId);
  };

  const handleLogout = () => {
    localStorage.removeItem('fiteasy_token');
    localStorage.removeItem('fiteasy_userId');
    setIsAuthenticated(false);
    setUserId(null);
  };

  return (
    <Router>
      <div className="App">
        <Routes>

          <Route path="/" element={<HomePage />} />
          <Route 
            path="/login" 
            element={
              !isAuthenticated ? 
              <LoginPage onLogin={handleLogin} /> : 
              <Navigate to="/dashboard" replace />
            } 
          />
          <Route 
            path="/signup" 
            element={
              !isAuthenticated ? 
              <SignUpPage /> : 
              <Navigate to="/dashboard" replace />
            } 
          />
         
          <Route 
            path="/dashboard" 
            element={
              isAuthenticated ? 
              <Dashboard userId={userId} onLogout={handleLogout} /> : 
              <Navigate to="/" replace />
            } 
          />
          <Route 
            path="/user-details" 
            element={
              isAuthenticated ? 
              <UserDetails userId={userId} onLogout={handleLogout} /> : 
              <Navigate to="/" replace />
            } 
          />
          <Route 
            path="/workout-plan" 
            element={
              isAuthenticated ? 
              <WorkoutPlan userId={userId} onLogout={handleLogout} /> : 
              <Navigate to="/" replace />
            } 
          />
          <Route 
            path="/helping-tools" 
            element={
              isAuthenticated ? 
              <HelpingTools userId={userId} onLogout={handleLogout} /> : 
              <Navigate to="/" replace />
            } 
          />
          
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;