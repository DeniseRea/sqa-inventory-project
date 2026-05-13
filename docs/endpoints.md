# Guía de Endpoints del Backend

> Esta guía explica cómo usar los endpoints disponibles y cómo encontrarlos tú mismo en el código.

---

## ¿Qué significan las palabras clave?

Antes de empezar, aquí te explico los términos que verás en esta guía:

| Término | Significado |
|---------|-------------|
| **Endpoint** | Una dirección URL dentro de la API a la que puedes enviar peticiones (por ejemplo: `/api/v1/products`). |
| **GET / POST / PUT / DELETE** | Son los **métodos HTTP** que indican qué acción quieres hacer: obtener datos (GET), crear (POST), actualizar (PUT) o eliminar (DELETE). |
| **Público** | Significa que **cualquiera** puede usar ese endpoint sin identificarse. No necesitas usuario ni contraseña. |
| **Protegido** | Significa que **solo usuarios registrados** pueden usarlo. Debes enviar un token de autenticación para que el servidor te deje pasar. |
| **Token / JWT** | Es una "llave digital" que obtienes al hacer login. Es un texto largo que demuestra quién eres. Se envía en cada petición protegida con el formato: `Authorization: Bearer tu-token-aqui`. |
| **Con token** | El endpoint requiere que envíes el JWT en el header `Authorization`. Sin él, el servidor responderá con error `401 Unauthorized`. |
| **Sin token** | El endpoint es público, no necesitas enviar ninguna credencial. |
| **Body** | Es el contenido (datos en formato JSON) que envías dentro de la petición, por ejemplo al crear un producto nuevo. |
| **Response / Respuesta** | Es lo que el servidor te devuelve después de recibir tu petición. |
| **Query parameter** | Son valores opcionales que se envían en la URL después de un `?`, por ejemplo: `/search?name=cafe`. |

---

## URLs Base

- Local: `http://localhost:8080`
- Prefijo de API: `/api/v1`

---

## Índice Completo de los 17 Endpoints

Aquí tienes la lista exhaustiva de todos los endpoints de la API, qué método usan, si requieren Token y cómo probarlos. 

**Leyenda para probarlos:**
- 🌐 **Navegador**: Solo copia y pega la URL en la barra de direcciones de tu navegador.
- 💻 **Consola F12 / Postman**: Requiere usar JavaScript `fetch()`, Postman o cURL porque envías un Token o usas métodos POST/PUT/DELETE.

| # | Controlador | Método | Endpoint (URL) | ¿Requiere Token? | ¿Cómo Probarlo? |
|---|-------------|--------|----------------|------------------|-----------------|
| 1 | Auth | **POST** | `/api/v1/auth/login` | ❌ No | 💻 Consola F12 (Enviar usuario/password) |
| 2 | Categories | **GET** | `/api/v1/categories` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/categories` |
| 3 | Categories | **GET** | `/api/v1/categories/{id}` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/categories/1` |
| 4 | Categories | **POST** | `/api/v1/categories` | ✅ Sí | 💻 Consola F12 + Token |
| 5 | Categories | **PUT** | `/api/v1/categories/{id}` | ✅ Sí | 💻 Consola F12 + Token |
| 6 | Categories | **DELETE** | `/api/v1/categories/{id}` | ✅ Sí | 💻 Consola F12 + Token |
| 7 | Products | **GET** | `/api/v1/products` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/products` |
| 8 | Products | **GET** | `/api/v1/products/{id}` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/products/1` |
| 9 | Products | **GET** | `/api/v1/products/category/{categoryId}` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/products/category/1` |
| 10| Products | **GET** | `/api/v1/products/status/{status}` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/products/status/ACTIVE` |
| 11| Products | **GET** | `/api/v1/products/search?name={name}` | ❌ No | 🌐 Navegador: `http://localhost:8080/api/v1/products/search?name=cafe` |
| 12| Products | **POST** | `/api/v1/products` | ✅ Sí | 💻 Consola F12 + Token |
| 13| Products | **PUT** | `/api/v1/products/{id}` | ✅ Sí | 💻 Consola F12 + Token |
| 14| Products | **DELETE** | `/api/v1/products/{id}` | ✅ Sí | 💻 Consola F12 + Token |
| 15| Stock Moves | **GET** | `/api/v1/stock-movements` | ✅ Sí | 💻 Consola F12 + Token |
| 16| Stock Moves | **GET** | `/api/v1/stock-movements/product/{productId}` | ✅ Sí | 💻 Consola F12 + Token |
| 17| Stock Moves | **POST** | `/api/v1/stock-movements` | ✅ Sí | 💻 Consola F12 + Token |

