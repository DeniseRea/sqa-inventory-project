# Informe Técnico Detallado de Aseguramiento de Calidad (SQA) - Don Gato Inventory

Este documento constituye el informe técnico oficial del proceso de **Software Quality Assurance (SQA)** para el proyecto **Don Gato Inventory**. Detalla las metodologías, herramientas, scripts y resultados métricos obtenidos para garantizar la excelencia operativa del sistema.

---

## 1. Marco Metodológico y Estándares

El aseguramiento de calidad se ha estructurado bajo el modelo de calidad de **McCall**, enfocándose en los siguientes ejes:

*   **Correctitud:** Garantizar que el software realice exactamente lo que el usuario requiere.
*   **Fiabilidad:** Asegurar que el sistema sea capaz de mantener su nivel de prestación bajo condiciones establecidas.
*   **Eficiencia:** Optimizar el uso de recursos (tiempo de CPU, memoria) durante la ejecución.
*   **Integridad:** Controlar el acceso al software y a los datos por parte de personas no autorizadas.

---

## 2. Pruebas de Integración y End-to-End (E2E) con Postman

Se ha desarrollado una suite de pruebas **Black Box** utilizando **Postman/Newman** para validar el comportamiento del sistema desde el punto de vista del consumidor de la API.

### 2.1. Arquitectura de la Colección
La colección `DonGato_Postman_Collection.json` está organizada en carpetas que siguen el ciclo de vida lógico de los datos:
1.  **Authentication:** Gestión de identidad y seguridad.
2.  **Categories:** Definición de taxonomía de productos.
3.  **Products & Stock:** Gestión de catálogo y transacciones de inventario.

### 2.2. Lógica de Scripts y Automatización
Se han implementado scripts en **JavaScript (Postman Sandbox)** para permitir la ejecución autónoma y secuencial:

*   **Encadenamiento de Peticiones (Chaining):**
    *   Tras el `Login`, el script extrae el token JWT y lo guarda en la variable de entorno `{{jwt_token}}`.
    *   Al crear una `Categoría`, se captura el ID generado y se almacena en `{{category_id}}` para su uso inmediato en la creación de productos.
    *   Este patrón se repite para el `product_id`, permitiendo que la suite se ejecute en cualquier entorno limpio (Dev, QA, Staging).

*   **Validaciones de Integridad de Datos (Assertions):**
    *   **Cálculo de Stock:** Una prueba crítica realiza una `ENTRADA` de 15 unidades y una `SALIDA` de 2 unidades, validando mediante un script que el GET final del producto devuelva exactamente **13 unidades**.
    *   **Validación de Esquema:** Se verifica que las respuestas contengan los campos obligatorios y tipos de datos correctos (ej: `stock` debe ser numérico).

*   **Pruebas de Robustez (Negative Testing):**
    *   Se incluye una petición diseñada para fallar (`Negative Test: Exceed Stock Limit`), donde se intenta retirar una cantidad superior a la existencia. El script valida que el servidor responda con un **HTTP 409 Conflict** y un mensaje de error descriptivo ("Insufficient stock").

---

## 3. Pruebas de Carga y Estrés (Apache JMeter)

Para garantizar la **Eficiencia** y **Escalabilidad**, se ejecutó un plan de pruebas de carga automatizado.

### 3.1. Configuración del Escenario
*   **Herramienta:** Apache JMeter 5.6.3.
*   **Carga de Trabajo:** 100 hilos (Threads) concurrentes.
*   **Distribución:** 80% operaciones de consulta (Read-heavy) y 20% operaciones de escritura (Write-heavy).
*   **Ramp-up:** 10 segundos para alcanzar la carga máxima.

### 3.2. Métricas de Rendimiento Obtenidas
*   **Throughput:** ~11.3 peticiones por segundo.
*   **Latencia Promedio:** 88 ms (Muy por debajo del límite de 200ms establecido).
*   **Tasa de Error:** 0.00% bajo carga máxima sostenida durante 300 segundos.

---

## 4. Análisis de Cobertura y Estático (JaCoCo & OWASP)

### 4.1. Cobertura de Código (White Box Testing)
Utilizando **JaCoCo**, se analizó la profundidad de las pruebas unitarias y de integración.
*   **Cobertura Global:** **97%**.
*   **Exclusiones:** Se excluyeron modelos anémicos y DTOs para centrar el esfuerzo en la lógica de negocio (`usecases`) y adaptadores.
*   **Quality Gate:** Configurado en Maven para abortar el despliegue si la cobertura cae por debajo del 85%.

### 4.2. Seguridad de la Cadena de Suministro
Se integró **OWASP Dependency-Check** para identificar vulnerabilidades conocidas en las 45+ dependencias de terceros (Spring Boot, Jackson, JWT, etc.).
*   **Resultado:** 0 vulnerabilidades de severidad Alta/Crítica detectadas.

---

## 5. Resumen de Calidad (Cuadro de Mando)

| Métrica | Valor Obtenido | Estado |
| :--- | :--- | :--- |
| **Pruebas Unitarias** | 100% Pass Rate | ✅ |
| **Cobertura de Código** | 97% | ✅ |
| **Vulnerabilidades (CVE)** | 0 Detectadas | ✅ |
| **Latencia Media** | 88 ms | ✅ |
| **Pruebas E2E (Postman)** | 100% Éxito (Incluyendo Negativas) | ✅ |

**Conclusión:** El sistema **Don Gato Inventory** ha superado todas las fases de aseguramiento de calidad, demostrando una madurez técnica apta para entornos de producción.
