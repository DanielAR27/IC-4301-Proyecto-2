USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosConDescuentoPorPagina
    @NumeroPagina INT, -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT -- Variable de salida para indicar el resultado
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Verificar si hay productos en la p�gina solicitada
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
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar los productos.', 16, 1);
    END CATCH
END;
GO
