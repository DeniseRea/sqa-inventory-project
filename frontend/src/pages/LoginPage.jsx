import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authService } from "../services/auth";

export const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      await authService.login(username, password);
      navigate("/dashboard");
    } catch (err) {
      setError("Credenciales inválidas.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-[var(--bg-main)] p-6">
      <div className="w-full max-w-[400px] animate-slide-in">
        <div className="bg-[var(--bg-card)] rounded-[2.5rem] shadow-2xl overflow-hidden border border-[var(--bg-sidebar)]/20">
          {/* Header */}
          <div className="p-10 pb-6 flex flex-col items-center text-center">
            <div className="w-20 h-20 bg-white/10 rounded-2xl p-4 mb-6 shadow-inner border border-white/5">
              <img src="/favicon.png" alt="Logo" className="w-full h-full object-contain" />
            </div>
            <h1 className="text-2xl font-bold text-[var(--text-light)] tracking-tight font-heading">Don Gato Inv</h1>
            <p className="text-[var(--text-light)]/40 text-[10px] font-black uppercase tracking-widest mt-1">Gestión Gourmet de Inventarios</p>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit} className="px-10 pb-10 space-y-5">
            <div className="space-y-1.5">
              <label className="block text-[10px] font-black text-[var(--text-light)]/60 uppercase tracking-widest ml-1">Usuario</label>
              <div className="relative">
                <input
                  type="text"
                  required
                  className="w-full bg-black/20 border border-white/5 rounded-xl p-4 text-[var(--text-light)] text-sm outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all placeholder:opacity-20"
                  placeholder="admin"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="block text-[10px] font-black text-[var(--text-light)]/60 uppercase tracking-widest ml-1">Contraseña</label>
              <div className="relative">
                <input
                  type="password"
                  required
                  className="w-full bg-black/20 border border-white/5 rounded-xl p-4 text-[var(--text-light)] text-sm outline-none focus:ring-2 focus:ring-[var(--accent)] transition-all placeholder:opacity-20"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
            </div>

            {error && (
              <div className="bg-red-500/10 border border-red-500/20 text-red-400 p-3 rounded-xl text-[10px] font-bold text-center uppercase tracking-widest">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full py-4 bg-[var(--accent)] hover:bg-[var(--accent-hover)] text-white font-black text-xs uppercase tracking-widest rounded-xl shadow-lg transition-all transform active:scale-95 disabled:opacity-50 mt-2"
            >
              {loading ? "Iniciando..." : "Entrar al Portal"}
            </button>
          </form>
        </div>
        
        <p className="text-center text-[var(--bg-sidebar)]/30 mt-8 text-[10px] font-black uppercase tracking-[0.3em]">
          &copy; 2026 DON GATO GOURMET
        </p>
      </div>
    </div>
  );
};
