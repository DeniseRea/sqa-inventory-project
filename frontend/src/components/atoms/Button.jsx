export const Button = ({
  children,
  onClick,
  type = "button",
  className = "",
  style,
}) => (
  <button
    type={type}
    onClick={onClick}
    style={style}
    className={`w-full py-2 px-4 rounded-lg transition duration-200 ${className}`}
  >
    {children}
  </button>
);
