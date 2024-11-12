USE DB_Proyecto;
CREATE PROCEDURE GetPaises
AS
BEGIN
    SELECT Nombre
    FROM Paises;
END;


EXEC GetPaises
