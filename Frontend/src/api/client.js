import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Normalize backend error shapes (field-validation map, ErrorResponse, or network errors)
// into a single readable message + optional field-error map.
export function parseApiError(error) {
  if (!error.response) {
    return { message: 'Could not reach the server. Is the backend running on ' + BASE_URL + '?', fields: {} };
  }
  const data = error.response.data;
  if (data && typeof data === 'object') {
    if (data.message) {
      return { message: data.message, fields: {} };
    }
    // field validation error map: { fieldName: "message" }
    const fields = data;
    const firstMsg = Object.values(fields)[0];
    return { message: firstMsg || 'Validation failed', fields };
  }
  return { message: 'Something went wrong (HTTP ' + error.response.status + ')', fields: {} };
}

export default api;
