export default function StatusBadge({ status }) {
  const cls =
    status === 'APPROVED' ? 'stamp-approved' : status === 'REJECTED' ? 'stamp-rejected' : 'stamp-pending';
  return <span className={`stamp ${cls}`}>{status}</span>;
}
