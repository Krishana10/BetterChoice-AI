import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function Navbar() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <header className="sticky top-0 z-50 border-b border-gray-200 bg-white/80 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6">
        <Link to="/" className="flex items-center gap-2">
          <span className="text-xl font-bold text-brand-700">BetterChoice</span>
          <span className="rounded bg-brand-100 px-2 py-0.5 text-xs font-semibold text-brand-700">AI</span>
        </Link>

        <nav className="hidden items-center gap-6 md:flex">
          <Link to="/search" className="text-sm font-medium text-gray-600 hover:text-brand-600">
            Search
          </Link>
          {isAuthenticated && (
            <>
              <Link to="/compare" className="text-sm font-medium text-gray-600 hover:text-brand-600">
                Compare
              </Link>
              <Link to="/dashboard" className="text-sm font-medium text-gray-600 hover:text-brand-600">
                Dashboard
              </Link>
            </>
          )}
        </nav>

        <div className="flex items-center gap-3">
          {isAuthenticated ? (
            <>
              <span className="hidden text-sm text-gray-600 sm:inline">{user?.fullName}</span>
              <button onClick={handleLogout} className="btn-secondary text-sm">
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-secondary text-sm">
                Login
              </Link>
              <Link to="/register" className="btn-primary text-sm">
                Sign Up
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
