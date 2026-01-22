# Instrucciones para implementar y probar el endpoint de Colecciones

## Paso 1: Ejecutar los scripts SQL

### 1.1 Crear la tabla Colecciones
Ejecuta el siguiente script en tu SQL Server:
```sql
-- Archivo: /data-base-layer/AgregarTablaColecciones.sql
```

### 1.2 Insertar datos de ejemplo
Ejecuta el siguiente script para llenar la tabla con datos de ejemplo:
```sql
-- Archivo: /data-base-layer/InsertarDatosColecciones.sql
```

## Paso 2: Recompilar el proyecto Maven

```bash
cd /home/edreo/CoNetlng/domain-layer
./mvnw clean compile
```

O si prefieres con Maven instalado globalmente:
```bash
mvn clean compile
```

## Paso 3: Iniciar la aplicación

```bash
cd /home/edreo/CoNetlng/domain-layer
./mvnw spring-boot:run
```

La aplicación debería iniciarse en `http://localhost:8080` (o el puerto configurado en application.properties)

## Paso 4: Probar el endpoint

### 4.1 Obtener todas las colecciones (GET)
```bash
curl -X GET http://localhost:8080/api/colecciones
```

Respuesta esperada (JSON):
```json
[
  {
    "coleccionId": 1,
    "producto": {
      "productoId": 1,
      "nombre": "Colección 1984",
      ...
    },
    "titulo": "1984",
    "autor": "George Orwell",
    "descripcion": "Una novela sobre un régimen totalitario distópico",
    "isbn": "978-0451524935",
    "año": 1949,
    "editorial": "Penguin Books",
    "imagenUrl": null,
    "tipoColeccion": "Novela",
    "fechaCreacion": "2026-01-21T...",
    "activo": true
  },
  ...
]
```

### 4.2 Obtener una colección por ID (GET)
```bash
curl -X GET http://localhost:8080/api/colecciones/1
```

### 4.3 Crear una nueva colección (POST)
```bash
curl -X POST http://localhost:8080/api/colecciones \
  -H "Content-Type: application/json" \
  -d '{
    "producto": {
      "productoId": 1
    },
    "titulo": "Fundación",
    "autor": "Isaac Asimov",
    "descripcion": "Primera novela de la serie Fundación",
    "isbn": "978-0553293357",
    "año": 1951,
    "editorial": "Gnome Press",
    "tipoColeccion": "Novela"
  }'
```

### 4.4 Actualizar una colección (PUT)
```bash
curl -X PUT http://localhost:8080/api/colecciones/1 \
  -H "Content-Type: application/json" \
  -d '{
    "producto": {
      "productoId": 1
    },
    "titulo": "1984 - Edición Revisada",
    "autor": "George Orwell",
    "descripcion": "Una novela sobre un régimen totalitario distópico - Edición 2024",
    "año": 1949,
    "editorial": "Penguin Books",
    "tipoColeccion": "Novela"
  }'
```

### 4.5 Eliminar una colección (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/colecciones/1
```

## Archivos creados

- `Modelos/Coleccion.java` - Entidad JPA que mapea la tabla Colecciones
- `Repositorios/ColeccionRepositorio.java` - Interfaz JPA para acceso a datos
- `Controladores/ColeccionControlador.java` - Controlador REST con endpoint público
- `/data-base-layer/AgregarTablaColecciones.sql` - Script para crear la tabla
- `/data-base-layer/InsertarDatosColecciones.sql` - Script con datos de ejemplo

## Notas importantes

1. El endpoint NO requiere autenticación (sin `@PreAuthorize`)
2. La tabla `Colecciones` tiene una relación de clave foránea con `Producto`
3. Todos los campos de libro están integrados directamente en la tabla
4. Se utiliza eliminación lógica (activo = 0) en lugar de eliminación física
5. Los tipos de colección pueden ser: "Enciclopedia", "Novela", "Libro Técnico" u otros

## Tipos de colección soportados

- **Enciclopedia**: Para obras de referencia
- **Novela**: Para novelas literarias y de ficción
- **Libro Técnico**: Para libros de programación, ciencia, etc.
- Cualquier otro tipo personalizado
