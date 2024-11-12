USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarDetallesFacturaPorPagina
    @FacturaID INT,
    @NumeroPagina INT,
    @Resultado INT OUTPUT -- Variable de salida para indicar si hay detalles
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- Número de página * 5 elementos por página

        -- Verificar si hay detalles de la factura en la página solicitada
        IF EXISTS (
            SELECT 1
            FROM LineasFactura LF
            WHERE LF.FacturaID = @FacturaID
            ORDER BY LF.LineaFacturaID ASC
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 5 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay detalles disponibles
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron detalles
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar los detalles de la factura.', 16, 1);
    END CATCH
END;
GO

BEGIN
    DECLARE @Resultado AS INT;
    EXEC VerificarDetallesFacturaPorPagina 1002, 0, @Resultado;

    IF @Resultado IS NOT NULL
    BEGIN
        PRINT 'Resultado es ' + CAST(@Resultado AS VARCHAR(10));
    END
    ELSE
    BEGIN
        PRINT 'No se pudo obtener un resultado.';
    END
END;