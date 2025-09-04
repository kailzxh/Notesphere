import { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './routes/Login';
import Signup from './routes/Signup';
import NotesList from './routes/NotesList';
import NoteDetail from './routes/NoteDeatils';
import SharedNote from './routes/SharedNote';
import Navbar from './components/Navbar';
import { getAccessToken, setAccessToken, clearAccessToken } from './lib/auth';
import client from './api/client';

function App() {
  const [theme, setTheme] = useState('light');
  const token = getAccessToken();
  const navigate = useNavigate();

  // Check login status on mount
  useEffect(() => {
    if (token) {
      client.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }, [token]);

  const handleLogout = async () => {
    try {
      await client.post('/auth/logout', null);
      clearAccessToken();
      navigate('/login');
    } catch (e) {
      console.error('Logout failed', e);
    }
  };

  const toggleTheme = () => {
    const newTheme = theme === 'light' ? 'dark' : 'light';
    setTheme(newTheme);
    document.documentElement.classList.toggle('dark', newTheme === 'dark');
  };

  return (
    <div className="min-h-screen">
      <Navbar  onToggleTheme={toggleTheme} />
      <Routes>
        <Route path="/" element={
    getAccessToken() ? <Navigate to="/notes" replace /> : <Navigate to="/login" replace />
  }/>
      <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/notes" element={<NotesList />} />
        <Route path="/notes/:id" element={<NoteDetail />} />
        <Route path="/shared/:shareId" element={<SharedNote />} />
      </Routes>
    </div>
  );
}

export default App;
