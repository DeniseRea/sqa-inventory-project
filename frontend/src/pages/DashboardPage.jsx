import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { productService } from "../services/productService";
import { categoryService } from "../services/categoryService";
import { stockService } from "../services/stockService";

export const DashboardPage = () => {
  const username = localStorage.getItem("username") || "Administrador";
  const navigate = useNavigate();
  const [stats, setStats] = useState({ categories: 0, products: 0, movements: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [c, p, m] = await Promise.all([
          categoryService.getAll(),
          productService.getAll(),
          stockService.getAll(),
        ]);
        setStats({ categories: c.length, products: p.length, movements: m.length });
      } catch (error) { console.error(error); }
      finally { setLoading(false); }
    };
    fetchStats();
  }, []);

  const cards = [
    { title: "Categorías", value: stats.categories, label: "Secciones", icon: "M7 7h.01M7 11h.01M7 15h.01M11 7h8M11 11h8M11 15h8", path: "/categories" },
    { title: "Productos", value: stats.products, label: "Artículos", icon: "M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4", path: "/products" },
    { title: "Movimientos", value: stats.movements, label: "Registros", icon: "M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4", path: "/movements" }
  ];

  return (
    <AdminTemplate>
      <header className="mb-10">
        <div className="flex items-center gap-3 text-[var(--accent)] font-black text-xs uppercase tracking-[0.2em] mb-2">
          <div className="h-1 w-8 bg-[var(--accent)] rounded-full"></div>
          Vista General
        </div>
        <h2 className="text-4xl font-bold text-[var(--text-dark)] tracking-tight">
          Hola, <span className="text-[var(--accent)]">{username}</span>
        </h2>
        <p className="text-[var(--text-muted)] mt-1 font-medium italic">Resumen actual del inventario artesanal.</p>
      </header>

      {loading ? (
        <div className="h-64 flex items-center justify-center">
          <div className="w-10 h-10 border-4 border-[var(--accent)] border-t-transparent rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
          {cards.map((card, idx) => (
            <div 
              key={idx} 
              onClick={() => navigate(card.path)}
              className="bg-[var(--bg-card)] p-8 rounded-3xl shadow-xl border border-white/5 relative overflow-hidden group cursor-pointer hover:scale-[1.02] transition-all duration-300"
            >
              <div className="flex items-start justify-between mb-6">
                <div className="p-3 bg-white/10 text-[var(--text-light)] rounded-xl group-hover:bg-[var(--accent)] transition-colors">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d={card.icon} />
                  </svg>
                </div>
                <span className="text-[var(--text-light)]/5 text-6xl font-black">{idx + 1}</span>
              </div>
              <h3 className="text-[var(--text-light)]/60 text-[10px] font-black uppercase tracking-widest mb-1">{card.title}</h3>
              <div className="flex items-baseline gap-2">
                <span className="text-4xl font-bold text-[var(--text-light)] tracking-tighter">{card.value}</span>
                <span className="text-[var(--text-light)]/40 text-[10px] font-bold uppercase">{card.label}</span>
              </div>
            </div>
          ))}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white/50 p-8 rounded-3xl border border-white/20">
          <h3 className="font-bold text-lg mb-6 flex items-center gap-2">
            <div className="w-1.5 h-1.5 bg-[var(--accent)] rounded-full"></div>
            Acciones Rápidas
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <button 
              onClick={() => navigate("/products")}
              className="p-5 bg-white hover:bg-[var(--accent)] hover:text-white rounded-2xl transition-all shadow-sm border border-black/5 text-left group"
            >
              <span className="block font-bold text-sm mb-1 uppercase tracking-tight">Nuevo Producto</span>
              <span className="block text-[10px] opacity-60 font-bold uppercase">Añadir al catálogo</span>
            </button>
            <button 
              onClick={() => navigate("/movements")}
              className="p-5 bg-white hover:bg-[var(--accent)] hover:text-white rounded-2xl transition-all shadow-sm border border-black/5 text-left group"
            >
              <span className="block font-bold text-sm mb-1 uppercase tracking-tight">Bitácora</span>
              <span className="block text-[10px] opacity-60 font-bold uppercase">Ver movimientos</span>
            </button>
          </div>
        </div>

        <div className="bg-[var(--bg-sidebar)] p-10 rounded-3xl text-[var(--text-light)] relative overflow-hidden flex flex-col justify-center border-b-4 border-b-[var(--accent)]">
          <div className="absolute top-0 right-0 w-32 h-32 bg-[var(--accent)]/10 -mr-10 -mt-10 blur-3xl rounded-full"></div>
          <h3 className="text-xl font-bold italic mb-2">Estado del Sistema</h3>
          <p className="text-[var(--text-light)]/50 text-sm font-medium mb-6 leading-relaxed">
            Todos los servicios están operando correctamente. El stock está sincronizado con la base de datos.
          </p>
          <div className="flex items-center gap-2 bg-white/5 w-fit px-4 py-2 rounded-full border border-white/5">
            <span className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></span>
            <span className="text-[10px] font-black uppercase tracking-widest text-emerald-400">Sistema en línea</span>
          </div>
        </div>
      </div>
    </AdminTemplate>
  );
};
