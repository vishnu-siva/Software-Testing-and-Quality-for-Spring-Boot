import axios from 'axios';

// Base URL for API calls
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('fiteasy_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      // Unauthorized - redirect to login
      localStorage.removeItem('fiteasy_token');
      localStorage.removeItem('fiteasy_userId');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// User API functions
export const userAPI = {
  // Login user
  login: async (credentials) => {
    const response = await api.post('/users/login', credentials);
    return response.data;
  },

  // Register new user
  register: async (userData) => {
    const response = await api.post('/users/register', userData);
    return response.data;
  },

  // Get user by ID
  getUserById: async (userId) => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
  },

  // Update user
  updateUser: async (userId, userData) => {
    const response = await api.put(`/users/${userId}`, userData);
    return response.data;
  },

  // Delete user
  deleteUser: async (userId) => {
    const response = await api.delete(`/users/${userId}`);
    return response.data;
  },

  // Get dashboard counts
  getDashboardCounts: async (userId) => {
    const response = await api.get(`/users/${userId}/counts`);
    return response.data;
  },

  // Check username availability
  checkUsername: async (username) => {
    const response = await api.get(`/users/check-username/${username}`);
    return response.data;
  },

  // Check email availability
  checkEmail: async (email) => {
    const response = await api.get(`/users/check-email/${email}`);
    return response.data;
  },

  // Logout
  logout: async () => {
    const response = await api.post('/users/logout');
    return response.data;
  }
};

// Workout Plan API functions
export const workoutAPI = {
  // Create workout plan
  createWorkoutPlan: async (workoutData) => {
    const response = await api.post('/workout-plans', workoutData);
    return response.data;
  },

  // Get workout plans for user
  getWorkoutPlansByUser: async (userId) => {
    const response = await api.get(`/workout-plans/user/${userId}`);
    return response.data;
  },

  // Get workout plan by ID
  getWorkoutPlanById: async (planId) => {
    const response = await api.get(`/workout-plans/${planId}`);
    return response.data;
  },

  // Update workout plan
  updateWorkoutPlan: async (planId, workoutData) => {
    const response = await api.put(`/workout-plans/${planId}`, workoutData);
    return response.data;
  },

  // Delete workout plan
  deleteWorkoutPlan: async (planId) => {
    const response = await api.delete(`/workout-plans/${planId}`);
    return response.data;
  },

  // Calculate BMI
  calculateBMI: async (height, weight) => {
    const response = await api.post('/workout-plans/calculate-bmi', {
      height,
      weight
    });
    return response.data;
  },

  // Get workout plan count
  getWorkoutPlanCount: async (userId) => {
    const response = await api.get(`/workout-plans/count/user/${userId}`);
    return response.data;
  },

  // Get BMI results count
  getBMIResultsCount: async (userId) => {
    const response = await api.get(`/workout-plans/bmi-count/user/${userId}`);
    return response.data;
  },

  // Get latest workout plan
  getLatestWorkoutPlan: async (userId) => {
    const response = await api.get(`/workout-plans/latest/user/${userId}`);
    return response.data;
  },

  // Get workout plans with BMI
  getWorkoutPlansWithBMI: async (userId) => {
    const response = await api.get(`/workout-plans/with-bmi/user/${userId}`);
    return response.data;
  }
};

// Helping Tools API functions
export const helpingToolsAPI = {
  // Create helping tool
  createHelpingTool: async (toolData) => {
    const response = await api.post('/helping-tools', toolData);
    return response.data;
  },

  // Get helping tools for user
  getHelpingToolsByUser: async (userId) => {
    const response = await api.get(`/helping-tools/user/${userId}`);
    return response.data;
  },

  // Get YouTube links
  getYouTubeLinks: async (userId) => {
    const response = await api.get(`/helping-tools/user/${userId}/youtube`);
    return response.data;
  },

  // Get equipment links
  getEquipmentLinks: async (userId) => {
    const response = await api.get(`/helping-tools/user/${userId}/equipment`);
    return response.data;
  },

  // Get helping tool by ID
  getHelpingToolById: async (toolId) => {
    const response = await api.get(`/helping-tools/${toolId}`);
    return response.data;
  },

  // Update helping tool
  updateHelpingTool: async (toolId, toolData) => {
    const response = await api.put(`/helping-tools/${toolId}`, toolData);
    return response.data;
  },

  // Delete helping tool
  deleteHelpingTool: async (toolId) => {
    const response = await api.delete(`/helping-tools/${toolId}`);
    return response.data;
  },

  // Add YouTube link
  addYouTubeLink: async (userId, url, description = '') => {
    const response = await api.post('/helping-tools/youtube', {
      userId,
      url,
      description
    });
    return response.data;
  },

  // Add equipment link
  addEquipmentLink: async (userId, url, description = '') => {
    const response = await api.post('/helping-tools/equipment', {
      userId,
      url,
      description
    });
    return response.data;
  },

  // Delete helping tools by type
  deleteHelpingToolsByType: async (userId, type) => {
    const response = await api.delete(`/helping-tools/user/${userId}/type/${type}`);
    return response.data;
  },

  // Get helping tools count
  getHelpingToolsCount: async (userId) => {
    const response = await api.get(`/helping-tools/count/user/${userId}`);
    return response.data;
  }
};

// Export default api instance for custom calls
export default api;