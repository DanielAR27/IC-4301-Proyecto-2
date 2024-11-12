USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarDetallesFacturaPorPagina
    @FacturaID INT,
    @NumeroPagina INT,
    @Resultado INT OUTPUT -- Variable de salida para indicar si hay detalles
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- N�mero de p�gina * 5 elementos por p�gina

        -- Verificar si hay detalles de la factura en la p�gina solicitada
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
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar los detalles de la factura.', 16, 1);
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