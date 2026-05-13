import { useState, useEffect } from "react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { productService } from "../services/productService";
import { categoryService } from "../services/categoryService";
import { stockService } from "../services/stockService";

export const DashboardPage = () => {
  const username = localStorage.getItem("username") || "Administrador";
  const [stats, setStats] = useState({
    categories: 0,
    products: 0,
    movements: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [categories, products, movements] = await Promise.all([
          categoryService.getAll(),
          productService.getAll(),
          stockService.getAll(),
        ]);

        setStats({
          categories: categories.length,
          products: products.length,
          movements: movements.length,
        });
      } catch (error) {
        console.error("Error fetching stats:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  const cards = [
    {
      title: "Categorías Activas",
      value: stats.categories,
      label: "Categorías registradas",
      color: "bg-blue-500",
      icon: (
        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 7h.01M7 11h.01M7 15h.01M11 7h8M11 11h8M11 15h8" />
        </svg>
      )
    },
    {
      title: "Total Productos",
      value: stats.products,
      label: "En catálogo",
      color: "bg-indigo-500",
      icon: (
        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
        </svg>
      )
    },
    {
      title: "Movimientos",
      value: stats.movements,
      label: "Entradas y salidas",
      color: "bg-emerald-500",
      icon: (
        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
        </svg>
      )
    }
  ];

  return (
    <AdminTemplate>
      <header className="mb-12">
        <div className="flex items-center gap-2 text-[var(--accent)] font-semibold mb-2">
          <span className="w-8 h-px bg-[var(--accent)]"></span>
          Sistema de Inventario
        </div>
        <h2 className="text-4xl font-bold tracking-tight mb-3">
          Hola, <span className="text-[var(--accent)]">{username}</span>.
        </h2>
        <p className="text-[var(--text-muted)] text-lg max-w-2xl">
          Bienvenido al panel de control central. Aquí tienes un resumen rápido del estado actual de tu inventario.
        </p>
      </header>

      {loading ? (
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-[var(--accent)]"></div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {cards.map((card, idx) => (
            <div key={idx} className="bg-white p-8 rounded-[2rem] shadow-sm border border-slate-100 hover:shadow-xl hover:-translate-y-1 transition-all duration-300 relative overflow-hidden group">
              <div className={`absolute top-0 right-0 w-32 h-32 ${card.color} opacity-5 -mr-16 -mt-16 rounded-full group-hover:opacity-10 transition-opacity`}></div>
              
              <div className="flex items-center justify-between mb-6">
                <div className={`w-12 h-12 ${card.color} text-white rounded-2xl flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform`}>
                  {card.icon}
                </div>
              </div>
              
              <h3 className="text-slate-500 font-medium mb-1">{card.title}</h3>
              <div className="flex items-baseline gap-2">
                <p className="text-4xl font-bold text-slate-900 tracking-tight">
                  {card.value}
                </p>
                <span className="text-slate-400 text-sm">{card.label}</span>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Placeholder for Quick Actions or Recent Activity */}
      <div className="mt-12 grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white p-8 rounded-[2rem] border border-slate-100 shadow-sm">
          <h3 className="text-xl font-bold mb-6 flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            Accesos Rápidos
          </h3>
          <div className="grid grid-cols-2 gap-4">
            <button className="p-4 bg-slate-50 hover:bg-[var(--accent-soft)] hover:text-[var(--accent)] rounded-2xl transition-all duration-200 text-left">
              <p className="font-bold mb-1">Nuevo Producto</p>
              <p className="text-xs text-slate-500">Agrega un artículo al catálogo</p>
            </button>
            <button className="p-4 bg-slate-50 hover:bg-[var(--accent-soft)] hover:text-[var(--accent)] rounded-2xl transition-all duration-200 text-left">
              <p className="font-bold mb-1">Registrar Salida</p>
              <p className="text-xs text-slate-500">Documenta una venta o retiro</p>
            </button>
          </div>
        </div>
        
        <div className="bg-slate-900 p-8 rounded-[2rem] text-white overflow-hidden relative">
          <div className="absolute top-0 right-0 w-64 h-64 bg-indigo-500 opacity-20 -mr-32 -mt-32 blur-3xl rounded-full"></div>
          <h3 className="text-xl font-bold mb-4 relative z-1">Estado del Sistema</h3>
          <p className="text-slate-400 mb-6 relative z-1 text-sm">
            Todos los servicios están operando normalmente. La base de datos está sincronizada.
          </p>
          <div className="flex items-center gap-2 relative z-1">
            <span className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></span>
            <span className="text-emerald-500 font-bold text-sm">Operativo</span>
          </div>
        </div>
      </div>
    </AdminTemplate>
  );
};


