# Frontend Folder Structure

```
frontend/src/
├── app/
│   ├── App.jsx                 # Root app + router
│   └── ProtectedRoute.jsx      # Auth guard
├── assets/                     # Static images, icons
├── components/
│   ├── ui/                     # Design system (Button, Input, Card, Modal)
│   ├── layout/                 # Navbar, Footer, Layout
│   └── features/               # Domain components
│       ├── comparison/         # ComparisonTable, ComparisonBuilder
│       ├── product/            # ProductCard, ProductGrid
│       ├── search/             # SearchBar, FilterPanel
│       └── ai/                 # AIInsightPanel, ChatWidget (Phase 3)
├── constants/
│   └── index.js                # Routes, roles, category types
├── context/
│   └── AuthContext.jsx         # Global auth state
├── hooks/
│   ├── useAuth.js              # Re-export from context
│   ├── useComparison.js        # TODO
│   ├── useSearch.js            # TODO
│   └── useDebounce.js          # TODO
├── pages/
│   ├── auth/                   # Login, Register
│   ├── home/                   # Landing page
│   ├── search/                 # Search results
│   ├── compare/                # Comparison builder
│   ├── dashboard/              # User dashboard
│   └── profile/                # TODO: User profile
├── services/
│   ├── api.js                  # Axios instance + interceptors
│   └── index.js                # Domain API services
├── utils/
│   ├── formatters.js           # TODO: price, date formatting
│   └── validators.js           # TODO: form validation helpers
├── index.css                   # Tailwind + component classes
└── main.jsx                    # Entry point
```

## Conventions

- **Pages** = route-level, fetch data, compose features
- **features/** = business UI components tied to a domain
- **ui/** = generic, reusable, no API calls
- **services/** = all HTTP calls; components never import axios directly

## Planned Additions (Phase 1)

- `components/features/comparison/ComparisonTable.jsx`
- `components/features/product/ProductCard.jsx`
- `hooks/useDebounce.js` for search
- `pages/products/ProductDetailPage.jsx`
