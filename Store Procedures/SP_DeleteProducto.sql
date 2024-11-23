USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE EliminarProducto
    @ProductoID INT,
    @Resultado INT OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Intentar borrar el producto
        DELETE FROM Productos
        WHERE ProductoID = @ProductoID;

        -- Verificar si se eliminó alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            SET @Resultado = -1; -- No se eliminó porque hay dependencias activas
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Si se eliminó correctamente
        SET @Resultado = 0; -- Operación exitosa
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar excepciones
        IF ERROR_NUMBER() = 547 -- Código de error para violación de restricción de clave foránea
        BEGIN
            SET @Resultado = -1; -- Violación de FK (asumimos dependencias activas)
        END
        ELSE
        BEGIN
            SET @Resultado = -2; -- Error genérico
        END

        -- Hacer rollback
        ROLLBACK TRANSACTION;
    END CATCH
END;
GO


SELECT DISTINCT P.Nombre
FROM LineasFactura LF
INNER JOIN Productos P ON LF.ProductoID = P.ProductoID

SELECT *
FROM Productos

SELECT OBJECT_NAME(parent_object_id)
FROM sys.foreign_keys
WHERE name = 'FK_LineasFactura_Productos';