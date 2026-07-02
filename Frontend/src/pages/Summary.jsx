import { useEffect, useState } from 'react';
import api, { parseApiError } from '../api/client';
import { formatCurrency, MONTH_NAMES, todayIsoDate, validateMonth, validateYear } from '../validators';

export default function Summary() {
  const today = new Date(todayIsoDate());
  const [month, setMonth] = useState(today.getMonth() + 1);
  const [year, setYear] = useState(today.getFullYear());
  const [errors, setErrors] = useState({});
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  async function load() {
    const next = { month: validateMonth(month), year: validateYear(year) };
    setErrors(next);
    if (Object.values(next).some(Boolean)) return;

    setLoading(true);
    setError('');
    try {
      const res = await api.get('/summary', { params: { month, year } });
      setRows(res.data);
    } catch (err) {
      setError(parseApiError(err).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); /* eslint-disable-next-line */ }, []);

  const totals = rows.reduce(
    (acc, r) => ({
      budget: acc.budget + Number(r.monthlyBudget || 0),
      approved: acc.approved + Number(r.totalApprovedExpense || 0),
      pending: acc.pending + Number(r.totalPendingExpense || 0),
    }),
    { budget: 0, approved: 0, pending: 0 }
  );

  return (
    <div>
      <div className="page-head">
        <div className="page-eyebrow">Finance Manager · Monthly Overview</div>
        <h1 className="page-title">Finance Summary</h1>
        <p className="page-sub">Budget utilisation and claim counts across all departments for the selected period.</p>
      </div>

      <div className="card card-pad">
        <div className="filters-bar">
          <div className="field">
            <label>Month</label>
            <select value={month} onChange={(e) => setMonth(Number(e.target.value))}>
              {MONTH_NAMES.map((m, idx) => <option key={m} value={idx + 1}>{m}</option>)}
            </select>
          </div>
          <div className="field">
            <label>Year</label>
            <input
              type="number" min="2000" max="2100"
              value={year}
              onChange={(e) => setYear(e.target.value)}
              className={errors.year ? 'invalid' : ''}
            />
            {errors.year && <span className="field-error">{errors.year}</span>}
          </div>
          <button className="btn btn-primary" onClick={load}>View Summary</button>
        </div>

        {error && <div className="banner error">⚠ {error}</div>}

        {!loading && rows.length > 0 && (
          <div className="stat-grid">
            <div className="stat-tile">
              <div className="stat-label">Total Budget</div>
              <div className="stat-value accent">₹{formatCurrency(totals.budget)}</div>
            </div>
            <div className="stat-tile">
              <div className="stat-label">Approved Expense</div>
              <div className="stat-value positive">₹{formatCurrency(totals.approved)}</div>
            </div>
            <div className="stat-tile">
              <div className="stat-label">Pending Expense</div>
              <div className="stat-value">₹{formatCurrency(totals.pending)}</div>
            </div>
            <div className="stat-tile">
              <div className="stat-label">Remaining Budget</div>
              <div className={`stat-value ${totals.budget - totals.approved < 0 ? 'negative' : 'positive'}`}>
                ₹{formatCurrency(totals.budget - totals.approved)}
              </div>
            </div>
          </div>
        )}

        <div className="table-wrap">
          <table className="ledger">
            <thead>
              <tr>
                <th>Department</th><th>Budget</th><th>Approved</th><th>Pending</th>
                <th>Remaining</th><th>Pending #</th><th>Approved #</th><th>Rejected #</th><th>Utilisation</th>
              </tr>
            </thead>
            <tbody>
              {loading && <tr><td colSpan={9} className="empty-row">Loading summary…</td></tr>}
              {!loading && rows.length === 0 && <tr><td colSpan={9} className="empty-row">No data for this period.</td></tr>}
              {!loading && rows.map((r) => {
                const pct = r.monthlyBudget > 0 ? Math.min(100, (r.totalApprovedExpense / r.monthlyBudget) * 100) : 0;
                const over = Number(r.remainingBudget) < 0;
                return (
                  <tr key={r.departmentId}>
                    <td>{r.departmentName}</td>
                    <td className="num">₹{formatCurrency(r.monthlyBudget)}</td>
                    <td className="num">₹{formatCurrency(r.totalApprovedExpense)}</td>
                    <td className="num">₹{formatCurrency(r.totalPendingExpense)}</td>
                    <td className={`num ${over ? 'stamp-rejected' : ''}`}>₹{formatCurrency(r.remainingBudget)}</td>
                    <td className="num">{r.pendingCount}</td>
                    <td className="num">{r.approvedCount}</td>
                    <td className="num">{r.rejectedCount}</td>
                    <td style={{ minWidth: 110 }}>
                      <div className="budget-bar-track">
                        <div className={`budget-bar-fill ${over ? 'over' : ''}`} style={{ width: `${pct}%` }} />
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
