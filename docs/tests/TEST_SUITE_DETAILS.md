# Detalle Exhaustivo de la Suite de Pruebas - Proyecto Don Gato SQA

Este documento proporciona una visión técnica profunda de la estrategia de pruebas implementada, desglosando cada nivel, escenario y métrica de calidad aplicada al backend del sistema de inventario.

---

## 1. Mapeo Estratégico SQA

### 1.1 Matriz de Factores de Calidad McCall
| Factor de McCall | Objetivo SQA | Suite de Pruebas Asociada |
| :--- | :--- | :--- |
| **Correctitud** | Cumplimiento funcional | `CategoryUseCaseTest`, `ProductUseCaseTest` |
| **Fiabilidad** | Robustez ante errores | `GlobalExceptionHandlerTest` |
| **Eficiencia** | Rendimiento bajo carga | `JMeter Load-Stress-Test` |
| **Integridad** | Seguridad de acceso | `JwtTokenProviderTest`, `AuthControllerTest` |
| **Mantenibilidad** | Facilidad de cambio | Tests Unitarios de Dominio |
| **Testabilidad** | Facilidad de validación | JUnit 5 + Mockito |

### 1.2 Mapeo Ciclo PDCA
| Fase PDCA | Acción Técnica | Evidencia / Resultado |
| :--- | :--- | :--- |
| **Plan** | Definición de Requerimientos | `requeriments.md`, `Master_Test_Plan_SQA.md` |
| **Do** | Implementación SOLID + Maven CI/CD | Pipeline (`mvn verify`) |
| **Check** | JUnit 5, JaCoCo, JMeter | Reportes (Coverage 97%, 0% Error Rate) |
| **Act** | Refactorización | Optimización de Concurrencia |

---

## 2. Inventario de Pruebas Unitarias (Nivel Componente)

Se utiliza **JUnit 5** y **Mockito** para aislar la lógica de negocio.

### 2.1. Capa de Aplicación (Casos de Uso)
Ubicación: `src/test/java/com/dongato/inventory/application/usecases/`

| Clase de Prueba | Escenarios Validados | Propósito SQA |
| :--- | :--- | :--- |
| **ProductUseCaseTest** | Creación, validación, búsqueda, actualización, borrado. | Validar reglas de negocio. |
| **CategoryUseCaseTest** | Creación, actualización, eliminación. | Integridad de taxonomía. |
| **StockMovementTest** | Entradas/salidas, stock insuficiente, cálculos. | Atomicidad de movimientos. |

---

## 3. Pruebas de Rendimiento (Load Testing)

Se utiliza **Apache JMeter** automatizado vía Maven.

### Especificación del Escenario "Load-Stress-Test"
*   **Usuarios Concurrentes:** 100 hilos totales (80 lectura, 20 escritura).
*   **Duración:** 5 minutos (300 segundos).
*   **Resultados (Ejecución 2026-05-13):**
    *   **Throughput Total:** ~11.3 req/s.
    *   **Error Rate:** 0.00% (Verificado).
    *   **Tiempo de respuesta promedio:** 88 ms.

### Hallazgos de Estabilidad
*   La optimización de los flujos de concurrencia ha permitido eliminar errores sistemáticos en los endpoints de escritura (POST).

---

## 4. Evolución de la Calidad (Cobertura JaCoCo)

| Fase de Proyecto | Cobertura JaCoCo | Estado |
| :--- | :--- | :--- |
| Inicio de Proyecto | 92% | Baseline |
| Actual | 97% | Óptimo |

*   **Nota:** Incremento logrado mediante pruebas de borde en adaptadores y manejo de excepciones.

---

## 5. Auditoría de Seguridad (SCA)

Utilizando **OWASP Dependency-Check**.
*   **Frecuencia:** Cada ejecución de `mvn verify`.
*   **Criterio:** Fallo automático si CVSS > 7.0.

