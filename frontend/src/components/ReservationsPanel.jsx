import { useEffect, useState } from "react";
import { api } from "../api";

export default function ReservationsPanel({ onError }) {
  const [reservations, setReservations] = useState([]);

  // dropdown data
  const [facilities, setFacilities] = useState([]);

  // form state
  const [facilityId, setFacilityId] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  async function loadReservations() {
    try {
      const data = await api.listMyReservations();
      setReservations(data?.content ?? []);
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  async function loadFacilities() {
    try {
      const data = await api.listFacilities();
      setFacilities(data?.content ?? []);
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  useEffect(() => {
    loadReservations();
    loadFacilities();
  }, []);

  async function create() {
    onError("");

    if (!facilityId || !startTime || !endTime) {
      onError("Select facility, startTime and endTime");
      return;
    }

    try {
      await api.createReservation({
        facilityId: Number(facilityId),
        startTime,
        endTime,
      });

      setStartTime("");
      setEndTime("");
      await loadReservations();
    } catch (e) {
      onError(String(e.message ?? e));
    }
  }

  return (
    <>
      <div className="card">
        <h2>Create reservation</h2>

        <div className="form-grid">
          <label className="label span-2">
            Facility
            <select value={facilityId} onChange={(e) => setFacilityId(e.target.value)}>
              <option value="">-- Select facility --</option>
              {facilities.map((f) => (
                <option key={f.id} value={f.id}>
                  #{f.id} - {f.name} ({String(f.type)})
                </option>
              ))}
            </select>
          </label>

          <label className="label">
            Start
            <input
              type="datetime-local"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
            />
          </label>

          <label className="label">
            End
            <input
              type="datetime-local"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
            />
          </label>
        </div>

        <div className="actions">
          <button onClick={create}>Create</button>
          <button
            className="secondary"
            onClick={() => {
              loadReservations();
              loadFacilities();
            }}
          >
            Reload
          </button>
        </div>
      </div>

      <div className="card">
        <h2>
          My reservations <span className="badge">{reservations.length}</span>
        </h2>

        {reservations.length === 0 ? (
          <p className="muted">No reservations found.</p>
        ) : (
          <ul className="list">
            {reservations.map((r) => (
              <li className="item" key={r.id}>
                <div>
                  <div>
                    <strong>#{r.id}</strong>{" "}
                    <small>facility {r.facilityId}</small>
                  </div>
                  <div className="muted">
                    {r.startTime} → {r.endTime}
                  </div>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </>
  );
}