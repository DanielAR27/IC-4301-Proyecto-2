USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetMarcas
AS
BEGIN
    SELECT Nombre
    FROM Marcas
	ORDER BY MarcaID;
END;
GO