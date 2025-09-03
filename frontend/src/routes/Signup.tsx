import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import client from '../api/client';
import { setAccessToken } from '../lib/auth';
import { toast } from 'react-toastify';

export default function Signup() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSignup = async () => {
    try {
      const res = await client.post('/auth/signup', { email, password });
      setAccessToken(res.data.accessToken);
      toast.success('Account created');
      navigate('/notes');
    } catch (e: any) {
      toast.error(e.response?.data?.message || 'Signup failed');
    }
  };

  return (
    <div className="p-6 max-w-sm mx-auto">
      <h1 className="text-2xl mb-4">Sign Up</h1>
      <input type="email" placeholder="Email" value={email}
             onChange={e => setEmail(e.target.value)} className="mb-2 w-full border px-2 py-1"/>
      <input type="password" placeholder="Password" value={password}
             onChange={e => setPassword(e.target.value)} className="mb-4 w-full border px-2 py-1"/>
      <button onClick={handleSignup} className="w-full bg-green-500 text-white py-1 rounded">Sign Up</button>
      <p className="mt-4 text-sm">
        Have an account? <Link to="/login" className="text-blue-500">Login</Link>
      </p>
    </div>
  );
}
