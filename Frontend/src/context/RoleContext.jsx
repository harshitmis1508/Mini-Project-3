import { createContext, useContext, useEffect, useState } from 'react';

export const ROLES = {
  EMPLOYEE: 'EMPLOYEE',
  FINANCE_MANAGER: 'FINANCE_MANAGER',
};

const ROLE_LABELS = {
  [ROLES.EMPLOYEE]: 'Employee',
  [ROLES.FINANCE_MANAGER]: 'Finance Manager',
};

const RoleContext = createContext(null);

export function RoleProvider({ children }) {
  const [role, setRole] = useState(() => {
    try {
      return localStorage.getItem('eas_role') || ROLES.EMPLOYEE;
    } catch {
      return ROLES.EMPLOYEE;
    }
  });

  useEffect(() => {
    try {
      localStorage.setItem('eas_role', role);
    } catch {
      /* ignore */
    }
  }, [role]);

  const isFinanceManager = role === ROLES.FINANCE_MANAGER;
  const isEmployee = role === ROLES.EMPLOYEE;

  return (
    <RoleContext.Provider value={{ role, setRole, isFinanceManager, isEmployee, ROLE_LABELS }}>
      {children}
    </RoleContext.Provider>
  );
}

export function useRole() {
  const ctx = useContext(RoleContext);
  if (!ctx) throw new Error('useRole must be used within RoleProvider');
  return ctx;
}
