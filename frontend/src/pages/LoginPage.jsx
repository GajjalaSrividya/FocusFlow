import { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import authService from '../services/authService';
import './AuthForm.css';

function LoginPage() {
  const [credentials, setCredentials] = useState({
    username: '',
    password: ''
  });

  const navigate = useNavigate();

  const handleChange = e => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleLogin = async e => {
    e.preventDefault();
    try {
      const token = await authService.login(credentials);
      localStorage.setItem('token', token);
      alert('Login successful!');
      navigate('/dashboard'); 
    } catch (err) {
      alert('Login failed: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <input name="username" placeholder="Username" onChange={handleChange} required />
      <input name="password" type="password" placeholder="Password" onChange={handleChange} required />
      <button type="submit">Login</button>
    </form>
  );
}

export default LoginPage;
