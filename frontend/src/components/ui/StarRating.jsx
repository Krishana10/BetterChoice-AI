import { formatRating, formatReviewCount } from '../../utils/formatters';

export default function StarRating({ rating, reviewCount, size = 'sm' }) {
  const stars = 5;
  const filled = Math.round(Number(rating) || 0);
  const textSize = size === 'lg' ? 'text-sm' : 'text-xs';

  return (
    <div className={`flex items-center gap-1 ${textSize} text-gray-600`}>
      <span className="font-medium text-gray-900">{formatRating(rating)}</span>
      <span className="text-amber-400" aria-hidden>
        {'★'.repeat(filled)}{'☆'.repeat(stars - filled)}
      </span>
      {reviewCount != null && (
        <span className="text-gray-400">({formatReviewCount(reviewCount)})</span>
      )}
    </div>
  );
}
