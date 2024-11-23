USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProvinciasConID
AS
BEGIN
	SELECT EP.EstadoProvinciaID, EP.Nombre NombreProvincia, P.Nombre NombrePais, P.Codigo
	FROM Estados_Provincias EP
	INNER JOIN Paises P ON P.PaisID = EP.PaisID;
END;
GO