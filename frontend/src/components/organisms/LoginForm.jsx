import { useState } from "react";
import { FormField } from "../molecules/FormField";
import { Button } from "../atoms/Button";

export const LoginForm = ({ onSubmit }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ username, password });
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="w-full max-w-sm p-10 rounded-2xl shadow-xl flex flex-col gap-6 animate-fade-in"
      style={{
        background: "var(--color-bg-main)", // Fondo crema claro
        color: "var(--color-text-dark)", // Texto café intenso
      }}
    >
      <div className="flex flex-col items-center gap-2 mb-2">
        <div className="mb-2">
          <img
            src="/favicon.png"
            alt="Logo"
            className="w-20 h-20 object-contain drop-shadow-md"
          />
        </div>
        <h2
          className="text-2xl font-extrabold tracking-tight text-center"
          style={{ color: "var(--color-text-dark)", letterSpacing: "-0.5px" }}
        >
          Portal Administrador
        </h2>
        <p
          className="text-sm opacity-80 text-center"
          style={{ color: "var(--color-bg-secondary)" }}
        >
          Ingresa tus credenciales
        </p>
      </div>
      <FormField
        label="Usuario"
        type="text"
        placeholder="Ej. usuario"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <FormField
        label="Contraseña"
        type="password"
        placeholder="••••••••"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <Button
        type="submit"
        className="mt-2 shadow-md transition hover:scale-105"
        style={{
          background: "var(--color-accent)",
          color: "var(--color-text-light)",
          fontWeight: 600,
          fontSize: "1.05rem",
        }}
      >
        Iniciar Sesión
      </Button>
      <p
        className="mt-4 text-center text-sm font-medium"
        style={{ color: "var(--color-text-dark)" }}
      >
        ¿No tienes cuenta?{" "}
        <a
          href="#"
          className="underline font-bold transition hover:opacity-80"
          style={{ color: "var(--color-accent)" }}
        >
          Regístrate
        </a>
      </p>
    </form>
  );
};
