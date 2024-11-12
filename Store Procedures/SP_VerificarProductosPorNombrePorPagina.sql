USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosPorNombrePorPagina
    @Nombre NVARCHAR(100), -- Parámetro de entrada para el nombre de la marca
    @NumeroPagina INT,          -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT       -- Variable de salida para indicar si hay productos
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Verificar si hay productos que cumplan con el filtro de marca en la página solicitada
        IF EXISTS (
            SELECT 1
            FROM Productos P
            WHERE P.Stock > 0 -- Verificar que el stock sea mayor que cero
              AND LOWER(P.Nombre) LIKE '%' + LOWER(@Nombre) + '%' -- Búsqueda en cualquier posición
            ORDER BY P.ProductoID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 10 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay productos disponibles
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron productos
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar los productos por marca.', 16, 1);
    END CATCH
END;
GO
