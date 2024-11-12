USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosConDescuentoPorPagina
    @NumeroPagina INT, -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT -- Variable de salida para indicar el resultado
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Verificar si hay productos en la página solicitada
        IF EXISTS (
            SELECT 1
            FROM Productos P
			LEFT JOIN Descuentos D ON P.ProductoID = D.ProductoID
			WHERE P.Stock > 0 AND DATEDIFF(SECOND, GETDATE(), D.FechaInicio) <= 0 
            AND DATEDIFF(SECOND, GETDATE(), D.FechaFin) >= 0
            ORDER BY P.ProductoID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 10 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay al menos un producto
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron productos
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar los productos.', 16, 1);
    END CATCH
END;
GO
