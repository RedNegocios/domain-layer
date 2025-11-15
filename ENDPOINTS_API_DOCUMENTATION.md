# 📚 **RED NEGOCIOS - API ENDPOINTS DOCUMENTATION**

**Versión:** 3.4.0  
**Base URL:** `http://localhost:8080/api`  
**Autenticación:** Bearer Token (JWT)  
**Fecha:** Noviembre 2025

---

## 🔐 **AUTENTICACIÓN**

### Login
```http
POST /api/login
Content-Type: application/json

{
  "username": "rodrigo",
  "password": "rodrigo"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "usuario": {
    "usuarioId": 1,
    "username": "rodrigo",
    "email": "rodrigo@test.com",
    "autoridades": ["ADMIN"]
  }
}
```

### Registro
```http
POST /api/registro
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "email": "usuario@email.com",
  "password": "password123"
}
```

---

## 📊 **ANALYTICS & KPI ENDPOINTS**

**Base URL:** `/api/kpi`  
**Autenticación:** Requerida  
**Permisos:** USER, ADMIN, ADMIN_NEGOCIO

### 1. Ventas y Tendencias
```http
POST /api/kpi/ventas-tendencia
Authorization: Bearer {token}
Content-Type: application/json

{
  "negocioIds": [1, 2, 3],
  "fechaInicio": "2024-01-01",
  "fechaFin": "2024-12-31",
  "periodo": "MENSUAL"
}

Response:
{
  "periodos": ["2024-01", "2024-02", "2024-03"],
  "ventasTotales": [15000.50, 18500.75, 22000.00],
  "cantidadOrdenes": [45, 52, 68],
  "ticketPromedio": [333.34, 355.78, 323.53],
  "crecimientoVentas": [0.0, 23.33, 18.92],
  "crecimientoOrdenes": [0.0, 15.56, 30.77]
}
```

### 2. Performance de Categorías
```http
POST /api/kpi/categorias-performance
Authorization: Bearer {token}
Content-Type: application/json

{
  "negocioIds": [1, 2],
  "fechaInicio": "2024-01-01",
  "fechaFin": "2024-12-31"
}

Response:
{
  "categorias": [
    {
      "categoriaId": 1,
      "nombreCategoria": "Electrónicos",
      "ventasTotales": 50000.00,
      "cantidadVendida": 150,
      "margenPromedio": 25.5,
      "participacionVentas": 35.2,
      "crecimientoVentas": 15.8
    }
  ]
}
```

### 3. Embudo de Conversión
```http
POST /api/kpi/embudo-conversion
Authorization: Bearer {token}
Content-Type: application/json

{
  "negocioIds": [1, 2],
  "fechaInicio": "2024-01-01",
  "fechaFin": "2024-12-31"
}

Response:
{
  "visitantes": 5000,
  "interesados": 2500,
  "ordenesCreadas": 1200,
  "ordenesCompletadas": 1000,
  "tasaInteres": 50.0,
  "tasaConversion": 48.0,
  "tasaCompletacion": 83.33
}
```

### 4. KPIs Comparativos
```http
POST /api/kpi/comparativo
Authorization: Bearer {token}
Content-Type: application/json

{
  "negocioIds": [1, 2],
  "fechaInicio": "2024-01-01",
  "fechaFin": "2024-12-31",
  "fechaComparacionInicio": "2023-01-01",
  "fechaComparacionFin": "2023-12-31"
}

Response:
{
  "periodoActual": {
    "ventasTotales": 120000.00,
    "cantidadOrdenes": 450,
    "clientesUnicos": 180,
    "ticketPromedio": 266.67
  },
  "periodoAnterior": {
    "ventasTotales": 100000.00,
    "cantidadOrdenes": 380,
    "clientesUnicos": 150,
    "ticketPromedio": 263.16
  },
  "variaciones": {
    "ventasTotales": 20.0,
    "cantidadOrdenes": 18.42,
    "clientesUnicos": 20.0,
    "ticketPromedio": 1.33
  }
}
```

---

## 🏷️ **CATÁLOGO DE ESTADOS**

