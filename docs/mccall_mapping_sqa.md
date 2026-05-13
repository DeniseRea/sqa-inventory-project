# Mapeo del Modelo de Calidad de McCall al Proyecto Don Gato

Este documento describe cómo se aplican los factores de calidad de McCall en el aseguramiento de calidad (SQA) del sistema de inventario.

## 1. Operación del Producto (¿Cómo funciona?)

| Factor | Implementación en Don Gato | Evidencia SQA |
| :--- | :--- | :--- |
| **Corrección** | El sistema realiza cálculos exactos de stock (Entradas - Salidas). | Pruebas de Integración en Postman (15/15 Pass). |
| **Confiabilidad** | Manejo de excepciones para evitar caídas del servidor. | JUnit: `GlobalExceptionHandlerTest`. |
| **Eficiencia** | Tiempos de respuesta óptimos bajo carga de 100 usuarios. | Reporte JMeter (Promedio < 10ms). |
| **Integridad** | Protección de datos mediante autenticación JWT y escaneo de CVEs. | OWASP Dependency Check & Spring Security. |
| **Usabilidad** | API REST estandarizada con mensajes de error claros en español. | DTOs validados con `jakarta.validation`. |

## 2. Revisión del Producto (¿Qué tan fácil es mantenerlo?)

| Factor | Implementación en Don Gato | Evidencia SQA |
| :--- | :--- | :--- |
| **Mantenibilidad** | Arquitectura Hexagonal que separa la lógica de negocio de la base de datos. | Estructura de paquetes: `domain`, `application`, `infrastructure`. |
| **Flexibilidad** | Fácil adición de nuevos tipos de movimientos o productos. | Uso de Enums y Entidades desacopladas. |
| **Facilidad de Prueba** | Código diseñado para ser testeado modularmente. | Cobertura JaCoCo > 85%. |

## 3. Transición del Producto (¿Se adapta a otros entornos?)

| Factor | Implementación en Don Gato | Evidencia SQA |
| :--- | :--- | :--- |
| **Portabilidad** | Despliegue agnóstico al sistema operativo (Windows/Linux/Mac). | Dockerización con `Dockerfile` y `compose.yaml`. |
| **Reusabilidad** | Mappers y DTOs reutilizables en otros módulos. | Mappers de MapStruct y componentes genéricos. |
| **Interoperabilidad** | Comunicación estándar vía JSON para cualquier cliente (Web/Móvil). | Contrato de API documentado en los tests. |

---

## Relación con el Ciclo PDCA en nuestro SQA

Nuestro flujo de trabajo ha seguido exactamente este ciclo:

1.  **PLAN (Planear):** Definimos alcanzar un 85% de cobertura y 0 vulnerabilidades.
2.  **DO (Hacer):** Implementamos los controladores, seguridad JWT y los tests unitarios.
3.  **CHECK (Verificar):** Ejecutamos **JaCoCo** para medir cobertura, **OWASP** para seguridad y **JMeter** para eficiencia (Factores de McCall).
4.  **ACT (Actuar):** Ajustamos el código (como el fix de los Enums en Postman) para llegar al 100% de éxito.

> **Conclusión:** El proyecto no solo funciona, sino que es **auditable** y **escalable**, cumpliendo con estándares internacionales de ingeniería de software.
