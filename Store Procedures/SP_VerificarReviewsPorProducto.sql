USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarReviewsPorProducto
    @ProductoID INT,       -- Par�metro de entrada para el ID del producto
    @NumeroPagina INT,     -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT  -- Variable de salida para indicar si hay reviews
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 2; -- N�mero de p�gina * 2 elementos por p�gina

        -- Verificar si hay reviews en la p�gina solicitada
        IF EXISTS (
            SELECT 1
            FROM Reviews R
            WHERE R.ProductoID = @ProductoID
            ORDER BY R.ReviewID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 2 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay reviews disponibles
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron reviews
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar las reviews del producto.', 16, 1);
    END CATCH
END;
GO
