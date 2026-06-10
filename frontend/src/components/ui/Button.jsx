const variants = {
  primary: 'btn-primary',
  secondary: 'btn-secondary',
  ghost: 'rounded-lg px-4 py-2 font-medium text-gray-600 transition hover:bg-gray-100',
};

export default function Button({
  children,
  variant = 'primary',
  className = '',
  type = 'button',
  disabled = false,
  ...props
}) {
  return (
    <button
      type={type}
      disabled={disabled}
      className={`${variants[variant] || variants.primary} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
