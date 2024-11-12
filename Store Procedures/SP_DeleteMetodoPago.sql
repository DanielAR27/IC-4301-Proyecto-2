USE DB_Proyecto;
GO

CREATE PROCEDURE DeleteMetodoPago
    @MetodoPagoID INT,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacción

        -- Verificar si el método de pago existe
        IF NOT EXISTS (SELECT 1 FROM MetodoPago WHERE MetodoPagoID = @MetodoPagoID)
        BEGIN
            SET @Resultado = -1; -- Código de error para método de pago no encontrado
            ROLLBACK TRANSACTION; -- Revertir la transacción
            RETURN;
        END

        -- Eliminar el método de pago
        DELETE FROM MetodoPago
        WHERE MetodoPagoID = @MetodoPagoID;

        -- Verificar si la eliminación afectó alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            SET @Resultado = 0; -- Indica éxito en la eliminación
            COMMIT TRANSACTION; -- Confirmar la transacción
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Indica que no se encontró el método de pago (por precaución)
            ROLLBACK TRANSACTION; -- Revertir la transacción
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacción en caso de error

        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Error al intentar eliminar el método de pago. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
