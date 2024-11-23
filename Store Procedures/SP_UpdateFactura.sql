USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdateFactura
    @FacturaID INT,             -- ID de la factura a actualizar
    @FechaFactura DATE,         -- Fecha de la factura
    @CostoEnvio DECIMAL(10, 2), -- Costo de envío
    @Estado NVARCHAR(50),       -- Estado de la factura
    @Resultado INT OUTPUT       -- Variable de salida para el resultado
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

        -- Validar que ninguno de los datos sea inválido
        IF @FechaFactura IS NULL OR
           @CostoEnvio IS NULL OR @CostoEnvio < 0 OR
           @Estado IS NULL OR LTRIM(RTRIM(@Estado)) = ''
        BEGIN
            SET @Resultado = -2; -- Error: Datos inválidos
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar la factura
        UPDATE Facturas
        SET FechaFactura = @FechaFactura,
            CostoEnvio = @CostoEnvio,
            Estado = @Estado
        WHERE FacturaID = @FacturaID;

        SET @Resultado = 0; -- Actualización exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Código de error genérico
        RAISERROR('Ocurrió un error al actualizar la factura. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
