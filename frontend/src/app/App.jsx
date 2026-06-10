import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext';
import Layout from '../components/layout/Layout';
import ProtectedRoute from './ProtectedRoute';
import HomePage from '../pages/home/HomePage';
import LoginPage from '../pages/auth/LoginPage';
import RegisterPage from '../pages/auth/RegisterPage';
import SearchPage from '../pages/search/SearchPage';
import ProductDetailPage from '../pages/products/ProductDetailPage';
import ComparePage from '../pages/compare/ComparePage';
import DashboardPage from '../pages/dashboard/DashboardPage';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route index element={<HomePage />} />
            <Route path="login" element={<LoginPage />} />
            <Route path="register" element={<RegisterPage />} />
            <Route path="search" element={<SearchPage />} />
            <Route path="products/:slug" element={<ProductDetailPage />} />
            <Route element={<ProtectedRoute />}>
              <Route path="compare" element={<ComparePage />} />
              <Route path="dashboard" element={<DashboardPage />} />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
