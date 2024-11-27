USE DB_Proyecto;
GO

CREATE OR ALTER TRIGGER trg_UpdateCalificaciones
ON Reviews
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    -- Recalcula la calificación promedio para los productos afectados
    UPDATE Productos
    SET CalificacionPromedio = (
        SELECT ISNULL(AVG(CAST(Calificacion AS DECIMAL(3,2))), 0.0)
        FROM Reviews
        WHERE Reviews.ProductoID = Productos.ProductoID
    )
    WHERE ProductoID IN (
        -- Unifica los ProductoID afectados por las operaciones INSERT, UPDATE, DELETE
        SELECT DISTINCT ProductoID
        FROM INSERTED
        UNION
        SELECT DISTINCT ProductoID
        FROM DELETED
    );
END;
GO