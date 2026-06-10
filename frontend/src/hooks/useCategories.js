import { useCallback, useEffect, useState } from 'react';
import { categoryService } from '../services';

export function useCategories(type) {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchCategories = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const params = type ? { type } : undefined;
      const { data } = await categoryService.list(params);
      setCategories(data.data ?? []);
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Failed to load categories');
      setCategories([]);
    } finally {
      setLoading(false);
    }
  }, [type]);

  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  return { categories, loading, error, refetch: fetchCategories };
}
