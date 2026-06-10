import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import Badge from '../../components/ui/Badge';
import StarRating from '../../components/ui/StarRating';
import PriceDisplay from '../../components/features/product/PriceDisplay';
import { productService } from '../../services';
import { formatPrice, platformLabel } from '../../utils/formatters';

export default function ProductDetailPage() {
  const { slug } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setLoading(true);
      setError(null);
      try {
        const { data } = await productService.getBySlug(slug);
        if (!cancelled) setProduct(data.data);
      } catch (err) {
        if (!cancelled) {
          setError(err.response?.data?.error?.message || 'Product not found');
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    load();
    return () => { cancelled = true; };
  }, [slug]);

  if (loading) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
        <div className="animate-pulse space-y-4">
          <div className="h-8 w-1/3 rounded bg-gray-200" />
          <div className="grid gap-8 lg:grid-cols-2">
            <div className="aspect-square rounded-xl bg-gray-200" />
            <div className="space-y-4">
              <div className="h-6 w-full rounded bg-gray-200" />
              <div className="h-4 w-2/3 rounded bg-gray-200" />
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-16 text-center sm:px-6">
        <p className="text-gray-500">{error || 'Product not found'}</p>
        <Link to="/search" className="mt-4 inline-block text-brand-600 hover:underline">
          Back to search
        </Link>
      </div>
    );
  }

  const lowestSource = product.sources?.length
    ? [...product.sources].sort((a, b) => (a.price ?? 0) - (b.price ?? 0))[0]
    : null;

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
      <nav className="mb-6 text-sm text-gray-500">
        <Link to="/" className="hover:text-brand-600">Home</Link>
        <span className="mx-2">/</span>
        <Link to="/search" className="hover:text-brand-600">Search</Link>
        <span className="mx-2">/</span>
        <span className="text-gray-900">{product.name}</span>
      </nav>

      <div className="grid gap-8 lg:grid-cols-2">
        <div className="flex items-center justify-center rounded-xl border border-gray-200 bg-gray-50 p-8">
          {product.imageUrl ? (
            <img src={product.imageUrl} alt={product.name} className="max-h-96 object-contain" />
          ) : (
            <span className="text-gray-300">No image</span>
          )}
        </div>

        <div className="space-y-4">
          {product.brand && (
            <p className="text-sm font-medium uppercase tracking-wide text-gray-500">{product.brand}</p>
          )}
          <h1 className="text-3xl font-bold text-gray-900">{product.name}</h1>
          <StarRating rating={product.ratingAvg} reviewCount={product.reviewCount} size="lg" />
          <PriceDisplay price={product.basePrice} currency={product.currency} className="text-2xl" />
          {product.categoryName && <Badge variant="brand">{product.categoryName}</Badge>}
          {product.description && (
            <p className="text-gray-600 leading-relaxed">{product.description}</p>
          )}

          {product.attributes?.length > 0 && (
            <div className="rounded-xl border border-gray-200 p-4">
              <h2 className="mb-3 font-semibold text-gray-900">Specifications</h2>
              <dl className="grid gap-2 sm:grid-cols-2">
                {product.attributes.map((attr) => (
                  <div key={attr.id} className="flex justify-between gap-4 border-b border-gray-100 py-2 text-sm">
                    <dt className="text-gray-500">{attr.key}</dt>
                    <dd className="font-medium text-gray-900">{attr.value}</dd>
                  </div>
                ))}
              </dl>
            </div>
          )}
        </div>
      </div>

      {product.sources?.length > 0 && (
        <section className="mt-12">
          <h2 className="mb-4 text-xl font-bold text-gray-900">Compare prices across platforms</h2>
          <div className="overflow-hidden rounded-xl border border-gray-200">
            <table className="w-full text-left text-sm">
              <thead className="bg-gray-50 text-gray-500">
                <tr>
                  <th className="px-4 py-3 font-medium">Platform</th>
                  <th className="px-4 py-3 font-medium">Price</th>
                  <th className="px-4 py-3 font-medium">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {product.sources.map((source) => (
                  <tr key={source.id} className={source.id === lowestSource?.id ? 'bg-emerald-50/50' : ''}>
                    <td className="px-4 py-3 font-medium text-gray-900">
                      {platformLabel(source.platform)}
                      {source.id === lowestSource?.id && (
                        <Badge variant="success" className="ml-2">Best price</Badge>
                      )}
                    </td>
                    <td className="px-4 py-3 font-semibold text-emerald-600">
                      {formatPrice(source.price, product.currency)}
                    </td>
                    <td className="px-4 py-3">
                      {source.url ? (
                        <a
                          href={source.url}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-brand-600 hover:underline"
                        >
                          View deal ↗
                        </a>
                      ) : (
                        <span className="text-gray-400">—</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      )}
    </div>
  );
}