**Base URL:** `/api/catalogo-estados`  
**Autenticación:** Requerida

### 1. CRUD Básico

#### Obtener todos los estados
```http
GET /api/catalogo-estados
Authorization: Bearer {token}
Permisos: ADMIN, ADMIN_NEGOCIO

Response:
[
  {
    "catalogoEstadoId": 1,
    "entidad": "Usuario",
    "estado": "Activo",
    "descripcion": "Usuario completamente funcional",
    "color": "#007BFF",
    "icono": "fa-user",
    "orden": 3,
    "esEstadoInicial": false,
    "esEstadoFinal": false,
    "permiteTransicionA": "Suspendido,Inactivo",
    "activo": true,
    "fechaCreacion": "2024-11-15T10:00:00"
  }
]
```

#### Obtener estado por ID
```http
GET /api/catalogo-estados/{id}
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO
```

#### Crear nuevo estado
```http
POST /api/catalogo-estados
Authorization: Bearer {token}
Permisos: ADMIN
Content-Type: application/json

{
  "entidad": "Producto",
  "estado": "Disponible",
  "descripcion": "Producto disponible para venta",
  "color": "#28A745",
  "icono": "fa-check-circle",
  "orden": 1,
  "esEstadoInicial": true,
  "esEstadoFinal": false,
  "permiteTransicionA": "Agotado,Descontinuado"
}
```

#### Actualizar estado
```http
PUT /api/catalogo-estados/{id}
Authorization: Bearer {token}
Permisos: ADMIN
Content-Type: application/json

{
  "entidad": "Producto",
  "estado": "Disponible",
  "descripcion": "Producto disponible para venta actualizado",
  "color": "#28A745",
  "icono": "fa-check-circle",
  "orden": 1
}
```

#### Eliminar estado (soft delete)
```http
DELETE /api/catalogo-estados/{id}
Authorization: Bearer {token}
Permisos: ADMIN
```

### 2. Consultas por Entidad

#### Obtener estados de una entidad
```http
GET /api/catalogo-estados/entidad/{entidad}
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO

Ejemplo: GET /api/catalogo-estados/entidad/Usuario
Ejemplo: GET /api/catalogo-estados/entidad/Orden
```

#### Obtener estados paginados
```http
GET /api/catalogo-estados/entidad/{entidad}/paginado?page=0&size=10
Authorization: Bearer {token}
Permisos: ADMIN, ADMIN_NEGOCIO
```

#### Obtener estado específico
```http
GET /api/catalogo-estados/entidad/{entidad}/estado/{estado}
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO

Ejemplo: GET /api/catalogo-estados/entidad/Usuario/estado/Activo
```

#### Obtener todas las entidades
```http
GET /api/catalogo-estados/entidades
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO

Response: ["Usuario", "Negocio", "Orden", "UsuarioNegocio", "NegocioNegocio"]
```

### 3. Flujo de Estados

#### Obtener estado inicial
```http
GET /api/catalogo-estados/entidad/{entidad}/inicial
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO
```

#### Obtener estados finales
```http
GET /api/catalogo-estados/entidad/{entidad}/finales
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO
```

#### Obtener estados que permiten transición
```http
GET /api/catalogo-estados/entidad/{entidad}/transiciones-hacia/{estadoDestino}
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO
```

#### Validar transición
```http
POST /api/catalogo-estados/validar-transicion
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO
Content-Type: application/json

{
  "entidad": "Orden",
  "estadoOrigen": "Pendiente",
  "estadoDestino": "Confirmada"
}

Response:
{
  "transicionValida": true
}
```

### 4. Búsqueda y Configuración

#### Buscar por descripción
```http
GET /api/catalogo-estados/entidad/{entidad}/buscar?descripcion=activo
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO
```

#### Verificar si existe estado
```http
GET /api/catalogo-estados/entidad/{entidad}/existe/{estado}
Authorization: Bearer {token}
Permisos: USER, ADMIN, ADMIN_NEGOCIO

Response: { "existe": true }
```

#### Establecer como estado inicial
```http
PUT /api/catalogo-estados/{id}/establecer-inicial
Authorization: Bearer {token}
Permisos: ADMIN
```

