import { useState, useEffect, useCallback } from "react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { productService } from "../services/productService";
import { categoryService } from "../services/categoryService";

export const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  
  const [formData, setFormData] = useState({ 
    name: "", 
    price: "", 
    stock: 0, 
    categoryId: ""
  });

  const fetchData = useCallback(async (query = "") => {
    try {
      setLoading(true);
      const [p, c] = await Promise.all([
        query ? productService.search(query) : productService.getAll(),
        categoryService.getAll()
      ]);
      setProducts(p); 
      setCategories(c);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchData(searchQuery);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchQuery, fetchData]);

  const handleOpenCreate = () => {
    setEditingProduct(null);
    setFormData({ name: "", price: "", stock: 0, categoryId: "" });
    setIsModalOpen(true);
  };

  const handleOpenEdit = (product) => {
    setEditingProduct(product);
    setFormData({ 
      name: product.name, 
      price: product.price, 
      stock: product.stock, 
      categoryId: product.categoryId 
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = {
        ...formData,
        price: parseFloat(formData.price),
        stock: parseInt(formData.stock),
        categoryId: parseInt(formData.categoryId)
      };

      if (editingProduct) {
        await productService.update(editingProduct.id, data);
      } else {
        await productService.create(data);
      }
      setIsModalOpen(false);
      fetchData(searchQuery);
    } catch (err) { alert("Error al procesar producto"); }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Eliminar este producto?")) {
      try {
        await productService.delete(id);
        fetchData(searchQuery);
      } catch (err) { alert("Error al eliminar"); }
    }
  };

  return (
    <AdminTemplate>
      <header className="flex flex-col lg:flex-row justify-between items-start lg:items-end gap-6 mb-10">
        <div className="flex-1">
          <div className="flex items-center gap-2 text-[var(--accent)] font-black text-[10px] uppercase tracking-widest mb-2">
            <div className="h-0.5 w-6 bg-[var(--accent)] rounded-full"></div>
            Catálogo
          </div>
          <h2 className="text-3xl font-bold text-[var(--text-dark)] tracking-tight mb-4">Productos</h2>
          
          {/* Barra de Búsqueda (Implementación de GET /search) */}
          <div className="relative w-full max-w-md group">
            <svg className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-[var(--accent)] transition-colors" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input 
              type="text"
              placeholder="Buscar por nombre..."
              className="w-full pl-12 pr-4 py-3 bg-white border border-black/5 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] shadow-sm transition-all text-sm font-medium"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
        </div>
        
        <button 
          onClick={handleOpenCreate}
          className="flex items-center gap-2 px-6 py-4 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold text-sm rounded-2xl shadow-lg transition-all whitespace-nowrap"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nuevo Producto
        </button>
      </header>

      {loading && products.length === 0 ? (
        <div className="h-64 flex items-center justify-center">
          <div className="w-8 h-8 border-4 border-[var(--accent)] border-t-transparent rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="bg-white rounded-[2.5rem] shadow-xl border border-black/5 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="bg-[var(--bg-sidebar)] text-[var(--text-light)]">
                  <th className="p-6 font-black uppercase tracking-widest text-[10px] opacity-40">ID</th>
                  <th className="p-6 font-bold text-sm uppercase">Nombre</th>
                  <th className="p-6 font-bold text-sm uppercase">Categoría</th>
                  <th className="p-6 font-bold text-sm uppercase text-right">Precio</th>
                  <th className="p-6 font-bold text-sm uppercase text-right">Stock</th>
                  <th className="p-6 font-bold text-sm uppercase text-center">Estado</th>
                  <th className="p-6 font-bold text-sm uppercase text-right">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm">
                {products.length > 0 ? products.map((prod) => {
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
                      <td className="p-6 text-right">
                        <div className="flex justify-end gap-1">
                          <button onClick={() => handleOpenEdit(prod)} className="p-2 hover:bg-slate-100 text-slate-400 hover:text-[var(--accent)] rounded-lg transition-all">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                          </button>
                          <button onClick={() => handleDelete(prod.id)} className="p-2 hover:bg-red-50 text-slate-400 hover:text-red-500 rounded-lg transition-all">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                            </svg>
                          </button>
                        </div>
                      </td>
                    </tr>
                  )
                }) : (
                  <tr>
                    <td colSpan="7" className="p-12 text-center text-slate-400 italic font-medium">No se encontraron productos...</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {isModalOpen && (
        <div className="fixed inset-0 bg-[var(--bg-sidebar)]/60 backdrop-blur-md flex items-center justify-center z-50 p-4 pl-72 sm:p-6 transition-all duration-500">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-md animate-slide-in relative overflow-hidden border border-white/20">
            <div className="absolute top-0 left-0 w-full h-1.5 bg-[var(--accent)]"></div>
            <h3 className="text-xl font-bold mb-1 text-[var(--text-dark)] uppercase tracking-tight">
              {editingProduct ? "Editar Producto" : "Nuevo Producto"}
            </h3>
            <p className="text-[var(--text-muted)] text-[10px] font-bold uppercase tracking-widest mb-8">Datos del Inventario</p>
            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Nombre</label>
                <input required type="text" className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" placeholder="Ej: Café Molido" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Categoría</label>
                <select required className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" value={formData.categoryId} onChange={(e) => setFormData({...formData, categoryId: e.target.value})}>
                  <option value="">Seleccionar...</option>
                  {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Precio</label>
                  <input required type="number" step="0.01" className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" value={formData.price} onChange={(e) => setFormData({...formData, price: e.target.value})} />
                </div>
                {!editingProduct && (
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Stock Inicial</label>
                    <input required type="number" className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm" value={formData.stock} onChange={(e) => setFormData({...formData, stock: e.target.value})} />
                  </div>
                )}
              </div>
              <div className="flex gap-3 pt-4">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 p-4 text-xs font-bold uppercase text-slate-400 hover:bg-slate-50 rounded-xl transition-all">Cancelar</button>
                <button type="submit" className="flex-1 p-4 bg-[var(--bg-sidebar)] text-white text-xs font-bold uppercase rounded-xl hover:bg-black shadow-lg transition-all">
                  {editingProduct ? "Actualizar" : "Guardar"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
