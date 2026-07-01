import { useEffect, useMemo, useState } from "react";
import { Edit3, FolderKanban, Plus, Search, Trash2 } from "lucide-react";
import { AdminTemplate } from "../components/templates/AdminTemplate";
import { PageHeader } from "../components/ui/PageHeader";
import { categoryService } from "../services/categoryService";
import { ConfirmDialog } from "../components/ui/ConfirmDialog";
import { Feedback, FieldError } from "../components/ui/Feedback";
import { hasErrors, normalizeText, validateCategory } from "../utils/validation";

export const CategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);
  const [formData, setFormData] = useState({ name: "" });
  const [search, setSearch] = useState("");
  const [feedback, setFeedback] = useState(null);
  const [categoryToDelete, setCategoryToDelete] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      const data = await categoryService.getAll();
      setCategories(data);
    } catch {
      setFeedback({ type: "error", message: "No se pudieron cargar las categorias." });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchData();
  }, []);

  const errors = validateCategory(formData, categories, editingCategory?.id ?? null);
  const isInvalid = hasErrors(errors);

  const filteredCategories = useMemo(() => {
    const query = normalizeText(search).toLowerCase();
    if (!query) return categories;
    return categories.filter((category) => normalizeText(category.name).toLowerCase().includes(query));
  }, [categories, search]);

  const handleOpenCreate = () => {
    setEditingCategory(null);
    setFormData({ name: "" });
    setFeedback(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (category) => {
    setEditingCategory(category);
    setFormData({ name: category.name });
    setFeedback(null);
    setIsModalOpen(true);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (isInvalid) return;

    try {
      const payload = { name: normalizeText(formData.name) };
      if (editingCategory) {
        await categoryService.update(editingCategory.id, payload);
        setFeedback({ type: "success", message: "Categoria actualizada correctamente." });
      } else {
        await categoryService.create(payload);
        setFeedback({ type: "success", message: "Categoria creada correctamente." });
      }
      setIsModalOpen(false);
      fetchData();
    } catch {
      setFeedback({ type: "error", message: "No se pudo procesar la categoria." });
    }
  };

  const handleDelete = async () => {
    if (!categoryToDelete) return;

    try {
      await categoryService.delete(categoryToDelete.id);
      setFeedback({ type: "success", message: "Categoria eliminada correctamente." });
      setCategoryToDelete(null);
      fetchData();
    } catch {
      setFeedback({
        type: "error",
        message: "No se puede eliminar la categoria porque puede tener productos asociados.",
      });
      setCategoryToDelete(null);
    }
  };

  return (
    <AdminTemplate>
      <PageHeader
        eyebrow="Organizacion"
        image="/categories-hero.png"
        title="Categorias"
        description="Agrupa productos para encontrarlos rapido y mantener limpio el catalogo."
        stat={{ label: "Total", value: categories.length }}
        action={
          <button type="button" onClick={handleOpenCreate} className="btn-primary px-5 py-3 text-sm">
            <Plus size={18} />
            Nueva categoria
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
            placeholder="Buscar categoria..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </div>
      </section>

      {loading ? (
        <div className="flex h-64 items-center justify-center">
          <div className="h-9 w-9 animate-spin rounded-full border-4 border-[var(--accent)] border-t-transparent" />
        </div>
      ) : (
        <>
          <div className="data-shell hidden overflow-hidden rounded-3xl md:block">
            <table className="w-full text-left">
              <thead className="table-head text-[var(--text-light)]">
                <tr>
                  <th className="px-6 py-4 text-xs font-black uppercase tracking-[0.16em] text-white/48">ID</th>
                  <th className="px-6 py-4 text-xs font-black uppercase tracking-[0.16em]">Nombre</th>
                  <th className="px-6 py-4 text-right text-xs font-black uppercase tracking-[0.16em]">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border-soft)]">
                {filteredCategories.map((category) => (
                  <tr key={category.id} className="transition hover:bg-[var(--surface-soft)]/45">
                    <td className="px-6 py-4 font-mono text-xs font-bold text-[var(--text-muted)]">#{category.id}</td>
                    <td className="px-6 py-4 text-base font-black">{category.name}</td>
                    <td className="px-6 py-4">
                      <div className="flex justify-end gap-2">
                        <button
                          type="button"
                          onClick={() => handleOpenEdit(category)}
                          className="rounded-xl p-2 text-[var(--text-muted)] transition hover:bg-white hover:text-[var(--accent)]"
                          aria-label={`Editar ${category.name}`}
                        >
                          <Edit3 size={18} />
                        </button>
                        <button
                          type="button"
                          onClick={() => setCategoryToDelete(category)}
                          className="rounded-xl p-2 text-[var(--text-muted)] transition hover:bg-red-50 hover:text-red-600"
                          aria-label={`Eliminar ${category.name}`}
                        >
                          <Trash2 size={18} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="grid gap-3 md:hidden">
            {filteredCategories.map((category) => (
              <article key={category.id} className="surface-panel rounded-3xl p-4">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex min-w-0 items-center gap-3">
                    <span className="rounded-2xl bg-[var(--bg-sidebar)] p-3 text-[var(--text-light)]">
                      <FolderKanban size={20} />
                    </span>
                    <div className="min-w-0">
                      <p className="truncate text-base font-black">{category.name}</p>
                      <p className="font-mono text-xs font-bold text-[var(--text-muted)]">#{category.id}</p>
                    </div>
                  </div>
                  <div className="flex gap-1">
                    <button
                      type="button"
                      onClick={() => handleOpenEdit(category)}
                      className="rounded-xl bg-white/75 p-2 text-[var(--text-muted)]"
                      aria-label={`Editar ${category.name}`}
                    >
                      <Edit3 size={17} />
                    </button>
                    <button
                      type="button"
                      onClick={() => setCategoryToDelete(category)}
                      className="rounded-xl bg-red-50 p-2 text-red-600"
                      aria-label={`Eliminar ${category.name}`}
                    >
                      <Trash2 size={17} />
                    </button>
                  </div>
                </div>
              </article>
            ))}
          </div>
        </>
      )}

      {!loading && filteredCategories.length === 0 && (
        <div className="surface-panel rounded-3xl p-10 text-center text-sm font-semibold text-[var(--text-muted)]">
          No se encontraron categorias.
        </div>
      )}

      {isModalOpen && (
        <div className="fixed inset-0 z-[100] flex items-center justify-center bg-[var(--bg-sidebar)]/60 p-4 backdrop-blur-sm">
          <div className="w-full max-w-md rounded-3xl bg-white p-6 shadow-2xl">
            <div className="mb-6">
              <p className="text-xs font-black uppercase tracking-[0.16em] text-[var(--accent)]">
                {editingCategory ? "Editar" : "Nueva"}
              </p>
              <h2 className="mt-1 text-2xl font-black text-[var(--text-dark)]">Categoria</h2>
            </div>
            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Nombre de la categoria
                </label>
                <input
                  type="text"
                  className={`input-base px-4 py-3 text-sm font-semibold ${errors.name ? "input-error" : ""}`}
                  placeholder="Ej: Cafes de especialidad"
                  value={formData.name}
                  onChange={(event) => setFormData({ name: event.target.value })}
                />
                <FieldError message={errors.name} />
              </div>
              <div className="flex gap-3 pt-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="btn-secondary flex-1 px-4 py-3">
                  Cancelar
                </button>
                <button type="submit" disabled={isInvalid} className="btn-primary flex-1 px-4 py-3">
                  {editingCategory ? "Actualizar" : "Guardar"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <ConfirmDialog
        open={Boolean(categoryToDelete)}
        title="Eliminar categoria"
        message={`Esta accion intentara eliminar "${categoryToDelete?.name}". Si tiene productos asociados, el sistema lo rechazara.`}
        confirmLabel="Eliminar"
        danger
        onConfirm={handleDelete}
        onCancel={() => setCategoryToDelete(null)}
      />
    </AdminTemplate>
  );
};