#### Toggle estado final
```http
PUT /api/catalogo-estados/{id}/toggle-final
Authorization: Bearer {token}
Permisos: ADMIN
```

### 5. Inicialización del Sistema

#### Crear estados base para una entidad
```http
POST /api/catalogo-estados/crear-estados-base/{entidad}
Authorization: Bearer {token}
Permisos: ADMIN

Entidades soportadas: Usuario, Negocio, Orden, UsuarioNegocio, NegocioNegocio
```

#### Inicializar todo el sistema
```http
POST /api/catalogo-estados/inicializar-sistema
Authorization: Bearer {token}
Permisos: ADMIN

Response:
{
  "mensaje": "Inicialización completada",
  "detalles": "Usuario: ✓ Negocio: ✓ Orden: ✓ UsuarioNegocio: ✓ NegocioNegocio: ✓"
}
```

---

## 🏪 **CATEGORÍAS**

**Base URL:** `/api/categorias`  
**Autenticación:** Requerida

### CRUD Completo
```http
# Listar todas (paginado)
GET /api/categorias?page=0&size=10&sort=nombre,asc

# Obtener por ID
GET /api/categorias/{id}

# Crear nueva
POST /api/categorias
{
  "nombre": "Tecnología",
  "descripcion": "Productos tecnológicos"
}

# Actualizar
PUT /api/categorias/{id}
{
  "nombre": "Tecnología Actualizada",
  "descripcion": "Productos tecnológicos modernos"
}

# Eliminar (soft delete)
DELETE /api/categorias/{id}

# Activar/Desactivar
PUT /api/categorias/{id}/toggle-activo

# Buscar por nombre
GET /api/categorias/buscar?nombre=tecno
```

---

## 🏢 **NEGOCIOS**

**Base URL:** `/api/negocios`

### CRUD Completo
```http
# Listar todos
GET /api/negocios

# Obtener por ID
GET /api/negocios/{id}

# Crear nuevo
POST /api/negocios
{
  "nombre": "Mi Negocio",
  "descripcion": "Descripción del negocio"
}

# Actualizar
PUT /api/negocios/{id}

# Eliminar
DELETE /api/negocios/{id}
```

---

## 📦 **PRODUCTOS**

**Base URL:** `/api/productos`

### CRUD Completo
```http
# Listar todos
GET /api/productos

# Obtener por ID
GET /api/productos/{id}

# Crear nuevo
POST /api/productos
{
  "nombre": "Producto Test",
  "descripcion": "Descripción del producto",
  "precio": 99.99,
  "categoriaId": 1
}

# Actualizar
PUT /api/productos/{id}

# Eliminar
DELETE /api/productos/{id}
```

---

## 🛍️ **ÓRDENES**

**Base URL:** `/api/ordenes`

### CRUD Completo
```http
# Listar todas
GET /api/ordenes

# Obtener por ID
GET /api/ordenes/{id}

# Crear nueva orden
POST /api/ordenes
{
  "negocioId": 1,
  "usuarioId": 1,
  "numeroOrden": "ORD-001",
  "montoTotal": 299.99,
  "estado": "Pendiente",
  "lineasOrden": [
    {
      "negocioProductoId": 1,
      "cantidad": 2,
      "precioUnitario": 149.99
    }
  ]
}

# Actualizar
PUT /api/ordenes/{id}

# Eliminar
DELETE /api/ordenes/{id}
```

---

## 🔗 **TIPOS DE RELACIÓN**

**Base URL:** `/api/tipos-relacion`

### CRUD Completo
```http
# Listar todos
GET /api/tipos-relacion

# Obtener por ID
GET /api/tipos-relacion/{id}

# Crear nuevo
POST /api/tipos-relacion
{
  "descripcion": "Proveedor"
}

# Actualizar
PUT /api/tipos-relacion/{id}

# Eliminar
DELETE /api/tipos-relacion/{id}
```

---

## 📋 **ESTADOS PREDEFINIDOS POR ENTIDAD**

