import { useState, useEffect } from "react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { categoryService } from "../services/categoryService";

export const CategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newCategory, setNewCategory] = useState({ name: "", description: "" });

  const fetchCategories = async () => {
    try {
      const data = await categoryService.getAll();
      setCategories(data);
    } catch (error) { console.error(error); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchCategories(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await categoryService.create(newCategory);
      setIsModalOpen(false);
      setNewCategory({ name: "", description: "" });
      fetchCategories();
    } catch (error) { alert("Error al crear"); }
  };

  return (
    <AdminTemplate>
      <header className="flex flex-col sm:flex-row justify-between items-start sm:items-end gap-4 mb-10">
        <div>
          <div className="flex items-center gap-2 text-[var(--accent)] font-black text-[10px] uppercase tracking-widest mb-2">
            <div className="h-0.5 w-6 bg-[var(--accent)] rounded-full"></div>
            Inventario
          </div>
          <h2 className="text-3xl font-bold text-[var(--text-dark)] tracking-tight">Categorías</h2>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 px-6 py-3 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold text-sm rounded-xl shadow-lg transition-all"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nueva Categoría
        </button>
      </header>

      {loading ? (
        <div className="h-64 flex items-center justify-center">
          <div className="w-8 h-8 border-4 border-[var(--accent)] border-t-transparent rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="bg-white rounded-3xl shadow-xl border border-black/5 overflow-hidden">
          <table className="w-full text-left">
            <thead>
              <tr className="bg-[var(--bg-sidebar)] text-[var(--text-light)]">
                <th className="p-6 font-black uppercase tracking-widest text-[10px] opacity-40">ID</th>
                <th className="p-6 font-bold text-sm uppercase">Nombre</th>
                <th className="p-6 font-bold text-sm uppercase hidden md:table-cell">Descripción</th>
                <th className="p-6 font-bold text-sm uppercase text-right">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {categories.map((cat) => (
                <tr key={cat.id} className="hover:bg-slate-50 transition-colors">
                  <td className="p-6 text-[var(--text-muted)] font-mono text-xs">#{cat.id}</td>
                  <td className="p-6 font-bold text-[var(--text-dark)]">{cat.name}</td>
                  <td className="p-6 text-sm text-[var(--text-muted)] hidden md:table-cell">{cat.description}</td>
                  <td className="p-6 text-right">
                    <button className="p-2 text-slate-300 hover:text-red-500 transition-colors">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal simplifies distribution */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-[var(--bg-sidebar)]/60 backdrop-blur-md flex items-center justify-center z-50 p-4 pl-72 sm:p-6 transition-all duration-500">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-md animate-slide-in relative overflow-hidden border border-white/20">
            <div className="absolute top-0 left-0 w-full h-1.5 bg-[var(--accent)]"></div>
            <h3 className="text-xl font-bold mb-1 text-[var(--text-dark)] uppercase tracking-tight">Nueva Categoría</h3>
            <p className="text-[var(--text-muted)] text-[10px] font-bold uppercase tracking-widest mb-8">Gestión de Secciones</p>
            <form onSubmit={handleCreate} className="space-y-6">
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Nombre</label>
                <input
                  type="text"
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all font-bold text-sm"
                  placeholder="Ej: Postres"
                  value={newCategory.name}
                  onChange={(e) => setNewCategory({...newCategory, name: e.target.value})}
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black uppercase tracking-widest text-slate-400 ml-1">Descripción</label>
                <textarea
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all text-sm h-32"
                  placeholder="Descripción breve..."
                  value={newCategory.description}
                  onChange={(e) => setNewCategory({...newCategory, description: e.target.value})}
                />
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
