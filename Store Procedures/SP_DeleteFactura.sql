USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeleteFactura
    @FacturaID INT,           -- ID de la factura a eliminar
    @Resultado INT OUTPUT      -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar que la factura exista
        IF NOT EXISTS (SELECT 1 FROM Facturas WHERE FacturaID = @FacturaID)
        BEGIN
            SET @Resultado = -1; -- Error: Factura no encontrada
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Eliminar la factura
        DELETE FROM Facturas
        WHERE FacturaID = @FacturaID;

        SET @Resultado = 0; -- Eliminación exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Código de error genérico
        RAISERROR('Ocurrió un error al intentar eliminar la factura. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
