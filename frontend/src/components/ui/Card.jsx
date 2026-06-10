export default function Card({ children, className = '', hover = false, ...props }) {
  return (
    <div
      className={`card ${hover ? 'transition hover:border-brand-300 hover:shadow-md' : ''} ${className}`}
      {...props}
    >
      {children}
    </div>
  );
}
