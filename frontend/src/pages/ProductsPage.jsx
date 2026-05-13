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
    description: "",
    sku: "",
    price: "",
    stock: 0,
    minStock: 5,
    categoryId: "",
  });

  const fetchData = async () => {
    try {
      const [pData, cData] = await Promise.all([
        productService.getAll(),
        categoryService.getAll(),
      ]);
      setProducts(pData);
      setCategories(cData);
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await productService.create({
        ...newProduct,
        price: parseFloat(newProduct.price),
        stock: parseInt(newProduct.stock),
        minStock: parseInt(newProduct.minStock),
        categoryId: parseInt(newProduct.categoryId),
      });
      setIsModalOpen(false);
      setNewProduct({
        name: "",
        description: "",
        sku: "",
        price: "",
        stock: 0,
        minStock: 5,
        categoryId: "",
      });
      fetchData();
    } catch (error) {
      alert("Error al crear producto");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Estás seguro de eliminar este producto?")) {
      try {
        await productService.delete(id);
        fetchData();
      } catch (error) {
        alert("Error al eliminar el producto");
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
          <h2 className="text-4xl font-bold tracking-tight">Productos</h2>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 px-6 py-3 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-bold rounded-2xl shadow-lg shadow-indigo-500/20 transition-all duration-200 transform hover:scale-[1.02]"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nuevo Producto
        </button>
      </header>

      {loading ? (
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-[var(--accent)]"></div>
        </div>
      ) : (
        <div className="bg-white rounded-[2rem] shadow-sm border border-slate-100 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-slate-50/50 border-b border-slate-100">
                  <th className="p-6 font-bold text-slate-400 text-xs uppercase tracking-wider">SKU</th>
                  <th className="p-6 font-bold text-slate-800">Producto</th>
                  <th className="p-6 font-bold text-slate-800">Categoría</th>
                  <th className="p-6 font-bold text-slate-800">Precio</th>
                  <th className="p-6 font-bold text-slate-800">Stock</th>
                  <th className="p-6 font-bold text-slate-800">Estado</th>
                  <th className="p-6 font-bold text-slate-800 text-right">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-50">
                {products.map((prod) => (
                  <tr key={prod.id} className="hover:bg-slate-50/50 transition-colors group">
                    <td className="p-6 text-slate-400 font-mono text-xs">{prod.sku}</td>
                    <td className="p-6">
                      <div className="flex flex-col">
                        <span className="font-semibold text-slate-900">{prod.name}</span>
                        <span className="text-xs text-slate-400 truncate max-w-[200px]">{prod.description}</span>
                      </div>
                    </td>
                    <td className="p-6">
                      <span className="px-3 py-1 bg-slate-100 text-slate-600 rounded-full text-xs font-bold">
                        {prod.categoryName}
                      </span>
                    </td>
                    <td className="p-6 font-bold text-slate-900">${prod.price.toFixed(2)}</td>
                    <td className="p-6">
                      <div className="flex items-center gap-2">
                        <span className={`font-bold ${prod.stock <= prod.minStock ? 'text-red-500' : 'text-slate-900'}`}>
                          {prod.stock}
                        </span>
                        {prod.stock <= prod.minStock && (
                          <span className="w-2 h-2 bg-red-500 rounded-full animate-pulse" title="Bajo Stock"></span>
                        )}
                      </div>
                    </td>
                    <td className="p-6">
                      <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold ${
                        prod.status === 'ACTIVE' ? 'bg-emerald-50 text-emerald-600' : 'bg-slate-100 text-slate-400'
                      }`}>
                        <span className={`w-1.5 h-1.5 rounded-full ${prod.status === 'ACTIVE' ? 'bg-emerald-500' : 'bg-slate-400'}`}></span>
                        {prod.status === 'ACTIVE' ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    <td className="p-6 text-right">
                      <button 
                        onClick={() => handleDelete(prod.id)}
                        className="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all opacity-0 group-hover:opacity-100"
                        title="Eliminar"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </td>
                  </tr>
                ))}
                {products.length === 0 && (
                  <tr>
                    <td colSpan="7" className="p-20 text-center">
                      <div className="flex flex-col items-center">
                        <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center text-slate-300 mb-4">
                          <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                          </svg>
                        </div>
                        <p className="text-slate-400 font-medium">No hay productos registrados.</p>
                      </div>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal de Creación */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-50 p-4">
          <div className="bg-white p-10 rounded-[2.5rem] shadow-2xl w-full max-w-2xl transform transition-all animate-fade-in-up max-h-[90vh] overflow-y-auto">
            <h3 className="text-2xl font-bold mb-2">Nuevo Producto</h3>
            <p className="text-slate-500 mb-8">Completa los datos para añadir un artículo al catálogo.</p>
            
            <form onSubmit={handleCreate} className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="md:col-span-2">
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Nombre del Producto</label>
                <input
                  type="text"
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  placeholder="Ej: Teclado Mecánico RGB"
                  value={newProduct.name}
                  onChange={(e) => setNewProduct({...newProduct, name: e.target.value})}
                />
              </div>
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">SKU / Código</label>
                <input
                  type="text"
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  placeholder="Ej: TEC-001"
                  value={newProduct.sku}
                  onChange={(e) => setNewProduct({...newProduct, sku: e.target.value})}
                />
              </div>
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Categoría</label>
                <select
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  value={newProduct.categoryId}
                  onChange={(e) => setNewProduct({...newProduct, categoryId: e.target.value})}
                >
                  <option value="">Seleccionar...</option>
                  {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                </select>
              </div>
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Precio Unitario ($)</label>
                <input
                  type="number"
                  step="0.01"
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  placeholder="0.00"
                  value={newProduct.price}
                  onChange={(e) => setNewProduct({...newProduct, price: e.target.value})}
                />
              </div>
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Stock Inicial</label>
                <input
                  type="number"
                  required
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  placeholder="0"
                  value={newProduct.stock}
                  onChange={(e) => setNewProduct({...newProduct, stock: e.target.value})}
                />
              </div>
              <div className="md:col-span-2">
                <label className="block text-sm font-bold text-slate-700 mb-2 ml-1">Descripción</label>
                <textarea
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all"
                  rows="2"
                  placeholder="Detalles adicionales del producto..."
                  value={newProduct.description}
                  onChange={(e) => setNewProduct({...newProduct, description: e.target.value})}
                ></textarea>
              </div>
              <div className="md:col-span-2 flex gap-4 pt-4">
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
                  Guardar Producto
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
