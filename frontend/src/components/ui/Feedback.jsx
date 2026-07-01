import { AlertCircle, CheckCircle2, Info, X } from "lucide-react";

const variants = {
  error: {
    icon: AlertCircle,
    className: "border-red-200 bg-red-50 text-red-700",
  },
  success: {
    icon: CheckCircle2,
    className: "border-emerald-200 bg-emerald-50 text-emerald-700",
  },
  warning: {
    icon: Info,
    className: "border-amber-200 bg-amber-50 text-amber-700",
  },
  info: {
    icon: Info,
    className: "border-sky-200 bg-sky-50 text-sky-700",
  },
};

export const Feedback = ({ type = "info", title, message, onClose }) => {
  if (!message && !title) return null;

  const variant = variants[type] ?? variants.info;
  const Icon = variant.icon;

  return (
    <div className={`flex items-start gap-3 rounded-xl border px-4 py-3 text-sm ${variant.className}`}>
      <Icon size={18} className="mt-0.5" />
      <div className="min-w-0 flex-1">
        {title && <p className="font-bold">{title}</p>}
        {message && <p className="text-sm leading-relaxed opacity-90">{message}</p>}
      </div>
      {onClose && (
        <button
          type="button"
          onClick={onClose}
          className="rounded-lg p-1 opacity-70 transition hover:bg-black/5 hover:opacity-100"
          aria-label="Cerrar mensaje"
        >
          <X size={16} />
        </button>
      )}
    </div>
  );
};

export const FieldError = ({ message }) => {
  if (!message) return null;

  return <p className="mt-1 text-xs font-semibold text-red-600">{message}</p>;
};

export const FieldWarning = ({ message }) => {
  if (!message) return null;

  return <p className="mt-1 text-xs font-semibold text-amber-700">{message}</p>;
};
