const inrFormatter = new Intl.NumberFormat('en-IN', {
  style: 'currency',
  currency: 'INR',
  maximumFractionDigits: 0,
});

export function formatPrice(amount, currency = 'INR') {
  if (amount == null) return '—';
  if (currency === 'INR') return inrFormatter.format(amount);
  return new Intl.NumberFormat('en-US', { style: 'currency', currency }).format(amount);
}

export function formatRating(rating) {
  if (rating == null) return '0.0';
  return Number(rating).toFixed(1);
}

export function formatReviewCount(count) {
  if (count == null || count === 0) return '0';
  if (count >= 1000) return `${(count / 1000).toFixed(1)}K`;
  return String(count);
}

export function platformLabel(platform) {
  const labels = {
    AMAZON: 'Amazon',
    FLIPKART: 'Flipkart',
    CROMA: 'Croma',
    RELIANCE_DIGITAL: 'Reliance Digital',
    MYNTRA: 'Myntra',
    AJIO: 'AJIO',
  };
  return labels[platform] || platform;
}
