import React, { useCallback, useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

const API = 'http://localhost:8081';
const TRANSACTIONS_API = 'http://localhost:8082';
// Debe coincidir con AES_SECRET_KEY del backend. Solo es aceptable como demostracion de la evaluacion.
const AES_KEY_BASE64 = 'MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=';

function toBase64(bytes) {
  let binary = '';
  bytes.forEach(byte => { binary += String.fromCharCode(byte); });
  return btoa(binary);
}

async function encryptSecret(value) {
  const rawKey = Uint8Array.from(atob(AES_KEY_BASE64), char => char.charCodeAt(0));
  const key = await crypto.subtle.importKey('raw', rawKey, 'AES-GCM', false, ['encrypt']);
  const iv = crypto.getRandomValues(new Uint8Array(12));
  const encrypted = new Uint8Array(await crypto.subtle.encrypt({ name: 'AES-GCM', iv }, key, new TextEncoder().encode(value)));
  const combined = new Uint8Array(iv.length + encrypted.length);
  combined.set(iv); combined.set(encrypted, iv.length);
  return toBase64(combined);
}

function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [notice, setNotice] = useState(null);
  const [login, setLogin] = useState({ usuario: '', password: '' });
  const [operation, setOperation] = useState({ operacion: 'venta', importe: '100.00', cliente: '', secreto: '' });
  const [transactions, setTransactions] = useState([]);
  const [page, setPage] = useState({ number: 0, totalPages: 0, totalElements: 0 });
  const [loading, setLoading] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [transactionToCancel, setTransactionToCancel] = useState(null);
  const update = (setter, state) => event => setter({ ...state, [event.target.name]: event.target.value });
  const request = async (url, method, body) => {
    const response = await fetch(url, { method, headers: { 'Content-Type': 'application/json' }, body: body ? JSON.stringify(body) : undefined });
    const data = response.status === 204 ? null : await response.json();
    if (!response.ok) throw new Error(data.error || Object.values(data).join(', '));
    return data;
  };
  const loadTransactions = useCallback(async (pageNumber = 0) => {
    setLoading(true);
    try {
      const response = await fetch(`${TRANSACTIONS_API}/api/transacciones?page=${pageNumber}&size=5&sortBy=id&direction=DESC`);
      const data = await response.json();
      if (!response.ok) throw new Error(data.error || 'No se pudo consultar la información.');
      setTransactions(data.content);
      setPage({ number: data.number, totalPages: data.totalPages, totalElements: data.totalElements });
    } catch (error) { setNotice({ ok: false, text: error.message }); }
    finally { setLoading(false); }
  }, []);
  useEffect(() => { if (loggedIn) loadTransactions(); }, [loggedIn, loadTransactions]);
  const onLogin = async event => {
    event.preventDefault();
    try { await request(`${API}/api/auth/login`, 'POST', login); setLoggedIn(true); setNotice({ ok: true, text: 'Login correcto.' }); }
    catch (e) { setNotice({ ok: false, text: e.message }); }
  };
  const onOperation = async event => {
    event.preventDefault();
    try {
      const secretEncrypted = await encryptSecret(operation.secreto);
      const result = await request(`${API}/api/operaciones`, 'POST', { ...operation, importe: Number(operation.importe), secreto: secretEncrypted });
      setNotice({ ok: true, text: `Operacion ${result.operacion} aprobada. ID: ${result.id}; referencia: ${result.referencia}.` });
      setOperation({ operacion: 'venta', importe: '100.00', cliente: '', secreto: '' });
      setShowCreateModal(false);
      loadTransactions(0);
    } catch (e) { setNotice({ ok: false, text: e.message }); }
  };
  const onCancel = async () => {
    try {
      await request(`${TRANSACTIONS_API}/api/transacciones/cancelar`, 'PATCH', { id: transactionToCancel.id, referencia: transactionToCancel.referencia, estatus: 'cancelar' });
      setNotice({ ok: true, text: `La transacción ${transactionToCancel.referencia} fue cancelada.` });
      setTransactionToCancel(null);
      loadTransactions(page.number);
    } catch (error) { setNotice({ ok: false, text: error.message }); }
  };
  const currency = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' });
  return <main>
    <section className={loggedIn ? 'dashboard' : 'card'}><h1>Evaluación Java Senior</h1>
      {notice && <p className={notice.ok ? 'notice ok' : 'notice error'}>{notice.text}</p>}
      {!loggedIn ? <form onSubmit={onLogin}><h2>Iniciar sesion</h2>
        <label>Usuario<input name="usuario" value={login.usuario} onChange={update(setLogin, login)} required /></label>
        <label>Password<input name="password" type="password" value={login.password} onChange={update(setLogin, login)} required /></label>
        <button>Entrar</button><small>Demo: angel / Password123!</small>
      </form> : <>
        <header className="dashboard-header"><div><h2>Transacciones</h2><p>Consulta y administra las operaciones registradas.</p></div><button onClick={() => setShowCreateModal(true)}>+ Nueva operación</button></header>
        <div className="table-wrapper"><table><thead><tr><th>ID</th><th>Operación</th><th>Importe</th><th>Cliente</th><th>Referencia</th><th>Estatus</th><th>Acciones</th></tr></thead>
          <tbody>{loading ? <tr><td colSpan="7">Cargando información...</td></tr> : transactions.length === 0 ? <tr><td colSpan="7">Aún no hay transacciones registradas.</td></tr> : transactions.map(transaction => <tr key={transaction.id}><td>{transaction.id}</td><td>{transaction.operacion}</td><td>{currency.format(transaction.importe)}</td><td>{transaction.cliente}</td><td>{transaction.referencia}</td><td><span className={`status ${transaction.estatus === 'Aprobada' ? 'approved' : 'cancelled'}`}>{transaction.estatus}</span></td><td>{transaction.estatus === 'Aprobada' && <button className="cancel-button" onClick={() => setTransactionToCancel(transaction)}>Cancelar</button>}</td></tr>)}</tbody>
        </table></div>
        <footer className="pagination"><span>{page.totalElements} registro(s)</span><div><button disabled={page.number === 0 || loading} onClick={() => loadTransactions(page.number - 1)}>Anterior</button><span>Página {page.totalPages === 0 ? 0 : page.number + 1} de {page.totalPages}</span><button disabled={page.number + 1 >= page.totalPages || loading} onClick={() => loadTransactions(page.number + 1)}>Siguiente</button></div></footer>
      </>}
    </section>
    {showCreateModal && <div className="modal-backdrop" role="presentation"><section className="modal" role="dialog" aria-modal="true" aria-labelledby="create-title"><header><h2 id="create-title">Nueva operación</h2><button className="close-button" onClick={() => setShowCreateModal(false)} aria-label="Cerrar">×</button></header><form onSubmit={onOperation}>
      <label>Operación<input name="operacion" value={operation.operacion} onChange={update(setOperation, operation)} required /></label>
      <label>Importe<input name="importe" type="number" min="0.01" step="0.01" value={operation.importe} onChange={update(setOperation, operation)} required /></label>
      <label>Cliente<input name="cliente" value={operation.cliente} onChange={update(setOperation, operation)} required /></label>
      <label>Secreto<input name="secreto" type="password" value={operation.secreto} onChange={update(setOperation, operation)} required /></label>
      <div className="modal-actions"><button type="button" className="secondary-button" onClick={() => setShowCreateModal(false)}>Cancelar</button><button>Guardar operación</button></div>
    </form></section></div>}
    {transactionToCancel && <div className="modal-backdrop" role="presentation"><section className="modal confirm-modal" role="dialog" aria-modal="true" aria-labelledby="cancel-title"><h2 id="cancel-title">¿Cancelar transacción?</h2><p>Se cancelará la referencia <strong>{transactionToCancel.referencia}</strong>. Esta acción cambia su estatus a Cancelada.</p><div className="modal-actions"><button className="secondary-button" onClick={() => setTransactionToCancel(null)}>Volver</button><button className="cancel-button" onClick={onCancel}>Sí, cancelar</button></div></section></div>}
  </main>;
}
createRoot(document.getElementById('root')).render(<App />);
