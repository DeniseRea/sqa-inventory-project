import api from "./api";

export const stockService = {
  register: async (movementData) => {
    const response = await api.post("/stock-movements", movementData);
    return response.data;
  },

  getAll: async () => {
    const response = await api.get("/stock-movements");
    return response.data;
  },

  getByProduct: async (productId) => {
    const response = await api.get(`/stock-movements/product/${productId}`);
    return response.data;
  },
};
