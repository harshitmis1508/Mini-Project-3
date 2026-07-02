import { useEffect, useState } from 'react';
import api, { parseApiError } from '../api/client';
import { useToast } from '../components/Toast';
import { validateRemark, formatCurrency } from '../validators';
// Note: status column omitted here since this view only ever shows PENDING claims.

export default function Review() {
  const { pushToast } = useToast();
  const [claims, setClaims] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [remarkDraft, setRemarkDraft] = useState({});
  const [remarkError, setRemarkError] = useState({});
  const [busyId, setBusyId] = useState(null);

  async function load() {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/claims', { params: { status: 'PENDING' } });
      setClaims(res.data);
    } catch (err) {
      setError(parseApiError(err).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function act(claim, action) {
    const remark = (remarkDraft[claim.id] || '').trim();
    const remarkErr = validateRemark(remark, { required: action === 'reject' });
    if (remarkErr) {
      setRemarkError((e) => ({ ...e, [claim.id]: remarkErr }));
      return;
    }

    setBusyId(claim.id);
    try {
      await api.put(`/claims/${claim.id}/${action}`, { reviewRemark: remark || null });
      pushToast(
        `Claim #${claim.id} ${action === 'approve' ? 'approved' : 'rejected'}.`,
        action === 'approve' ? 'success' : 'error'
      );
      setClaims((prev) => prev.filter((c) => c.id !== claim.id));
    } catch (err) {
      pushToast(parseApiError(err).message, 'error');
    } finally {
      setBusyId(null);
    }
  }

  return (
    <div>
      <div className="page-head">
        <div className="page-eyebrow">Finance Manager · Claim Review</div>
        <h1 className="page-title">Pending Expense Claims</h1>
        <p className="page-sub">Approve or reject claims awaiting your decision. Rejections require a remark.</p>
      </div>

      <div className="card card-pad">
        {error && <div className="banner error">⚠ {error}</div>}

        <div className="table-wrap">
          <table className="ledger">
            <thead>
              <tr>
                <th>ID</th>
                <th>Employee</th>
                <th>Department</th>
                <th>Category</th>
                <th>Amount</th>
                <th>Date</th>
                <th>Remark</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr><td colSpan={8} className="empty-row">Loading pending claims…</td></tr>
              )}
              {!loading && claims.length === 0 && (
                <tr><td colSpan={8} className="empty-row">No pending claims — all caught up. ✓</td></tr>
              )}
              {!loading && claims.map((c) => (
                <tr key={c.id}>
                  <td className="num">#{c.id}</td>
                  <td>{c.employeeName}</td>
                  <td>{c.departmentName}</td>
                  <td>{c.category}</td>
                  <td className="num">₹{formatCurrency(c.amount)}</td>
                  <td className="num">{c.expenseDate}</td>
                  <td style={{ minWidth: 200 }}>
                    <input
                      type="text"
                      placeholder="Optional for approve, required for reject"
                      value={remarkDraft[c.id] || ''}
                      maxLength={500}
                      onChange={(e) => {
                        setRemarkDraft((d) => ({ ...d, [c.id]: e.target.value }));
                        setRemarkError((d) => ({ ...d, [c.id]: '' }));
                      }}
                      className={remarkError[c.id] ? 'invalid' : ''}
                      style={{
                        width: '100%', padding: '7px 9px', borderRadius: 6,
                        border: '1px solid var(--border-strong)', background: 'var(--bg-elevated)',
                        color: 'var(--text)', fontSize: 12.5,
                      }}
                    />
                    {remarkError[c.id] && <div className="field-error">{remarkError[c.id]}</div>}
                  </td>
                  <td>
                    <div className="row-gap">
                      <button
                        className="btn btn-success btn-sm"
                        disabled={busyId === c.id}
                        onClick={() => act(c, 'approve')}
                      >
                        Approve
                      </button>
                      <button
                        className="btn btn-danger btn-sm"
                        disabled={busyId === c.id}
                        onClick={() => act(c, 'reject')}
                      >
                        Reject
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
