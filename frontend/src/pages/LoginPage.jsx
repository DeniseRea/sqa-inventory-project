import { LoginForm } from "../components/organisms/LoginForm";

export const LoginPage = () => {
  const handleLogin = (data) => {
    console.log("Login data:", data);
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center relative overflow-hidden">
      {/* Fondo degradado animado usando los colores de la paleta */}
      <div
        className="absolute inset-0 animate-gradient"
        style={{
          background:
            "linear-gradient(-45deg, var(--color-bg-main), var(--color-bg-secondary), var(--color-accent), var(--color-bg-main))",
          backgroundSize: "400% 400%",
          zIndex: -1,
        }}
      ></div>

      {/* Overlay oscuro sutil para que el login resalte más */}
      <div className="absolute inset-0 bg-black/10 z-0"></div>

      <div className="z-10 w-full flex justify-center px-4">
        <LoginForm onSubmit={handleLogin} />
      </div>

      <style>{`
        @keyframes gradient {
          0% { background-position: 0% 50%; }
          50% { background-position: 100% 50%; }
          100% { background-position: 0% 50%; }
        }
        .animate-gradient {
          animation: gradient 15s ease infinite;
        }
      `}</style>
    </div>
  );
};
