USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeleteLineaFactura
    @LineaFacturaID INT,           -- ID de la linea factura a eliminar
    @Resultado INT OUTPUT      -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar que la factura exista
        IF NOT EXISTS (SELECT 1 FROM LineasFactura WHERE LineaFacturaID = @LineaFacturaID)
        BEGIN
            SET @Resultado = -1; -- Error: Linea Factura no encontrada
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Eliminar la factura
        DELETE FROM LineasFactura
        WHERE LineaFacturaID = @LineaFacturaID;

        SET @Resultado = 0; -- Eliminación exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Código de error genérico
        RAISERROR('Ocurrió un error al intentar eliminar la linea de la factura. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
