USE DB_Proyecto;
GO
CREATE PROCEDURE GetDescuentoPorID
    @DescuentoID INT
AS
BEGIN
    SELECT 
        DescuentoID,
        ProductoID,
        Porcentaje,
        CONVERT(VARCHAR(10), FechaInicio, 120) AS FechaInicio,
        CONVERT(VARCHAR(10), FechaFin, 120) AS FechaFin
    FROM Descuentos
    WHERE DescuentoID = @DescuentoID;
END;
