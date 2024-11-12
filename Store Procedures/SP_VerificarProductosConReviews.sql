USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosConReviews
    @NumeroPagina INT,     -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT   -- Parámetro de salida para indicar si hay productos con reseñas
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Verificar si existen productos con reseñas en la página solicitada
        IF EXISTS (
            SELECT R.ProductoID
            FROM Reviews R
            LEFT JOIN Productos P ON R.ProductoID = P.ProductoID
            GROUP BY R.ProductoID
            ORDER BY R.ProductoID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 10 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay productos con reseñas en la página
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron productos con reseñas en la página
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar los productos con reseñas.', 16, 1);
    END CATCH
END;
GO
