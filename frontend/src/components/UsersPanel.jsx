import { useEffect, useState } from "react";
import { api } from "../api";

export default function UsersPanel({ onError }) {
  const [users, setUsers] = useState([]);
  const [newUsername, setNewUsername] = useState("");
  const [newEmail, setNewEmail] = useState("");

  async function load() {
    try {
      const data = await api.listUsers();
      setUsers(data?.content ?? []);
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function create() {
    onError("");
    try {
      await api.createUser({ username: newUsername, email: newEmail });
      setNewUsername("");
      setNewEmail("");
      await load();
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  async function del(id) {
    onError("");
    try {
      await api.deleteUser(id);
      await load();
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  return (
    <div className="card">
      <h2>
        Users <span className="badge">{users.length}</span>
      </h2>

      <div className="row">
        <label className="label">
          Username
          <input value={newUsername} onChange={(e) => setNewUsername(e.target.value)} />
        </label>

        <label className="label">
          Email
          <input value={newEmail} onChange={(e) => setNewEmail(e.target.value)} />
        </label>
      </div>

      <div className="actions">
        <button onClick={create}>Create</button>
        <button className="secondary" onClick={load}>
          Reload
        </button>
      </div>

      <div style={{ marginTop: 12 }}>
        {users.length === 0 ? (
          <p className="muted">No users.</p>
        ) : (
          <ul className="list">
            {users.map((u) => (
              <li className="item" key={u.id}>
                <div>
                  <strong>#{u.id}</strong> <small>{u.username}</small>
                  <div className="muted">{u.email}</div>
                </div>
                <button className="secondary" onClick={() => del(u.id)}>
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}