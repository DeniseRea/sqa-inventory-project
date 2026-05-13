# Reporte de Implementación SQA: Aseguramiento de la Calidad del Backend

Este documento detalla todas las configuraciones, pruebas y estándares de calidad (Software Quality Assurance - SQA) que han sido implementados exitosamente en el backend del proyecto **Don Gato Inventory** para garantizar su fiabilidad, seguridad y rendimiento.

---

## 1. Cobertura de Código y Pruebas Unitarias (JaCoCo & Mockito)

El objetivo establecido fue alcanzar una cobertura de código mínima del **85%** sobre la lógica de negocio, validadores y adaptadores. Se implementó una suite robusta de pruebas que elevó la cobertura global del **31% al 91.5%**.

### Tecnologías Utilizadas
- **JUnit 5 & Mockito:** Para la creación de pruebas unitarias aisladas sin levantar el contexto completo de Spring.
- **JaCoCo Maven Plugin (0.8.14):** Se configuró como *Quality Gate* en la fase `verify` de Maven. Si el código nuevo baja el promedio del 85%, el proceso de construcción (build) fallará automáticamente. Se configuraron exclusiones específicas para modelos de dominio, entidades y DTOs, enfocando la cobertura estrictamente en lógica funcional.

### Módulos Probados Exhaustivamente (100% de cobertura en la mayoría):
1. **Casos de Uso (Lógica de Negocio Central):**
   - `ProductUseCaseTest`
   - `CategoryUseCaseTest`
   - `StockMovementUseCaseTest`
2. **Controladores REST (Capa de Presentación):**
   - `ProductControllerTest`, `CategoryControllerTest`, `StockMovementControllerTest`, `AuthControllerTest`.
3. **Mappers (Transformación de Datos):**
   - `ProductRestMapperTest`, `CategoryRestMapperTest`, `StockMovementRestMapperTest`.
   - `ProductPersistenceMapperTest`, `CategoryPersistenceMapperTest`, `StockMovementPersistenceMapperTest`.
4. **Adaptadores de Repositorio (Capa de Persistencia):**
   - `ProductRepositoryAdapterTest`, `CategoryRepositoryAdapterTest`, `StockMovementRepositoryAdapterTest`.
5. **Seguridad y Excepciones:**
   - `JwtTokenProviderTest` (Validación y expiración de tokens).
   - `GlobalExceptionHandlerTest` (Manejo correcto de respuestas HTTP 400, 401, 404, 409, 500).

---

## 2. Auditoría de Seguridad Continua (OWASP)

Para prevenir vulnerabilidades críticas derivadas de bibliotecas obsoletas o comprometidas (ataques de la cadena de suministro), se incorporó un análisis de seguridad profundo.

### Configuración
- **OWASP Dependency-Check Maven Plugin (11.1.0):** Se añadió este plugin al ciclo de vida de compilación (`pom.xml`).
- **Funcionamiento:** Durante la fase de `verify` o ejecutando `mvn dependency-check:check`, el plugin escanea todas las dependencias del proyecto comparándolas contra la base de datos oficial del National Vulnerability Database (NVD).
- **Propósito SQA:** Identificar CVEs (Vulnerabilidades y Exposiciones Comunes) antes de que el código sea desplegado a producción.

---

## 3. Pruebas de Carga y Rendimiento (Apache JMeter)

Para garantizar la fiabilidad operativa del servidor bajo estrés y mantener la latencia en niveles aceptables, se definió una línea base estricta de pruebas de carga.

### Configuración
- **Plan de Pruebas (.jmx):** Se creó el archivo `load-test.jmx` en `src/test/jmeter/` simulando **50 usuarios concurrentes**.
- **Criterio de Aceptación (Quality Gate):** El tiempo de respuesta del servidor (endpoint `GET /api/v1/products`) debe mantenerse sistemáticamente **por debajo de los 200 milisegundos**.
- **Automatización (CI):** Se integró el `jmeter-maven-plugin` para que corra los tests de JMeter nativamente dentro del proceso de Maven.

### Estrategia de Ejecución Local
Para asegurar que los tests de JMeter se puedan ejecutar en local sin intervención manual, el `spring-boot-maven-plugin` fue modificado para:
1. Arrancar la aplicación automáticamente en el puerto 8080 (fase `pre-integration-test`) usando el puerto JMX `9005` para evitar colisiones.
2. Ejecutar los reportes y aserciones de JMeter (fase `integration-test`).
3. Apagar la aplicación limpiamente una vez concluida la prueba (fase `post-integration-test`).

---

## 5. Documentación Adicional Detallada

Para una revisión granular de cada caso de prueba, escenarios validados y configuración técnica específica de cada herramienta, consulte el siguiente documento:

*   [**Detalle Exhaustivo de la Suite de Pruebas (TEST_SUITE_DETAILS.md)**](./TEST_SUITE_DETAILS.md)

---
**Build Success:** Significa que el código cubre el 85%, no tiene vulnerabilidades altas, y la API responde a 50 usuarios concurrentes en menos de 200ms.
* **Build Failure:** Ocurrirá si alguna de las métricas anteriores no se cumple, lo cual detendrá el despliegue automático hacia entornos de producción, salvaguardando la calidad del sistema final.
