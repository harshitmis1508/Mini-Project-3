import { useRole } from '../context/RoleContext';

export default function RequireRole({ allow, children }) {
  const { role, ROLE_LABELS } = useRole();
  if (!allow.includes(role)) {
    return (
      <div className="card card-pad access-note">
        <h3>🔒 Restricted</h3>
        <p className="muted">
          This page is only available to the <strong>{ROLE_LABELS[allow[0]]}</strong> role.
          Switch roles from the top-right dropdown to continue.
        </p>
      </div>
    );
  }
  return children;
}
