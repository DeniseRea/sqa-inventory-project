# Guía Proyecto Final Parcial I: Desarrollo de API REST Empresarial

> Integración de Spring Boot, Clean Architecture y DevSecOps bajo el Modelo de Calidad de McCall (Ciclo PDCA)
>
> **Docente:** Ing. Diego L. Gamboa — dlgamboa1@espe.edu.ec
> *Departamento Ciencias de la Computación - ESPE*

---

## Índice

1. [Introducción y Enfoque Diferencial](#1-introducción-y-enfoque-diferencial)
2. [PLAN (PDCA) + CALIDAD (McCall)](#2-plan-pdca--calidad-mccall)
3. [Configuración del Entorno](#3-configuración-del-entorno)
4. [Diseño de Arquitectura (Clean Architecture)](#4-diseño-de-arquitectura-clean-architecture)
5. [DO (Implementación)](#5-do-implementación)
6. [Pruebas Automatizadas (Testability & Reliability)](#6-pruebas-automatizadas-testability--reliability)
7. [CHECK (PDCA) - Métricas y Calidad](#7-check-pdca---métricas-y-calidad)
8. [DevSecOps Pipeline](#8-devsecops-pipeline)
9. [Quality Scoring Engine](#9-quality-scoring-engine)
10. [ACT (PDCA) - Mejora Continua](#10-act-pdca---mejora-continua)
11. [Capturas Simuladas y Validación](#11-capturas-simuladas-y-validación)

---

## 1. Introducción y Enfoque Diferencial

El presente documento constituye una guía técnica rigurosa para la construcción de una **API REST empresarial (educativa)** para la **Gestión de Tareas (Task Management), Inventario, Financieros, E-Commerce, etc.** Se aborda el ciclo de vida completo del software a través del marco **PDCA (Plan-Do-Check-Act)**, asegurando que cada decisión arquitectónica y línea de código sea trazable a los factores de calidad del **Modelo de McCall**.

### 1.1. El Modelo de McCall como Eje Transversal

El Modelo de McCall categoriza la calidad del software en tres perspectivas:

1. **Operación del Producto:** Correctitud (Correctness), Confiabilidad (Reliability), Eficiencia (Efficiency), Integridad (Integrity), Usabilidad (Usability).
2. **Revisión del Producto:** Mantenibilidad (Maintainability), Flexibilidad (Flexibility), Testabilidad (Testability).
3. **Transición del Producto:** Portabilidad (Portability), Reusabilidad (Reusability), Interoperabilidad (Interoperability).

---

## 2. PLAN (PDCA) + CALIDAD (McCall)

### 2.1. Definición del Problema y Alcance

Las organizaciones requieren gestionar las tareas/procesos de forma concurrente, segura y auditable. El proyecto consistirá en un **CRUD de Tareas/Procesos** con asignación de estados y validación de seguridad **(JWT)**.

### 2.2. Mapeo de Requerimientos a Factores de McCall

| Requisito | Factor McCall | Métrica | Validación |
|---|---|---|---|
| Cifrado y Auth JWT | Integrity | 0 Vulnerabilidades Críticas | SAST (SonarQube/SpotBugs) |
| Respuesta < 200ms | Efficiency | Tiempo de latencia (p95) | Pruebas de Carga (JMeter) |
| Cálculo exacto de fechas | Correctness | 0 Defectos Funcionales | Unit Tests (JUnit) |
| Arquitectura Hexagonal | Maintainability | Deuda Técnica < 5% | SonarQube Code Smells |
| Despliegue Dockerizado | Portability | Tiempo de despliegue | CI/CD Pipeline Success |
| Alta cobertura de código | Testability | Line Coverage > 85% | JaCoCo Report |

---

## 3. Configuración del Entorno

Para garantizar la **Portabilidad** y **Reusabilidad**, estandarizar el entorno:

- **JDK:** Java 25 (LTS) — Beneficios de Virtual Threads para *Efficiency*.
- **Framework:** Spring Boot 4.0.x.
- **Build Tool:** Maven.

### 3.1. Dependencias Core (pom.xml)

```xml
<dependencies>
    <!-- Web y Core -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Persistencia (Integrity, Reliability) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- Validaciones (Correctness) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <!-- Herramientas (Maintainability) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## 4. Diseño de Arquitectura (Clean Architecture)

Aplicamos **Arquitectura Limpia** para potenciar la Mantenibilidad, Testabilidad y Flexibilidad (Modelo de McCall). La regla de dependencia exige que las capas externas dependan de las internas, nunca al revés.

```
com.empresa.tasks
|-- domain          (Reglas de negocio puras, Entidades - Correctness)
|-- application     (Casos de uso, Puertos/Interfaces - Reusability)
|-- infrastructure  (JPA, DB, APIs externas - Portability)
|-- interfaces      (Controladores REST, DTOs - Interoperability)
```

---

## 5. DO (Implementación)

### 5.1. Capa de Dominio (Domain)

> **Impacto McCall:** *Correctness* (las reglas de negocio no se corrompen).

```java
package com.empresa.tasks.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;

    public void markAsCompleted() {
        if (this.status == TaskStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete cancelled task");
        }
        this.status = TaskStatus.COMPLETED;
    }
}
```

### 5.2. Capa de Aplicación (Application)

> **Impacto McCall:** *Reusability* y *Flexibilidad*.

```java
package com.empresa.tasks.application.usecases;

import com.empresa.tasks.domain.model.Task;
import com.empresa.tasks.application.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskUseCase {

    private final TaskRepositoryPort taskRepository;

    @Transactional
    public Task createTask(Task task) {
        // Business logic validation (Correctness)
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }
}
```

### 5.3. Capa de Interfaces (Controllers)

> **Impacto McCall:** *Interoperability* (APIs REST estándar) e *Integrity* (Validación de entrada).

```java
package com.empresa.tasks.interfaces.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskUseCase taskUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO create(@Valid @RequestBody TaskCreateDTO dto) {
        Task task = TaskMapper.toDomain(dto);
        Task createdTask = taskUseCase.createTask(task);
        return TaskMapper.toDto(createdTask);
    }
}
```

---

## 6. Pruebas Automatizadas (Testability & Reliability)

Las pruebas garantizan la **Confiabilidad** del software ante cambios.

```java
package com.empresa.tasks.application.usecases;

import com.empresa.tasks.domain.model.Task;
import com.empresa.tasks.application.ports.out.TaskRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepository;

    @InjectMocks
    private TaskUseCase taskUseCase;

    @Test
    void shouldCreateTaskSuccessfully_CorrectnessValidated() {
        // Arrange
        Task task = new Task(null, "Test", "Desc", null, null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = taskUseCase.createTask(task);

        // Assert (Correctness & Reliability)
        assertNotNull(result);
        verify(taskRepository, times(1)).save(task);
    }
}
```

---

## 7. CHECK (PDCA) - Métricas y Calidad

Para el componente "Check", integramos análisis estático.

| Herramienta | Métrica | Factor McCall | Umbral Aceptado |
|---|---|---|---|
| JaCoCo | Code Coverage | Testability | ≥ 85% en Domain/App |
| SonarQube | Code Smells | Maintainability | Grado A (< 5% Deuda) |
| SpotBugs | Bugs / Defectos | Correctness | 0 Blocker/Critical |
| OWASP Dep. Check | Vulnerabilidades | Integrity | 0 CVEs detectados |

---

## 8. DevSecOps Pipeline

Pipeline en **GitHub Actions** para garantizar Integridad y Portabilidad mediante CI/CD continuo.

```yaml
name: Java DevSecOps Pipeline
on: [push, pull_request]
jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build and Test with Maven
        run: mvn clean verify
      - name: SonarQube Analysis
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn sonar:sonar
```

---

## 9. Quality Scoring Engine

Implementación de un motor Java para calcular matemáticamente la calidad basada en McCall.

```java
package com.empresa.tasks.sqa;

public class QualityScoringEngine {

    // Pesos asignados segun importancia del proyecto
    private static final double WEIGHT_CORRECTNESS     = 0.3;
    private static final double WEIGHT_TESTABILITY     = 0.3;
    private static final double WEIGHT_MAINTAINABILITY = 0.2;
    private static final double WEIGHT_INTEGRITY       = 0.2;

    public static double calculateScore(double coveragePercent, int bugs, int smells, int vulns) {
        double testabilityScore      = Math.min(coveragePercent, 100.0);
        double correctnessScore      = Math.max(100.0 - (bugs * 10), 0);
        double maintainabilityScore  = Math.max(100.0 - (smells * 2), 0);
        double integrityScore        = vulns == 0 ? 100.0 : 0.0; // Tolerancia 0

        return (testabilityScore     * WEIGHT_TESTABILITY)     +
               (correctnessScore     * WEIGHT_CORRECTNESS)     +
               (maintainabilityScore * WEIGHT_MAINTAINABILITY) +
               (integrityScore       * WEIGHT_INTEGRITY);
    }
}
```

---

## 10. ACT (PDCA) - Mejora Continua

Las acciones resultantes (Act) tras analizar el Quality Score; aplicar el **Ciclo PDCA para automatizar el flujo del proceso** de refactorización:

- **Mantenibilidad (Maintainability):** Si los *Code Smells* aumentan, programar un Sprint técnico de *Refactorización*.
- **Integridad (Integrity):** Si OWASP reporta fallos, actualizar bibliotecas (bump version) en el `pom.xml`.
- **Eficiencia (Efficiency):** Implementar *Caching* (Redis) si los tiempos de respuesta de la API superan los 200ms.

---

## 11. Capturas Simuladas y Validación

### Simulación: SonarQube Dashboard

```
Quality Gate: ✓ PASSED

Bugs: 0 (Rating A)       |  Vulnerabilities: 0 (Rating A)
Code Smells: 12 (Rating A)|  Coverage: 88.5%
```

### 11.1. Checklist de Validación Final

- [ ] **Proyecto compila** (Maven Build Success).
- [ ] **API responde** (HTTP 201 Created en POST).
- [ ] **Tests pasan** (100% JUnit pass rate).
- [ ] **SonarQube OK** (Quality Gate Passed, sin severidades altas).
- [ ] **Pipeline CI/CD exitoso** (Artifact Dockerizado).
- [ ] **Integración de todas las herramientas** y correcto flujo de trabajo (Automatización de los procesos).
- [ ] **Documento PDF** (Informe Final):
  - **Introducción:** Contexto del problema, objetivos y alcance del MVP.
  - **Metodología:** Descripción del ciclo PDCA + aplicación del Modelo de McCall.
  - **Resultados:**
    - Métricas de calidad obtenidas (cobertura, bugs, vulnerabilidades)
    - Quality Score calculado por el motor
    - Evidencias de ejecución (capturas de SonarQube, pipeline)
  - **Discusión:**
    - Análisis de factores McCall más críticos
    - Limitaciones encontradas y decisiones arquitectónicas
    - Comparativa con enfoques alternativos
  - **Conclusiones:**
    - Lecciones aprendidas sobre calidad medible
    - Recomendaciones para escalar el framework
    - Trabajo futuro (ej: integrar ISO 25010, IA en testing, etc.)

---

> **Nota de Evaluación**
>
> No se dispone de una rúbrica específica de evaluación. La valoración se realizará considerando la ejecución integral del proceso, la coherencia metodológica, la trazabilidad de las actividades y la consistencia técnica de la solución desarrollada de inicio a fin. Incluyendo el Informe Final, que demuestra el sustento técnico del desarrollo del presente proyecto.