import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import client from '../api/client';
import { setAccessToken } from '../lib/auth';
import { toast } from 'react-toastify';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const res = await client.post('/auth/login', { email, password });
      setAccessToken(res.data.accessToken);
      toast.success('Logged in');
      navigate('/notes');
    } catch (e: any) {
      toast.error(e.response?.data?.message || 'Login failed');
    }
  };

  return (
    <div className="p-6 max-w-sm mx-auto">
      <h1 className="text-2xl mb-4">Login</h1>
      <input type="email" placeholder="Email" value={email}
             onChange={e => setEmail(e.target.value)} className="mb-2 w-full border px-2 py-1"/>
      <input type="password" placeholder="Password" value={password}
             onChange={e => setPassword(e.target.value)} className="mb-4 w-full border px-2 py-1"/>
      <button onClick={handleLogin} className="w-full bg-blue-500 text-white py-1 rounded">Login</button>
      <p className="mt-4 text-sm">
        New? <Link to="/signup" className="text-blue-500">Sign Up</Link>
      </p>
    </div>
  );
}
