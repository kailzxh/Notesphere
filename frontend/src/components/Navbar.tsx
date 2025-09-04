import { Link, useNavigate } from 'react-router-dom';
import { clearAccessToken, getAccessToken } from '../lib/auth';
import axios from 'axios';

interface NavbarProps {
  onToggleTheme: () => void;
}

export default function Navbar({ onToggleTheme }: NavbarProps) {
  const navigate = useNavigate();
  const accessToken = getAccessToken(); // returns token if logged in

  const handleLogout = async () => {
    clearAccessToken();

    try {
      await axios.post('/auth/logout');
    } catch (err) {
      console.error('Logout error:', err);
    }

    navigate('/login');
  };

  return (
    <nav className="bg-white dark:bg-gray-800 p-4 flex justify-between items-center">
      <Link to="/notes" className="text-xl font-bold text-gray-800 dark:text-gray-100">
        Notesphere
      </Link>
      <div>
        <button
          onClick={onToggleTheme}
          className="mr-4 px-3 py-1 bg-gray-200 dark:bg-gray-700 rounded"
        >
          Toggle Theme
        </button>

        {accessToken ? (
          <button onClick={handleLogout} className="px-3 py-1 bg-red-500 text-white rounded">
            Logout
          </button>
        ) : (
          <Link to="/login" className="px-3 py-1 bg-green-500 text-white rounded">
            Login
          </Link>
        )}
      </div>
    </nav>
  );
}
