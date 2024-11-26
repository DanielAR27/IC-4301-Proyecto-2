USE DB_Proyecto;
GO

CREATE PROCEDURE DeleteReview
    @ReviewID INT,         -- ID de la reseña a eliminar
    @Resultado INT OUTPUT  -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacción

        -- Verificar si la reseña existe
        IF NOT EXISTS (SELECT 1 FROM Reviews WHERE ReviewID = @ReviewID)
        BEGIN
            SET @Resultado = -1; -- Código de error para reseña no encontrada
            ROLLBACK TRANSACTION; -- Revertir la transacción
            RETURN;
        END

        -- Eliminar la reseña
        DELETE FROM Reviews
        WHERE ReviewID = @ReviewID;

        -- Verificar si la eliminación afectó alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            SET @Resultado = 0; -- Indica éxito en la eliminación
            COMMIT TRANSACTION; -- Confirmar la transacción
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Precaución por si no afectó filas
            ROLLBACK TRANSACTION; -- Revertir la transacción
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacción en caso de error

        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Error al intentar eliminar la reseña. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
