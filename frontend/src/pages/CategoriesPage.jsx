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
    } catch (error) {
      console.error("Error fetching categories:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await categoryService.create(newCategory);
      setIsModalOpen(false);
      setNewCategory({ name: "", description: "" });
      fetchCategories();
    } catch (error) {
      alert("Error al crear categoría");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Estás seguro de eliminar esta categoría?")) {
      try {
        await categoryService.delete(id);
        fetchCategories();
      } catch (error) {
        alert("No se puede eliminar la categoría (podría tener productos asociados)");
      }
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
          <h2 className="text-4xl font-bold tracking-tight">Categorías</h2>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 px-6 py-3 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold rounded-2xl shadow-lg shadow-indigo-500/20 transition-all duration-200 transform hover:scale-[1.02]"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nueva Categoría
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
                <th className="p-6 font-bold text-slate-400 text-xs uppercase tracking-wider">ID</th>
                <th className="p-6 font-bold text-slate-800">Nombre</th>
                <th className="p-6 font-bold text-slate-800">Descripción</th>
                <th className="p-6 font-bold text-slate-800 text-right">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-50">
              {categories.map((cat) => (
                <tr key={cat.id} className="hover:bg-slate-50/50 transition-colors group">
                  <td className="p-6 text-slate-400 font-mono text-sm">#{cat.id}</td>
                  <td className="p-6">
                    <span className="font-semibold text-slate-900">{cat.name}</span>
                  </td>
                  <td className="p-6 text-slate-500 max-w-xs truncate">{cat.description}</td>
                  <td className="p-6 text-right">
                    <div className="flex justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                      <button 
                        onClick={() => handleDelete(cat.id)}
                        className="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all"
                        title="Eliminar"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {categories.length === 0 && (
                <tr>
                  <td colSpan="4" className="p-20 text-center">
                    <div className="flex flex-col items-center">
                      <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center text-slate-300 mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                        </svg>
                      </div>
                      <p className="text-slate-400 font-medium">No hay categorías registradas.</p>
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal de Creación */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-50 p-4">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-md transform transition-all animate-fade-in-up">
            <h3 className="text-2xl font-bold mb-2">Nueva Categoría</h3>
            <p className="text-slate-500 mb-8">Define una nueva sección para organizar tus artículos.</p>
            
            <form onSubmit={handleCreate} className="space-y-6">
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Nombre</label>
                <input
                  type="text"
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  placeholder="Ej: Electrónicos"
                  value={newCategory.name}
                  onChange={(e) => setNewCategory({...newCategory, name: e.target.value})}
                />
              </div>
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Descripción</label>
                <textarea
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  rows="3"
                  placeholder="Describe qué tipos de productos incluye esta categoría..."
                  value={newCategory.description}
                  onChange={(e) => setNewCategory({...newCategory, description: e.target.value})}
                ></textarea>
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
                  Guardar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
