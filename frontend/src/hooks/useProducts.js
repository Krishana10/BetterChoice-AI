import { useCallback, useEffect, useState } from 'react';
import { productService } from '../services';

export function useProducts(params = {}) {
  const [products, setProducts] = useState([]);
  const [pagination, setPagination] = useState({ page: 0, size: 20, totalElements: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchProducts = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const { data } = await productService.list(params);
      const page = data.data;
      setProducts(page?.content ?? []);
      setPagination({
        page: page?.page ?? 0,
        size: page?.size ?? 20,
        totalElements: page?.totalElements ?? 0,
        totalPages: page?.totalPages ?? 0,
      });
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Failed to load products');
      setProducts([]);
    } finally {
      setLoading(false);
    }
  }, [JSON.stringify(params)]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  return { products, pagination, loading, error, refetch: fetchProducts };
}
