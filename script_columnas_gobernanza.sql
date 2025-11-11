-- Script para agregar todas las columnas de gobernanza faltantes
-- Ejecutar en SQL Server

USE RedNegocios;
GO

-- Tabla: NegocioProducto - Agregar visualizacionProducto
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioProducto' AND COLUMN_NAME = 'visualizacionProducto')
BEGIN
    ALTER TABLE NegocioProducto ADD visualizacionProducto BIT NOT NULL DEFAULT 1;
    PRINT 'Columna visualizacionProducto agregada a NegocioProducto';
END
ELSE
    PRINT 'Columna visualizacionProducto ya existe en NegocioProducto';

-- Tabla: LineaOrden - Agregar campos de gobernanza (ya fueron removidos del modelo Java)
-- No se agregan porque ya corregimos el modelo Java para que coincida con la BD

-- Tabla: Autoridad - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE Autoridad ADD creadoPor NVARCHAR(100);
    PRINT 'Columna creadoPor agregada a Autoridad';
END
ELSE
    PRINT 'Columna creadoPor ya existe en Autoridad';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'fechaCreacion')
BEGIN
    ALTER TABLE Autoridad ADD fechaCreacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaCreacion agregada a Autoridad';
END
ELSE
    PRINT 'Columna fechaCreacion ya existe en Autoridad';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'modificadoPor')
BEGIN
    ALTER TABLE Autoridad ADD modificadoPor NVARCHAR(100);
    PRINT 'Columna modificadoPor agregada a Autoridad';
END
ELSE
    PRINT 'Columna modificadoPor ya existe en Autoridad';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'fechaModificacion')
BEGIN
    ALTER TABLE Autoridad ADD fechaModificacion DATETIME;
    PRINT 'Columna fechaModificacion agregada a Autoridad';
END
ELSE
    PRINT 'Columna fechaModificacion ya existe en Autoridad';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'activo')
BEGIN
    ALTER TABLE Autoridad ADD activo BIT NOT NULL DEFAULT 1;
    PRINT 'Columna activo agregada a Autoridad';
END
ELSE
    PRINT 'Columna activo ya existe en Autoridad';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'eliminadoPor')
BEGIN
    ALTER TABLE Autoridad ADD eliminadoPor NVARCHAR(100);
    PRINT 'Columna eliminadoPor agregada a Autoridad';
END
ELSE
    PRINT 'Columna eliminadoPor ya existe en Autoridad';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Autoridad' AND COLUMN_NAME = 'fechaEliminacion')
BEGIN
    ALTER TABLE Autoridad ADD fechaEliminacion DATETIME;
    PRINT 'Columna fechaEliminacion agregada a Autoridad';
END
ELSE
    PRINT 'Columna fechaEliminacion ya existe en Autoridad';

-- Tabla: Beneficio - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE Beneficio ADD creadoPor NVARCHAR(100) NOT NULL DEFAULT 'system';
    PRINT 'Columna creadoPor agregada a Beneficio';
END
ELSE
    PRINT 'Columna creadoPor ya existe en Beneficio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'fechaCreacion')
BEGIN
    ALTER TABLE Beneficio ADD fechaCreacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaCreacion agregada a Beneficio';
END
ELSE
    PRINT 'Columna fechaCreacion ya existe en Beneficio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'modificadoPor')
BEGIN
    ALTER TABLE Beneficio ADD modificadoPor NVARCHAR(100);
    PRINT 'Columna modificadoPor agregada a Beneficio';
END
ELSE
    PRINT 'Columna modificadoPor ya existe en Beneficio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'fechaModificacion')
BEGIN
    ALTER TABLE Beneficio ADD fechaModificacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaModificacion agregada a Beneficio';
END
ELSE
    PRINT 'Columna fechaModificacion ya existe en Beneficio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'activo')
BEGIN
    ALTER TABLE Beneficio ADD activo BIT NOT NULL DEFAULT 1;
    PRINT 'Columna activo agregada a Beneficio';
END
ELSE
    PRINT 'Columna activo ya existe en Beneficio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'eliminadoPor')
BEGIN
    ALTER TABLE Beneficio ADD eliminadoPor NVARCHAR(100);
    PRINT 'Columna eliminadoPor agregada a Beneficio';
END
ELSE
    PRINT 'Columna eliminadoPor ya existe en Beneficio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Beneficio' AND COLUMN_NAME = 'fechaEliminacion')
BEGIN
    ALTER TABLE Beneficio ADD fechaEliminacion DATETIME;
    PRINT 'Columna fechaEliminacion agregada a Beneficio';
END
ELSE
    PRINT 'Columna fechaEliminacion ya existe en Beneficio';

-- Tabla: UsuarioNegocio - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE UsuarioNegocio ADD creadoPor NVARCHAR(100);
    PRINT 'Columna creadoPor agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna creadoPor ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'fechaCreacion')
BEGIN
    ALTER TABLE UsuarioNegocio ADD fechaCreacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaCreacion agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna fechaCreacion ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'modificadoPor')
BEGIN
    ALTER TABLE UsuarioNegocio ADD modificadoPor NVARCHAR(100);
    PRINT 'Columna modificadoPor agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna modificadoPor ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'fechaModificacion')
