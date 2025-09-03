import axios from 'axios';
import { getAccessToken, setAccessToken, clearAccessToken } from '../lib/auth';

const API_URL = import.meta.env.VITE_API_BASE_URL || '';

const client = axios.create({
  baseURL: API_URL,
  withCredentials: true, // include httpOnly cookies for refresh token
});

client.interceptors.request.use(config => {
  const token = getAccessToken();
  if (token) {
    config.headers = config.headers || {};
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

client.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const res = await client.post('/auth/refresh');
        const newToken = res.data.accessToken;
        setAccessToken(newToken);
        client.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
        return client(originalRequest);
      } catch (refreshError) {
        clearAccessToken();
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default client;
