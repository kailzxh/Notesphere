import { Link , useNavigate} from 'react-router-dom';
import { clearAccessToken } from '../lib/auth';
import axios from 'axios';
interface NavbarProps {
  onLogout: () => void;
  onToggleTheme: () => void;
}
export default function Navbar({ onLogout, onToggleTheme }: NavbarProps) {
   const navigate = useNavigate();
  const handleLogout = async () => {
    // 1. Clear access token from memory & localStorage
    clearAccessToken();

    // 2. Call backend to clear refresh token cookie
    try {
      await axios.post('/auth/logout');
    } catch (err) {
      console.error('Logout error:', err);
    }

    // 3. Redirect to login page
    navigate('/login');
  };


  return (
    <nav className="bg-white dark:bg-gray-800 p-4 flex justify-between items-center">
      <Link to="/notes" className="text-xl font-bold text-gray-800 dark:text-gray-100">NotesApp</Link>
      <div>
        <button onClick={onToggleTheme} className="mr-4 px-3 py-1 bg-gray-200 dark:bg-gray-700 rounded">
          Toggle Theme
        </button>
        <button onClick={onLogout} className="px-3 py-1 bg-red-500 text-white rounded">Logout</button>
      </div>
    </nav>
  );
}
