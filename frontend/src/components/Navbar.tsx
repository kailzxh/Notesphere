import { Link } from 'react-router-dom';

interface NavbarProps {
  onLogout: () => void;
  onToggleTheme: () => void;
}
export default function Navbar({ onLogout, onToggleTheme }: NavbarProps) {
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
