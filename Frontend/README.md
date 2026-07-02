# Expense Approval System — Frontend

React + Vite single-page app for the Department Expense Approval System.

## Setup

```bash
npm install
npm run dev
```

App runs at `http://localhost:5173`. It talks to the Spring Boot backend at
`http://localhost:8080/api` (configurable via `.env` → `VITE_API_BASE_URL`).

Make sure the backend's CORS config (`WebConfig.java`) allows `http://localhost:5173`
(already included).

## Features

- **Role-based access** — a top-right dropdown switches between **Employee** and
  **Finance Manager**. Tabs that don't belong to the active role are locked/hidden,
  and direct navigation to a restricted route shows an access-denied card. This is a
  UI-level role switch (no login system was requested); wire it to real auth/JWT
  later by replacing `RoleContext`'s `setRole` source with your auth claims.
- **Light/dark theme** toggle (moon/sun icon in the top bar), persisted across reloads.
- **Submit Claim** (Employee) — full client-side validation, mirrored from backend rules.
- **Review** (Finance Manager) — approve/reject pending claims; rejecting requires a remark.
- **All Claims** — filterable ledger (department, status, category, month, year), visible to both roles.
- **Budgets** (Finance Manager) — create/list monthly department budgets.
- **Summary** (Finance Manager) — monthly budget utilisation dashboard with totals and progress bars.

## Validation rules enforced (client + mirrored server-side)

- No negative or zero amounts; max 2 decimal places; sane upper bound.
- Expense date must be a valid date and cannot be in the future.
- Employee/department names: letters, spaces, apostrophes and hyphens only.
- Month must be 1–12; year must be a 4-digit value between 2000–2100.
- All required fields enforced before submit; server errors are surfaced inline.
- Rejecting a claim requires a remark.
- Budget amount must be greater than zero, and the backend additionally rejects:
  - duplicate budgets for the same department/month/year,
  - approving a claim that would exceed the monthly budget,
  - submitting a single claim larger than the department's whole monthly budget,
  - lowering a budget below what's already approved for that period.
