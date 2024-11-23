USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE VerificarAllFacturasPorPagina
    @NumeroPagina INT, -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT -- Variable de salida para indicar el resultado
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- Número de página * 5 elementos por página

        -- Verificar si hay facturas en la página solicitada
        IF EXISTS (
            SELECT 1
            FROM Facturas
            ORDER BY FechaFactura DESC
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 5 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay al menos una factura
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron facturas
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar las facturas.', 16, 1);
    END CATCH
END;
GO