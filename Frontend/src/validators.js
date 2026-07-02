// Shared validation helpers — mirror the backend's real-world constraints so the
// person gets instant feedback before a round trip to the server.

export const NAME_PATTERN = /^[A-Za-z][A-Za-z .'-]*$/;
export const TWO_DECIMALS_PATTERN = /^\d{1,13}(\.\d{1,2})?$/;

export function todayIsoDate() {
  const d = new Date();
  const tz = d.getTimezoneOffset() * 60000;
  return new Date(d - tz).toISOString().slice(0, 10);
}

export function validateEmployeeName(value) {
  if (!value || !value.trim()) return 'Employee name is required';
  const v = value.trim();
  if (v.length < 2 || v.length > 100) return 'Name must be between 2 and 100 characters';
  if (!NAME_PATTERN.test(v)) return 'Name may only contain letters, spaces, apostrophes and hyphens';
  return '';
}

export function validateRequiredSelect(value, label) {
  if (value === '' || value === null || value === undefined) return `${label} is required`;
  return '';
}

export function validateCategory(value) {
  if (!value || !value.trim()) return 'Expense category is required';
  if (value.trim().length > 100) return 'Category must not exceed 100 characters';
  return '';
}

export function validateAmount(value, { max = 99999999999.99 } = {}) {
  if (value === '' || value === null || value === undefined) return 'Amount is required';
  const num = Number(value);
  if (Number.isNaN(num)) return 'Amount must be a valid number';
  if (num <= 0) return 'Amount must be greater than zero (no negative or zero amounts)';
  if (num > max) return 'Amount is unrealistically large';
  if (!TWO_DECIMALS_PATTERN.test(String(value))) return 'Amount can have at most 2 decimal places';
  return '';
}

export function validateExpenseDate(value) {
  if (!value) return 'Expense date is required';
  if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) return 'Date must be in YYYY-MM-DD format';
  if (value > todayIsoDate()) return 'Expense date cannot be in the future';
  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) return 'Enter a valid calendar date';
  return '';
}

export function validateDescription(value) {
  if (value && value.length > 500) return 'Description must not exceed 500 characters';
  return '';
}

export function validateMonth(value) {
  if (value === '' || value === null || value === undefined) return 'Month is required';
  const num = Number(value);
  if (!Number.isInteger(num) || num < 1 || num > 12) return 'Month must be between 1 and 12';
  return '';
}

export function validateYear(value) {
  if (value === '' || value === null || value === undefined) return 'Year is required';
  const num = Number(value);
  if (!Number.isInteger(num) || String(value).length !== 4) return 'Enter a valid 4-digit year';
  if (num < 2000 || num > 2100) return 'Year must be between 2000 and 2100';
  return '';
}

export function validateBudgetAmount(value) {
  return validateAmount(value);
}

export function validateRemark(value, { required = false } = {}) {
  if (required && (!value || !value.trim())) return 'A remark is required';
  if (value && value.length > 500) return 'Remark must not exceed 500 characters';
  return '';
}

export const MONTH_NAMES = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December',
];

export function formatCurrency(value) {
  const num = Number(value || 0);
  return num.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}
