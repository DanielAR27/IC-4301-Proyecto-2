USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetDescuentosConID
AS
BEGIN
	SELECT D.DescuentoID, P.Nombre, D.Porcentaje, D.FechaInicio, D.FechaFin
	FROM Descuentos D
	LEFT JOIN Productos P ON D.ProductoID = P.ProductoID
END;

EXEC GetDescuentosConID