USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE VerificarAllFacturasPorPagina
    @NumeroPagina INT, -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT -- Variable de salida para indicar el resultado
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- N�mero de p�gina * 5 elementos por p�gina

        -- Verificar si hay facturas en la p�gina solicitada
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
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar las facturas.', 16, 1);
    END CATCH
END;
GO