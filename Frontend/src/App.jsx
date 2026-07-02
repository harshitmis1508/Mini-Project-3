import { Navigate, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import RequireRole from './components/RequireRole';
import { ROLES, useRole } from './context/RoleContext';
import SubmitClaim from './pages/SubmitClaim';
import Review from './pages/Review';
import AllClaims from './pages/AllClaims';
import Budgets from './pages/Budgets';
import Summary from './pages/Summary';

function Home() {
  const { role } = useRole();
  return <Navigate to={role === ROLES.FINANCE_MANAGER ? '/review' : '/submit'} replace />;
}

export default function App() {
  return (
    <div className="app-shell">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route
            path="/submit"
            element={<RequireRole allow={[ROLES.EMPLOYEE]}><SubmitClaim /></RequireRole>}
          />
          <Route
            path="/review"
            element={<RequireRole allow={[ROLES.FINANCE_MANAGER]}><Review /></RequireRole>}
          />
          <Route
            path="/claims"
            element={<RequireRole allow={[ROLES.EMPLOYEE, ROLES.FINANCE_MANAGER]}><AllClaims /></RequireRole>}
          />
          <Route
            path="/budgets"
            element={<RequireRole allow={[ROLES.FINANCE_MANAGER]}><Budgets /></RequireRole>}
          />
          <Route
            path="/summary"
            element={<RequireRole allow={[ROLES.FINANCE_MANAGER]}><Summary /></RequireRole>}
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  );
}
