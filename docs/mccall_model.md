# Modelo de Calidad de McCall - Don Gato Inventory

Este documento define la aplicación del **Modelo de Calidad de Software de McCall** para el proyecto "Don Gato Inventory", tomando como referencia los lineamientos establecidos en la *Guía Proyecto Final Parcial 1*. 

El modelo de McCall estructura la evaluación de la calidad del software a través de tres perspectivas principales (Operación, Revisión y Transición), que a su vez se dividen en 11 factores de calidad. En este repositorio, gran parte de estos factores están trazados directamente en el código a nivel de componentes arquitectónicos (Arquitectura Hexagonal) y evaluados mediante un Motor de Calidad de Software (SQA).

---

## 1. Operación del Producto (Product Operations)
*Se centra en las características operativas del software una vez desplegado y en manos del usuario o sistema final.*

### 1.1 Corrección (Correctness)
**Definición:** Medida en que el software satisface sus especificaciones y cumple con los objetivos de la lógica de negocio.
* **Aplicación en Don Gato:** Validaciones estrictas en las entidades del Dominio (e.g., `Product`, `Category`). Se previenen reglas de negocio inválidas, impidiendo, por ejemplo, que el inventario se registre con valores negativos mediante excepciones como `InsufficientStockException`. Las reglas de negocio operan aisladas de la infraestructura y los frameworks.

### 1.2 Fiabilidad (Reliability)
**Definición:** Capacidad del sistema para no fallar y responder predeciblemente.
* **Aplicación en Don Gato:** Implementación de controladores globales de excepciones (`GlobalExceptionHandler`) para prevenir caídas no controladas del servidor y estandarización en las respuestas (HTTP 500, 400). Inyección de pruebas unitarias (`JUnit 5`, `Mockito`) que blindan las rutas críticas del proyecto de fallos en nuevas iteraciones.

### 1.3 Eficiencia (Efficiency)
**Definición:** Uso óptimo de recursos (procesador, memoria) al ejecutar sus tareas.
* **Aplicación en Don Gato:** Arquitectura asíncrona en el frontend (`React/Vite`) mediante hooks y llamadas a la API limpias. En el Backend, evaluaciones de estrés con **Apache JMeter**, estableciendo una barrera de calidad para soportar más de 50 usuarios concurrentes con tiempos de respuesta bajo los 200 milisegundos.

### 1.4 Integridad (Integrity)
**Definición:** Seguridad del sistema contra accesos no autorizados, modificaciones o la pérdida de datos de auditoría.
* **Aplicación en Don Gato:** Se implementó una capa fuerte de seguridad utilizando **JWT (JSON Web Tokens)** desde `AuthController`. Además, la trazabilidad del inventario se audita estrictamente a través de clases de dominio exclusivas (`MovementReason`, `MovementType`), sin borrados físicos no autorizados y asegurando el historial de operaciones de stock a través de Repositorios controlados. Adicionalmente, se configuró `OWASP Dependency-Check` para prevenir vulnerabilidades desde componentes externos (CVEs).

### 1.5 Usabilidad (Usability)
**Definición:** Facilidad de uso para los usuarios finales (interfaz limpia, gestión amena de errores).
* **Aplicación en Don Gato:** En el cliente (Frontend), construcción de interfaces de usuario responsivas y guiadas usando Componentes Atómicos en React. A nivel API (Backend), exposición de errores semánticos e informativos estandarizados a través de los Data Transfer Objects (`ApiErrorDTO`), devolviendo códigos y mensajes HTTP expresivos (404 Not Found, 409 Conflict, etc.).

---

## 2. Revisión del Producto (Product Revision)
*Se centra en el nivel de esfuerzo necesario para poder modificar el software (correcciones de errores, adaptaciones).*

### 2.1 Mantenibilidad (Maintainability)
**Definición:** Facilidad para localizar y reparar errores.
* **Aplicación en Don Gato:** Promovida extensamente usando el paradigma de **Clean Architecture** (Arquitectura Hexagonal). Se utilizan Mappers (ej. `CategoryRestMapper`, `ProductPersistenceMapper`) para aislar los contratos e interfaces del API (REST) estrictamente separadas de la estructura del Dominio. Esto facilita que arreglos en una de las esquinas (DB o API) no impacten el núcleo de negocio. Análisis con *JaCoCo* asegura que las futuras modificaciones en la lógica del negocio no rompan el funcionamiento, basándose en la métrica meta del 85% de code coverage.

### 2.2 Flexibilidad (Flexibility)
**Definición:** Esfuerzo necesario para modificar un software en funcionamiento.
* **Aplicación en Don Gato:** El enrutamiento de dependencias a través de los adaptadores minimiza la atadura entre módulos funcionales. El mapeo ORM centralizado con Spring Data JPA permite la adaptación de flujos y requerimientos funcionales de manera independiente.

### 2.3 Capacidad de Prueba (Testability)
**Definición:** Facilidad para establecer criterios de pruebas y comprobar que el sistema funciona bajo las especificaciones establecidas.
* **Aplicación en Don Gato:** Uno de los puntos focales del proyecto en base a la Guía Parcial 1. Capacidad de prueba en nivel óptimo (logrando > 90% en la cobertura de casos de uso y lógica). La base del diseño hexagonal provee Inyección de Dependencias, lo que permite mockeos fáciles a los puertos (`ProductRepositoryPort`). El pipeline incluye métricas restrictivas de JaCoCo al hacer la compilación Maven.

---

## 3. Transición del Producto (Product Transition)
*Se evalúa la versatilidad de la solución técnica para conectar, reutilizar su código o exportarse sobre distintos ecosistemas.*

### 3.1 Portabilidad (Portability)
**Definición:** Complejidad de trasladar el aplicativo de un entorno de hardware/software a otro.
* **Aplicación en Don Gato:** Uso estandarizado de contenedores. Archivo `Dockerfile` y configuración multicontenedor en `compose.yaml` (Docker Compose) para separar la capa de Base de Datos y la lógica Backend. El front se ejecuta desde el robusto Vite bundler. Independencia total de sistemas operativos al desplegar.

### 3.2 Reusabilidad (Reusability)
**Definición:** En qué medida el software o sus partes pueden utilizarse en otras aplicaciones.
* **Aplicación en Don Gato:** Abstracción altísima vía puertos e interfaces (`Ports Out / Ports In` de Clean Architecture). Los Repositorios que hoy abstraen lógica de persistencia hacia un motor relacional en PostgreSQL son interfaces que cualquier otro Adaptador podría implementar mañana para persistir sobre una base de datos distribuida o NoSQL sin afectar al dominio o los casos de uso (`UseCase`).

### 3.3 Interoperabilidad (Interoperability)
**Definición:** Esfuerzo necesario para comunicarse fluidamente con otro software.
* **Aplicación en Don Gato:** Exposición RESTful madura usando `JSON`. Uso de estándares HTTP predecibles con endpoints muy descritos que responden con tipos *MIME* universales. Integración de Actuadores o controladores dedicados como el `QualityController` que permiten acceder directamente a un reporte estandarizado y parseable para sistemas externos de monitorización.

---

*Nota: Este modelo conceptual de McCall se ve reflejado tangencialmente en el SQA (Software Quality Engine) programado explícitamente en el core del proyecto y sus pipelines CI/CD.*