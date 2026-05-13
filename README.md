# 🐱 Cafetería Don Gato - Sistema de Inventario Gourmet

Bienvenido al sistema de gestión de inventario de **Don Gato**, una solución moderna y elegante diseñada para cafeterías de especialidad que valoran tanto la precisión técnica como la estética artesanal.

![Gourmet UI](https://img.shields.io/badge/Aesthetic-Gourmet-B55239?style=for-the-badge)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql)

## ✨ Características Principales

*   **🎨 Estética Gourmet**: Interfaz diseñada con una paleta de colores Terracota, Beige y Café Intenso, inspirada en granos de café y cerámica artesanal.
*   **📊 Dashboard Inteligente**: Resumen en tiempo real de categorías, productos y movimientos registrados.
*   **📦 Gestión de Catálogo**: Control total sobre categorías y productos con búsqueda instantánea y estados automatizados (Disponible/Agotado).
*   **🔄 Bitácora de Movimientos**: Trazabilidad completa de cada entrada y salida de stock con motivos específicos (Venta, Reposición, Merma, etc.).
*   **🔐 Seguridad Robusta**: Autenticación basada en JWT (JSON Web Tokens) y arquitectura limpia.

## 🏗️ Arquitectura Técnica

El proyecto sigue los más altos estándares de ingeniería de software:

### Backend (Java / Spring Boot)
*   **Arquitectura Hexagonal**: Separación clara entre el dominio, la aplicación y la infraestructura.
*   **Persistencia**: PostgreSQL con Hibernate/JPA.
*   **Validación**: Uso de Bean Validation (JSR 303) para integridad de datos.
*   **McCall Factor**: Enfocado en la **Correctitud** (validación estricta) y **Mantenibilidad** (desacoplamiento).

### Frontend (React / Vite)
*   **React 19**: Aprovechando las últimas mejoras de rendimiento y estabilidad.
*   **Tailwind CSS v4**: Estilizado moderno con variables CSS dinámicas para la paleta Gourmet.
*   **Axios**: Gestión de peticiones con interceptores para manejo automático de tokens.
*   **React Router**: Navegación fluida y protegida por guards de autenticación.

## 🚀 Instalación y Uso

### Prerrequisitos
*   Java 25+
*   Node.js 20+
*   Docker & Docker Compose

### 1. Levantar el Backend
```bash
cd backend
# Levantar la base de datos y el servicio
docker compose up -d
```
El servidor estará disponible en `http://localhost:8080`.

### 2. Levantar el Frontend
```bash
cd frontend
npm install
npm run dev
```
La aplicación web estará disponible en `http://localhost:5173`.

## 🎨 Paleta de Colores (Gourmet Palette)

*   **Beige Cálido (`#F2DFC2`)**: Fondo principal, evoca limpieza y confort.
*   **Marrón Oscuro (`#2B1B17`)**: Sidebar y navegación, representa la base sólida del café.
*   **Terracota (`#B55239`)**: Acentos y botones principales, destaca las acciones importantes.
*   **Café Madera (`#4A2C24`)**: Tarjetas y elementos secundarios.

## ✅ Aseguramiento de la Calidad (SQA)

Este proyecto implementa un marco de SQA riguroso basado en los **Factores de Calidad de McCall**, garantizando que el software no solo funcione, sino que sea confiable, eficiente y seguro.

*   **Cobertura de Código**: >91% validado mediante **JaCoCo**.
*   **Pruebas de Rendimiento**: Validación de latencia <200ms para 50 usuarios concurrentes con **Apache JMeter**.
*   **Auditoría de Seguridad**: Análisis continuo de dependencias con **OWASP Dependency-Check**.
*   **Integridad de Negocio**: Suite completa de pruebas unitarias y de integración con **JUnit 5** y **Mockito**.

Para más detalles, consulte nuestra [**Documentación Exhaustiva de Pruebas**](./docs/tests/TEST_SUITE_DETAILS.md).

---
Desarrollado con ❤️ para **Cafetería Don Gato**.
