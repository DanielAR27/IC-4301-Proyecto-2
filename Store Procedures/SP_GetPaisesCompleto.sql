USE DB_Proyecto
GO
CREATE OR ALTER PROCEDURE GetPaisesCompleto
AS
BEGIN
    SELECT PaisID, Codigo, Nombre, CostoEnvio
    FROM Paises
    ORDER BY Nombre;
END;
GO
