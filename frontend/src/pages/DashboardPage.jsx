import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Activity, ArrowRight, Boxes, FolderKanban, Package, RefreshCcw } from "lucide-react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { PageHeader } from "../components/ui/PageHeader";
import { productService } from "../services/productService";
import { categoryService } from "../services/categoryService";
import { stockService } from "../services/stockService";

export const DashboardPage = () => {
  const username = localStorage.getItem("username") || "Administrador";
  const navigate = useNavigate();
  const [stats, setStats] = useState({ categories: 0, products: 0, movements: 0, lowStock: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        const [categories, products, movements] = await Promise.all([
          categoryService.getAll(),
          productService.getAll(),
          stockService.getAll(),
        ]);
        setStats({
          categories: categories.length,
          products: products.length,
          movements: movements.length,
          lowStock: products.filter((product) => Number(product.stock) <= 5).length,
        });
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  const cards = useMemo(
    () => [
      {
        title: "Categorias",
        value: stats.categories,
        label: "Secciones activas",
        icon: FolderKanban,
        path: "/categories",
      },
      {
        title: "Productos",
        value: stats.products,
        label: "Articulos registrados",
        icon: Package,
        path: "/products",
      },
      {
        title: "Movimientos",
        value: stats.movements,
        label: "Registros de stock",
        icon: Activity,
        path: "/movements",
      },
    ],
    [stats],
  );

  return (
    <AdminTemplate>
      <PageHeader
        eyebrow="Vista general"
        image="/dashboard-hero.png"
        title={
          <>
            Hola, <span className="text-[var(--accent-soft)]">{username}</span>
          </>
        }
        description="Resumen del inventario y accesos rapidos para las operaciones del dia."
        stat={{ label: "Stock bajo", value: stats.lowStock }}
        action={
          <button type="button" onClick={() => navigate("/movements")} className="btn-primary px-5 py-3 text-sm">
            Registrar movimiento
            <ArrowRight size={18} />
          </button>
        }
      />

      {loading ? (
        <div className="flex h-64 items-center justify-center">
          <div className="h-10 w-10 animate-spin rounded-full border-4 border-[var(--accent)] border-t-transparent" />
        </div>
      ) : (
          <div className="grid gap-5 md:grid-cols-3">
            {cards.map((card) => {
            const Icon = card.icon;
            return (
              <button
                key={card.title}
                type="button"
                onClick={() => navigate(card.path)}
                className="surface-panel group rounded-3xl p-6 text-left transition hover:-translate-y-1 hover:shadow-2xl"
              >
                <div className="mb-6 flex items-center justify-between">
                  <span className="rounded-2xl bg-gradient-to-br from-[var(--bg-sidebar)] to-[var(--bg-card)] p-3 text-[var(--text-light)] shadow-lg">
                    <Icon size={22} />
                  </span>
                  <ArrowRight size={18} className="text-[var(--text-muted)] transition group-hover:text-[var(--accent)]" />
                </div>
                <p className="text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">{card.title}</p>
                <div className="mt-2 flex items-end gap-2">
                  <span className="text-4xl font-black text-[var(--text-dark)]">{card.value}</span>
                  <span className="pb-1 text-xs font-bold text-[var(--text-muted)]">{card.label}</span>
                </div>
              </button>
            );
          })}
        </div>
      )}

      <div className="mt-6 grid gap-5 lg:grid-cols-[1.1fr_0.9fr]">
        <section className="surface-panel rounded-3xl p-6">
          <div className="mb-5 flex items-center justify-between gap-4">
            <div>
              <h2 className="text-lg font-black">Acciones rapidas</h2>
              <p className="text-sm text-[var(--text-muted)]">Tareas frecuentes para mantener el inventario al dia.</p>
            </div>
            <Boxes className="text-[var(--accent)]" size={24} />
          </div>
          <div className="grid gap-3 sm:grid-cols-2">
            <button type="button" onClick={() => navigate("/products")} className="btn-secondary px-4 py-4 text-left">
              <Package size={19} />
              Nuevo producto
            </button>
            <button type="button" onClick={() => navigate("/categories")} className="btn-secondary px-4 py-4 text-left">
              <FolderKanban size={19} />
              Organizar categorias
            </button>
          </div>
        </section>

        <section className="page-hero rounded-3xl p-6 text-[var(--text-light)] shadow-2xl">
          <div className="mb-5 flex items-center justify-between">
            <div>
              <p className="text-xs font-black uppercase tracking-[0.16em] text-[var(--accent)]">Estado</p>
              <h2 className="mt-1 text-xl font-black">Sistema en linea</h2>
            </div>
            <span className="flex h-11 w-11 items-center justify-center rounded-2xl bg-white/10">
              <RefreshCcw size={21} />
            </span>
          </div>
          <p className="text-sm leading-6 text-white/60">
            Los servicios responden y el stock se mantiene sincronizado con la base de datos.
          </p>
          <div className="mt-5 rounded-2xl border border-white/10 bg-white/5 p-4">
            <p className="text-xs font-bold uppercase tracking-[0.12em] text-white/48">Productos con stock bajo</p>
            <p className="mt-2 text-3xl font-black">{stats.lowStock}</p>
          </div>
        </section>
      </div>
    </AdminTemplate>
  );
};
