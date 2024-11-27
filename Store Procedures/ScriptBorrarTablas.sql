USE DB_Proyecto; -- Cambia a la base de datos deseada

-- Deshabilitar comprobación de restricciones
EXEC sp_msforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT ALL";

-- Eliminar restricciones de clave externa
DECLARE @sql NVARCHAR(MAX);

-- Generar las instrucciones para eliminar claves externas
SELECT @sql = STRING_AGG('ALTER TABLE ' + QUOTENAME(OBJECT_SCHEMA_NAME(parent_object_id)) + '.' + QUOTENAME(OBJECT_NAME(parent_object_id)) + ' DROP CONSTRAINT ' + QUOTENAME(name), '; ')
FROM sys.foreign_keys;

-- Ejecutar las instrucciones para eliminar claves externas
IF @sql IS NOT NULL
    EXEC sp_executesql @sql;

-- Eliminar todas las tablas
EXEC sp_msforeachtable "DROP TABLE ?";

-- Verificar que no queden tablas
SELECT * FROM sys.tables;

-- Confirmación
PRINT 'Todas las tablas han sido eliminadas correctamente.';
