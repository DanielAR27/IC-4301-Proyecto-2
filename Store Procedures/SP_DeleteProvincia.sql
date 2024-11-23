USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeleteProvincia
    @EstadoProvinciaID INT,  -- ID de la provincia a eliminar
    @Resultado INT OUTPUT     -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Intentar borrar la provincia
        DELETE FROM Estados_Provincias
        WHERE EstadoProvinciaID = @EstadoProvinciaID;

        -- Verificar si se eliminó alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            SET @Resultado = -1; -- Provincia no encontrada o dependencias activas
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
            SET @Resultado = -1; -- Dependencias activas
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