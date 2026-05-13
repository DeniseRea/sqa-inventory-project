import api from "./api";

export const authService = {
  login: async (username, password) => {
    // El backend espera recibir username y password, aunque en tu frontend pusiste 'email'.
    // Vamos a mapearlo como username ya que los usuarios son 'admin' o 'cajero'
    const response = await api.post("/auth/login", { username, password });

    // Guardar en localStorage
    if (response.data.token) {
      localStorage.setItem("token", response.data.token);
      localStorage.setItem("username", response.data.username);
    }

    return response.data;
  },

  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
  },

  isAuthenticated: () => {
    return !!localStorage.getItem("token");
  },
};
