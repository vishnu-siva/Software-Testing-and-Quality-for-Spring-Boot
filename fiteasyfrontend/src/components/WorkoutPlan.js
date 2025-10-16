import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { workoutAPI } from '../utils/api';
import '../styles/WorkoutPlan.css';

const WorkoutPlan = ({ userId, onLogout }) => {
  const navigate = useNavigate();
  const [workoutData, setWorkoutData] = useState({
    dateCreated: '',
    age: '',
    gender: '',
    height: '',
    weight: '',
    trainer: '',
    gymName: '',
    spentTimeInGym: '',
    workOut: '',
    repsSets: ''
  });
  const [bmiResult, setBmiResult] = useState(null);
  const [workoutPlans, setWorkoutPlans] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    if (userId) {
      fetchWorkoutPlans();
    }
  }, [userId]);

  useEffect(() => {
    
    if (workoutData.height && workoutData.weight) {
      calculateBMI();
    }
  }, [workoutData.height, workoutData.weight]);

  const fetchWorkoutPlans = async () => {
    try {
      const response = await workoutAPI.getWorkoutPlansByUser(userId);
      setWorkoutPlans(response);
      
      // Load the latest workout plan data if available
      if (response.length > 0) {
        const latest = response[0];
        setWorkoutData({
          dateCreated: latest.dateCreated || '',
          age: latest.age || '',
          gender: latest.gender || '',
          height: latest.height || '',
          weight: latest.weight || '',
          trainer: latest.trainer || '',
          gymName: latest.gymName || '',
          spentTimeInGym: latest.spentTimeInGym || '',
          workOut: latest.workOut || '',
          repsSets: latest.repsSets || ''
        });
        
        if (latest.bmiData) {
          setBmiResult({
            bmi: latest.bmiData,
            category: getBMICategory(parseFloat(latest.bmiData))
          });
        }
      }
    } catch (error) {
      console.error('Error fetching workout plans:', error);
      setError('Failed to load workout plans');
    }
  };

  const calculateBMI = async () => {
    if (!workoutData.height || !workoutData.weight) return;
    
    try {
      const response = await workoutAPI.calculateBMI(
        parseFloat(workoutData.height),
        parseFloat(workoutData.weight)
      );
      setBmiResult(response);
    } catch (error) {
      console.error('Error calculating BMI:', error);
      setError('Error calculating BMI');
    }
  };

  const getBMICategory = (bmi) => {
    if (bmi < 18.5) return 'Underweight';
    if (bmi < 25) return 'Normal weight';
    if (bmi < 30) return 'Overweight';
    return 'Obese';
  };

  const handleInputChange = (e) => {
    setWorkoutData({
      ...workoutData,
      [e.target.name]: e.target.value
    });
    // Clear messages when user starts typing
    if (error) setError('');
    if (success) setSuccess('');
  };

  const handleSave = async () => {
    try {
      setLoading(true);
      setError('');
      setSuccess('');
      
      const dataToSave = {
        ...workoutData,
        userId: userId,
        bmiData: bmiResult?.bmi || null
      };
      
      await workoutAPI.createWorkoutPlan(dataToSave);
      setIsEditing(false);
      setSuccess('Workout plan saved successfully!');
      await fetchWorkoutPlans();
    } catch (error) {
      console.error('Error saving workout plan:', error);
      setError(error.response?.data?.message || 'Error saving workout plan');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (planId) => {
    if (window.confirm('Are you sure you want to delete this workout plan?')) {
      try {
        await workoutAPI.deleteWorkoutPlan(planId);
        setSuccess('Workout plan deleted successfully!');
        await fetchWorkoutPlans();
        
        // Clear form if deleted plan was being displayed
        setWorkoutData({
          dateCreated: '',
          age: '',
          gender: '',
          height: '',
          weight: '',
          trainer: '',
          gymName: '',
          spentTimeInGym: '',
          workOut: '',
          repsSets: ''
        });
        setBmiResult(null);
      } catch (error) {
        console.error('Error deleting workout plan:', error);
        setError('Error deleting workout plan');
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

  return (
    <div className="workout-plan-page fade-in">
     
      <div className="sidebar slide-in-left">
        
        <nav className="sidebar-nav">
          <div 
            className="nav-item" 
            onClick={() => handleNavigation('/dashboard')}
          >
            Dash Board
          </div>
          <div 
            className="nav-item" 
            onClick={() => handleNavigation('/user-details')}
          >
            User Details
          </div>
          <div className="nav-item active">
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
        
        <div className="workout-form">
          {/* Form Section */}
          <div className="form-left">
            <div className="form-group">
              <label>BMI</label>
              <input
                type="text"
                value={bmiResult?.bmi || ''}
                disabled
                placeholder="BMI will be calculated automatically"
              />
            </div>
            
            <div className="form-group">
              <label>Date</label>
              <input
                type="date"
                name="dateCreated"
                value={workoutData.dateCreated}
                onChange={handleInputChange}
                disabled={!isEditing}
              />
            </div>
            
            <div className="form-group">
              <label>Age</label>
              <input
                type="number"
                name="age"
                value={workoutData.age}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Enter your age"
                min="1"
                max="120"
              />
            </div>
            
            <div className="form-group">
              <label>Gender</label>
              <select
                name="gender"
                value={workoutData.gender}
                onChange={handleInputChange}
                disabled={!isEditing}
              >
                <option value="">Select Gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>
            
            <div className="form-group">
              <label>Height (cm)</label>
              <input
                type="number"
                name="height"
                placeholder="Height in cm"
                value={workoutData.height}
                onChange={handleInputChange}
                disabled={!isEditing}
                min="50"
                max="300"
              />
            </div>
            
            <div className="form-group">
              <label>Weight (kg)</label>
              <input
                type="number"
                name="weight"
                placeholder="Weight in kg"
                value={workoutData.weight}
                onChange={handleInputChange}
                disabled={!isEditing}
                min="10"
                max="500"
                step="0.1"
              />
            </div>
          </div>

          {/* BMI Display Section */}
          <div className="form-right">
            <div className="bmi-display">
              <h3>BMI Calculator</h3>
              {bmiResult ? (
                <div className="bmi-result">
                  <div className="bmi-value">
                    <span className="bmi-number">{bmiResult.bmi}</span>
                    <span className="bmi-category">{bmiResult.category}</span>
                  </div>
                  <p>Normal range: 18.5 - 24.9</p>
                  <p>Category: {bmiResult.category}</p>
                  {bmiResult.recommendations && (
                    <p className="bmi-advice">{bmiResult.recommendations.advice}</p>
                  )}
                </div>
              ) : (
                <div className="bmi-placeholder">
                  <p>Enter height and weight to calculate BMI</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Workout Details Section */}
        <div className="workout-details">
          <h3>Work out plan Details</h3>
          <div className="workout-table">
            <div className="table-headers">
              <span>Date</span>
              <span>Trainer</span>
              <span>Gym name</span>
              <span>Spent time in Gym</span>
              <span>Work Out</span>
              <span>Reps/Sets</span>
            </div>
            <div className="table-row">
              <input
                type="date"
                name="dateCreated"
                value={workoutData.dateCreated}
                onChange={handleInputChange}
                disabled={!isEditing}
              />
              <input
                type="text"
                name="trainer"
                value={workoutData.trainer}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Trainer name"
              />
              <input
                type="text"
                name="gymName"
                value={workoutData.gymName}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Gym name"
              />
              <input
                type="text"
                name="spentTimeInGym"
                value={workoutData.spentTimeInGym}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Time spent"
              />
              <input
                type="text"
                name="workOut"
                value={workoutData.workOut}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Workout description"
              />
              <input
                type="text"
                name="repsSets"
                value={workoutData.repsSets}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="Reps and sets"
              />
            </div>
          </div>
        </div>
        {workoutPlans.length > 0 && (
          <div className="workout-history">
            <h3>Previous Workout Plans</h3>
            <div className="plans-list">
              {workoutPlans.map((plan, index) => (
                 <div key={plan.id} className="plan-item">
                  <div className="plan-info">
                    <span>Date: {plan.dateCreated}</span>
                    <span>BMI: {plan.bmiData}</span>
                    <span>Age: {plan.age}</span>
                    <span>Gender: {plan.gender}</span>
                    <span>Height: {plan.height}</span>
                    <span>Weight: {plan.weight}</span>
                    <span>Trainer: {plan.trainer}</span>
                    <span>Spent Time: {plan.spentTimeInGym}</span>
                    <span>Work Out: {plan.workOut}</span>
                    <span>Reps/Sets: {plan.repsSets}</span>
                    <span>Gym: {plan.gymName}</span>
                    <span></span>
                  </div>
                  <button 
                    className="delete-plan-btn"
                    onClick={() => handleDelete(plan.id)}
                  >
                    Delete
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Action Buttons */}
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
                onClick={() => {
                  if (workoutPlans.length > 0) {
                    handleDelete(workoutPlans[0].id);
                  }
                }}
                disabled={workoutPlans.length === 0}
              >
                DELETE
              </button>
            </>
          ) : (
            <>
              <button 
                className="save-btn" 
                onClick={handleSave}
                disabled={loading}
              >
                {loading ? 'Saving...' : 'SAVE'}
              </button>
              <button 
                className="cancel-btn" 
                onClick={() => {
                  setIsEditing(false);
                  fetchWorkoutPlans(); // Reset form
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

export default WorkoutPlan;