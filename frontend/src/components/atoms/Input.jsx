export const Input = ({
  type = "text",
  placeholder,
  value,
  onChange,
  label,
  className = "",
}) => (
  <div className="flex flex-col gap-1 w-full">
    {label && (
      <label
        className="text-sm font-semibold text-[var(--color-bg-secondary)]"
        style={{ letterSpacing: "-0.2px" }}
      >
        {label}
      </label>
    )}
    <input
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      className={`w-full px-3 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[var(--color-accent)] focus:border-transparent transition ${className}`}
    />
  </div>
);
