# Guia de Compilacion y Despliegue - WSL + Docker

Comandos usados para compilar y levantar el proyecto `sqa-inventory-project` desde Windows usando WSL Ubuntu + Docker.

---

## 1. Verificacion del entorno

```bash
# Verificar WSL
wsl --status

# Verificar Docker dentro de Ubuntu
wsl -d Ubuntu -- bash -c "docker --version && docker compose version"
```

---

## 2. Navegar al proyecto

```bash
# Desde Windows, el proyecto vive en /mnt/c/ dentro de WSL
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && pwd && ls -la"
```

---

## 3. Compilar imagenes (sin levantar)

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose build"
```

**Salida esperada:**

| Imagen | Tamano |
| --- | --- |
| `dongato/sqa-backend:latest` | ~424 MB |
| `dongato/sqa-frontend:latest` | ~95 MB |
| `postgres:16-alpine` | (se descarga al levantar) |

---

## 4. Levantar los servicios

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose up -d"
```

**Contenedores resultantes:**

| Contenedor | Imagen | Puerto host |
| --- | --- | --- |
| `dongato-postgres` | `postgres:16-alpine` | `5433 -> 5432` |
| `dongato-app` | `dongato/sqa-backend:latest` | `8080 -> 8080` |
| `dongato-frontend` | `dongato/sqa-frontend:latest` | `80 -> 80` |

---

## 5. Verificar estado

```bash
wsl -d Ubuntu -- bash -c "docker ps --format 'table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}'"
```

---

## 6. Comandos utiles del dia a dia

### Ver logs en vivo

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose logs -f"
```

### Ver logs solo del backend

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose logs -f app"
```

### Detener todo

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose down"
```

### Detener y borrar volumenes (limpieza total)

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose down -v"
```

### Reconstruir despues de cambios en codigo (LO MAS COMUN)

**No hace falta detener nada antes.** Docker reemplaza el contenedor automaticamente y conserva la base de datos:

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && docker compose up -d --build"
```

### Cuando usar cada comando

| Comando | Caso de uso | Borra la BD? |
| --- | --- | --- |
| `docker compose up -d --build` | Cambiaste codigo y quieres re-desplegar | NO |
| `docker compose down` | Apagar todo para liberar recursos | NO |
| `docker compose down -v` | Empezar de cero (BD vacia) | SI |

**Usa `down -v` solo si:**
- La BD tiene datos corruptos
- Cambiaste el modelo de datos y `ddl-auto=update` se lio
- Quieres probar el seed inicial de nuevo

---

## 7. Acceso desde el navegador

| Servicio | URL | Credenciales |
| --- | --- | --- |
| Frontend | http://localhost | `admin` / `dongato2026` (rol ADMIN) |
| Backend (API) | http://localhost:8080 | `cajero` / `cafe123` (rol USER) |
| PostgreSQL | `localhost:5433` | DB `dongato_db`, user `dongato_user`, pass `dongato123` |

---

## 8. Probar login contra la API (curl)

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"dongato2026"}'
```

---

## 9. Cambiar credenciales por variables de entorno

```bash
wsl -d Ubuntu -- bash -c "cd '/mnt/c/Users/mesia/Desktop/Universidad/Calidad/1P/Proyecto/sqa-inventory-project' && ADMIN_PASSWORD=miclave USER_PASSWORD=otraclave docker compose up -d"
```

---

## Resumen del flujo completo

```text
wsl
  └── Ubuntu
        └── /mnt/c/Users/mesia/.../sqa-inventory-project
              ├── docker compose build      # compila imagenes
              │     ├── dongato/sqa-backend  (424 MB)
              │     └── dongato/sqa-frontend (95 MB)
              │
              └── docker compose up -d      # levanta servicios
                    ├── dongato-postgres    (5433)
                    ├── dongato-app         (8080)
                    └── dongato-frontend    (80)
```
