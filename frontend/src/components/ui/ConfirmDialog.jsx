import { AlertTriangle } from "lucide-react";

export const ConfirmDialog = ({
  open,
  title,
  message,
  confirmLabel = "Confirmar",
  cancelLabel = "Cancelar",
  danger = false,
  onConfirm,
  onCancel,
}) => {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-[120] flex items-center justify-center bg-[var(--bg-sidebar)]/60 p-4 backdrop-blur-sm">
      <div className="w-full max-w-sm rounded-2xl border border-white/40 bg-white p-6 shadow-2xl">
        <div className="mb-4 flex items-start gap-3">
          <div className={`rounded-xl p-2 ${danger ? "bg-red-50 text-red-600" : "bg-amber-50 text-amber-700"}`}>
            <AlertTriangle size={22} />
          </div>
          <div>
            <h3 className="text-lg font-black text-[var(--text-dark)]">{title}</h3>
            <p className="mt-1 text-sm leading-relaxed text-[var(--text-muted)]">{message}</p>
          </div>
        </div>
        <div className="flex justify-end gap-3">
          <button
            type="button"
            onClick={onCancel}
            className="rounded-xl px-4 py-2 text-sm font-bold text-[var(--text-muted)] transition hover:bg-slate-100"
          >
            {cancelLabel}
          </button>
          <button
            type="button"
            onClick={onConfirm}
            className={`rounded-xl px-4 py-2 text-sm font-bold text-white shadow-lg transition ${
              danger ? "bg-red-600 hover:bg-red-700" : "bg-[var(--accent)] hover:bg-[var(--accent-hover)]"
            }`}
          >
            {confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
};
