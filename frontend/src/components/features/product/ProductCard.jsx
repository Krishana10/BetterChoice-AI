import { Link } from 'react-router-dom';
import Badge from '../../ui/Badge';
import StarRating from '../../ui/StarRating';
import PriceDisplay from './PriceDisplay';
export default function ProductCard({ product }) {
  const sourceLabel = product.sourceCount > 1
    ? `${product.sourceCount} platforms`
    : product.sourceCount === 1
      ? '1 platform'
      : null;

  return (
    <Link
      to={`/products/${product.slug}`}
      className="group flex flex-col overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm transition hover:-translate-y-0.5 hover:border-brand-300 hover:shadow-md"
    >
      <div className="relative aspect-square overflow-hidden bg-gray-50 p-4">
        {product.imageUrl ? (
          <img
            src={product.imageUrl}
            alt={product.name}
            className="h-full w-full object-contain transition group-hover:scale-105"
            loading="lazy"
          />
        ) : (
          <div className="flex h-full items-center justify-center text-gray-300">No image</div>
        )}
        {product.sourceCount > 1 && (
          <div className="absolute left-2 top-2">
            <Badge variant="success">Compare prices</Badge>
          </div>
        )}
      </div>

      <div className="flex flex-1 flex-col gap-2 p-4">
        {product.brand && (
          <p className="text-xs font-medium uppercase tracking-wide text-gray-500">{product.brand}</p>
        )}
        <h3 className="line-clamp-2 font-semibold text-gray-900 group-hover:text-brand-700">
          {product.name}
        </h3>

        <div className="mt-auto space-y-2">
          <PriceDisplay price={product.basePrice} currency={product.currency} />
          {sourceLabel && (
            <p className="text-xs text-gray-500">Listed on {sourceLabel}</p>
          )}
          <StarRating rating={product.ratingAvg} reviewCount={product.reviewCount} />
          {product.categoryName && (
            <Badge variant="brand">{product.categoryName}</Badge>
          )}
        </div>
      </div>
    </Link>
  );
}
