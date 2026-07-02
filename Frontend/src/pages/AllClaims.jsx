import { useEffect, useState } from 'react';
import api, { parseApiError } from '../api/client';
import StatusBadge from '../components/StatusBadge';
import { formatCurrency, validateMonth, validateYear } from '../validators';

const EMPTY_FILTERS = { departmentId: '', status: '', category: '', month: '', year: '' };

export default function AllClaims() {
  const [departments, setDepartments] = useState([]);
  const [claims, setClaims] = useState([]);
  const [filters, setFilters] = useState(EMPTY_FILTERS);
  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/departments').then((res) => setDepartments(res.data)).catch(() => {});
  }, []);

  async function load(activeFilters = filters) {
    setLoading(true);
    setError('');
    try {
      const params = {};
      Object.entries(activeFilters).forEach(([k, v]) => { if (v !== '') params[k] = v; });
      const res = await api.get('/claims', { params });
      setClaims(res.data);
    } catch (err) {
      setError(parseApiError(err).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(EMPTY_FILTERS); /* eslint-disable-next-line */ }, []);

  function update(field, value) {
    setFilters((f) => ({ ...f, [field]: value }));
    setFieldErrors((e) => ({ ...e, [field]: '' }));
  }

  function applyFilters() {
    const next = {
      month: filters.month === '' ? '' : validateMonth(filters.month),
      year: filters.year === '' ? '' : validateYear(filters.year),
    };
    setFieldErrors(next);
    if (Object.values(next).some(Boolean)) return;
    load(filters);
  }

  function resetFilters() {
    setFilters(EMPTY_FILTERS);
    setFieldErrors({});
    load(EMPTY_FILTERS);
  }

  return (
    <div>
      <div className="page-head">
        <div className="page-eyebrow">Expense Tracking</div>
        <h1 className="page-title">All Claims</h1>
        <p className="page-sub">Search and filter claims across departments, status, category, and period.</p>
      </div>

      <div className="card card-pad">
        <div className="filters-bar">
          <div className="field">
            <label>Department</label>
            <select value={filters.departmentId} onChange={(e) => update('departmentId', e.target.value)}>
              <option value="">All</option>
              {departments.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
            </select>
          </div>
          <div className="field">
            <label>Status</label>
            <select value={filters.status} onChange={(e) => update('status', e.target.value)}>
              <option value="">All</option>
              <option value="PENDING">Pending</option>
              <option value="APPROVED">Approved</option>
              <option value="REJECTED">Rejected</option>
            </select>
          </div>
          <div className="field">
            <label>Category</label>
            <input type="text" placeholder="e.g. Travel" value={filters.category} onChange={(e) => update('category', e.target.value)} />
          </div>
          <div className="field">
            <label>Month</label>
            <input
              type="number" min="1" max="12" placeholder="1–12"
              value={filters.month}
              onChange={(e) => update('month', e.target.value)}
              className={fieldErrors.month ? 'invalid' : ''}
            />
            {fieldErrors.month && <span className="field-error">{fieldErrors.month}</span>}
          </div>
          <div className="field">
            <label>Year</label>
            <input
              type="number" min="2000" max="2100" placeholder="YYYY"
              value={filters.year}
              onChange={(e) => update('year', e.target.value)}
              className={fieldErrors.year ? 'invalid' : ''}
            />
            {fieldErrors.year && <span className="field-error">{fieldErrors.year}</span>}
          </div>
          <div className="row-gap">
            <button className="btn btn-primary" onClick={applyFilters}>Apply</button>
            <button className="btn btn-ghost" onClick={resetFilters}>Reset</button>
          </div>
        </div>

        {error && <div className="banner error">⚠ {error}</div>}

        <div className="table-wrap">
          <table className="ledger">
            <thead>
              <tr>
                <th>ID</th><th>Employee</th><th>Department</th><th>Category</th>
                <th>Amount</th><th>Date</th><th>Status</th><th>Remark</th>
              </tr>
            </thead>
            <tbody>
              {loading && <tr><td colSpan={8} className="empty-row">Loading claims…</td></tr>}
              {!loading && claims.length === 0 && <tr><td colSpan={8} className="empty-row">No claims match these filters.</td></tr>}
              {!loading && claims.map((c) => (
                <tr key={c.id}>
                  <td className="num">#{c.id}</td>
                  <td>{c.employeeName}</td>
                  <td>{c.departmentName}</td>
                  <td>{c.category}</td>
                  <td className="num">₹{formatCurrency(c.amount)}</td>
                  <td className="num">{c.expenseDate}</td>
                  <td><StatusBadge status={c.status} /></td>
                  <td className="remark-cell">{c.reviewRemark || '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
