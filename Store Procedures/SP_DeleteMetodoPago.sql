USE DB_Proyecto;
GO

CREATE PROCEDURE DeleteMetodoPago
    @MetodoPagoID INT,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacci�n

        -- Verificar si el m�todo de pago existe
        IF NOT EXISTS (SELECT 1 FROM MetodoPago WHERE MetodoPagoID = @MetodoPagoID)
        BEGIN
            SET @Resultado = -1; -- C�digo de error para m�todo de pago no encontrado
            ROLLBACK TRANSACTION; -- Revertir la transacci�n
            RETURN;
        END

        -- Eliminar el m�todo de pago
        DELETE FROM MetodoPago
        WHERE MetodoPagoID = @MetodoPagoID;

        -- Verificar si la eliminaci�n afect� alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            SET @Resultado = 0; -- Indica �xito en la eliminaci�n
            COMMIT TRANSACTION; -- Confirmar la transacci�n
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Indica que no se encontr� el m�todo de pago (por precauci�n)
            ROLLBACK TRANSACTION; -- Revertir la transacci�n
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacci�n en caso de error

        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Error al intentar eliminar el m�todo de pago. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
