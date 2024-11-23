USE [DB_Proyecto]
GO
CREATE OR ALTER PROCEDURE GetProductos
AS
BEGIN
	SELECT P.ProductoID, P.Nombre, C.Nombre Categoria, M.Nombre Marca, P.Stock
	FROM Productos P
	LEFT JOIN Marcas M ON M.MarcaID = P.MarcaID
	LEFT JOIN Categorias C ON C.CategoriaID = P.CategoriaID 
END;
GO