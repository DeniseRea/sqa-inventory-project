# Master Test Plan (MTP) - SQA Project: Don Gato Inventory
**Versión:** 1.0 | **Fecha:** 12/05/2026 | **Estado:** Aprobado

---

## 1. Introducción y Propósito
El propósito de este *Plan Maestro de Pruebas (Master Test Plan - MTP)*, alineado con los principios SQA (Aseguramiento de Calidad de Software) y buenas prácticas de la industria (basado en el estándar IEEE 829), es proporcionar un enfoque metodológico riguroso para la evaluación y validación del backend del **Sistema de Inventario Don Gato**. 

Este documento establece las normativas para garantizar los Factores de Calidad de McCall (Fiabilidad, Eficiencia, Mantenibilidad, Testabilidad y Seguridad), asegurando que el producto final cumpla estrictamente con los requisitos funcionales y no funcionales (NFR).

---

## 2. Alcance de las Pruebas (Scope)

### 2.1. Elementos dentro del Alcance (In Scope)
La estrategia de SQA cubrirá exclusivamente el **Backend (API REST)** desarrollado en Spring Boot, validando las siguientes capas de arquitectura hexagonal:
1. **Dominio y Aplicación (Use Cases):** Validar la lógica central y las reglas de negocio aisladas.
2. **Infraestructura (Adaptadores y Mappers):** Validar el correcto mapeo y persistencia hacia la base de datos (H2 en entorno test).
3. **Controladores (Capa REST):** Garantizar la correcta respuesta HTTP, manejo de excepciones (GlobalExceptionHandler) y flujos.
4. **Seguridad (Capa JWT y OWASP):** Análisis de vulnerabilidades y acceso no autorizado.
5. **Rendimiento bajo estrés:** Respuesta ante peticiones masivas y concurrentes.

### 2.2. Elementos fuera del Alcance (Out of Scope)
- Interfaz gráfica o Frontend (UI/UX).
- Bases de datos en producción (PostgreSQL).
- Pruebas de Recuperación de Desastres (Disaster Recovery).

---

## 3. Estrategia de Pruebas (Testing Strategy)

Se utilizará una aproximación de pruebas automatizadas continua (CI) en múltiples niveles:

| Tipo de Prueba | Nivel de Prueba | Herramienta | Objetivo Específico |
| :--- | :--- | :--- | :--- |
| **Pruebas Unitarias** | Componente | `JUnit 5` + `Mockito` | Comprobar de forma aislada el comportamiento de un bloque (función/clase) sin sus dependencias externas. |
| **Pruebas de Seguridad (SCA)** | Integración/Build | `OWASP Dependency-Check` | Evitar Riesgos de la Cadena de Suministro detectando dependencias con CVEs críticos antes de generar el JAR. |
| **Pruebas de Carga/Rendimiento** | Sistema (Performance) | `Apache JMeter` | Identificar cuellos de botella y verificar que el servidor soporte 50 usuarios concurrentes sin degradación. |

---

## 4. Criterios de Aceptación y Quality Gates

Para garantizar la calidad continua, el proceso de construcción (`Maven Build`) implementa "Quality Gates" rígidos. El código no pasará a producción a menos que cumpla lo siguiente:

### 4.1. Criterios de Entrada (Entry Criteria)
* Código nuevo integrado a la rama correspondiente (Ej. `tests` o `main`).
* El código fuente compila sin advertencias severas ni errores en `JDK 21`.
* Todo código de nueva lógica debe contener sus respectivos archivos `*Test.java`.

### 4.2. Quality Gates (Métricas SQA)
* **Cobertura (Code Coverage):** Evaluado por JaCoCo Maven Plugin. **Mínimo requerido:** 85% de líneas cubiertas. El sistema rechazará el build si se encuentra por debajo de este límite. Actualmente alcanzado: **91.5%**.
* **Latencia y Rendimiento:** Tolerancia SQA definida (Apdex Target). Tiempo de respuesta **menor a 200 milisegundos (<200ms)** bajo un escenario concurrente.
* **Tolerancia a Vulnerabilidades:** Cero dependencias con vulnerabilidad CVSS score > 7.0 (High/Critical) según NVD. Evaluado por OWASP.

### 4.3. Criterios de Salida (Exit Criteria)
* 100% de los tests unitarios en estado *PASSED* (0 failures, 0 errors).
* Reporte JaCoCo generado mostrando el cumplimiento > 85%.
* Reporte JMeter en dashboard HTML demostrando 0% de solicitudes fallidas en estrés.

---

## 5. Escenarios de Pruebas y Casos Relevantes

### 5.1. Casos de Uso (Business Logic via Mockito)
Se inyectarán dependencias falsas (Mocks) para probar de forma estricta:
1. **Creación de Entidades:** Simular la respuesta de persistencia y validar si devuelve la entidad construida.
2. **Validación de Excepciones:** Forzar el escenario de stock insuficiente (`InsufficientStockException`) o categoría inexistente (`ResourceNotFoundException`).

### 5.2. Escenario de Pruebas de Carga (JMeter)
* **Thread Group:** 50 Usuarios Concurrentes (Threads).
* **Ramp-Up Period:** 10 segundos.
* **Loop Count:** 20 (Total 1000 Peticiones).
* **Endpoint Evaluado:** `GET /api/v1/products`.
* **Assertion (Aserciones):**
  - **Duración:** `< 200 ms`.
  - **Respuesta:** Código `200 OK`.

---

## 6. Entorno de Pruebas (Test Environment)
- **Lenguaje:** Java SE 21.
- **Framework de Desarrollo:** Spring Boot 3.2.x.
- **Base de Datos (Testing):** H2 (In-Memory Database, volátil, rápida).
- **Herramienta de Construcción:** Maven Wrapper (`mvnw`).
- **Plugins de Ejecución Automática:**
  - `maven-surefire-plugin` (Ejecución de Tests).
  - `jacoco-maven-plugin` (Verificación y Reportes).
  - `dependency-check-maven` (Auditoría de Seguridad).
  - `jmeter-maven-plugin` + `spring-boot-maven-plugin` (Arranque y Stress Test).

---

## 7. Entregables de las Pruebas (Test Deliverables)
Al finalizar el ciclo de evaluación técnica SQA, el equipo contará con:
1. **Test Scripts Source Code:** Archivos `*Test.java` agregados a `/src/test`.
2. **Test Scripts de Rendimiento:** Archivo de configuración `/src/test/jmeter/load-test.jmx`.
3. **Reporte HTML JaCoCo:** Dashboard visual con métricas de cobertura por clase y método.
4. **Reporte HTML JMeter:** Dashboard con gráficos estadísticos de APDEX y tiempo de respuesta de los endpoints.
5. **Este Plan Maestro SQA:** Documento normativo guardado en `/docs`.

---

**Aprobado Por:** Equipo de Desarrollo Backend SQA.
**Firma / Commit Hash:** `ffdbe4e` (y posteriores).
