# Department Expense Approval System — Backend

Java 17, Spring Boot 3.2, Spring Data JPA, MySQL.

## Setup
1. Start MySQL locally.
2. Run `src/main/resources/schema.sql` against your MySQL instance
   (creates `expense_db` with seed data), or just let Hibernate create
   tables automatically on startup (`ddl-auto=update` is already set).
3. Update `src/main/resources/application.properties` with your MySQL
   username/password (defaults to `root/root`).
4. Run:
   ```
   mvn spring-boot:run
   ```
   Backend starts on `http://localhost:8080`.

## Structure
```
src/main/java/com/company/expense/
├── entity/        JPA entities
├── repository/    Spring Data JPA repositories
├── service/       Service interfaces
├── service/impl/  Business logic implementations
├── controller/     REST controllers
├── dto/           Request/response DTOs
├── config/        CORS configuration
└── exception/     Global exception handling
```

## API Overview
| Method | Endpoint                                  | Purpose                       |
|--------|---------------------------------------------|--------------------------------|
| POST   | /api/departments                            | Create department              |
| GET    | /api/departments                            | List departments               |
| POST   | /api/budgets                                | Create monthly budget          |
| PUT    | /api/budgets/{id}                           | Update budget amount           |
| GET    | /api/budgets?month=&year=                   | List budgets                   |
| POST   | /api/claims                                 | Submit expense claim           |
| GET    | /api/claims?departmentId=&status=&...       | Search/filter claims           |
| PUT    | /api/claims/{id}/approve                    | Approve a pending claim        |
| PUT    | /api/claims/{id}/reject                     | Reject a pending claim         |
| GET    | /api/summary?month=&year=                   | Monthly summary, all depts     |
| GET    | /api/summary/department/{id}?month=&year=   | Monthly summary, one dept      |

Expects the frontend (or any client) at `http://localhost:3000` — see
`config/WebConfig.java` to change allowed CORS origin.

## Update: Real-world validation hardening (this revision)

Backend additions in this pass:
- `ExpenseClaimRequest`: name pattern, length limits, amount max-2-decimals (`@Digits`), upper bound, expense date can't be in the future (`@PastOrPresent`).
- `DepartmentBudgetRequest`: year range 2000–2100, amount max-2-decimals, upper bound.
- `DepartmentRequest`: name pattern + length limits.
- `ClaimReviewRequest`: remark length limit; rejecting now requires a non-blank remark (enforced in `ExpenseClaimServiceImpl`).
- Submitting a claim now rejects if the amount exceeds the department's entire monthly budget (when one exists for that period).
- Creating/updating a budget now rejects if the new amount is less than the claims already approved for that department/month/year — i.e. **budget amount is always kept ≥ approved claim totals**, on top of the existing FR-4 check that blocks approving a claim that would exceed the budget.
- `WebConfig` CORS now also allows the Vite dev server on `http://localhost:5173`.
- `GlobalExceptionHandler` now returns clean 400s for malformed query params and unreadable JSON bodies instead of a 500.

A matching React/Vite frontend (with role-based UI, light/dark theme, and mirrored client-side validation) is provided separately.
