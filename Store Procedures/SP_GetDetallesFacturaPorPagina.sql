USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetDetallesFacturaPorPagina
    @FacturaID INT,
    @NumeroPagina INT -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- N�mero de p�gina * 5 elementos por p�gina

        -- Seleccionar los detalles de la factura con paginaci�n
        SELECT LF.LineaFacturaID, LF.ProductoID, LF.ProductoNombre,
               P.Descripcion, LF.Cantidad,
               LF.PrecioOriginal, LF.DescuentoAplicado, LF.LineaTotal,
               P.Imagen
        FROM LineasFactura LF
        LEFT JOIN Productos P ON LF.ProductoID = P.ProductoID
        WHERE LF.FacturaID = @FacturaID
        ORDER BY LF.LineaFacturaID ASC
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 5 ROWS ONLY; -- Obtener los pr�ximos 5 elementos
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los detalles de la factura.', 16, 1);
    END CATCH
END;
GO


EXEC GetDetallesFacturaPorPagina 1, 0