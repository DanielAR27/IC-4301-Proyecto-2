USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetReviewsPorProducto
    @ProductoID INT,      -- Par�metro de entrada para el ID del producto
    @NumeroPagina INT     -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 2; -- N�mero de p�gina * 2 elementos por p�gina

        -- Seleccionar las reviews para el producto especificado, mostrando 2 por p�gina
        SELECT R.ReviewID, U.Nombre, U.Apellido, U.Email, R.Calificacion, R.Comentario
        FROM Reviews R
        LEFT JOIN Usuarios U ON R.UsuarioID = U.UsuarioID
        WHERE R.ProductoID = @ProductoID
        ORDER BY R.ReviewID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 2 ROWS ONLY; -- Obtener los pr�ximos 2 reviews

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener las reviews del producto.', 16, 1);
    END CATCH
END;
GO

DECLARE @Resultado