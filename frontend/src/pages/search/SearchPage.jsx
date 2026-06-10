import { useMemo, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import SearchBar from '../../components/features/search/SearchBar';
import ProductGrid from '../../components/features/product/ProductGrid';
import { useProducts } from '../../hooks/useProducts';
import { useCategories } from '../../hooks/useCategories';
import { CATEGORY_TYPES } from '../../constants';

export default function SearchPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [query, setQuery] = useState(searchParams.get('q') || '');

  const activeQuery = searchParams.get('q') || '';
  const categoryType = searchParams.get('category') || searchParams.get('categoryType') || '';
  const categoryId = searchParams.get('categoryId') || '';

  const productParams = useMemo(() => {
    const params = { page: 0, size: 20 };
    if (activeQuery) params.search = activeQuery;
    if (categoryId) params.categoryId = categoryId;
    else if (categoryType) params.categoryType = categoryType;
    return params;
  }, [activeQuery, categoryId, categoryType]);

  const { products, pagination, loading, error } = useProducts(productParams);
  const { categories } = useCategories();

  const handleSearch = (e) => {
    e.preventDefault();
    const params = new URLSearchParams(searchParams);
    if (query.trim()) params.set('q', query.trim());
    else params.delete('q');
    setSearchParams(params);
  };

  const handleCategoryTypeChange = (e) => {
    const params = new URLSearchParams(searchParams);
    params.delete('categoryId');
    if (e.target.value) params.set('category', e.target.value);
    else params.delete('category');
    setSearchParams(params);
  };

  const handleCategoryIdChange = (e) => {
    const params = new URLSearchParams(searchParams);
    params.delete('category');
    if (e.target.value) params.set('categoryId', e.target.value);
    else params.delete('categoryId');
    setSearchParams(params);
  };

  const selectedCategory = categories.find((c) => c.id === categoryId);

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">Search & Compare</h1>
        <p className="mt-1 text-gray-500">Find products across multiple Indian marketplaces</p>
      </div>

      <SearchBar query={query} onQueryChange={setQuery} onSubmit={handleSearch} />

      <div className="mt-4 flex flex-col gap-3 sm:flex-row">
        <select
          className="input-field sm:w-56"
          value={categoryType}
          onChange={handleCategoryTypeChange}
          aria-label="Category type"
        >
          <option value="">All types</option>
          {CATEGORY_TYPES.map((c) => (
            <option key={c.value} value={c.value}>{c.label}</option>
          ))}
        </select>

        <select
          className="input-field sm:w-56"
          value={categoryId}
          onChange={handleCategoryIdChange}
          aria-label="Category"
        >
          <option value="">All categories</option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
      </div>

      <div className="mt-8 flex items-center justify-between">
        <p className="text-sm text-gray-500">
          {loading ? 'Searching...' : (
            <>
              {pagination.totalElements} result{pagination.totalElements !== 1 ? 's' : ''}
              {activeQuery && <> for &quot;{activeQuery}&quot;</>}
              {selectedCategory && <> in {selectedCategory.name}</>}
            </>
          )}
        </p>
      </div>

      {error && (
        <div className="mt-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      <div className="mt-6">
        <ProductGrid
          products={products}
          loading={loading}
          emptyMessage={
            activeQuery || categoryId || categoryType
              ? 'No products match your search. Try different keywords or filters.'
              : 'Start typing to search our catalog, or browse all products below.'
          }
        />
      </div>
    </div>
  );
}
