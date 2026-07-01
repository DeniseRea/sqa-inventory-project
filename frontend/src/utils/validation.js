export const PRODUCT_PRICE_WARNING = 25;
export const PRODUCT_PRICE_MAX = 999.99;
export const STOCK_MAX = 10000;

const DECIMAL_2_PATTERN = /^\d+(\.\d{1,2})?$/;
const INTEGER_PATTERN = /^\d+$/;

export const normalizeText = (value) => String(value ?? "").trim();

export const formatCurrency = (value) => {
  const numericValue = Number(value);
  if (!Number.isFinite(numericValue)) return "$0.00";

  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(numericValue);
};

export const validateCategory = (category, categories = [], editingId = null) => {
  const errors = {};
  const name = normalizeText(category.name);

  if (!name) {
    errors.name = "Ingresa el nombre de la categoria.";
  } else if (name.length < 2) {
    errors.name = "El nombre debe tener al menos 2 caracteres.";
  } else if (name.length > 100) {
    errors.name = "El nombre no debe superar 100 caracteres.";
  } else {
    const repeated = categories.some(
      (item) =>
        item.id !== editingId &&
        normalizeText(item.name).toLowerCase() === name.toLowerCase(),
    );

    if (repeated) {
      errors.name = "Ya existe una categoria con ese nombre.";
    }
  }

  return errors;
};

export const validateProduct = (product) => {
  const errors = {};
  const warnings = {};
  const name = normalizeText(product.name);
  const price = normalizeText(product.price);
  const stock = normalizeText(product.stock);

  if (!name) {
    errors.name = "Ingresa el nombre del producto.";
  } else if (name.length < 2) {
    errors.name = "El nombre debe tener al menos 2 caracteres.";
  } else if (name.length > 150) {
    errors.name = "El nombre no debe superar 150 caracteres.";
  }

  if (!product.categoryId) {
    errors.categoryId = "Selecciona una categoria.";
  }

  if (!price) {
    errors.price = "Ingresa el precio unitario.";
  } else if (!DECIMAL_2_PATTERN.test(price)) {
    errors.price = "Usa un precio valido con maximo 2 decimales.";
  } else {
    const numericPrice = Number(price);
    if (!Number.isFinite(numericPrice) || numericPrice <= 0) {
      errors.price = "El precio debe ser mayor a $0.00.";
    } else if (numericPrice > PRODUCT_PRICE_MAX) {
      errors.price = `El precio maximo permitido es ${formatCurrency(PRODUCT_PRICE_MAX)}.`;
    } else if (numericPrice > PRODUCT_PRICE_WARNING) {
      warnings.price = `Este precio es alto para una cafeteria (${formatCurrency(numericPrice)}). Revisa si esta correcto.`;
    }
  }

  if (product.includeStock) {
    if (!stock && stock !== "0") {
      errors.stock = "Ingresa el stock inicial.";
    } else if (!INTEGER_PATTERN.test(stock)) {
      errors.stock = "El stock debe ser un numero entero.";
    } else {
      const numericStock = Number(stock);
      if (numericStock < 0) {
        errors.stock = "El stock no puede ser negativo.";
      } else if (numericStock > STOCK_MAX) {
        errors.stock = `El stock maximo permitido es ${STOCK_MAX}.`;
      }
    }
  }

  return { errors, warnings };
};

export const validateMovement = (movement, products = []) => {
  const errors = {};
  const quantity = normalizeText(movement.quantity);
  const selectedProduct = products.find((item) => String(item.id) === String(movement.productId));

  if (!movement.productId) {
    errors.productId = "Selecciona un producto.";
  }

  if (!quantity) {
    errors.quantity = "Ingresa la cantidad.";
  } else if (!INTEGER_PATTERN.test(quantity)) {
    errors.quantity = "La cantidad debe ser un numero entero.";
  } else {
    const numericQuantity = Number(quantity);
    if (numericQuantity < 1) {
      errors.quantity = "La cantidad minima es 1.";
    } else if (numericQuantity > STOCK_MAX) {
      errors.quantity = `La cantidad maxima permitida es ${STOCK_MAX}.`;
    } else if (
      movement.type === "SALIDA" &&
      selectedProduct &&
      numericQuantity > Number(selectedProduct.stock)
    ) {
      errors.quantity = `Solo hay ${selectedProduct.stock} unidades disponibles.`;
    }
  }

  if (!movement.reason) {
    errors.reason = "Selecciona un motivo.";
  }

  return { errors, selectedProduct };
};

export const hasErrors = (errors) => Object.keys(errors).length > 0;
