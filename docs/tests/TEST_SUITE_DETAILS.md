# Detalle Exhaustivo de la Suite de Pruebas - Proyecto Don Gato SQA

Este documento proporciona una visión técnica profunda de la estrategia de pruebas implementada, desglosando cada nivel, escenario y métrica de calidad aplicada al backend del sistema de inventario.

---

## 1. Arquitectura de Pruebas y Factores de McCall

La suite de pruebas ha sido diseñada bajo el prisma de los **Factores de Calidad de McCall**, priorizando:

*   **Testabilidad:** Facilidad de realizar pruebas (Asegurada mediante Inyección de Dependencias y Mockito).
*   **Corrección (Correctness):** El sistema cumple con su especificación (Validado mediante 100% de éxito en casos de uso).
*   **Fiabilidad (Reliability):** El sistema es robusto ante errores (Manejo de excepciones global).
*   **Eficiencia (Efficiency):** Rendimiento óptimo bajo carga (Validado con JMeter).
*   **Integridad (Integrity):** Seguridad de los datos y acceso (Validado con JWT y OWASP).

---

## 2. Inventario de Pruebas Unitarias (Nivel Componente)

Se utiliza **JUnit 5** y **Mockito** para aislar la lógica de negocio.

### 2.1. Capa de Aplicación (Casos de Uso)
Ubicación: `src/test/java/com/dongato/inventory/application/usecases/`

| Clase de Prueba | Escenarios Validados | Propósito SQA |
| :--- | :--- | :--- |
| **ProductUseCaseTest** | Creación exitosa, validación de categoría inexistente, default de stock a 0, búsqueda por ID/Nombre/Categoría/Estado, actualización exitosa, borrado controlado. | Validar reglas de negocio de productos y consistencia de datos. |
| **CategoryUseCaseTest** | Creación, actualización, eliminación y búsqueda de categorías. Validación de nombres duplicados y referencias. | Garantizar la integridad de la taxonomía del inventario. |
| **StockMovementTest** | Registro de entradas/salidas, validación de stock insuficiente (excepción custom), cálculo de saldos. | Asegurar que los movimientos de inventario sean atómicos y correctos. |

### 2.2. Capa de Dominio (Modelos)
Ubicación: `src/test/java/com/dongato/inventory/domain/model/`

*   **ProductTest / CategoryTest / StockMovementTest:** Prueban los constructores, el patrón *Builder* (Lombok) y las validaciones intrínsecas del modelo sin dependencias.

### 2.3. Capa de Infraestructura (Adaptadores y Mappers)
Ubicación: `src/test/java/com/dongato/inventory/infrastructure/`

*   **Persistence Adapters:** Validan la integración con Spring Data JPA usando la base de datos **H2** en memoria.
*   **Mappers:** Aseguran que la transformación entre Entidades JPA, Modelos de Dominio y DTOs de REST no pierda información ni rompa tipos.

### 2.4. Capa de Interfaces (REST Controllers)
Ubicación: `src/test/java/com/dongato/inventory/interfaces/rest/`

| Clase de Prueba | Validaciones Específicas |
| :--- | :--- |
| **ProductControllerTest** | Códigos HTTP (200, 201, 400, 404), estructura JSON de respuesta, mapeo de parámetros de URL. |
| **GlobalExceptionHandlerTest** | Validación de que cada excepción de negocio se traduzca al código HTTP correcto (ej. `InsufficientStockException` -> 409 Conflict). |
| **AuthControllerTest** | Proceso de login y generación de tokens JWT. |

---

## 3. Motor de Calidad SQA (Quality Scoring)

Se implementó una clase especializada `QualityScoringEngine` que calcula una "Calificación de Salud" del software basada en métricas reales de ejecución de pruebas.

### Algoritmo de Calificación
El puntaje final (0-100) se pondera de la siguiente manera:
*   **Cobertura (30%):** JaCoCo Line Coverage.
*   **Corrección (30%):** Inversa de la densidad de fallos detectados.
*   **Mantenibilidad (20%):** Basado en la ausencia de "Code Smells".
*   **Integridad (20%):** Penalización crítica por vulnerabilidades OWASP.

---

## 4. Pruebas de Rendimiento (Load Testing)

Se utiliza **Apache JMeter** automatizado vía Maven.

### Especificación del Escenario "Stress-Base"
*   **Usuarios Concurrentes:** 50 hilos (Threads) atacando simultáneamente.
*   **Ramp-up:** 10 segundos (entrada gradual).
*   **Iteraciones:** 20 por usuario (Total 1,000 peticiones).
*   **Endpoint Crítico:** `GET /api/v1/products`.
*   **Aserciones de Calidad:**
    *   **Tiempo de respuesta:** Debe ser `< 200ms` (Métrica de Eficiencia).
    *   **Tasa de error:** Debe ser `0.00%`.

---

## 5. Auditoría de Seguridad (SCA)

Utilizando el estándar **OWASP Dependency-Check**.

*   **Frecuencia:** Cada ejecución de `mvn verify`.
*   **Base de Datos:** Sincronización en tiempo real con la **NVD (National Vulnerability Database)**.
*   **Criterio de Fallo:** El build fallará si se detecta una vulnerabilidad con CVSS > 7.0.

---

## 6. Configuración del Quality Gate (JaCoCo)

El proyecto impone una barrera de calidad estricta mediante el plugin de JaCoCo:

```xml
<rule>
    <element>BUNDLE</element>
    <limits>
        <limit>
            <counter>LINE</counter>
            <value>COVEREDRATIO</value>
            <minimum>0.85</minimum>
        </limit>
    </limits>
</rule>
```

**Exclusiones Estratégicas:** Se excluyen modelos puramente declarativos (POJOs de Lombok) para no inflar artificialmente la métrica, enfocando el reporte en la **Lógica de Decisión**.

---

## 7. Resumen de Herramientas SQA Utilizadas

| Herramienta | Función | Fase de Maven |
| :--- | :--- | :--- |
| **JUnit 5** | Pruebas Unitarias | `test` |
| **Mockito** | Mocking de Dependencias | `test` |
| **JaCoCo** | Análisis de Cobertura | `prepare-agent` / `report` |
| **OWASP Check** | Seguridad de Dependencias | `verify` |
| **Apache JMeter** | Pruebas de Carga | `integration-test` |
| **Spring Boot Test** | Integration Context | `integration-test` |
