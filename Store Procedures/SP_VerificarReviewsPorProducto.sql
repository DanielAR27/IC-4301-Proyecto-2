USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarReviewsPorProducto
    @ProductoID INT,       -- Parámetro de entrada para el ID del producto
    @NumeroPagina INT,     -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT  -- Variable de salida para indicar si hay reviews
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 2; -- Número de página * 2 elementos por página

        -- Verificar si hay reviews en la página solicitada
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
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar las reviews del producto.', 16, 1);
    END CATCH
END;
GO
