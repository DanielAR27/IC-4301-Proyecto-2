USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetTopSugerenciasPorProductoID
    @ProductoID INT     -- ID del producto para buscar sugerencias
AS
BEGIN
    BEGIN TRY
        -- Obtener los 5 productos más frecuentes asociados a las facturas del producto dado
        SELECT TOP 5 
            P.ProductoID,
            P.Nombre,
            COUNT(LF.ProductoID) AS Cuenta
        FROM 
            Productos P
        LEFT JOIN 
            LineasFactura LF 
        ON 
            P.ProductoID = LF.ProductoID 
            AND LF.FacturaID IN (
                SELECT DISTINCT FacturaID
                FROM LineasFactura
                WHERE ProductoID = @ProductoID
            )
        WHERE 
            P.ProductoID <> @ProductoID -- Excluir el producto dado
        GROUP BY 
            P.ProductoID, P.Nombre
        ORDER BY 
            Cuenta DESC, 
            P.ProductoID ASC;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener las sugerencias para el producto especificado.', 16, 1);
    END CATCH
END;
GO
