import { Link } from 'react-router-dom';
import { CATEGORY_TYPES } from '../../constants';

export default function HomePage() {
  return (
    <div>
      {/* Hero */}
      <section className="bg-gradient-to-br from-brand-900 via-brand-700 to-brand-600 text-white">
        <div className="mx-auto max-w-7xl px-4 py-24 sm:px-6 lg:py-32">
          <h1 className="max-w-3xl text-4xl font-bold tracking-tight sm:text-5xl lg:text-6xl">
            Compare anything. Choose smarter with AI.
          </h1>
          <p className="mt-6 max-w-2xl text-lg text-brand-100">
            Products, colleges, restaurants, courses, and more — side-by-side comparisons
            powered by intelligent insights from Gemini AI.
          </p>
          <div className="mt-10 flex flex-wrap gap-4">
            <Link to="/search" className="rounded-lg bg-white px-6 py-3 font-semibold text-brand-700 hover:bg-brand-50">
              Start Searching
            </Link>
            <Link to="/register" className="rounded-lg border border-white/30 px-6 py-3 font-semibold hover:bg-white/10">
              Create Free Account
            </Link>
          </div>
        </div>
      </section>

      {/* Categories */}
      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6">
        <h2 className="text-2xl font-bold text-gray-900">Compare across categories</h2>
        <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {CATEGORY_TYPES.map((cat) => (
            <Link
              key={cat.value}
              to={`/search?category=${cat.value}`}
              className="card transition hover:border-brand-300 hover:shadow-md"
            >
              <h3 className="font-semibold text-gray-900">{cat.label}</h3>
              <p className="mt-1 text-sm text-gray-500">AI-powered comparison</p>
            </Link>
          ))}
        </div>
      </section>

      {/* Features */}
      <section className="bg-gray-100 py-16">
        <div className="mx-auto max-w-7xl px-4 sm:px-6">
          <h2 className="text-2xl font-bold text-gray-900">Why BetterChoice AI?</h2>
          <div className="mt-8 grid gap-6 md:grid-cols-3">
            {[
              { title: 'Universal Comparison', desc: 'One platform for products, services, education, and more.' },
              { title: 'AI Insights', desc: 'Gemini-powered summaries, pros/cons, and smart recommendations.' },
              { title: 'Save & Share', desc: 'Keep comparison history and share results with a link.' },
            ].map((f) => (
              <div key={f.title} className="card">
                <h3 className="font-semibold text-gray-900">{f.title}</h3>
                <p className="mt-2 text-sm text-gray-600">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
