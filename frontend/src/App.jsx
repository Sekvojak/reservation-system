import { useEffect, useState } from "react";
import "./App.css";

import ReservationsPanel from "./components/ReservationsPanel";
import UsersPanel from "./components/UsersPanel";
import FacilitiesPanel from "./components/FacilitiesPanel";
import AuthPanel from "./components/AuthPanel";
import { clearAuth } from "./api";
import { isAuthed } from "./api";

function App() {
  const [error, setError] = useState("");
  const [authed, setAuthed] = useState(isAuthed());
  const [authVersion, setAuthVersion] = useState(0);

  useEffect(() => {
    setAuthed(isAuthed());
  }, []);

  function handleAuthChange() {
    setAuthed(isAuthed());
    setAuthVersion((v) => v + 1);
  }

  // 1) NOT LOGGED IN: center auth screen
  if (!authed) {
    return (
      <div className="auth-page">
        <div className="auth-wrapper">
          <h1 className="title">Reservation System</h1>

          {error && <div className="error">Error: {error}</div>}

          <AuthPanel onError={setError} onAuthChange={handleAuthChange} />
        </div>
      </div>
    );
  }

  // 2) LOGGED IN: show dashboard
  return (
    <div className="center-page">
      <div className="center-wrapper">
        <div className="header">
          <h1 className="title">Reservation System</h1>

          <div className="header-right">
            <button
              className="secondary"
              onClick={() => {
                clearAuth();
                handleAuthChange();
              }}
            >
              Logout
            </button>
          </div>
        </div>

        {error && <div className="error">Error: {error}</div>}

        <div className="grid">
          <FacilitiesPanel key={`fac-${authVersion}`} onError={setError} />
          <ReservationsPanel key={`res-${authVersion}`} onError={setError} />
        </div>
      </div>
    </div>
  );
}

export default App;