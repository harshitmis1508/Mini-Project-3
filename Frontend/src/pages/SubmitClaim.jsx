import { useEffect, useState } from 'react';
import api, { parseApiError } from '../api/client';
import { useToast } from '../components/Toast';
import {
  validateEmployeeName,
  validateRequiredSelect,
  validateCategory,
  validateAmount,
  validateExpenseDate,
  validateDescription,
  todayIsoDate,
} from '../validators';

const CATEGORIES = ['Travel', 'Meals & Entertainment', 'Office Supplies', 'Software & Subscriptions', 'Training', 'Lodging', 'Other'];

const EMPTY_FORM = {
  employeeName: '',
  departmentId: '',
  category: '',
  amount: '',
  expenseDate: '',
  description: '',
};

export default function SubmitClaim() {
  const { pushToast } = useToast();
  const [departments, setDepartments] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [serverError, setServerError] = useState('');

  useEffect(() => {
    api.get('/departments').then((res) => setDepartments(res.data)).catch(() => {});
  }, []);

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
    setErrors((e) => ({ ...e, [field]: '' }));
    setServerError('');
  }

  function validateAll() {
    const next = {
      employeeName: validateEmployeeName(form.employeeName),
      departmentId: validateRequiredSelect(form.departmentId, 'Department'),
      category: validateCategory(form.category),
      amount: validateAmount(form.amount),
      expenseDate: validateExpenseDate(form.expenseDate),
      description: validateDescription(form.description),
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
      await api.post('/claims', {
        employeeName: form.employeeName.trim(),
        departmentId: Number(form.departmentId),
        category: form.category,
        amount: Number(form.amount),
        expenseDate: form.expenseDate,
        description: form.description.trim() || null,
      });
      pushToast('Claim submitted successfully and is now Pending review.', 'success');
      setForm(EMPTY_FORM);
      setErrors({});
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
        <div className="page-eyebrow">Employee · Expense Claims</div>
        <h1 className="page-title">Submit an Expense Claim</h1>
        <p className="page-sub">Fill in the details below. New claims always start in the Pending status.</p>
      </div>

      <div className="card card-pad">
        {serverError && <div className="banner error">⚠ {serverError}</div>}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-grid">
            <div className="field">
              <label>Employee Name <span className="req">*</span></label>
              <input
                type="text"
                placeholder="e.g. Aditi Sharma"
                value={form.employeeName}
                onChange={(e) => update('employeeName', e.target.value)}
                className={errors.employeeName ? 'invalid' : ''}
                maxLength={100}
              />
              {errors.employeeName && <span className="field-error">{errors.employeeName}</span>}
            </div>

            <div className="field">
              <label>Department <span className="req">*</span></label>
              <select
                value={form.departmentId}
                onChange={(e) => update('departmentId', e.target.value)}
                className={errors.departmentId ? 'invalid' : ''}
              >
                <option value="">Select department</option>
                {departments.map((d) => (
                  <option key={d.id} value={d.id}>{d.name}</option>
                ))}
              </select>
              {errors.departmentId && <span className="field-error">{errors.departmentId}</span>}
            </div>

            <div className="field">
              <label>Expense Category <span className="req">*</span></label>
              <select
                value={form.category}
                onChange={(e) => update('category', e.target.value)}
                className={errors.category ? 'invalid' : ''}
              >
                <option value="">Select category</option>
                {CATEGORIES.map((c) => (
                  <option key={c} value={c}>{c}</option>
                ))}
              </select>
              {errors.category && <span className="field-error">{errors.category}</span>}
            </div>

            <div className="field">
              <label>Amount (₹) <span className="req">*</span></label>
              <input
                type="number"
                inputMode="decimal"
                step="0.01"
                min="0.01"
                placeholder="0.00"
                value={form.amount}
                onChange={(e) => update('amount', e.target.value)}
                className={`amount-input ${errors.amount ? 'invalid' : ''}`}
              />
              {errors.amount ? (
                <span className="field-error">{errors.amount}</span>
              ) : (
                <span className="field-hint">Must be greater than zero, up to 2 decimal places</span>
              )}
            </div>

            <div className="field">
              <label>Expense Date <span className="req">*</span></label>
              <input
                type="date"
                value={form.expenseDate}
                max={todayIsoDate()}
                onChange={(e) => update('expenseDate', e.target.value)}
                className={errors.expenseDate ? 'invalid' : ''}
              />
              {errors.expenseDate ? (
                <span className="field-error">{errors.expenseDate}</span>
              ) : (
                <span className="field-hint">Cannot be a future date</span>
              )}
            </div>
          </div>

          <div className="field spacer-top">
            <label>Description (optional)</label>
            <textarea
              rows={3}
              maxLength={500}
              placeholder="Brief note about this expense…"
              value={form.description}
              onChange={(e) => update('description', e.target.value)}
              className={errors.description ? 'invalid' : ''}
            />
            {errors.description && <span className="field-error">{errors.description}</span>}
          </div>

          <div className="row-gap spacer-top">
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Submitting…' : 'Submit Claim'}
            </button>
            <button
              type="button"
              className="btn btn-ghost"
              onClick={() => { setForm(EMPTY_FORM); setErrors({}); setServerError(''); }}
              disabled={submitting}
            >
              Clear
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
