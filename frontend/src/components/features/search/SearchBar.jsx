import Button from '../../ui/Button';

export default function SearchBar({
  query,
  onQueryChange,
  onSubmit,
  placeholder = 'Search laptops, mobiles, restaurants...',
}) {
  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-3 sm:flex-row">
      <div className="relative flex-1">
        <input
          className="input-field w-full rounded-full py-3 pl-5 pr-12"
          placeholder={placeholder}
          value={query}
          onChange={(e) => onQueryChange(e.target.value)}
          aria-label="Search query"
        />
        <button
          type="submit"
          className="absolute right-2 top-1/2 -translate-y-1/2 rounded-full bg-brand-600 p-2 text-white transition hover:bg-brand-700"
          aria-label="Search"
        >
          <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </button>
      </div>
      <Button type="submit" className="hidden sm:inline-flex sm:rounded-full sm:px-8">
        Search
      </Button>
    </form>
  );
}
