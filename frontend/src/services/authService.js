import axios from 'axios';
const API_BASE = `${import.meta.env.VITE_API_URL}/api/auth`;
const signup = async (data) => {
  return axios.post(`${API_BASE}/signup`, data);
};

const login = async (data) => {
  const response = await axios.post(`${API_BASE}/login`, data);
  return response.data.token;
};

export default { signup, login };