BEGIN
    ALTER TABLE UsuarioNegocio ADD fechaModificacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaModificacion agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna fechaModificacion ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'activo')
BEGIN
    ALTER TABLE UsuarioNegocio ADD activo BIT NOT NULL DEFAULT 1;
    PRINT 'Columna activo agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna activo ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'eliminadoPor')
BEGIN
    ALTER TABLE UsuarioNegocio ADD eliminadoPor NVARCHAR(100);
    PRINT 'Columna eliminadoPor agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna eliminadoPor ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'fechaEliminacion')
BEGIN
    ALTER TABLE UsuarioNegocio ADD fechaEliminacion DATETIME;
    PRINT 'Columna fechaEliminacion agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna fechaEliminacion ya existe en UsuarioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioNegocio' AND COLUMN_NAME = 'estatusId')
BEGIN
    ALTER TABLE UsuarioNegocio ADD estatusId INT;
    PRINT 'Columna estatusId agregada a UsuarioNegocio';
END
ELSE
    PRINT 'Columna estatusId ya existe en UsuarioNegocio';

-- Tabla: NegocioNegocio - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE NegocioNegocio ADD creadoPor NVARCHAR(100);
    PRINT 'Columna creadoPor agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna creadoPor ya existe en NegocioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'fechaCreacion')
BEGIN
    ALTER TABLE NegocioNegocio ADD fechaCreacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaCreacion agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna fechaCreacion ya existe en NegocioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'modificadoPor')
BEGIN
    ALTER TABLE NegocioNegocio ADD modificadoPor NVARCHAR(100);
    PRINT 'Columna modificadoPor agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna modificadoPor ya existe en NegocioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'fechaModificacion')
BEGIN
    ALTER TABLE NegocioNegocio ADD fechaModificacion DATETIME;
    PRINT 'Columna fechaModificacion agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna fechaModificacion ya existe en NegocioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'activo')
BEGIN
    ALTER TABLE NegocioNegocio ADD activo BIT NOT NULL DEFAULT 1;
    PRINT 'Columna activo agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna activo ya existe en NegocioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'eliminadoPor')
BEGIN
    ALTER TABLE NegocioNegocio ADD eliminadoPor NVARCHAR(100);
    PRINT 'Columna eliminadoPor agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna eliminadoPor ya existe en NegocioNegocio';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NegocioNegocio' AND COLUMN_NAME = 'fechaEliminacion')
BEGIN
    ALTER TABLE NegocioNegocio ADD fechaEliminacion DATETIME;
    PRINT 'Columna fechaEliminacion agregada a NegocioNegocio';
END
ELSE
    PRINT 'Columna fechaEliminacion ya existe en NegocioNegocio';

-- Tabla: Usuario - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Usuario' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE Usuario ADD creadoPor NVARCHAR(100);
    PRINT 'Columna creadoPor agregada a Usuario';
END
ELSE
    PRINT 'Columna creadoPor ya existe en Usuario';

-- Tabla: UsuarioToken - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuarioToken' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE UsuarioToken ADD creadoPor NVARCHAR(100);
    PRINT 'Columna creadoPor agregada a UsuarioToken';
END
ELSE
    PRINT 'Columna creadoPor ya existe en UsuarioToken';

-- Tabla: MensajeOrden - Verificar campos de gobernanza
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'creadoPor')
BEGIN
    ALTER TABLE MensajeOrden ADD creadoPor NVARCHAR(100);
    PRINT 'Columna creadoPor agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna creadoPor ya existe en MensajeOrden';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'fechaCreacion')
BEGIN
    ALTER TABLE MensajeOrden ADD fechaCreacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaCreacion agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna fechaCreacion ya existe en MensajeOrden';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'modificadoPor')
BEGIN
    ALTER TABLE MensajeOrden ADD modificadoPor NVARCHAR(100);
    PRINT 'Columna modificadoPor agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna modificadoPor ya existe en MensajeOrden';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'fechaModificacion')
BEGIN
    ALTER TABLE MensajeOrden ADD fechaModificacion DATETIME NOT NULL DEFAULT GETDATE();
    PRINT 'Columna fechaModificacion agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna fechaModificacion ya existe en MensajeOrden';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'activo')
BEGIN
    ALTER TABLE MensajeOrden ADD activo BIT NOT NULL DEFAULT 1;
    PRINT 'Columna activo agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna activo ya existe en MensajeOrden';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'eliminadoPor')
BEGIN
    ALTER TABLE MensajeOrden ADD eliminadoPor NVARCHAR(100);
    PRINT 'Columna eliminadoPor agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna eliminadoPor ya existe en MensajeOrden';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'MensajeOrden' AND COLUMN_NAME = 'fechaEliminacion')
BEGIN
    ALTER TABLE MensajeOrden ADD fechaEliminacion DATETIME;
    PRINT 'Columna fechaEliminacion agregada a MensajeOrden';
END
ELSE
    PRINT 'Columna fechaEliminacion ya existe en MensajeOrden';

PRINT 'Script de columnas de gobernanza completado.';