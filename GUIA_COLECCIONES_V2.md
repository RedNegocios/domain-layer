# Guía: Endpoint de Colecciones y Productos (Libros)

## Estructura de Base de Datos (Nuevo Esquema)

### Tablas

#### 1. **Colecciones** (Géneros/Categorías Semánticas)
Contiene los géneros/tipos de libros.

```sql
CREATE TABLE Colecciones (
    coleccionId INT PRIMARY KEY IDENTITY(1,1),
    nombre NVARCHAR(255) NOT NULL,           -- Ej: Novela, Enciclopedia, Libro Técnico
    descripcion NVARCHAR(MAX),               -- Descripción del género
    icono NVARCHAR(50),                      -- Icono para UI (ej: 'book', 'code', 'wand')
    fechaCreacion DATETIME DEFAULT GETDATE() NOT NULL,
    modificadoPor NVARCHAR(255),
    fechaModificacion DATETIME DEFAULT GETDATE(),
    activo BIT DEFAULT 1,
    eliminadoPor NVARCHAR(255),
    fechaEliminacion DATETIME
);
```

**Géneros iniciales:**
- Novela
- Enciclopedia
- Libro Técnico
- Realismo Mágico
- Novela Clásica
- Ciencia Ficción

#### 2. **Producto** (Libros)
Contiene los libros individuales, cada uno pertenece a una colección (género).

```sql
CREATE TABLE Producto (
    productoId INT PRIMARY KEY IDENTITY(1,1),
    -- Datos del libro
    titulo NVARCHAR(255),
    autor NVARCHAR(255),
    isbn NVARCHAR(20),
    año INT,
    editorial NVARCHAR(255),
    descripcion NVARCHAR(MAX),
    imagenUrl NVARCHAR(MAX),
    precio DECIMAL(10, 2),
    -- Relación con Colecciones
    coleccionId INT NOT NULL,
    FOREIGN KEY (coleccionId) REFERENCES Colecciones(coleccionId),
    -- Metadatos
    fechaCreacion DATETIME DEFAULT GETDATE() NOT NULL,
    modificadoPor NVARCHAR(255),
    fechaModificacion DATETIME DEFAULT GETDATE(),
    activo BIT DEFAULT 1,
    eliminadoPor NVARCHAR(255),
    fechaEliminacion DATETIME,
    categoriaId INT
);
```

### Relación

```
Colecciones (1) ──────┐
                      │ 1:N
                      │
                   Producto (N)
                   
Ejemplo:
- Colección: "Novela"
  ├─ Producto: "1984" (George Orwell)
  └─ Producto: "El Quijote" (Cervantes)

- Colección: "Realismo Mágico"
  └─ Producto: "Cien Años de Soledad" (García Márquez)
```

## Endpoints

### **Colecciones (Géneros)**

#### GET `/api/colecciones`
Obtener todos los géneros disponibles (público, sin autenticación).

**Respuesta (200 OK):**
```json
[
  {
    "coleccionId": 1,
    "nombre": "Novela",
    "descripcion": "Libros de ficción narrativa",
    "icono": "book",
    "activo": true,
    "fechaCreacion": "2026-01-23T12:00:00"
  },
  {
    "coleccionId": 2,
    "nombre": "Enciclopedia",
    "descripcion": "Obras de referencia y consulta general",
    "icono": "encyclopedia",
    "activo": true,
    "fechaCreacion": "2026-01-23T12:00:00"
  }
]
```

#### GET `/api/colecciones/{id}`
Obtener un género específico.

#### POST `/api/colecciones`
Crear un nuevo género (requiere autenticación).

**Body:**
```json
{
  "nombre": "Ensayo",
  "descripcion": "Libros de reflexión y análisis",
  "icono": "pen"
}
```

#### PUT `/api/colecciones/{id}`
Actualizar un género (requiere autenticación).

#### DELETE `/api/colecciones/{id}`
Eliminar un género (soft delete, requiere autenticación).

---

### **Productos (Libros)**

#### GET `/api/productos`
Obtener todos los libros (público, sin autenticación).

**Respuesta (200 OK):**
```json
[
  {
    "productoId": 1,
    "titulo": "1984",
    "autor": "George Orwell",
    "isbn": "978-0451524935",
    "año": 1949,
    "editorial": "Secker & Warburg",
    "descripcion": "Novela distópica sobre un régimen totalitario",
    "precio": 29.99,
    "coleccion": {
      "coleccionId": 1,
      "nombre": "Novela",
      "descripcion": "Libros de ficción narrativa",
      "icono": "book",
      "activo": true
    },
    "activo": true,
    "fechaCreacion": "2026-01-23T12:00:00"
  }
]
```

#### GET `/api/productos/{id}`
Obtener un libro específico.

#### POST `/api/productos`
Crear un nuevo libro (requiere autenticación).

**Body:**
```json
{
  "titulo": "Fundación",
  "autor": "Isaac Asimov",
  "isbn": "978-0553293357",
  "año": 1951,
  "editorial": "Gnome Press",
  "descripcion": "Novela de ciencia ficción sobre el colapso de un imperio",
  "precio": 19.99,
  "coleccion": {
    "coleccionId": 6
  }
}
```

#### PUT `/api/productos/{id}`
Actualizar un libro (requiere autenticación).

#### DELETE `/api/productos/{id}`
Eliminar un libro (soft delete, requiere autenticación).

---

## Ejemplos de Uso (cURL)

### 1. Obtener todos los géneros
```bash
curl -X GET "http://localhost:8080/negocios/api/colecciones" \
  -H "Content-Type: application/json"
```

### 2. Obtener todos los libros
```bash
curl -X GET "http://localhost:8080/negocios/api/productos" \
  -H "Content-Type: application/json"
```

### 3. Crear un nuevo género
```bash
curl -X POST "http://localhost:8080/negocios/api/colecciones" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "nombre": "Ensayo",
    "descripcion": "Escritura reflexiva y analítica",
    "icono": "pen"
  }'
```

### 4. Crear un nuevo libro
```bash
curl -X POST "http://localhost:8080/negocios/api/productos" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "titulo": "Fundación",
    "autor": "Isaac Asimov",
    "isbn": "978-0553293357",
    "año": 1951,
    "editorial": "Gnome Press",
    "descripcion": "Novela de ciencia ficción",
    "precio": 19.99,
    "coleccion": {"coleccionId": 6}
  }'
```

### 5. Obtener libros paginados
```bash
curl -X GET "http://localhost:8080/negocios/api/productos?page=0&size=10" \
  -H "Content-Type: application/json"
```

---

## Notas Técnicas

- **Soft Delete**: Los registros no se eliminan, solo se marca `activo = false` y se registra `fechaEliminacion`.
- **Paginación**: Implementada en los endpoints GET para productos y colecciones.
- **Auditoría**: Se registran `fechaCreacion`, `fechaModificacion`, `modificadoPor`, y `eliminadoPor`.
- **FK**: Producto tiene FK a Colecciones (relación 1:N obligatoria).

---

## Scripts SQL

### Migración del esquema:
```bash
sqlcmd -S localhost -U rodrigo -P 'Rodr1go#Pass' -d RedNegocios -i MigrarEsquemaColecciones.sql
```

### Insertar datos de ejemplo:
```bash
sqlcmd -S localhost -U rodrigo -P 'Rodr1go#Pass' -d RedNegocios -i InsertarLibrosEjemplo.sql
```
