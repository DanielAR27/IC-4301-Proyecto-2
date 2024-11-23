USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE GetAllFacturasPorPagina
    @NumeroPagina INT -- Parámetro para la paginación
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- Número de página * 5 elementos por página

        -- Seleccionar las 5 facturas correspondientes a la página
        SELECT 
            FacturaID,
            FechaFactura,
            Total,
            CostoEnvio,
			Estado
        FROM 
            Facturas
        ORDER BY 
            FacturaID ASC
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 5 ROWS ONLY; -- Obtener los próximos 5 elementos

    END TRY
    BEGIN CATCH
        -- Manejar errores que puedan ocurrir
        RAISERROR('Ocurrió un error al obtener las facturas del usuario.', 16, 1);
    END CATCH
END;
GO

EXEC GetAllFacturasPorPagina 0