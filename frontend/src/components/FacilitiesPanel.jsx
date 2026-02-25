import { useEffect, useState } from "react";
import { api } from "../api";

export default function FacilitiesPanel({ onError }) {
  const [facilities, setFacilities] = useState([]);
  const [facilityName, setFacilityName] = useState("");
  const [facilityType, setFacilityType] = useState("");

  async function load() {
    try {
      const data = await api.listFacilities();
      setFacilities(data?.content ?? []);
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
      await api.createFacility({ name: facilityName, type: facilityType });
      setFacilityName("");
      setFacilityType("");
      await load();
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  async function del(id) {
    onError("");
    try {
      await api.deleteFacility(id);
      await load();
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  return (
    <div className="card">
      <h2>
        Facilities <span className="badge">{facilities.length}</span>
      </h2>

      <div className="row">
        <label className="label">
          Name
          <input value={facilityName} onChange={(e) => setFacilityName(e.target.value)} />
        </label>

        <label className="label">
          Type
          <input
            placeholder="e.g. TENNIS"
            value={facilityType}
            onChange={(e) => setFacilityType(e.target.value)}
          />
        </label>
      </div>

      <div className="actions">
        <button onClick={create}>Create</button>
        <button className="secondary" onClick={load}>
          Reload
        </button>
      </div>

      <div style={{ marginTop: 12 }}>
        {facilities.length === 0 ? (
          <p className="muted">No facilities.</p>
        ) : (
          <ul className="list">
            {facilities.map((f) => (
              <li className="item" key={f.id}>
                <div>
                  <strong>#{f.id}</strong> <small>{f.name}</small>
                  <div className="muted">{String(f.type)}</div>
                </div>
                <button className="secondary" onClick={() => del(f.id)}>
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