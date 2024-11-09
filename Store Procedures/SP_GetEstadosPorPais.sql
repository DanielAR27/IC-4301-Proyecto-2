USE DB_Proyecto;
CREATE PROCEDURE GetEstadosPorPais
    @NombrePais NVARCHAR(100) -- Par�metro de entrada para el nombre del pa�s
AS
BEGIN
	SELECT EP.Nombre
	FROM Estados_Provincias EP
	LEFT JOIN Paises P ON P.PaisID = EP.PaisID
	WHERE P.Nombre = @NombrePais
END;


SELECT EP.Nombre
FROM Estados_Provincias EP
LEFT JOIN Paises P ON P.PaisID = EP.PaisID
WHERE P.Nombre = 'Costa Rica'
