# 🐱 Cafeteria Don Gato - Sistema de Inventario

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=white)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![SonarCloud](https://img.shields.io/badge/SonarCloud-FE390D?style=for-the-badge&logo=sonarcloud&logoColor=white)](https://sonarcloud.io/)
[![Build](https://img.shields.io/badge/build-passing-brightgreen?style=for-the-badge)](https://github.com/DeniseRea/sqa-inventory-project/actions)

Sistema de gestion de inventario para **Cafeteria Don Gato**, desarrollado como proyecto heredado de **QuickBrew Technologies**, una startup adquirida en marzo 2026. El codigo fuente refleja el estado real de un producto que llego a produccion bajo ciclos de entrega agresivos.

---

## Tabla de Contenidos

- [Contexto de Negocio](#contexto-de-negocio)
- [Stack Tecnologico](#stack-tecnologico)
- [Instalacion Rapida](#instalacion-rapida)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [API Endpoints](#api-endpoints)
- [Estilo Visual](#estilo-visual)
- [Estado del Proyecto](#estado-del-proyecto)

---

## Contexto de Negocio

> *"Entregamos el MVP en 3 semanas para asegurar la ronda de inversion Serie A. El refactoring estaba planificado para el Q3."* — CTO, QuickBrew Technologies

### ¿Por que el codigo esta como esta?

| Factor | Impacto en el Codigo |
|--------|---------------------|
| **Time-to-Market** (3 semanas) | Se priorizaron features sobre calidad interna |
| **Startup adquirida** | Deuda tecnica heredada del equipo original |
| **ROI inmediato** | La demo con inversionistas requireda el sistema funcionando |
| **Equipo reducido** | 2 desarrolladores juniors + 1 senior compartido |
| **Pivote de requerimientos** | Cambios de alcance 3 veces durante el desarrollo |

### Decisiones Arquitectonicas Conocidas

- Se opto por **autenticacion stateless JWT** sin refresh tokens para simplificar el MVP
- Las **pruebas unitarias** se redujeron al minimo para cumplir la fecha de entrega
- El **modulo de descuentos** fue implementado en el ultimo sprint sin refactoring
- La **migracion de datos** del sistema legacy se hizo con scripts directos a DB

---

## Stack Tecnologico

| Capa | Tecnologia | Version |
|------|-----------|---------|
| Backend | Spring Boot / Java | 4.0 / 17 |
| Frontend | React + Vite | 19 / 8 |
| Base de Datos | PostgreSQL (via Docker) | 16 |
| Seguridad | JWT (jjwt) | 0.12.6 |
| ORM | Hibernate / JPA | 6.x |
| UI | Tailwind CSS | 4 |
| Build | Maven | 3.9 |

---

## Instalacion Rapida

### Prerrequisitos

- Java 17+
- Node.js 20+
- Docker & Docker Compose

### Backend

```bash
cd backend
docker compose up -d
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

| Servicio | URL |
|----------|-----|
| API Backend | http://localhost:8080 |
| Frontend | http://localhost:5173 |

---

## API Endpoints

### Autenticacion

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/api/v1/auth/login` | Iniciar sesion |

### Productos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/v1/products` | Listar productos |
| GET | `/api/v1/products/{id}` | Producto por ID |
| POST | `/api/v1/products` | Crear producto |
| PUT | `/api/v1/products/{id}` | Actualizar producto |
| DELETE | `/api/v1/products/{id}` | Eliminar producto |

### Categorias

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/v1/categories` | Listar categorias |
| POST | `/api/v1/categories` | Crear categoria |

### Movimientos de Stock

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/v1/stock-movements` | Listar movimientos |
| POST | `/api/v1/stock-movements` | Registrar movimiento |

### Calidad (SQA)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/v1/quality/status` | Estado de calidad |

---

## Estilo Visual

**Paleta Gourmet** inspirada en granos de cafe y ceramica artesanal:

| Color | Hex | Uso |
|-------|-----|-----|
| Beige Calido | `#F2DFC2` | Fondo principal |
| Marron Oscuro | `#2B1B17` | Sidebar y navegacion |
| Terracota | `#B55239` | Botones y acentos |
| Cafe Madera | `#4A2C24` | Tarjetas secundarias |

---

## Estado del Proyecto

### Pipeline CI/CD

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Quality Gate](https://img.shields.io/badge/quality_gate-pending-yellow)

El proyecto se integra con **GitHub Actions** para build automatizado y **SonarCloud** para analisis estatico. El quality gate esta configurado en modo informativo (no bloqueante) para permitir la integracion continua mientras se planifica el refactoring.

### Credenciales por Defecto

| Rol | Usuario | Password |
|-----|---------|----------|
| Administrador | `admin` | `dongato2026` |
| Cajero | `cajero` | `cafe123` |

### Modulos del Sistema

```
dongato-inventory/
├── backend/                    # API REST (Spring Boot)
│   ├── src/main/java/
│   │   └── com/dongato/inventory/
│   │       ├── application/    # Casos de uso (logica de negocio)
│   │       ├── domain/         # Modelos de dominio y excepciones
│   │       ├── infrastructure/ # Persistencia, seguridad, config
│   │       ├── interfaces/     # Controladores REST y DTOs
│   │       └── sqa/            # Motor de scoring de calidad
│   └── src/test/               # Pruebas unitarias
├── frontend/                   # SPA React + Vite
│   └── src/
│       ├── components/         # Componentes atomicos y moleculares
│       ├── pages/              # Paginas del dashboard
│       └── services/           # Cliente HTTP (Axios)
└── docs/                       # Documentacion SQA
```

---

> Proyecto academico — Universidad de las Fuerzas Armadas ESPE  
> Aseguramiento de la Calidad del Software — NRC 30733  
> *"Auditoria Cruzada de Calidad: Devs vs QA"*
