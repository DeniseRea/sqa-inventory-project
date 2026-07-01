import { useCallback, useEffect, useMemo, useState } from "react";
import { Edit3, Package, Plus, Search, Trash2 } from "lucide-react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { productService } from "../services/productService";
import { categoryService } from "../services/categoryService";
import { ConfirmDialog } from "../components/ui/ConfirmDialog";
import { Feedback, FieldError, FieldWarning } from "../components/ui/Feedback";
import { formatCurrency, hasErrors, validateProduct } from "../utils/validation";

export const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [feedback, setFeedback] = useState(null);
  const [productToDelete, setProductToDelete] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    price: "",
    stock: "0",
    categoryId: "",
  });

  const fetchData = useCallback(async (query = "") => {
    try {
      setLoading(true);
      const [productsData, categoriesData] = await Promise.all([
        query ? productService.search(query) : productService.getAll(),
        categoryService.getAll(),
      ]);
      setProducts(productsData);
      setCategories(categoriesData);
    } catch {
      setFeedback({ type: "error", message: "No se pudieron cargar los productos." });
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchData(searchQuery.trim());
    }, 300);
    return () => clearTimeout(timer);
  }, [fetchData, searchQuery]);

  const validation = validateProduct({
    ...formData,
    includeStock: !editingProduct,
  });
  const isInvalid = hasErrors(validation.errors);

  const categoryById = useMemo(() => {
    return categories.reduce((acc, category) => {
      acc[String(category.id)] = category;
      return acc;
    }, {});
  }, [categories]);

  const handleOpenCreate = () => {
    setEditingProduct(null);
    setFormData({ name: "", price: "", stock: "0", categoryId: "" });
    setFeedback(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (product) => {
    setEditingProduct(product);
    setFormData({
      name: product.name ?? "",
      price: String(product.price ?? ""),
      stock: String(product.stock ?? 0),
      categoryId: String(product.categoryId ?? ""),
    });
    setFeedback(null);
    setIsModalOpen(true);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (isInvalid) return;

    try {
      const payload = {
        name: formData.name.trim(),
        price: Number(formData.price),
        categoryId: Number(formData.categoryId),
      };

      if (editingProduct) {
        await productService.update(editingProduct.id, payload);
        setFeedback({ type: "success", message: "Producto actualizado correctamente." });
      } else {
        await productService.create({
          ...payload,
          stock: Number(formData.stock),
        });
        setFeedback({ type: "success", message: "Producto creado correctamente." });
      }

      setIsModalOpen(false);
      fetchData(searchQuery.trim());
    } catch {
      setFeedback({ type: "error", message: "No se pudo procesar el producto." });
    }
  };

  const handleDelete = async () => {
    if (!productToDelete) return;

    try {
      await productService.delete(productToDelete.id);
      setFeedback({ type: "success", message: "Producto eliminado correctamente." });
      setProductToDelete(null);
      fetchData(searchQuery.trim());
    } catch {
      setFeedback({ type: "error", message: "No se pudo eliminar el producto." });
      setProductToDelete(null);
    }
  };

  const renderStatus = (status) => (
    <span
      className={`inline-flex rounded-full px-3 py-1 text-[11px] font-black uppercase tracking-[0.12em] ${
        status === "AVAILABLE" ? "bg-emerald-50 text-emerald-700" : "bg-red-50 text-red-700"
      }`}
    >
      {status === "AVAILABLE" ? "Disponible" : "Agotado"}
    </span>
  );

  return (
    <AdminTemplate>
      <header className="mb-7 grid gap-5 lg:grid-cols-[1fr_auto] lg:items-end">
        <div>
          <div className="mb-2 flex items-center gap-2 text-xs font-black uppercase tracking-[0.18em] text-[var(--accent)]">
            <span className="h-1 w-8 rounded-full bg-[var(--accent)]" />
            Catalogo
          </div>
          <h1 className="text-3xl font-black tracking-normal text-[var(--text-dark)]">Productos</h1>
          <p className="mt-2 max-w-2xl text-sm leading-6 text-[var(--text-muted)]">
            Administra precios, categorias y disponibilidad del inventario.
          </p>
        </div>
        <button type="button" onClick={handleOpenCreate} className="btn-primary px-5 py-3 text-sm">
          <Plus size={18} />
          Nuevo producto
        </button>
      </header>

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
            placeholder="Buscar por nombre..."
            value={searchQuery}
            onChange={(event) => setSearchQuery(event.target.value)}
          />
        </div>
      </section>

      {loading && products.length === 0 ? (
        <div className="flex h-64 items-center justify-center">
          <div className="h-9 w-9 animate-spin rounded-full border-4 border-[var(--accent)] border-t-transparent" />
        </div>
      ) : products.length === 0 ? (
        <div className="surface-panel rounded-3xl p-10 text-center text-sm font-semibold text-[var(--text-muted)]">
          No se encontraron productos.
        </div>
      ) : (
        <>
          <div className="hidden overflow-hidden rounded-3xl border border-[var(--border-soft)] bg-white/90 shadow-xl lg:block">
            <table className="w-full text-left">
              <thead className="bg-[var(--bg-sidebar)] text-[var(--text-light)]">
                <tr>
                  <th className="px-5 py-4 text-xs font-black uppercase tracking-[0.16em] text-white/48">ID</th>
                  <th className="px-5 py-4 text-xs font-black uppercase tracking-[0.16em]">Nombre</th>
                  <th className="px-5 py-4 text-xs font-black uppercase tracking-[0.16em]">Categoria</th>
                  <th className="px-5 py-4 text-right text-xs font-black uppercase tracking-[0.16em]">Precio</th>
                  <th className="px-5 py-4 text-right text-xs font-black uppercase tracking-[0.16em]">Stock</th>
                  <th className="px-5 py-4 text-center text-xs font-black uppercase tracking-[0.16em]">Estado</th>
                  <th className="px-5 py-4 text-right text-xs font-black uppercase tracking-[0.16em]">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border-soft)]">
                {products.map((product) => {
                  const category = categoryById[String(product.categoryId)];
                  return (
                    <tr key={product.id} className="transition hover:bg-[var(--surface-soft)]/45">
                      <td className="px-5 py-4 font-mono text-xs font-bold text-[var(--text-muted)]">#{product.id}</td>
                      <td className="px-5 py-4 font-black">{product.name}</td>
                      <td className="px-5 py-4">
                        <span className="rounded-full bg-[var(--surface-soft)] px-3 py-1 text-xs font-bold text-[var(--text-muted)]">
                          {category?.name ?? "Sin categoria"}
                        </span>
                      </td>
                      <td className="px-5 py-4 text-right text-base font-black text-[var(--accent)]">
                        {formatCurrency(product.price)}
                      </td>
                      <td className="px-5 py-4 text-right text-base font-black">{product.stock}</td>
                      <td className="px-5 py-4 text-center">{renderStatus(product.status)}</td>
                      <td className="px-5 py-4">
                        <div className="flex justify-end gap-2">
                          <button
                            type="button"
                            onClick={() => handleOpenEdit(product)}
                            className="rounded-xl p-2 text-[var(--text-muted)] transition hover:bg-white hover:text-[var(--accent)]"
                            aria-label={`Editar ${product.name}`}
                          >
                            <Edit3 size={18} />
                          </button>
                          <button
                            type="button"
                            onClick={() => setProductToDelete(product)}
                            className="rounded-xl p-2 text-[var(--text-muted)] transition hover:bg-red-50 hover:text-red-600"
                            aria-label={`Eliminar ${product.name}`}
                          >
                            <Trash2 size={18} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>

          <div className="grid gap-4 lg:hidden">
            {products.map((product) => {
              const category = categoryById[String(product.categoryId)];
              return (
                <article key={product.id} className="surface-panel rounded-3xl p-4">
                  <div className="flex items-start justify-between gap-3">
                    <div className="flex min-w-0 gap-3">
                      <span className="rounded-2xl bg-[var(--bg-sidebar)] p-3 text-[var(--text-light)]">
                        <Package size={20} />
                      </span>
                      <div className="min-w-0">
                        <p className="truncate text-base font-black">{product.name}</p>
                        <p className="text-xs font-bold text-[var(--text-muted)]">{category?.name ?? "Sin categoria"}</p>
                      </div>
                    </div>
                    {renderStatus(product.status)}
                  </div>
                  <div className="mt-4 grid grid-cols-2 gap-3">
                    <div className="rounded-2xl bg-white/75 p-3">
                      <p className="text-[11px] font-black uppercase tracking-[0.12em] text-[var(--text-muted)]">Precio</p>
                      <p className="mt-1 text-lg font-black text-[var(--accent)]">{formatCurrency(product.price)}</p>
                    </div>
                    <div className="rounded-2xl bg-white/75 p-3">
                      <p className="text-[11px] font-black uppercase tracking-[0.12em] text-[var(--text-muted)]">Stock</p>
                      <p className="mt-1 text-lg font-black">{product.stock}</p>
                    </div>
                  </div>
                  <div className="mt-4 flex justify-end gap-2">
                    <button type="button" onClick={() => handleOpenEdit(product)} className="btn-secondary px-3 py-2 text-sm">
                      <Edit3 size={17} />
                      Editar
                    </button>
                    <button
                      type="button"
                      onClick={() => setProductToDelete(product)}
                      className="rounded-xl bg-red-50 px-3 py-2 text-sm font-bold text-red-600"
                    >
                      <Trash2 size={17} />
                    </button>
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
              <p className="text-xs font-black uppercase tracking-[0.16em] text-[var(--accent)]">
                {editingProduct ? "Editar" : "Nuevo"}
              </p>
              <h2 className="mt-1 text-2xl font-black text-[var(--text-dark)]">Producto</h2>
              <p className="mt-1 text-sm text-[var(--text-muted)]">Los precios se registran en dolares estadounidenses.</p>
            </div>
            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Nombre
                </label>
                <input
                  type="text"
                  className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.name ? "input-error" : ""}`}
                  placeholder="Ej: Cafe americano"
                  value={formData.name}
                  onChange={(event) => setFormData((prev) => ({ ...prev, name: event.target.value }))}
                />
                <FieldError message={validation.errors.name} />
              </div>

              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Categoria
                </label>
                <select
                  className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.categoryId ? "input-error" : ""}`}
                  value={formData.categoryId}
                  onChange={(event) => setFormData((prev) => ({ ...prev, categoryId: event.target.value }))}
                >
                  <option value="">Seleccionar categoria...</option>
                  {categories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
                <FieldError message={validation.errors.categoryId} />
              </div>

              <div className="grid gap-4 sm:grid-cols-2">
                <div>
                  <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                    Precio unitario
                  </label>
                  <input
                    type="number"
                    inputMode="decimal"
                    min="0.01"
                    max="999.99"
                    step="0.01"
                    className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.price ? "input-error" : ""}`}
                    placeholder="0.00"
                    value={formData.price}
                    onChange={(event) => setFormData((prev) => ({ ...prev, price: event.target.value }))}
                  />
                  <FieldError message={validation.errors.price} />
                  <FieldWarning message={validation.warnings.price} />
                </div>

                {!editingProduct && (
                  <div>
                    <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                      Stock inicial
                    </label>
                    <input
                      type="number"
                      inputMode="numeric"
                      min="0"
                      max="10000"
                      step="1"
                      className={`input-base px-4 py-3 text-sm font-semibold ${validation.errors.stock ? "input-error" : ""}`}
                      placeholder="0"
                      value={formData.stock}
                      onChange={(event) => setFormData((prev) => ({ ...prev, stock: event.target.value }))}
                    />
                    <FieldError message={validation.errors.stock} />
                  </div>
                )}
              </div>

              <div className="flex gap-3 pt-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="btn-secondary flex-1 px-4 py-3">
                  Cancelar
                </button>
                <button type="submit" disabled={isInvalid} className="btn-primary flex-1 px-4 py-3">
                  {editingProduct ? "Actualizar" : "Guardar"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <ConfirmDialog
        open={Boolean(productToDelete)}
        title="Eliminar producto"
        message={`Esta accion eliminara "${productToDelete?.name}" del catalogo.`}
        confirmLabel="Eliminar"
        danger
        onConfirm={handleDelete}
        onCancel={() => setProductToDelete(null)}
      />
    </AdminTemplate>
  );
};
