import api from './api';

export const authService = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
  refresh: (refreshToken) => api.post('/auth/refresh', { refreshToken }),
  logout: (refreshToken) => api.post('/auth/logout', { refreshToken }),
};

export const userService = {
  getMe: () => api.get('/users/me'),
  updateMe: (data) => api.put('/users/me', data),
};

export const productService = {
  list: (params) => api.get('/products', { params }),
  getById: (id) => api.get(`/products/${id}`),
  getBySlug: (slug) => api.get(`/products/slug/${slug}`),
};

export const comparisonService = {
  create: (data) => api.post('/comparisons', data),
  list: (params) => api.get('/comparisons', { params }),
  getById: (id) => api.get(`/comparisons/${id}`),
  run: (id) => api.post(`/comparisons/${id}/run`),
  save: (id) => api.post(`/comparisons/${id}/save`),
};

export const searchService = {
  search: (params) => api.get('/search', { params }),
  autocomplete: (q) => api.get('/search/autocomplete', { params: { q } }),
};
