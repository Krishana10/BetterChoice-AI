import { useAuth } from '../../context/AuthContext';

export default function DashboardPage() {
  const { user } = useAuth();

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
      <h1 className="text-2xl font-bold">Dashboard</h1>
      <p className="mt-1 text-gray-500">Welcome back, {user?.fullName}</p>

      <div className="mt-8 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <div className="card">
          <h3 className="font-semibold">Saved Comparisons</h3>
          <p className="mt-2 text-3xl font-bold text-brand-600">0</p>
        </div>
        <div className="card">
          <h3 className="font-semibold">Recent Searches</h3>
          <p className="mt-2 text-3xl font-bold text-brand-600">0</p>
        </div>
        <div className="card">
          <h3 className="font-semibold">AI Insights Used</h3>
          <p className="mt-2 text-3xl font-bold text-brand-600">0</p>
        </div>
      </div>
    </div>
  );
}
