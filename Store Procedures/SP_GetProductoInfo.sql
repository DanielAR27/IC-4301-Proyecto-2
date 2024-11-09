USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductoInfo
    @ProductoID INT
AS
BEGIN
    BEGIN TRY
        -- Verificar si el ProductoID existe
        IF NOT EXISTS (SELECT 1 FROM Productos WHERE ProductoID = @ProductoID)
        BEGIN
            PRINT 'Error: no se ha encontrado el producto con el ID solicitado';
            RETURN;
        END

		SELECT P.Nombre, P.Descripcion, P.Precio, P.Stock, 
			   C.Nombre AS Categoria, M.Nombre AS Marca, 
			   P.CalificacionPromedio, P.Imagen, 
			   (SELECT COUNT(*) FROM Reviews R WHERE R.ProductoID = P.ProductoID) AS Reviews,
			   ISNULL(D.Porcentaje, 0) AS Descuento
		FROM Productos P
		LEFT JOIN Categorias C ON C.CategoriaID = P.CategoriaID
		LEFT JOIN Marcas M ON M.MarcaID = P.MarcaID
		LEFT JOIN (
			SELECT TOP 1 ProductoID, Porcentaje
			FROM Descuentos
			WHERE DATEDIFF(SECOND, GETDATE(), FechaInicio) <= 0 
			  AND DATEDIFF(SECOND, GETDATE(), FechaFin) >= 0 AND ProductoID = @ProductoID
			ORDER BY FechaInicio ASC
		) D ON P.ProductoID = D.ProductoID
		WHERE P.ProductoID = @ProductoID;


    END TRY
    BEGIN CATCH
        PRINT 'Ocurrió un error al obtener la información del producto.';
    END CATCH
END;
GO

EXEC GetProductoInfo 1


SELECT *
FROM CarritoProducto


SELECT *
FROM Descuentos