export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/v1';

export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  SEARCH: '/search',
  PRODUCTS: '/products',
  PRODUCT_DETAIL: '/products/:slug',
  COMPARE: '/compare',
  COMPARISON: '/comparisons/:id',
  DASHBOARD: '/dashboard',
  PROFILE: '/profile',
};

export const ROLES = {
  USER: 'ROLE_USER',
  PREMIUM: 'ROLE_PREMIUM',
  ADMIN: 'ROLE_ADMIN',
};

export const CATEGORY_TYPES = [
  { value: 'PRODUCT', label: 'Products' },
  { value: 'COLLEGE', label: 'Colleges' },
  { value: 'RESTAURANT', label: 'Restaurants' },
  { value: 'COURSE', label: 'Courses' },
  { value: 'COMPANY', label: 'Companies' },
  { value: 'SALARY', label: 'Salaries' },
  { value: 'HOTEL', label: 'Hotels' },
  { value: 'SERVICE', label: 'Services' },
];
