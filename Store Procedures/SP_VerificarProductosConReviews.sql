USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosConReviews
    @NumeroPagina INT,     -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT   -- Par�metro de salida para indicar si hay productos con rese�as
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Verificar si existen productos con rese�as en la p�gina solicitada
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
            SET @Resultado = 1; -- Indica que hay productos con rese�as en la p�gina
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron productos con rese�as en la p�gina
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar los productos con rese�as.', 16, 1);
    END CATCH
END;
GO
