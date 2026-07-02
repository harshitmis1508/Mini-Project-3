import { useEffect, useState } from 'react';
import api, { parseApiError } from '../api/client';
import { useToast } from '../components/Toast';
import { validateRequiredSelect, validateMonth, validateYear, validateBudgetAmount, formatCurrency, MONTH_NAMES } from '../validators';

const EMPTY_FORM = { departmentId: '', budgetMonth: '', budgetYear: '', budgetAmount: '' };

export default function Budgets() {
  const { pushToast } = useToast();
  const [departments, setDepartments] = useState([]);
  const [budgets, setBudgets] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  async function loadAll() {
    setLoading(true);
    try {
      const [deptRes, budgetRes] = await Promise.all([api.get('/departments'), api.get('/budgets')]);
      setDepartments(deptRes.data);
      setBudgets(budgetRes.data);
    } catch (err) {
      setServerError(parseApiError(err).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { loadAll(); }, []);

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
    setErrors((e) => ({ ...e, [field]: '' }));
    setServerError('');
  }

  function validateAll() {
    const next = {
      departmentId: validateRequiredSelect(form.departmentId, 'Department'),
      budgetMonth: validateMonth(form.budgetMonth),
      budgetYear: validateYear(form.budgetYear),
      budgetAmount: validateBudgetAmount(form.budgetAmount),
    };
    setErrors(next);
    return Object.values(next).every((v) => !v);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setServerError('');
    if (!validateAll()) return;

    setSubmitting(true);
    try {
      await api.post('/budgets', {
        departmentId: Number(form.departmentId),
        budgetMonth: Number(form.budgetMonth),
        budgetYear: Number(form.budgetYear),
        budgetAmount: Number(form.budgetAmount),
      });
      pushToast('Budget saved successfully.', 'success');
      setForm(EMPTY_FORM);
      setErrors({});
      loadAll();
    } catch (err) {
      const { message, fields } = parseApiError(err);
      setServerError(message);
      setErrors((e) => ({ ...e, ...fields }));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <div className="page-head">
        <div className="page-eyebrow">Finance Manager · Budget Control</div>
        <h1 className="page-title">Department Budget Management</h1>
        <p className="page-sub">
          Each department may have only one budget per month/year. A budget can never be set below
          the total already approved for that period.
        </p>
      </div>

      <div className="card card-pad">
        {serverError && <div className="banner error">⚠ {serverError}</div>}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-grid">
            <div className="field">
              <label>Department <span className="req">*</span></label>
              <select
                value={form.departmentId}
                onChange={(e) => update('departmentId', e.target.value)}
                className={errors.departmentId ? 'invalid' : ''}
              >
                <option value="">Select department</option>
                {departments.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
              </select>
              {errors.departmentId && <span className="field-error">{errors.departmentId}</span>}
            </div>

            <div className="field">
              <label>Month <span className="req">*</span></label>
              <select
                value={form.budgetMonth}
                onChange={(e) => update('budgetMonth', e.target.value)}
                className={errors.budgetMonth ? 'invalid' : ''}
              >
                <option value="">Select month</option>
                {MONTH_NAMES.map((m, idx) => <option key={m} value={idx + 1}>{m}</option>)}
              </select>
              {errors.budgetMonth && <span className="field-error">{errors.budgetMonth}</span>}
            </div>

            <div className="field">
              <label>Year <span className="req">*</span></label>
              <input
                type="number" placeholder="YYYY" min="2000" max="2100"
                value={form.budgetYear}
                onChange={(e) => update('budgetYear', e.target.value)}
                className={errors.budgetYear ? 'invalid' : ''}
              />
              {errors.budgetYear && <span className="field-error">{errors.budgetYear}</span>}
            </div>

            <div className="field">
              <label>Budget Amount (₹) <span className="req">*</span></label>
              <input
                type="number" inputMode="decimal" step="0.01" min="0.01" placeholder="0.00"
                value={form.budgetAmount}
                onChange={(e) => update('budgetAmount', e.target.value)}
                className={`amount-input ${errors.budgetAmount ? 'invalid' : ''}`}
              />
              {errors.budgetAmount ? (
                <span className="field-error">{errors.budgetAmount}</span>
              ) : (
                <span className="field-hint">Must be greater than zero</span>
              )}
            </div>
          </div>

          <div className="row-gap spacer-top">
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Saving…' : 'Save Budget'}
            </button>
            <button type="button" className="btn btn-ghost" onClick={() => { setForm(EMPTY_FORM); setErrors({}); setServerError(''); }} disabled={submitting}>
              Clear
            </button>
          </div>
        </form>
      </div>

      <div className="card card-pad spacer-top">
        <div className="section-title">Existing Budgets</div>
        <div className="table-wrap">
          <table className="ledger">
            <thead>
              <tr><th>Department</th><th>Month</th><th>Year</th><th>Budget Amount</th></tr>
            </thead>
            <tbody>
              {loading && <tr><td colSpan={4} className="empty-row">Loading budgets…</td></tr>}
              {!loading && budgets.length === 0 && <tr><td colSpan={4} className="empty-row">No budgets defined yet.</td></tr>}
              {!loading && budgets
                .slice()
                .sort((a, b) => b.budgetYear - a.budgetYear || b.budgetMonth - a.budgetMonth || a.departmentName.localeCompare(b.departmentName))
                .map((b) => (
                  <tr key={b.id}>
                    <td>{b.departmentName}</td>
                    <td>{MONTH_NAMES[b.budgetMonth - 1] || b.budgetMonth}</td>
                    <td className="num">{b.budgetYear}</td>
                    <td className="num">₹{formatCurrency(b.budgetAmount)}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
