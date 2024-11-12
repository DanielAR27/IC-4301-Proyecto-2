USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetDetallesFacturaPorPagina
    @FacturaID INT,
    @NumeroPagina INT -- Parámetro de entrada para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- Número de página * 5 elementos por página

        -- Seleccionar los detalles de la factura con paginación
        SELECT LF.LineaFacturaID, LF.ProductoID, LF.ProductoNombre,
               P.Descripcion, LF.Cantidad,
               LF.PrecioOriginal, LF.DescuentoAplicado, LF.LineaTotal,
               P.Imagen
        FROM LineasFactura LF
        LEFT JOIN Productos P ON LF.ProductoID = P.ProductoID
        WHERE LF.FacturaID = @FacturaID
        ORDER BY LF.LineaFacturaID ASC
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 5 ROWS ONLY; -- Obtener los próximos 5 elementos
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener los detalles de la factura.', 16, 1);
    END CATCH
END;
GO


EXEC GetDetallesFacturaPorPagina 1, 0