USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetReviewsPorUsuario
    @UsuarioID INT,      -- Par�metro de entrada para el ID del producto
    @NumeroPagina INT     -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Seleccionar las reviews para el producto especificado, mostrando 10 por p�gina
        SELECT R.ReviewID, P.ProductoID, P.Nombre, R.Calificacion , P.Imagen
        FROM Reviews R
        LEFT JOIN Productos P ON R.ProductoID = P.ProductoID
        WHERE R.UsuarioID = @UsuarioID
        ORDER BY R.ReviewID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los pr�ximos 10 reviews

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener las reviews del producto.', 16, 1);
    END CATCH
END;
GO

EXEC GetReviewsPorUsuario 1, 1