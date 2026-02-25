import { useState } from "react";
import { api, setBasicAuth, clearAuth, isAuthed } from "../api";

export default function AuthPanel({ onError, onAuthChange }) {
  const [regUsername, setRegUsername] = useState("");
  const [regEmail, setRegEmail] = useState("");
  const [regPassword, setRegPassword] = useState("");

  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  const authed = isAuthed();

  async function register() {
    onError("");

    if (!regUsername || !regEmail || !regPassword) {
      onError("Fill username, email and password");
      return;
    }

    try {
      await api.register({ username: regUsername, email: regEmail, password: regPassword });

      // auto-login po registrácii
      setBasicAuth(regEmail, regPassword);

      // overíme, že credentials sú ok
      await api.me();

      onAuthChange?.();

      setRegUsername("");
      setRegEmail("");
      setRegPassword("");
    } catch (e) {
      clearAuth();
      onError(String(e.message ?? e));
    }
  }

  async function login() {
    onError("");

    if (!loginEmail || !loginPassword) {
      onError("Fill email and password");
      return;
    }

    setBasicAuth(loginEmail, loginPassword);

    try {
      await api.me(); // ✅ validácia loginu
      onAuthChange?.();
    } catch (e) {
      clearAuth();
      onError("Invalid email or password");
    }
  }

  function logout() {
    onError("");
    clearAuth();
    onAuthChange?.();
  }

  return (
    <div className="card">
      <h2>
        Auth <span className="badge">{authed ? "logged in" : "logged out"}</span>
      </h2>

      {!authed ? (
        <div className="auth-columns">
          <div>
            <h3 style={{ marginTop: 0 }}>Register</h3>

            <label className="label">
              Username
              <input value={regUsername} onChange={(e) => setRegUsername(e.target.value)} />
            </label>

            <label className="label">
              Email
              <input value={regEmail} onChange={(e) => setRegEmail(e.target.value)} />
            </label>

            <label className="label">
              Password
              <input
                type="password"
                value={regPassword}
                onChange={(e) => setRegPassword(e.target.value)}
              />
            </label>

            <div className="actions">
              <button onClick={register}>Register</button>
            </div>
          </div>

          <div>
            <h3 style={{ marginTop: 0 }}>Login</h3>

            <label className="label">
              Email
              <input value={loginEmail} onChange={(e) => setLoginEmail(e.target.value)} />
            </label>

            <label className="label">
              Password
              <input
                type="password"
                value={loginPassword}
                onChange={(e) => setLoginPassword(e.target.value)}
              />
            </label>

            <div className="actions">
              <button onClick={login}>Login</button>
            </div>
          </div>
        </div>
      ) : (
        <div className="actions">
          <button className="secondary" onClick={logout}>
            Logout
          </button>
        </div>
      )}
    </div>
  );
}