---

## 1. Autenticación

### `POST /api/v1/auth/login`
Obtiene un token JWT para usar en endpoints protegidos.

**Body:**
```json
{
  "username": "admin",
  "password": "dongato2026"
}
```

>Otro usuario disponible: `cajero` / `cafe123`

**Respuesta exitosa:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "type": "Bearer",
  "username": "admin",
  "expiresIn": 86400
}
```

**Uso en requests protegidos:**
```
Authorization: Bearer <token>
```

---

## 2. Categorías

### `POST /api/v1/categories` (Protegido)
Crear categoría.

**Body:**
```json
{
  "name": "Bebidas",
  "description": "Bebidas calientes y frías"
}
```

### `GET /api/v1/categories`
Listar todas (público).

### `GET /api/v1/categories/{id}`
Obtener categoría por ID (público).

### `PUT /api/v1/categories/{id}` (Protegido)
Actualizar categoría. Mismo body que `POST`.

### `DELETE /api/v1/categories/{id}` (Protegido)
Eliminar categoría.

---

## 3. Productos

### `POST /api/v1/products` (Protegido)
Crear producto.

**Body:**
```json
{
  "name": "Café Americano",
  "description": "Café negro fuerte",
  "price": 2.50,
  "stockQuantity": 100,
  "categoryId": 1
}
```

### `GET /api/v1/products`
Listar todos (público).

### `GET /api/v1/products/{id}`
Obtener producto por ID (público).

### `GET /api/v1/products/category/{categoryId}`
Productos por categoría (público).

### `GET /api/v1/products/status/{status}`
Productos por estado (público).

**Estados posibles:** `ACTIVE`, `INACTIVE`, `DISCONTINUED`

### `GET /api/v1/products/search?name=cafe`
Buscar por nombre (público).

### `PUT /api/v1/products/{id}` (Protegido)
Actualizar producto.

**Body:**
```json
{
  "name": "Café Americano Grande",
  "description": "Tamaño grande",
  "price": 3.50,
  "stockQuantity": 150,
  "categoryId": 1,
  "status": "ACTIVE"
}
```

### `DELETE /api/v1/products/{id}` (Protegido)
Eliminar producto.

---

## 4. Movimientos de Stock

### `POST /api/v1/stock-movements` (Protegido)
Registrar movimiento.

**Body:**
```json
{
  "productId": 1,
  "quantity": 10,
  "type": "IN",
  "reason": "PURCHASE",
  "notes": "Compra semanal"
}
```

**Tipos de movimiento (`type`):**
- `IN` = Entrada
- `OUT` = Salida

**Razones (`reason`):**
- `PURCHASE` = Compra
- `SALE` = Venta
- `RETURN` = Devolución
- `ADJUSTMENT` = Ajuste
- `EXPIRATION` = Vencimiento
- `DAMAGE` = Daño

### `GET /api/v1/stock-movements`
Listar todos los movimientos (protegido).

### `GET /api/v1/stock-movements/product/{productId}`
Movimientos de un producto específico (protegido).

---

## Seguridad: Qué requiere token y qué no

| Endpoint | Métodos | ¿Requiere JWT? |
|----------|---------|----------------|
| `/api/v1/auth/**` | Todos | No |
| `/api/v1/categories/**` | Solo `GET` | No |
| `/api/v1/categories/**` | `POST`, `PUT`, `DELETE` | Sí |
| `/api/v1/products/**` | Solo `GET` | No |
| `/api/v1/products/**` | `POST`, `PUT`, `DELETE` | Sí |
| `/api/v1/stock-movements/**` | Todos | Sí |

---

## Cómo probar los endpoints desde el navegador

### Endpoints GET públicos (barra de direcciones)
Los endpoints `GET` que no necesitan token los puedes probar directamente escribiendo la URL en el navegador:

```
http://localhost:8080/api/v1/categories
http://localhost:8080/api/v1/categories/1
http://localhost:8080/api/v1/products
http://localhost:8080/api/v1/products/1
http://localhost:8080/api/v1/products/category/1
http://localhost:8080/api/v1/products/status/ACTIVE
http://localhost:8080/api/v1/products/search?name=cafe
```

> **Nota:** El backend debe estar corriendo. Si ves un error de CORS, abre una pestaña en `about:blank` o en tu frontend (`localhost:3000` o `5173`) y usa la consola (F12) en vez de la barra de direcciones.

### Endpoints protegidos o que usan POST/PUT/DELETE (consola F12)
Para los demás endpoints abre el navegador, presiona **F12** → pestaña **Console** y usa `fetch()` de JavaScript.

#### 1. Hacer login y guardar el token
Pega esto en la consola:

```javascript
fetch('http://localhost:8080/api/v1/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: 'dongato2026'
  })
})
.then(r => r.json())
.then(data => {
  console.log('Token:', data.token);
  window.myToken = data.token;  // guardamos el token para reutilizarlo
})
.catch(err => console.error(err));
```

#### 2. Crear una categoría (POST protegido)
Usa el token guardado:

```javascript
fetch('http://localhost:8080/api/v1/categories', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + window.myToken
  },
  body: JSON.stringify({
    name: 'Bebidas',
    description: 'Bebidas calientes y frías'
  })
})
.then(r => r.json())
.then(data => console.log(data))
.catch(err => console.error(err));
```

#### 3. Actualizar un producto (PUT protegido)
```javascript
fetch('http://localhost:8080/api/v1/products/1', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + window.myToken
  },
  body: JSON.stringify({
    name: 'Café Americano Grande',
    description: 'Tamaño grande',
    price: 3.50,
    stockQuantity: 150,
    categoryId: 1,
    status: 'ACTIVE'
  })
})
.then(r => r.json())
.then(data => console.log(data))
.catch(err => console.error(err));
```

#### 4. Registrar un movimiento de stock (POST protegido)
```javascript
fetch('http://localhost:8080/api/v1/stock-movements', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + window.myToken
  },
  body: JSON.stringify({
    productId: 1,
    quantity: 10,
    type: 'IN',
    reason: 'PURCHASE',
    notes: 'Compra semanal'
  })
})
.then(r => r.json())
.then(data => console.log(data))
.catch(err => console.error(err));
```

#### 5. Eliminar una categoría (DELETE protegido)
```javascript
fetch('http://localhost:8080/api/v1/categories/1', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer ' + window.myToken
  }
})
.then(r => {
  if (r.status === 204) console.log('Eliminado correctamente');
  else console.log('Status:', r.status);
})
.catch(err => console.error(err));
```

---

## Cómo encontrar los endpoints tú mismo

Los endpoints en este proyecto se definen en los **controladores REST** de Spring Boot.

### Paso 1: Ubicar los controladores
Busca en la ruta:
```
backend/src/main/java/com/dongato/inventory/interfaces/rest/controller/
```

Los archivos que terminan en `Controller.java` son los que exponen los endpoints.

### Paso 2: Leer la clase
Cada controlador tiene:
- `@RequestMapping("/api/v1/xxx")` → define la **ruta base**
- Métodos con anotaciones como `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` → definen las **rutas específicas** y el **método HTTP**

Ejemplo:
```java
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @PostMapping           // → POST /api/v1/categories
    public ResponseEntity<...> create(...) { ... }

    @GetMapping("/{id}")   // → GET /api/v1/categories/{id}
    public ResponseEntity<...> findById(...) { ... }
}
```

### Paso 3: Ver los DTOs (qué enviar en el body)
Los métodos suelen recibir objetos con `@RequestBody`. Para saber qué campos enviar, busca las clases `DTO` en:
```
backend/src/main/java/com/dongato/inventory/interfaces/rest/dto/
```

Ejemplo: si el método recibe `CategoryCreateDTO`, abre ese archivo y verás los campos requeridos.

### Paso 4: Ver la seguridad
Abre el archivo:
```
backend/src/main/java/com/dongato/inventory/infrastructure/security/SecurityConfig.java
```

Ahí verás qué rutas están públicas y cuáles requieren autenticación, por ejemplo:
```java
.requestMatchers("/api/v1/auth/**").permitAll()
.requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
.anyRequest().authenticated()
```

### Paso 5: Ver el puerto del servidor
Abre:
```
backend/src/main/resources/application.properties
```

Busca:
```properties
server.port=8080
```

Ese es el puerto donde corre la aplicación.

---

## Resumen rápido de rutas de código

| Qué buscas | Dónde está |
|------------|-----------|
| Endpoints (rutas HTTP) | `interfaces/rest/controller/*Controller.java` |
| Qué enviar en el body | `interfaces/rest/dto/*DTO.java` |
| Seguridad (qué requiere token) | `infrastructure/security/SecurityConfig.java` |
| Puerto del servidor | `resources/application.properties` → `server.port` |
| Lógica de negocio | `application/usecases/*UseCase.java` |
| Modelos de dominio | `domain/model/*.java` |

---

> Última actualización: 2026-05-12
