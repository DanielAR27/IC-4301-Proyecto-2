USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetCategorias
AS
BEGIN
    SELECT Nombre
    FROM Categorias
	ORDER BY CategoriaID;
END;
GO