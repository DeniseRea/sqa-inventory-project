import { Link, useLocation, useNavigate } from "react-router-dom";
import {
  BarChart3,
  Coffee,
  FolderKanban,
  Home,
  LogOut,
  Menu,
  Package,
  X,
} from "lucide-react";
import { useState } from "react";

const navItems = [
  { name: "Dashboard", path: "/dashboard", icon: Home },
  { name: "Categorias", path: "/categories", icon: FolderKanban },
  { name: "Productos", path: "/products", icon: Package },
  { name: "Movimientos", path: "/movements", icon: BarChart3 },
];

const Brand = () => (
  <div className="flex items-center gap-3">
    <div className="flex h-12 w-12 items-center justify-center rounded-2xl border border-white/10 bg-white/10 shadow-inner">
      <img src="/favicon.png" alt="Don Gato" className="h-8 w-8 object-contain" />
    </div>
    <div>
      <p className="text-base font-black leading-tight text-[var(--text-light)]">Don Gato</p>
      <p className="text-[10px] font-black uppercase tracking-[0.18em] text-[var(--text-light)]/45">
        Inventario
      </p>
    </div>
  </div>
);

const NavLinks = ({ locationPath, mobile = false, onNavigate }) => (
  <nav className={mobile ? "grid gap-2" : "flex-1 space-y-2 overflow-y-auto px-4"}>
    {navItems.map((item) => {
      const Icon = item.icon;
      const isActive = locationPath === item.path;

      return (
        <Link
          key={item.path}
          to={item.path}
          onClick={onNavigate}
          className={`flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-bold transition ${
            isActive
              ? "bg-gradient-to-r from-[var(--accent)] to-[#d06b48] text-white shadow-lg shadow-black/24"
              : "text-[var(--text-light)]/62 hover:bg-white/10 hover:text-[var(--text-light)]"
          }`}
        >
          <Icon size={19} />
          <span>{item.name}</span>
          {isActive && <span className="ml-auto h-2 w-2 rounded-full bg-white" />}
        </Link>
      );
    })}
  </nav>
);

export const AdminTemplate = ({ children }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    navigate("/");
  };

  return (
    <div className="app-shell flex bg-[var(--bg-main)]">
      <aside className="premium-sidebar fixed inset-y-0 left-0 z-50 hidden w-64 flex-col shadow-2xl lg:flex">
        <div className="p-6">
          <Brand />
        </div>
        <div className="mx-6 mb-4 overflow-hidden rounded-3xl border border-white/10 bg-white/5 shadow-xl">
          <div className="h-20 bg-[url('/dashboard-hero.png')] bg-cover bg-center opacity-75" />
          <div className="p-4">
          <div className="mb-2 flex items-center gap-2 text-[var(--text-light)]">
            <Coffee size={17} />
            <span className="text-xs font-black uppercase tracking-[0.16em]">Operacion</span>
          </div>
          <p className="text-xs leading-relaxed text-[var(--text-light)]/48">
            Control diario de productos, categorias y stock.
          </p>
          </div>
        </div>
        <NavLinks locationPath={location.pathname} onNavigate={() => setMobileMenuOpen(false)} />
        <div className="p-6">
          <button
            type="button"
            onClick={handleLogout}
            className="flex w-full items-center justify-center gap-2 rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-xs font-black uppercase tracking-[0.14em] text-[var(--text-light)]/70 transition hover:bg-red-500/15 hover:text-red-300"
          >
            <LogOut size={17} />
            Cerrar sesion
          </button>
        </div>
      </aside>

      <div className="app-main min-w-0 flex-1 lg:pl-64">
        <header className="sticky top-0 z-40 border-b border-[var(--border-soft)] bg-[var(--bg-main)]/90 px-4 py-3 backdrop-blur-xl lg:hidden">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-[var(--bg-sidebar)]">
                <img src="/favicon.png" alt="Don Gato" className="h-7 w-7 object-contain" />
              </div>
              <div>
                <p className="text-sm font-black leading-tight">Don Gato</p>
                <p className="text-[10px] font-bold uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Inventario
                </p>
              </div>
            </div>
            <button
              type="button"
              onClick={() => setMobileMenuOpen(true)}
              className="rounded-xl border border-[var(--border-soft)] bg-white/70 p-2 text-[var(--text-dark)]"
              aria-label="Abrir menu"
            >
              <Menu size={22} />
            </button>
          </div>
        </header>

        {mobileMenuOpen && (
          <div className="fixed inset-0 z-[110] bg-[var(--bg-sidebar)]/60 p-4 backdrop-blur-sm lg:hidden">
            <div className="ml-auto flex h-full w-full max-w-sm flex-col rounded-3xl bg-[var(--bg-sidebar)] p-5 shadow-2xl">
              <div className="mb-6 flex items-center justify-between">
                <Brand />
                <button
                  type="button"
                  onClick={() => setMobileMenuOpen(false)}
                  className="rounded-xl bg-white/10 p-2 text-[var(--text-light)]"
                  aria-label="Cerrar menu"
                >
                  <X size={21} />
                </button>
              </div>
              <NavLinks locationPath={location.pathname} mobile onNavigate={() => setMobileMenuOpen(false)} />
              <button
                type="button"
                onClick={handleLogout}
                className="mt-auto flex items-center justify-center gap-2 rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-xs font-black uppercase tracking-[0.14em] text-[var(--text-light)]/70"
              >
                <LogOut size={17} />
                Cerrar sesion
              </button>
            </div>
          </div>
        )}

        <main className="min-h-screen px-4 py-5 pb-24 sm:px-6 lg:px-10 lg:py-9">
          <div className="mx-auto max-w-7xl animate-slide-in">{children}</div>
        </main>

        <nav className="fixed inset-x-3 bottom-3 z-40 grid grid-cols-4 rounded-3xl border border-white/50 bg-[var(--bg-sidebar)]/95 p-2 shadow-2xl backdrop-blur lg:hidden">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname === item.path;
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`flex flex-col items-center gap-1 rounded-2xl px-2 py-2 text-[10px] font-bold transition ${
                  isActive ? "bg-[var(--accent)] text-white" : "text-[var(--text-light)]/58"
                }`}
              >
                <Icon size={18} />
                <span className="max-w-full truncate">{item.name}</span>
              </Link>
            );
          })}
        </nav>
      </div>
    </div>
  );
};
