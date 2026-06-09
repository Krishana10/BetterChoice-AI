import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { authService } from '../services';
import { setAccessToken, setRefreshHandler } from '../services/api';

const AuthContext = createContext(null);

const TOKEN_KEY = 'bc_refresh_token';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const persistTokens = useCallback((authData) => {
    setAccessToken(authData.accessToken);
    localStorage.setItem(TOKEN_KEY, authData.refreshToken);
    setUser(authData.user);
  }, []);

  const login = useCallback(async (credentials) => {
    const { data } = await authService.login(credentials);
    persistTokens(data.data);
    return data.data;
  }, [persistTokens]);

  const register = useCallback(async (formData) => {
    const { data } = await authService.register(formData);
    persistTokens(data.data);
    return data.data;
  }, [persistTokens]);

  const logout = useCallback(async () => {
    const refreshToken = localStorage.getItem(TOKEN_KEY);
    if (refreshToken) {
      try {
        await authService.logout(refreshToken);
      } catch {
        /* ignore logout errors */
      }
    }
    setAccessToken(null);
    localStorage.removeItem(TOKEN_KEY);
    setUser(null);
  }, []);

  const refresh = useCallback(async () => {
    const refreshToken = localStorage.getItem(TOKEN_KEY);
    if (!refreshToken) throw new Error('No refresh token');
    const { data } = await authService.refresh(refreshToken);
    persistTokens(data.data);
  }, [persistTokens]);

  useEffect(() => {
    setRefreshHandler(refresh);
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
      refresh().catch(() => {
        localStorage.removeItem(TOKEN_KEY);
      }).finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [refresh]);

  const value = useMemo(
    () => ({
      user,
      loading,
      isAuthenticated: !!user,
      login,
      register,
      logout,
    }),
    [user, loading, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
}
