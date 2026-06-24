# Arquitectura del Flujo CI/CD - Don Gato Inventory

A continuación se presenta la visualización técnica del ecosistema de automatización y aseguramiento de calidad (SQA) que hemos implementado.

![Diagrama de Arquitectura CI/CD](file:///C:/Users/gamur/.gemini/antigravity/brain/c7a4c573-aecc-47fe-8209-7b8234c7bd0c/cicd_architecture_diagram_1778784141458.png)

## Descripción del Flujo Operativo

El flujo está diseñado bajo los principios de **DevSecOps**, integrando seguridad y calidad en cada paso del ciclo de vida del desarrollo:

### 1. Integración Continua (CI)
*   **Trigger:** Cada `push` o `pull request` actúa como el disparador del proceso.
*   **Validación de Calidad:**
    *   **Unit Tests:** JUnit asegura que los componentes individuales funcionen.
    *   **JaCoCo:** Mide que al menos el 85% del código esté cubierto por pruebas.
    *   **SonarCloud Scan:** Escanea el código en busca de vulnerabilidades, "code smells" y fallos de seguridad. Si el **Quality Gate** no se cumple, el proceso se bloquea.
    *   **JMeter:** (Configurado localmente, listo para CI) Valida que el rendimiento se mantenga dentro de los límites aceptables.

### 2. Notificaciones y Trazabilidad
*   **Discord Webhook:** El sistema notifica instantáneamente al equipo si ocurre un fallo en el Quality Gate o en la construcción de la imagen.
*   **GitHub Artifacts:** Todos los reportes (cobertura, resultados de tests, logs) se almacenan para auditorías de SQA.

### 3. Entrega Continua (CD)
*   **Dockerization:** Si todas las pruebas de calidad pasan, el sistema empaqueta el backend en un contenedor Docker.
*   **Registry:** La imagen final se sube a **Docker Hub**, permitiendo un despliegue rápido y consistente en cualquier entorno.

---
*Este flujo materializa el ciclo PDCA: Planificamos las pruebas, las ejecutamos automáticamente (Do), verificamos los resultados en Sonar/Discord (Check) y actuamos corrigiendo fallos antes de que lleguen a producción (Act).*
