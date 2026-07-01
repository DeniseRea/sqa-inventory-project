import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Coffee, Lock, PackageCheck, ShieldCheck, UserRound } from "lucide-react";
import { authService } from "../services/auth";
import { Feedback } from "../components/ui/Feedback";

export const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      await authService.login(username.trim(), password);
      navigate("/dashboard");
    } catch {
      setError("Usuario o contrasena incorrectos. Revisa tus datos e intenta otra vez.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-[var(--bg-sidebar)] p-3 text-[var(--text-light)] sm:p-5">
      <section className="relative min-h-[calc(100vh-1.5rem)] overflow-hidden rounded-[1.75rem] bg-[var(--bg-sidebar)] sm:min-h-[calc(100vh-2.5rem)]">
        <img
          src="/login-hero.png"
          alt="Barra de cafeteria Don Gato"
          className="absolute inset-0 h-full w-full object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-r from-[#1a120f]/95 via-[#2b1b17]/78 to-[#2b1b17]/22" />
        <div className="absolute inset-x-0 bottom-0 h-1/2 bg-gradient-to-t from-[#1a120f]/92 to-transparent" />

        <div className="relative z-10 grid min-h-[calc(100vh-1.5rem)] items-center gap-8 px-5 py-8 sm:min-h-[calc(100vh-2.5rem)] sm:px-8 lg:grid-cols-[1fr_420px] lg:px-14">
          <div className="max-w-2xl">
            <div className="mb-8 flex items-center gap-4">
              <div className="flex h-20 w-20 items-center justify-center rounded-[1.4rem] border border-white/10 bg-white/10 p-2 shadow-2xl backdrop-blur sm:h-24 sm:w-24">
                <img src="/favicon.png" alt="Don Gato" className="h-full w-full object-contain" />
              </div>
              <div>
                <p className="text-2xl font-black leading-tight sm:text-3xl">Don Gato</p>
                <p className="text-[11px] font-black uppercase tracking-[0.24em] text-white/50 sm:text-xs">
                  Gourmet Inventory
                </p>
              </div>
            </div>

            <div className="mb-4 inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/10 px-4 py-2 text-xs font-bold text-white/70 backdrop-blur">
              <Coffee size={16} />
              Cafeteria, stock y ventas bajo control
            </div>
            <h1 className="max-w-2xl text-4xl font-black leading-[1.02] tracking-normal text-white sm:text-5xl lg:text-6xl">
              Inventario claro para una cafeteria que se mueve rapido.
            </h1>
            <p className="mt-5 max-w-xl text-base leading-7 text-white/68">
              Gestiona productos, categorias y movimientos con una experiencia pensada para revisar datos sin perder tiempo.
            </p>

            <div className="mt-8 grid max-w-xl gap-3 sm:grid-cols-3">
              {[
                { icon: PackageCheck, label: "Stock visible" },
                { icon: ShieldCheck, label: "Datos validados" },
                { icon: Coffee, label: "Flujo diario" },
              ].map((item) => {
                const Icon = item.icon;
                return (
                  <div key={item.label} className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur">
                    <Icon size={20} className="mb-3 text-[var(--accent)]" />
                    <p className="text-sm font-bold text-white/80">{item.label}</p>
                  </div>
                );
              })}
            </div>
          </div>

          <div className="surface-panel w-full rounded-[1.5rem] p-5 text-[var(--text-dark)] sm:p-7">
            <div className="mb-6">
              <p className="text-xs font-black uppercase tracking-[0.18em] text-[var(--accent)]">Acceso interno</p>
              <h2 className="mt-2 text-2xl font-black">Entrar al portal</h2>
              <p className="mt-1 text-sm text-[var(--text-muted)]">
                Usa tus credenciales para administrar el inventario.
              </p>
            </div>

            {error && (
              <div className="mb-5">
                <Feedback type="error" message={error} onClose={() => setError("")} />
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Usuario
                </label>
                <div className="relative">
                  <UserRound
                    size={18}
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-[var(--text-muted)]"
                  />
                  <input
                    type="text"
                    required
                    autoComplete="username"
                    className="input-base py-3.5 pl-11 pr-4 text-sm font-semibold"
                    placeholder="admin"
                    value={username}
                    onChange={(event) => setUsername(event.target.value)}
                  />
                </div>
              </div>

              <div>
                <label className="mb-1.5 block text-xs font-black uppercase tracking-[0.14em] text-[var(--text-muted)]">
                  Contrasena
                </label>
                <div className="relative">
                  <Lock
                    size={18}
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-[var(--text-muted)]"
                  />
                  <input
                    type="password"
                    required
                    autoComplete="current-password"
                    className="input-base py-3.5 pl-11 pr-4 text-sm font-semibold"
                    placeholder="Ingresa tu contrasena"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                  />
                </div>
              </div>

              <button type="submit" disabled={loading} className="btn-primary w-full px-5 py-4 text-sm">
                {loading ? "Verificando..." : "Entrar al sistema"}
              </button>
            </form>
          </div>
        </div>
      </section>
    </main>
  );
};
