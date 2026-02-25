let authHeader = localStorage.getItem("basicAuth") ?? "";

export function setBasicAuth(email, password) {
  authHeader = "Basic " + btoa(`${email}:${password}`);
  localStorage.setItem("basicAuth", authHeader);
}

export function clearAuth() {
  authHeader = "";
  localStorage.removeItem("basicAuth");
}

export function isAuthed() {
  return Boolean(authHeader);
}

const BASE = "http://localhost:8080/api";

async function readJsonSafe(res) {
  try {
    return await res.json();
  } catch {
    return null;
  }
}

export async function apiRequest(path, options = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: {
    "Content-Type": "application/json",
    ...(authHeader ? { Authorization: authHeader } : {}),
    ...(options.headers ?? {}),
    },
    ...options,
  });

  if (res.ok) {
    // 204 No Content
    if (res.status === 204) return null;
    return await readJsonSafe(res);
  }

  const body = await readJsonSafe(res);

  // tvoj backend: {error, message} alebo {error, message?, fields}
  let msg = body?.message;

  if (!msg && body?.fields && typeof body.fields === "object") {
    msg = Object.entries(body.fields)
      .map(([k, v]) => `${k}: ${v}`)
      .join(", ");
  }

  if (!msg) msg = body?.error ?? `HTTP ${res.status}`;

  throw new Error(msg);
}

export const api = {
  // Reservations
  listReservations: (params = "") => apiRequest(`/reservations${params}`),
  createReservation: (payload) =>
    apiRequest(`/reservations`, { method: "POST", body: JSON.stringify(payload) }),

  listMyReservations: (params = "") => apiRequest(`/reservations/mine${params}`),

  // Users
  listUsers: () => apiRequest(`/users`),
  createUser: (payload) =>
    apiRequest(`/users`, { method: "POST", body: JSON.stringify(payload) }),
  deleteUser: (id) => apiRequest(`/users/${id}`, { method: "DELETE" }),

  // Facilities
  listFacilities: () => apiRequest(`/facilities`),
  createFacility: (payload) =>
    apiRequest(`/facilities`, { method: "POST", body: JSON.stringify(payload) }),
  deleteFacility: (id) => apiRequest(`/facilities/${id}`, { method: "DELETE" }),

    // Auth
  register: (payload) =>
    apiRequest(`/auth/register`, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  me: () => apiRequest(`/auth/me`),
};