# Detalles de la Suite de Pruebas SQA - Don Gato Inventory

Este documento describe la arquitectura y el propósito de las pruebas implementadas para garantizar la calidad del software (SQA) en el sistema de inventario.

## 1. Pruebas Unitarias (JUnit 5 + Mockito)

Las pruebas unitarias están diseñadas para validar componentes de forma aislada, garantizando que cada pieza del sistema cumpla con su responsabilidad individual.

### A. Capa de Dominio (Domain Model)
*   **Archivos:** `ProductTest.java`, `CategoryTest.java`, `StockMovementTest.java`
*   **Propósito:** Validar las reglas de negocio críticas e invariantes.
*   **Casos Clave:**
    *   Cálculo automático de estado (AVAILABLE/OUT_OF_STOCK).
    *   Validación de precios no negativos.
    *   Restricción de salida de stock si no hay existencias suficientes.

### B. Capa de Aplicación (Use Cases)
*   **Archivos:** `ProductUseCaseTest.java`, `CategoryUseCaseTest.java`, `StockMovementUseCaseTest.java`
*   **Propósito:** Verificar la orquestación de la lógica de negocio usando Mocks para las dependencias externas.
*   **Casos Clave:**
    *   Flujo de creación de categorías y productos.
    *   Registro de movimientos de stock y actualización coordinada del producto.

### C. Capa de Infraestructura (Persistence & Security)
*   **Archivos:** `ProductPersistenceMapperTest.java`, `JwtTokenProviderTest.java`
*   **Propósito:** Asegurar la correcta transformación de datos hacia la base de datos y la integridad de la seguridad.
*   **Casos Clave:**
    *   Mapeo de Entidades JPA a Modelos de Dominio.
    *   Generación y validación exitosa de tokens JWT para autenticación.

## 2. Pruebas de Integración (MockMvc + Postman)

Validan la interacción entre múltiples componentes del sistema, incluyendo el manejo de peticiones HTTP, seguridad y persistencia.

### A. Controladores REST (MockMvc)
*   **Archivos:** `ProductControllerTest.java`, `AuthControllerTest.java`
*   **Propósito:** Validar que los endpoints respondan con los códigos HTTP correctos (200, 201, 401, 409).
*   **Validaciones:**
    *   Protección de rutas con seguridad JWT.
    *   Manejo global de excepciones (GlobalExceptionHandler).

### B. Pruebas End-to-End (Postman/Newman)
*   **Archivo:** `DonGato_Postman_Collection.json`
*   **Propósito:** Simular el uso real del sistema desde el Login hasta el cálculo de stock final.
*   **Flujo Validado:**
    1.  Login como Administrador (Obtención de Token).
    2.  Creación de Categoría y Producto.
    3.  Inyección de Stock (ENTRADA).
    4.  Egreso de Stock (SALIDA).
    5.  Verificación matemática de que el Stock Final sea exacto.

## 3. Pruebas de Carga y Rendimiento (JMeter)

*   **Archivo:** `load-test.jmx`
*   **Propósito:** Evaluar la resiliencia del sistema bajo condiciones de estrés sostenido.
*   **Configuración:**
    *   80 hilos para operaciones de lectura.
    *   20 hilos para operaciones de escritura.
    *   Duración de 300 segundos para identificar fugas de memoria o cuellos de botella en la base de datos.

## 4. Análisis Estático y Seguridad (Metapuebas)

*   **JaCoCo:** Verificación automática de que la cobertura de líneas sea superior al 85%.
*   **OWASP Dependency Check:** Escaneo automático de vulnerabilidades conocidas (CVEs) en las dependencias del proyecto.
*   **QualityScoringEngine:** Motor interno personalizado que asigna un puntaje de calidad basado en métricas de código.

---
**Resultado General:** Cobertura > 85%, 0 Vulnerabilidades detectadas, 100% de éxito en integración E2E.
