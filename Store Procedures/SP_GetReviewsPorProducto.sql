USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetReviewsPorProducto
    @ProductoID INT,      -- Parámetro de entrada para el ID del producto
    @NumeroPagina INT     -- Parámetro de entrada para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 2; -- Número de página * 2 elementos por página

        -- Seleccionar las reviews para el producto especificado, mostrando 2 por página
        SELECT R.ReviewID, U.Nombre, U.Apellido, U.Email, R.Calificacion, R.Comentario
        FROM Reviews R
        LEFT JOIN Usuarios U ON R.UsuarioID = U.UsuarioID
        WHERE R.ProductoID = @ProductoID
        ORDER BY R.ReviewID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 2 ROWS ONLY; -- Obtener los próximos 2 reviews

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener las reviews del producto.', 16, 1);
    END CATCH
END;
GO

DECLARE @Resultado