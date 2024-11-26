USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetReviewsPorUsuario
    @UsuarioID INT,      -- Parámetro de entrada para el ID del producto
    @NumeroPagina INT     -- Parámetro de entrada para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Seleccionar las reviews para el producto especificado, mostrando 10 por página
        SELECT R.ReviewID, P.ProductoID, P.Nombre, R.Calificacion , P.Imagen
        FROM Reviews R
        LEFT JOIN Productos P ON R.ProductoID = P.ProductoID
        WHERE R.UsuarioID = @UsuarioID
        ORDER BY R.ReviewID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los próximos 10 reviews

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener las reviews del producto.', 16, 1);
    END CATCH
END;
GO

EXEC GetReviewsPorUsuario 1, 1