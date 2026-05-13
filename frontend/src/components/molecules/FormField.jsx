import { Input } from "../atoms/Input";

export const FormField = ({ label, type, placeholder, value, onChange }) => (
  <div className="mb-3">
    <Input
      label={label}
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      className="bg-white text-[var(--color-text-dark)] placeholder-gray-400 border border-gray-200 focus:border-[var(--color-accent)] font-medium shadow-sm"
    />
  </div>
);
