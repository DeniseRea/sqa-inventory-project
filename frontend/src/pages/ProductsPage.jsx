import { useState, useEffect } from "react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { productService } from "../services/productService";
import { categoryService } from "../services/categoryService";

export const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newProduct, setNewProduct] = useState({ 
    name: "", 
    price: "", 
    stock: 0, 
    categoryId: ""
  });

  const fetchData = async () => {
    try {
      const [p, c] = await Promise.all([productService.getAll(), categoryService.getAll()]);
      setProducts(p); setCategories(c);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchData(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await productService.create({ 
        ...newProduct, 
        price: parseFloat(newProduct.price), 
        stock: parseInt(newProduct.stock), 
        categoryId: parseInt(newProduct.categoryId)
      });
      setIsModalOpen(false);
      setNewProduct({ name: "", price: "", stock: 0, categoryId: "" });
      fetchData();
    } catch (err) { alert("Error al crear producto"); }
  };

  return (
    <AdminTemplate>
      <header className="flex flex-col sm:flex-row justify-between items-start sm:items-end gap-4 mb-10">
        <div>
          <div className="flex items-center gap-2 text-[var(--accent)] font-black text-[10px] uppercase tracking-widest mb-2">
            <div className="h-0.5 w-6 bg-[var(--accent)] rounded-full"></div>
            Catálogo
          </div>
          <h2 className="text-3xl font-bold text-[var(--text-dark)] tracking-tight">Productos</h2>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 px-6 py-3 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold text-sm rounded-xl shadow-lg transition-all"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nuevo Producto
        </button>
      </header>

      {loading ? (
        <div className="h-64 flex items-center justify-center">
          <div className="w-8 h-8 border-4 border-[var(--accent)] border-t-transparent rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="bg-white rounded-3xl shadow-xl border border-black/5 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="bg-[var(--bg-sidebar)] text-[var(--text-light)]">
                  <th className="p-6 font-black uppercase tracking-widest text-[10px] opacity-40">ID</th>
                  <th className="p-6 font-bold text-sm uppercase">Nombre del Producto</th>
                  <th className="p-6 font-bold text-sm uppercase">Categoría</th>
                  <th className="p-6 font-bold text-sm uppercase text-right">Precio</th>
                  <th className="p-6 font-bold text-sm uppercase text-right">Stock</th>
                  <th className="p-6 font-bold text-sm uppercase text-center">Estado</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm">
                {products.map((prod) => {
                  const category = categories.find(c => c.id === prod.categoryId);
                  return (
                    <tr key={prod.id} className="hover:bg-slate-50 transition-colors">
                      <td className="p-6 text-[var(--text-muted)] font-mono text-xs font-bold">#{prod.id}</td>
                      <td className="p-6 font-bold text-[var(--text-dark)]">{prod.name}</td>
                      <td className="p-6">
                        <span className="px-3 py-1 bg-slate-100 text-[var(--text-dark)] rounded-full text-[10px] font-black uppercase tracking-widest border border-black/5">
                          {category ? category.name : 'Sin Categoría'}
                        </span>
                      </td>
                      <td className="p-6 text-right font-black text-[var(--accent)] text-lg">${prod.price.toFixed(2)}</td>
                      <td className="p-6 text-right font-black text-xl text-slate-900">{prod.stock}</td>
                      <td className="p-6 text-center">
                        <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-widest ${
                          prod.status === 'AVAILABLE' ? 'bg-emerald-50 text-emerald-600' : 'bg-red-50 text-red-600'
                        }`}>
                          {prod.status === 'AVAILABLE' ? 'Disponible' : 'Agotado'}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {isModalOpen && (
        <div className="fixed inset-0 bg-[var(--bg-sidebar)]/60 backdrop-blur-md flex items-center justify-center z-50 p-4 pl-72 sm:p-6 transition-all duration-500">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-md animate-slide-in relative overflow-hidden border border-white/20">
            <div className="absolute top-0 left-0 w-full h-1.5 bg-[var(--accent)]"></div>
            <h3 className="text-xl font-bold mb-1 text-[var(--text-dark)] uppercase tracking-tight">Nuevo Producto</h3>
            <p className="text-[var(--text-muted)] text-[10px] font-bold uppercase tracking-widest mb-8">Datos del Inventario</p>
            <form onSubmit={handleCreate} className="space-y-6">
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Nombre</label>
                <input required type="text" className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" placeholder="Ej: Café Molido" value={newProduct.name} onChange={(e) => setNewProduct({...newProduct, name: e.target.value})} />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Categoría</label>
                <select required className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" value={newProduct.categoryId} onChange={(e) => setNewProduct({...newProduct, categoryId: e.target.value})}>
                  <option value="">Seleccionar...</option>
                  {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Precio</label>
                  <input required type="number" step="0.01" className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" value={newProduct.price} onChange={(e) => setNewProduct({...newProduct, price: e.target.value})} />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Stock Inicial</label>
                  <input required type="number" className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" value={newProduct.stock} onChange={(e) => setNewProduct({...newProduct, stock: e.target.value})} />
                </div>
              </div>
              <div className="flex gap-3 pt-4">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 p-4 text-xs font-bold uppercase text-slate-400 hover:bg-slate-50 rounded-xl transition-all">Cancelar</button>
                <button type="submit" className="flex-1 p-4 bg-[var(--bg-sidebar)] text-white text-xs font-bold uppercase rounded-xl hover:bg-black shadow-lg transition-all">Guardar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
