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

-- Prueba

SELECT * FROM USUARIOS;

SELECT * FROM PRODUCTOS;

SELECT * FROM REVIEWS;

-- Cambiar el primer valor que es el UsuarioID
INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario, FechaReview) VALUES
	(11, 7, 5, 'Pesimo producto', GETDATE());

UPDATE Productos SET
CalificacionPromedio = (
SELECT AVG((CAST(Calificacion AS DECIMAL(3,2))))
FROM Reviews 
WHERE ProductoID = 7
GROUP BY ProductoID)
WHERE ProductoID = 7

DELETE FROM Usuarios WHERE UsuarioID = 11;

DELETE FROM Marcas WHERE Nombre = 'Pokemarket';