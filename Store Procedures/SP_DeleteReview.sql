USE DB_Proyecto;
GO

CREATE PROCEDURE DeleteReview
    @ReviewID INT,         -- ID de la rese�a a eliminar
    @Resultado INT OUTPUT  -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacci�n

        -- Verificar si la rese�a existe
        IF NOT EXISTS (SELECT 1 FROM Reviews WHERE ReviewID = @ReviewID)
        BEGIN
            SET @Resultado = -1; -- C�digo de error para rese�a no encontrada
            ROLLBACK TRANSACTION; -- Revertir la transacci�n
            RETURN;
        END

        -- Eliminar la rese�a
        DELETE FROM Reviews
        WHERE ReviewID = @ReviewID;

        -- Verificar si la eliminaci�n afect� alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            SET @Resultado = 0; -- Indica �xito en la eliminaci�n
            COMMIT TRANSACTION; -- Confirmar la transacci�n
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Precauci�n por si no afect� filas
            ROLLBACK TRANSACTION; -- Revertir la transacci�n
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacci�n en caso de error

        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Error al intentar eliminar la rese�a. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
