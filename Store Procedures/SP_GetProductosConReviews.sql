USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosConReviews
    @NumeroPagina INT -- Parámetro de entrada para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Seleccionar productos distintos con ID, nombre, precio e imagen que tengan reseñas
        SELECT R.ProductoID, P.Nombre, P.Precio, P.Imagen
        FROM Reviews R
        LEFT JOIN Productos P ON R.ProductoID = P.ProductoID
        GROUP BY R.ProductoID, P.Nombre, P.Precio, P.Imagen
        ORDER BY R.ProductoID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los próximos 10 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener los productos con reseñas.', 16, 1);
    END CATCH
END;
GO