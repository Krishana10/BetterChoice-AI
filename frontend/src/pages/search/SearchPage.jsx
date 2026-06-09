import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { CATEGORY_TYPES } from '../../constants';

export default function SearchPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [query, setQuery] = useState(searchParams.get('q') || '');
  const category = searchParams.get('category') || '';

  const handleSearch = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (query) params.set('q', query);
    if (category) params.set('category', category);
    setSearchParams(params);
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
      <h1 className="text-2xl font-bold">Search</h1>
      <p className="mt-1 text-gray-500">Find items to compare across any category</p>

      <form onSubmit={handleSearch} className="mt-6 flex flex-col gap-3 sm:flex-row">
        <input
          className="input-field flex-1"
          placeholder="Search products, colleges, restaurants..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <select
          className="input-field sm:w-48"
          value={category}
          onChange={(e) => {
            const params = new URLSearchParams(searchParams);
            if (e.target.value) params.set('category', e.target.value);
            else params.delete('category');
            setSearchParams(params);
          }}
        >
          <option value="">All categories</option>
          {CATEGORY_TYPES.map((c) => (
            <option key={c.value} value={c.value}>{c.label}</option>
          ))}
        </select>
        <button type="submit" className="btn-primary">Search</button>
      </form>

      <div className="mt-12 text-center text-gray-400">
        <p>Search results will appear here once the backend search module is connected.</p>
        {query && (
          <p className="mt-2 text-sm">
            Query: <span className="font-medium text-gray-600">&quot;{query}&quot;</span>
            {category && <> in <span className="font-medium text-gray-600">{category}</span></>}
          </p>
        )}
      </div>
    </div>
  );
}
