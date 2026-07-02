import { NavLink } from 'react-router-dom';
import { ROLES, useRole } from '../context/RoleContext';
import { useTheme } from '../context/ThemeContext';

const TABS = [
  { to: '/submit', label: 'Submit Claim', roles: [ROLES.EMPLOYEE] },
  { to: '/review', label: 'Review', roles: [ROLES.FINANCE_MANAGER] },
  { to: '/claims', label: 'All Claims', roles: [ROLES.EMPLOYEE, ROLES.FINANCE_MANAGER] },
  { to: '/budgets', label: 'Budgets', roles: [ROLES.FINANCE_MANAGER] },
  { to: '/summary', label: 'Summary', roles: [ROLES.FINANCE_MANAGER] },
];

export default function Navbar() {
  const { role, setRole, ROLE_LABELS } = useRole();
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="topbar">
      <div className="topbar-inner">
        <div className="brand">
          <span className="brand-mark">EA</span>
          Expense Approval System
        </div>

        <nav className="nav-tabs">
          {TABS.map((tab) => {
            const allowed = tab.roles.includes(role);
            return (
              <NavLink
                key={tab.to}
                to={allowed ? tab.to : '#'}
                onClick={(e) => !allowed && e.preventDefault()}
                className={({ isActive }) =>
                  `nav-tab ${isActive && allowed ? 'active' : ''} ${!allowed ? 'locked' : ''}`
                }
                title={!allowed ? `Only visible to ${ROLE_LABELS[tab.roles[0]]}` : undefined}
              >
                {tab.label}
              </NavLink>
            );
          })}
        </nav>

        <div className="topbar-controls">
          <button
            type="button"
            className="theme-toggle"
            onClick={toggleTheme}
            aria-label="Toggle dark and light theme"
            title={theme === 'light' ? 'Switch to dark theme' : 'Switch to light theme'}
          >
            {theme === 'light' ? '🌙' : '☀️'}
          </button>
          <select
            className="role-select"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            aria-label="Switch user role"
          >
            <option value={ROLES.EMPLOYEE}>Employee</option>
            <option value={ROLES.FINANCE_MANAGER}>Finance Manager</option>
          </select>
        </div>
      </div>
    </header>
  );
}
