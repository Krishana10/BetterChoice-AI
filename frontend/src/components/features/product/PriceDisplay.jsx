import { formatPrice } from '../../../utils/formatters';

export default function PriceDisplay({ price, currency = 'INR', className = '' }) {
  return (
    <span className={`text-lg font-bold text-emerald-600 ${className}`}>
      {formatPrice(price, currency)}
    </span>
  );
}
