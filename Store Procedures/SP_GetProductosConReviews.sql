USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosConReviews
    @NumeroPagina INT -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Seleccionar productos distintos con ID, nombre, precio e imagen que tengan rese�as
        SELECT R.ProductoID, P.Nombre, P.Precio, P.Imagen
        FROM Reviews R
        LEFT JOIN Productos P ON R.ProductoID = P.ProductoID
        GROUP BY R.ProductoID, P.Nombre, P.Precio, P.Imagen
        ORDER BY R.ProductoID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los pr�ximos 10 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos con rese�as.', 16, 1);
    END CATCH
END;
GO