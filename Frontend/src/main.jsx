import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import './index.css';
import { ThemeProvider } from './context/ThemeContext.jsx';
import { RoleProvider } from './context/RoleContext.jsx';
import { ToastProvider } from './components/Toast.jsx';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ThemeProvider>
      <RoleProvider>
        <ToastProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </ToastProvider>
      </RoleProvider>
    </ThemeProvider>
  </React.StrictMode>
);
