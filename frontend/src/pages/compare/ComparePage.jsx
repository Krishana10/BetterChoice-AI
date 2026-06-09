export default function ComparePage() {
  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
      <h1 className="text-2xl font-bold">Compare</h1>
      <p className="mt-1 text-gray-500">Select items to compare side-by-side with AI insights</p>

      <div className="mt-8 card text-center text-gray-400">
        <p>Comparison builder UI — connect to POST /api/v1/comparisons</p>
        <p className="mt-2 text-sm">Add products from search, then run AI-powered analysis</p>
      </div>
    </div>
  );
}
