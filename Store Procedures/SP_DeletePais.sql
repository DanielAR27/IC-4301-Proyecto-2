USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeletePais
    @PaisID INT,           -- ID del pa�s a eliminar
    @Resultado INT OUTPUT  -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Intentar borrar el pa�s
        DELETE FROM Paises
        WHERE PaisID = @PaisID;

        -- Verificar si se elimin� alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            SET @Resultado = -1; -- Pa�s no encontrado o dependencias activas
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Si se elimin� correctamente
        SET @Resultado = 0; -- Operaci�n exitosa
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar excepciones
        IF ERROR_NUMBER() = 547 -- C�digo de error para violaci�n de restricci�n de clave for�nea
        BEGIN
            SET @Resultado = -1; -- Violaci�n de FK (dependencias activas)
        END
        ELSE
        BEGIN
            SET @Resultado = -2; -- Error gen�rico
        END

        -- Hacer rollback
        ROLLBACK TRANSACTION;
    END CATCH
END;
GO
