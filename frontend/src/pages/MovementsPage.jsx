import { useCallback, useEffect, useMemo, useState } from "react";
import { ArrowDownLeft, ArrowUpRight, BarChart3, Plus, Search } from "lucide-react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { PageHeader } from "../components/ui/PageHeader";
import { stockService } from "../services/stockService";
import { productService } from "../services/productService";
import { Feedback, FieldError } from "../components/ui/Feedback";
import { hasErrors, validateMovement } from "../utils/validation";

const REASONS_BY_TYPE = {
  ENTRADA: ["REPOSICION", "DEVOLUCION", "AJUSTE"],
  SALIDA: ["VENTA", "MERMA", "AJUSTE"],
};

const formatDate = (value) => {
  if (!value) return "Sin fecha";
  return new Date(value).toLocaleString("es-EC", {
    dateStyle: "short",
    timeStyle: "short",
  });
};

export const MovementsPage = () => {
  const [movements, setMovements] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [feedback, setFeedback] = useState(null);
  const [newMovement, setNewMovement] = useState({
    productId: "",
    quantity: "",
    type: "ENTRADA",
    reason: "REPOSICION",
  });

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const [movementData, productData] = await Promise.all([stockService.getAll(), productService.getAll()]);
      setMovements(movementData);
      setProducts(productData);
    } catch {
      setFeedback({ type: "error", message: "No se pudieron cargar los movimientos." });
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchData();
  }, [fetchData]);

  const validation = validateMovement(newMovement, products);
  const isInvalid = hasErrors(validation.errors);

  const productById = useMemo(() => {
    return products.reduce((acc, product) => {
      acc[String(product.id)] = product;
      return acc;
    }, {});
  }, [products]);

  const filteredMovements = useMemo(() => {
    const query = searchTerm.trim().toLowerCase();
    if (!query) return movements;

    return movements.filter((movement) => {
      const product = productById[String(movement.productId)];
      return (product?.name ?? "").toLowerCase().includes(query);
    });
  }, [movements, productById, searchTerm]);

  const handleTypeChange = (type) => {
    setNewMovement((prev) => ({
      ...prev,
      type,
      reason: REASONS_BY_TYPE[type][0],
    }));
  };

  const handleRegister = async (event) => {
    event.preventDefault();
    if (isInvalid) return;

    try {
      await stockService.register({
        ...newMovement,
        productId: Number(newMovement.productId),
        quantity: Number(newMovement.quantity),
      });
      setIsModalOpen(false);
      setNewMovement({ productId: "", quantity: "", type: "ENTRADA", reason: "REPOSICION" });
      setFeedback({ type: "success", message: "Movimiento registrado correctamente." });
      fetchData();
    } catch {
      setFeedback({ type: "error", message: "No se pudo registrar el movimiento. Verifica el stock disponible." });
    }
  };

  const MovementBadge = ({ type }) => {
    const incoming = type === "ENTRADA";
    const Icon = incoming ? ArrowDownLeft : ArrowUpRight;
    return (
      <span
        className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1 text-[11px] font-black uppercase tracking-[0.12em] ${
          incoming ? "bg-emerald-50 text-emerald-700" : "bg-orange-50 text-orange-700"
        }`}
      >
        <Icon size={14} />
        {incoming ? "Entrada" : "Salida"}
      </span>
    );
  };

  return (
    <AdminTemplate>
      <PageHeader
        eyebrow="Bitacora"
        image="/movements-hero.png"
        title="Movimientos"
        description="Registra entradas y salidas manteniendo el stock consistente."
        stat={{ label: "Registros", value: movements.length }}
        action={
          <button type="button" onClick={() => setIsModalOpen(true)} className="btn-primary px-5 py-3 text-sm">
            <Plus size={18} />
            Registrar movimiento
          </button>
        }
      />

      {feedback && (
        <div className="mb-5">
          <Feedback type={feedback.type} message={feedback.message} onClose={() => setFeedback(null)} />
        </div>
      )}

      <section className="surface-panel mb-5 rounded-3xl p-4">
        <div className="relative max-w-md">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-[var(--text-muted)]" size={18} />
          <input
            type="text"
            className="input-base py-3 pl-11 pr-4 text-sm font-semibold"
            placeholder="Filtrar por producto..."
            value={searchTerm}
            onChange={(event) => setSearchTerm(event.target.value)}
          />
        </div>
      </section>

      {loading && movements.length === 0 ? (
        <div className="flex h-64 items-center justify-center">
          <div className="h-9 w-9 animate-spin rounded-full border-4 border-[var(--accent)] border-t-transparent" />
        </div>
      ) : filteredMovements.length === 0 ? (
        <div className="surface-panel rounded-3xl p-10 text-center text-sm font-semibold text-[var(--text-muted)]">
          No se encontraron movimientos.
        </div>
      ) : (
        <>
          <div className="data-shell hidden overflow-hidden rounded-3xl lg:block">
            <table className="w-full text-left">
              <thead className="table-head text-[var(--text-light)]">
                <tr>
                  <th className="px-5 py-4 text-xs font-black uppercase tracking-[0.16em] text-white/48">Fecha</th>
                  <th className="px-5 py-4 text-xs font-black uppercase tracking-[0.16em]">Producto</th>
                  <th className="px-5 py-4 text-center text-xs font-black uppercase tracking-[0.16em]">Tipo</th>
                  <th className="px-5 py-4 text-right text-xs font-black uppercase tracking-[0.16em]">Cantidad</th>
                  <th className="px-5 py-4 text-xs font-black uppercase tracking-[0.16em]">Motivo</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border-soft)]">
                {filteredMovements.map((movement) => {
                  const product = productById[String(movement.productId)];
                  const incoming = movement.type === "ENTRADA";
                  return (
                    <tr key={movement.id} className="transition hover:bg-[var(--surface-soft)]/45">
                      <td className="px-5 py-4 font-mono text-xs font-bold text-[var(--text-muted)]">
                        {formatDate(movement.createdAt)}
                      </td>
                      <td className="px-5 py-4 font-black">{product?.name ?? `Producto #${movement.productId}`}</td>
                      <td className="px-5 py-4 text-center">
                        <MovementBadge type={movement.type} />
                      </td>
                      <td
                        className={`px-5 py-4 text-right text-lg font-black ${
                          incoming ? "text-emerald-700" : "text-orange-700"
                        }`}
                      >
                        {incoming ? "+" : "-"}
                        {movement.quantity}
                      </td>
                      <td className="px-5 py-4">
                        <span className="rounded-full bg-[var(--surface-soft)] px-3 py-1 text-xs font-bold text-[var(--text-muted)]">
                          {movement.reason}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>

          <div className="grid gap-4 lg:hidden">
            {filteredMovements.map((movement) => {
              const product = productById[String(movement.productId)];
              const incoming = movement.type === "ENTRADA";
              return (
                <article key={movement.id} className="surface-panel rounded-3xl p-4">
                  <div className="flex items-start justify-between gap-3">
                    <div className="flex min-w-0 gap-3">
                      <span className="rounded-2xl bg-[var(--bg-sidebar)] p-3 text-[var(--text-light)]">
                        <BarChart3 size={20} />
                      </span>
                      <div className="min-w-0">
                        <p className="truncate text-base font-black">{product?.name ?? `Producto #${movement.productId}`}</p>
                        <p className="font-mono text-xs font-bold text-[var(--text-muted)]">{formatDate(movement.createdAt)}</p>
                      </div>
                    </div>
                    <MovementBadge type={movement.type} />
                  </div>
                  <div className="mt-4 grid grid-cols-2 gap-3">
                    <div className="rounded-2xl bg-white/75 p-3">
                      <p className="text-[11px] font-black uppercase tracking-[0.12em] text-[var(--text-muted)]">Cantidad</p>
                      <p className={`mt-1 text-lg font-black ${incoming ? "text-emerald-700" : "text-orange-700"}`}>
                        {incoming ? "+" : "-"}
                        {movement.quantity}
                      </p>
                    </div>
                    <div className="rounded-2xl bg-white/75 p-3">
                      <p className="text-[11px] font-black uppercase tracking-[0.12em] text-[var(--text-muted)]">Motivo</p>
                      <p className="mt-1 truncate text-sm font-black">{movement.reason}</p>
                    </div>
                  </div>
                </article>
              );
            })}
          </div>
        </>
      )}

      {isModalOpen && (
        <div className="fixed inset-0 z-[100] flex items-center justify-center bg-[var(--bg-sidebar)]/60 p-4 backdrop-blur-sm">
          <div className="max-h-[92vh] w-full max-w-lg overflow-y-auto rounded-3xl bg-white p-6 shadow-2xl">
            <div className="mb-6">
              <p className="text-xs font-black uppercase tracking-[0.16em] text-[var(--accent)]">Stock</p>
              <h2 className="mt-1 text-2xl font-black text-[var(--text-dark)]">Registrar movimiento</h2>
              <p className="mt-1 text-sm text-[var(--text-muted)]">
                Las salidas no pueden superar el stock disponible.
              </p>
            </div>
            <form onSubmit={handleRegister} className="space-y-5">
              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Producto
                </label>
                <select
                  className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.productId ? "input-error" : ""}`}
                  value={newMovement.productId}
                  onChange={(event) => setNewMovement((prev) => ({ ...prev, productId: event.target.value }))}
                >
                  <option value="">Seleccionar producto...</option>
                  {products.map((product) => (
                    <option key={product.id} value={product.id}>
                      {product.name} - Stock: {product.stock}
                    </option>
                  ))}
                </select>
                <FieldError message={validation.errors.productId} />
              </div>

              <div className="grid gap-4 sm:grid-cols-2">
                <div>
                  <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                    Tipo
                  </label>
                  <select
                    className="input-base px-4 py-3 text-sm font-semibold"
                    value={newMovement.type}
                    onChange={(event) => handleTypeChange(event.target.value)}
                  >
                    <option value="ENTRADA">Entrada (+)</option>
                    <option value="SALIDA">Salida (-)</option>
                  </select>
                </div>
                <div>
                  <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                    Cantidad
                  </label>
                  <input
                    type="number"
                    inputMode="numeric"
                    min="1"
                    max="10000"
                    step="1"
                    className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.quantity ? "input-error" : ""}`}
                    placeholder="1"
                    value={newMovement.quantity}
                    onChange={(event) => setNewMovement((prev) => ({ ...prev, quantity: event.target.value }))}
                  />
                  <FieldError message={validation.errors.quantity} />
                </div>
              </div>

              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Motivo
                </label>
                <select
                  className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.reason ? "input-error" : ""}`}
                  value={newMovement.reason}
                  onChange={(event) => setNewMovement((prev) => ({ ...prev, reason: event.target.value }))}
                >
                  {REASONS_BY_TYPE[newMovement.type].map((reason) => (
                    <option key={reason} value={reason}>
                      {reason}
                    </option>
                  ))}
                </select>
                <FieldError message={validation.errors.reason} />
              </div>

              {validation.selectedProduct && (
                <Feedback
                  type="info"
                  message={`Stock actual de ${validation.selectedProduct.name}: ${validation.selectedProduct.stock} unidades.`}
                />
              )}

              <div className="flex gap-3 pt-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="btn-secondary flex-1 px-4 py-3">
                  Cancelar
                </button>
                <button type="submit" disabled={isInvalid} className="btn-primary flex-1 px-4 py-3">
                  Registrar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminTemplate>
  );
};
