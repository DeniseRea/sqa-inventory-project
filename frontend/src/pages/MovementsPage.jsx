import { useState, useEffect, useCallback } from "react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { stockService } from "../services/stockService";
import { productService } from "../services/productService";

export const MovementsPage = () => {
  const [movements, setMovements] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [newMovement, setNewMovement] = useState({
    productId: "",
    quantity: "",
    type: "ENTRADA",
    reason: "REPOSICION",
  });

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
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
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await stockService.register({
        ...newMovement,
        productId: parseInt(newMovement.productId),
        quantity: parseInt(newMovement.quantity),
      });
      setIsModalOpen(false);
      setNewMovement({ productId: "", quantity: "", type: "ENTRADA", reason: "REPOSICION" });
      fetchData();
    } catch (error) {
      alert("Error al registrar movimiento. Verifique el stock disponible y los datos.");
    }
  };

  const REASONS_BY_TYPE = {
    ENTRADA: ["REPOSICION", "DEVOLUCION", "AJUSTE"],
    SALIDA: ["VENTA", "MERMA", "AJUSTE"]
  };

  useEffect(() => {
    setNewMovement(prev => ({ 
      ...prev, 
      reason: REASONS_BY_TYPE[prev.type][0] 
    }));
  }, [newMovement.type]);

  // Filtrado local por texto del nombre del producto
  const filteredMovements = movements.filter(mov => {
    const product = products.find(p => p.id === mov.productId);
    const productName = product ? product.name.toLowerCase() : "";
    return productName.includes(searchTerm.toLowerCase());
  });

  return (
    <AdminTemplate>
      <header className="flex flex-col lg:flex-row justify-between items-start lg:items-end gap-6 mb-10">
        <div className="flex-1">
          <div className="flex items-center gap-2 text-[var(--accent)] font-black text-[10px] uppercase tracking-widest mb-2">
            <div className="h-0.5 w-6 bg-[var(--accent)] rounded-full"></div>
            Bitácora
          </div>
          <h2 className="text-3xl font-bold text-[var(--text-dark)] tracking-tight mb-4">Movimientos</h2>
          
          {/* Filtro por Texto del Artículo */}
          <div className="relative w-full max-w-sm group">
            <svg className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-[var(--accent)] transition-colors" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input 
              type="text"
              placeholder="Filtrar por nombre de producto..."
              className="w-full pl-10 pr-4 py-2.5 bg-white border border-black/5 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] shadow-sm transition-all text-xs font-bold"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>
        
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 px-6 py-4 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold text-sm rounded-2xl shadow-lg transition-all whitespace-nowrap"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
          </svg>
          Registrar Movimiento
        </button>
      </header>

      {loading && movements.length === 0 ? (
        <div className="h-64 flex items-center justify-center">
          <div className="w-8 h-8 border-4 border-[var(--accent)] border-t-transparent rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="bg-white rounded-[2.5rem] shadow-xl border border-black/5 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="bg-[var(--bg-sidebar)] text-[var(--text-light)]">
                  <th className="p-6 font-black uppercase tracking-widest text-[10px] opacity-40">Fecha y Hora</th>
                  <th className="p-6 font-bold text-sm uppercase">Producto</th>
                  <th className="p-6 font-bold text-sm uppercase text-center">Tipo</th>
                  <th className="p-6 font-bold text-sm uppercase text-right">Cantidad</th>
                  <th className="p-6 font-bold text-sm uppercase">Razón</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm">
                {filteredMovements.length > 0 ? filteredMovements.map((mov) => {
                  const product = products.find(p => p.id === mov.productId);
                  return (
                    <tr key={mov.id} className="hover:bg-slate-50 transition-colors">
                      <td className="p-6 text-[var(--text-muted)] font-mono text-xs">
                        {new Date(mov.createdAt).toLocaleString()}
                      </td>
                      <td className="p-6">
                        <span className="font-bold text-[var(--text-dark)]">
                          {product ? product.name : `Producto #${mov.productId}`}
                        </span>
                      </td>
                      <td className="p-6 text-center">
                        <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-widest ${
                          mov.type === 'ENTRADA' ? 'bg-emerald-50 text-emerald-600' : 'bg-orange-50 text-orange-600'
                        }`}>
                          {mov.type === 'ENTRADA' ? 'ENTRADA' : 'SALIDA'}
                        </span>
                      </td>
                      <td className={`p-6 text-right font-black text-lg ${
                        mov.type === 'ENTRADA' ? 'text-emerald-600' : 'text-orange-600'
                      }`}>
                        {mov.type === 'ENTRADA' ? '+' : '-'}{mov.quantity}
                      </td>
                      <td className="p-6">
                         <span className="px-2 py-1 bg-slate-100 rounded text-[10px] font-bold text-slate-500">{mov.reason}</span>
                      </td>
                    </tr>
                  );
                }) : (
                  <tr>
                    <td colSpan="5" className="p-12 text-center text-slate-400 italic font-medium">No se encontraron movimientos para "{searchTerm}"</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-[var(--bg-sidebar)]/60 backdrop-blur-md flex items-center justify-center z-50 p-4 pl-72 sm:p-6 transition-all duration-500">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-md animate-slide-in relative overflow-hidden border border-white/20">
            <div className="absolute top-0 left-0 w-full h-1.5 bg-[var(--accent)]"></div>
            <h3 className="text-xl font-bold mb-1 text-[var(--text-dark)] uppercase tracking-tight">Registrar Movimiento</h3>
            <p className="text-[var(--text-muted)] text-[10px] font-bold uppercase tracking-widest mb-8">Gestión de Stock</p>
            
            <form onSubmit={handleRegister} className="space-y-6">
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Producto</label>
                <select
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm"
                  value={newMovement.productId}
                  onChange={(e) => setNewMovement({...newMovement, productId: e.target.value})}
                >
                  <option value="">Seleccionar producto...</option>
                  {products.map(p => <option key={p.id} value={p.id}>{p.name} (Stock: {p.stock})</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Tipo</label>
                  <select
                    className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm"
                    value={newMovement.type}
                    onChange={(e) => setNewMovement({...newMovement, type: e.target.value})}
                  >
                    <option value="ENTRADA">Entrada (+)</option>
                    <option value="SALIDA">Salida (-)</option>
                  </select>
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Cantidad</label>
                  <input
                    type="number"
                    min="1"
                    required
                    className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm"
                    placeholder="0"
                    value={newMovement.quantity}
                    onChange={(e) => setNewMovement({...newMovement, quantity: e.target.value})}
                  />
                </div>
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Motivo ({newMovement.type})</label>
                <select
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm"
                  value={newMovement.reason}
                  onChange={(e) => setNewMovement({...newMovement, reason: e.target.value})}
                >
                  {REASONS_BY_TYPE[newMovement.type].map(r => (
                    <option key={r} value={r}>{r}</option>
                  ))}
                </select>
              </div>
              <div className="flex gap-3 pt-4">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 p-4 text-xs font-bold uppercase text-slate-400 hover:bg-slate-50 rounded-xl transition-all">Cancelar</button>
                <button type="submit" className="flex-1 p-4 bg-[var(--bg-sidebar)] text-white text-xs font-bold uppercase rounded-xl hover:bg-black shadow-lg transition-all">Registrar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
