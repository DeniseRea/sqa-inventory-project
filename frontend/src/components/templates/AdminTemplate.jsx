import { Link, useLocation } from "react-router-dom";

export const AdminTemplate = ({ children }) => {
  const location = useLocation();
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    window.location.href = "/";
  };

  const navItems = [
    { name: "Dashboard", path: "/dashboard", icon: "M4 6h16M4 12h16M4 18h16" },
    { name: "Categorías", path: "/categories", icon: "M7 7h.01M7 11h.01M7 15h.01M11 7h8M11 11h8M11 15h8" },
    { name: "Productos", path: "/products", icon: "M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" },
    { name: "Movimientos", path: "/movements", icon: "M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" },
  ];

  return (
    <div className="flex h-screen bg-[var(--bg-main)] overflow-hidden">
      {/* Sidebar - Fixed Width */}
      <aside className="w-64 bg-[var(--bg-sidebar)] flex flex-col shadow-2xl z-[100]">
        <div className="p-8 flex flex-col items-center">
          <div className="w-16 h-16 bg-white/10 rounded-2xl p-3 mb-4 shadow-inner border border-white/5">
            <img src="/favicon.png" alt="Logo" className="w-full h-full object-contain" />
          </div>
          <h1 className="text-[var(--text-light)] font-bold text-lg tracking-tight font-heading">Don Gato Inv</h1>
          <p className="text-[var(--text-light)]/40 text-[10px] uppercase font-black tracking-widest mt-1">Gourmet System</p>
        </div>
        
        <nav className="flex-1 px-4 space-y-2 overflow-y-auto">
          {navItems.map((item) => {
            const isActive = location.pathname === item.path;
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`flex items-center gap-3 px-4 py-3.5 rounded-xl transition-all duration-200 group ${
                  isActive
                    ? "bg-[var(--accent)] text-white shadow-lg shadow-black/20"
                    : "text-[var(--text-light)]/50 hover:text-[var(--text-light)] hover:bg-white/5"
                }`}
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" className={isActive ? "opacity-100" : "opacity-40 group-hover:opacity-100"}>
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d={item.icon} />
                </svg>
                <span className="font-bold text-sm">{item.name}</span>
                {isActive && <div className="ml-auto w-1.5 h-1.5 bg-white rounded-full"></div>}
              </Link>
            );
          })}
        </nav>

        <div className="p-6 mt-auto">
          <button
            onClick={handleLogout}
            className="w-full flex items-center justify-center gap-2 py-3 px-4 bg-white/5 hover:bg-red-500/20 text-[var(--text-light)]/60 hover:text-red-400 rounded-xl transition-all font-bold text-xs uppercase tracking-widest border border-white/5"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            Cerrar Sesión
          </button>
        </div>
      </aside>

      {/* Main Content Area - Scrollable */}
      <main className="flex-1 overflow-y-auto bg-[var(--bg-main)] p-6 lg:p-10">
        <div className="max-w-7xl mx-auto animate-slide-in">
          {children}
        </div>
      </main>
    </div>
  );
};
