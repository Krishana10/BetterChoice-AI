import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import CategoryCarousel from '../../components/features/search/CategoryCarousel';
import ProductGrid from '../../components/features/product/ProductGrid';
import SearchBar from '../../components/features/search/SearchBar';
import { useCategories } from '../../hooks/useCategories';
import { useProducts } from '../../hooks/useProducts';

export default function HomePage() {
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const { categories, loading: categoriesLoading } = useCategories();
  const { products, loading: productsLoading } = useProducts({ page: 0, size: 8 });

  const handleSearch = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (query.trim()) params.set('q', query.trim());
    navigate(`/search?${params.toString()}`);
  };

  return (
    <div>
      <section className="bg-gradient-to-br from-brand-900 via-brand-700 to-brand-600 text-white">
        <div className="mx-auto max-w-7xl px-4 py-20 sm:px-6 lg:py-28">
          <h1 className="max-w-3xl text-4xl font-bold tracking-tight sm:text-5xl">
            Compare anything. Choose smarter.
          </h1>
          <p className="mt-4 max-w-2xl text-lg text-brand-100">
            Search laptops, mobiles, and more — compare prices across Amazon, Flipkart, Croma, and beyond.
          </p>
          <div className="mt-8 max-w-2xl">
            <SearchBar
              query={query}
              onQueryChange={setQuery}
              onSubmit={handleSearch}
              placeholder="Shop for anything..."
            />
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-12 sm:px-6">
        <div className="mb-6 flex items-center justify-between">
          <h2 className="text-xl font-bold text-gray-900">Browse categories</h2>
          <Link to="/search" className="text-sm font-medium text-brand-600 hover:underline">
            View all →
          </Link>
        </div>
        <CategoryCarousel categories={categories} loading={categoriesLoading} />
      </section>

      <section className="bg-gray-50 py-12">
        <div className="mx-auto max-w-7xl px-4 sm:px-6">
          <div className="mb-6 flex items-center justify-between">
            <h2 className="text-xl font-bold text-gray-900">Popular options</h2>
            <Link to="/search" className="text-sm font-medium text-brand-600 hover:underline">
              More →
            </Link>
          </div>
          <ProductGrid products={products} loading={productsLoading} />
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6">
        <h2 className="text-2xl font-bold text-gray-900">Why BetterChoice AI?</h2>
        <div className="mt-8 grid gap-6 md:grid-cols-3">
          {[
            { title: 'Multi-platform prices', desc: 'See the best deal across Indian marketplaces in one grid.' },
            { title: 'Smart comparison', desc: 'Side-by-side specs, ratings, and delivery — ready to compare.' },
            { title: 'AI insights', desc: 'Gemini-powered recommendations coming in Phase 3.' },
          ].map((f) => (
            <div key={f.title} className="card">
              <h3 className="font-semibold text-gray-900">{f.title}</h3>
              <p className="mt-2 text-sm text-gray-600">{f.desc}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
