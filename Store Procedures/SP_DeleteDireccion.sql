USE DB_Proyecto;
GO
ALTER PROCEDURE DeleteDireccion
    @DireccionID INT,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
		BEGIN TRANSACTION; -- Iniciar la transacción
        -- Verificar si la dirección existe
        IF NOT EXISTS (SELECT 1 FROM Direcciones WHERE DireccionID = @DireccionID)
        BEGIN
            SET @Resultado = -1; -- Código de error para dirección no encontrada
            RETURN;
        END

        -- Eliminar la dirección
        DELETE FROM Direcciones
        WHERE DireccionID = @DireccionID;

        -- Verificar si la eliminación afectó alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            SET @Resultado = 0; -- Indica éxito en la eliminación
			COMMIT TRANSACTION; -- Confirmar la transacción
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Indica que no se encontró la dirección (por precaución)
			ROLLBACK TRANSACTION; -- Revertir la transacción
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacción en caso de error

        -- Manejar errores en la base de datos
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Error al intentar eliminar la dirección. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