### **Usuario**
- `Registrado` (inicial) → `Verificado,Inactivo`
- `Verificado` → `Activo,Suspendido,Inactivo`  
- `Activo` → `Suspendido,Inactivo`
- `Suspendido` → `Activo,Inactivo`
- `Inactivo` (final)

### **Negocio**  
- `Pendiente` (inicial) → `Aprobado,Rechazado`
- `Aprobado` → `Activo,Suspendido`
- `Activo` → `Suspendido,Inactivo`
- `Suspendido` → `Activo,Inactivo`
- `Rechazado` (final)
- `Inactivo` (final)

### **Orden**
- `Pendiente` (inicial) → `Confirmada,Cancelada`
- `Confirmada` → `Procesando,Cancelada`
- `Procesando` → `Enviada,Lista,Cancelada`
- `Lista` → `Enviada,Entregada`
- `Enviada` → `Entregada,Devuelta`
- `Entregada` (final)
- `Cancelada` (final)
- `Devuelta` (final)

### **UsuarioNegocio**
- `Solicitado` (inicial) → `Aprobado,Rechazado`
- `Aprobado` → `Activo,Suspendido`
- `Activo` → `Suspendido,Inactivo`
- `Suspendido` → `Activo,Inactivo`
- `Rechazado` (final)
- `Inactivo` (final)

### **NegocioNegocio**
- `Propuesta` (inicial) → `Negociacion,Aprobada,Rechazada`
- `Negociacion` → `Aprobada,Rechazada,Pausada`
- `Aprobada` → `Activa,Pausada`
- `Activa` → `Pausada,Terminada`
- `Pausada` → `Activa,Terminada`
- `Rechazada` (final)
- `Terminada` (final)

---

## 🔐 **AUTORIZACIÓN Y PERMISOS**

### **Roles Disponibles:**
- `USER`: Usuario básico
- `ADMIN`: Administrador del sistema
- `ADMIN_NEGOCIO`: Administrador de negocio

### **Niveles de Acceso:**
- **Público**: Sin autenticación
- **Usuario**: Requiere token JWT
- **Admin Negocio**: Requiere rol ADMIN_NEGOCIO o ADMIN
- **Admin Sistema**: Requiere rol ADMIN únicamente

---

## 🚨 **CÓDIGOS DE RESPUESTA**

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Eliminación exitosa
- `400 Bad Request`: Datos inválidos
- `401 Unauthorized`: Token inválido o faltante
- `403 Forbidden`: Sin permisos suficientes
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

---

## 💡 **EJEMPLOS DE USO FRONTEND**

### **Inicialización de la App**
```javascript
// 1. Inicializar sistema de estados al primer deploy
POST /api/catalogo-estados/inicializar-sistema

// 2. Obtener estados disponibles para cada entidad
GET /api/catalogo-estados/entidad/Usuario
GET /api/catalogo-estados/entidad/Orden
```

### **Dashboard Analytics**
```javascript
// Obtener datos para dashboard
const analyticsData = await Promise.all([
  fetch('/api/kpi/ventas-tendencia', { method: 'POST', body: requestData }),
  fetch('/api/kpi/categorias-performance', { method: 'POST', body: requestData }),
  fetch('/api/kpi/embudo-conversion', { method: 'POST', body: requestData })
]);
```

### **Gestión de Estados**
```javascript
// Validar si se puede cambiar estado de una orden
const validacion = await fetch('/api/catalogo-estados/validar-transicion', {
  method: 'POST',
  body: JSON.stringify({
    entidad: 'Orden',
    estadoOrigen: 'Pendiente', 
    estadoDestino: 'Confirmada'
  })
});

if (validacion.transicionValida) {
  // Proceder con el cambio de estado
}
```

---

## 📞 **CONTACTO Y SOPORTE**

**Desarrollador:** GitHub Copilot  
**Fecha Documento:** Noviembre 2025  
**Versión API:** 3.4.0

---

*Esta documentación cubre todos los endpoints implementados hasta la fecha. Para actualizaciones y nuevas funcionalidades, consultar el código fuente o contactar al equipo de desarrollo.*