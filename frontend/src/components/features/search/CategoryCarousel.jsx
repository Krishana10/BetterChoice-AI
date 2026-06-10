import { Link } from 'react-router-dom';
import Skeleton from '../../ui/Skeleton';

const CATEGORY_ICONS = {
  electronics: '💻',
  laptops: '🖥️',
  restaurants: '🍽️',
  'online-courses': '📚',
  colleges: '🎓',
};

export default function CategoryCarousel({ categories, loading }) {
  if (loading) {
    return (
      <div className="flex gap-4 overflow-x-auto pb-2">
        {Array.from({ length: 5 }).map((_, i) => (
          <Skeleton key={i} className="h-32 w-28 shrink-0" />
        ))}
      </div>
    );
  }

  return (
    <div className="flex gap-4 overflow-x-auto pb-2 scrollbar-thin">
      {categories.map((cat) => (
        <Link
          key={cat.id}
          to={`/search?categoryId=${cat.id}`}
          className="flex w-28 shrink-0 flex-col items-center gap-2 rounded-xl border border-gray-200 bg-white p-4 transition hover:border-brand-300 hover:shadow-md"
        >
          <span className="text-3xl" aria-hidden>
            {CATEGORY_ICONS[cat.slug] || '📦'}
          </span>
          <span className="text-center text-sm font-medium text-gray-900">{cat.name}</span>
        </Link>
      ))}
    </div>
  );
}
