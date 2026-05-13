import { useState, useEffect } from "react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { stockService } from "../services/stockService";
import { productService } from "../services/productService";

export const MovementsPage = () => {
  const [movements, setMovements] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newMovement, setNewMovement] = useState({
    productId: "",
    quantity: "",
    type: "INPUT",
    reason: "",
  });

  const fetchData = async () => {
    try {
      const [mData, pData] = await Promise.all([
        stockService.getAll(),
        productService.getAll(),
      ]);
      setMovements(mData);
      setProducts(pData);
    } catch (error) {
      console.error("Error fetching movements:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await stockService.register({
        ...newMovement,
        productId: parseInt(newMovement.productId),
        quantity: parseInt(newMovement.quantity),
      });
      setIsModalOpen(false);
      setNewMovement({ productId: "", quantity: "", type: "INPUT", reason: "" });
      fetchData();
    } catch (error) {
      alert("Error al registrar movimiento. Verifique el stock disponible.");
    }
  };

  return (
    <AdminTemplate>
      <header className="flex justify-between items-end mb-12">
        <div>
          <div className="flex items-center gap-2 text-[var(--accent)] font-semibold mb-2">
            <span className="w-8 h-px bg-[var(--accent)]"></span>
            Gestión de Inventario
          </div>
          <h2 className="text-4xl font-bold tracking-tight">Movimientos</h2>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 px-6 py-3 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold rounded-2xl shadow-lg shadow-indigo-500/20 transition-all duration-200 transform hover:scale-[1.02]"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
          </svg>
          Registrar Movimiento
        </button>
      </header>

      {loading ? (
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-[var(--accent)]"></div>
        </div>
      ) : (
        <div className="bg-white rounded-[2rem] shadow-sm border border-slate-100 overflow-hidden">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50/50 border-b border-slate-100">
                <th className="p-6 font-bold text-slate-400 text-xs uppercase tracking-wider">Fecha y Hora</th>
                <th className="p-6 font-bold text-slate-800">Producto</th>
                <th className="p-6 font-bold text-slate-800">Tipo</th>
                <th className="p-6 font-bold text-slate-800 text-right">Cantidad</th>
                <th className="p-6 font-bold text-slate-800">Razón</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-50">
              {movements.map((mov) => (
                <tr key={mov.id} className="hover:bg-slate-50/50 transition-colors group">
                  <td className="p-6 text-slate-400 text-sm">
                    {new Date(mov.createdAt).toLocaleString()}
                  </td>
                  <td className="p-6">
                    <span className="font-semibold text-slate-900">{mov.productName}</span>
                  </td>
                  <td className="p-6">
                    <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold ${
                      mov.type === 'INPUT' ? 'bg-blue-50 text-blue-600' : 'bg-orange-50 text-orange-600'
                    }`}>
                      <span className={`w-1.5 h-1.5 rounded-full ${mov.type === 'INPUT' ? 'bg-blue-500' : 'bg-orange-500'}`}></span>
                      {mov.type === 'INPUT' ? 'ENTRADA' : 'SALIDA'}
                    </span>
                  </td>
                  <td className={`p-6 text-right font-bold text-lg ${
                    mov.type === 'INPUT' ? 'text-blue-600' : 'text-orange-600'
                  }`}>
                    {mov.type === 'INPUT' ? '+' : '-'}{mov.quantity}
                  </td>
                  <td className="p-6 text-slate-500 text-sm italic">"{mov.reason}"</td>
                </tr>
              ))}
              {movements.length === 0 && (
                <tr>
                  <td colSpan="5" className="p-20 text-center">
                    <div className="flex flex-col items-center">
                      <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center text-slate-300 mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                      </div>
                      <p className="text-slate-400 font-medium">No hay movimientos registrados.</p>
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal de Registro */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-50 p-4">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-md transform transition-all animate-fade-in-up">
            <h3 className="text-2xl font-bold mb-2">Registrar Movimiento</h3>
            <p className="text-slate-500 mb-8">Aumenta o disminuye el stock de un producto.</p>
            
            <form onSubmit={handleRegister} className="space-y-6">
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Producto</label>
                <select
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  value={newMovement.productId}
                  onChange={(e) => setNewMovement({...newMovement, productId: e.target.value})}
                >
                  <option value="">Seleccionar producto...</option>
                  {products.map(p => <option key={p.id} value={p.id}>{p.name} (Stock: {p.stock})</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Tipo</label>
                  <select
                    className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                    value={newMovement.type}
                    onChange={(e) => setNewMovement({...newMovement, type: e.target.value})}
                  >
                    <option value="INPUT">Entrada (+)</option>
                    <option value="OUTPUT">Salida (-)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Cantidad</label>
                  <input
                    type="number"
                    min="1"
                    required
                    className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                    placeholder="0"
                    value={newMovement.quantity}
                    onChange={(e) => setNewMovement({...newMovement, quantity: e.target.value})}
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Razón / Referencia</label>
                <input
                  type="text"
                  required
                  placeholder="Ej: Compra a proveedor, Ajuste..."
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  value={newMovement.reason}
                  onChange={(e) => setNewMovement({...newMovement, reason: e.target.value})}
                />
              </div>
              <div className="flex gap-4 pt-4">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="flex-1 p-4 text-slate-500 font-bold hover:bg-slate-50 rounded-2xl transition-all"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="flex-1 p-4 bg-slate-900 text-white font-bold rounded-2xl hover:bg-slate-800 shadow-lg transition-all"
                >
                  Registrar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
