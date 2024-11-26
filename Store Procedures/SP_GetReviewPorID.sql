USE DB_Proyecto;
GO
CREATE PROCEDURE GetReviewPorID
    @ReviewID INT
AS
BEGIN
    BEGIN TRY
        -- Seleccionar todos los métodos de pago asociados al UsuarioID proporcionado
        SELECT R.Calificacion, R.Comentario
        FROM Reviews R
        WHERE R.ReviewID = @ReviewID;
    END TRY
    BEGIN CATCH
        PRINT 'Ocurrió un error al obtener los métodos de pago del usuario.';
    END CATCH
END;
GO
