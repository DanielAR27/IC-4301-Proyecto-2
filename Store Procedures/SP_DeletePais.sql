USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeletePais
    @PaisID INT,           -- ID del país a eliminar
    @Resultado INT OUTPUT  -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Intentar borrar el país
        DELETE FROM Paises
        WHERE PaisID = @PaisID;

        -- Verificar si se eliminó alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            SET @Resultado = -1; -- País no encontrado o dependencias activas
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
            SET @Resultado = -1; -- Violación de FK (dependencias activas)
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
