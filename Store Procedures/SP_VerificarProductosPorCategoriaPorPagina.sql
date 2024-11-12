USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosPorCategoriaPorPagina
    @NombreCategoria NVARCHAR(100), -- Par�metro de entrada para el nombre de la marca
    @NumeroPagina INT,          -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT       -- Variable de salida para indicar si hay productos
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Verificar si hay productos que cumplan con el filtro de marca en la p�gina solicitada
        IF EXISTS (
            SELECT 1
            FROM Productos P
            JOIN Categorias C ON P.CategoriaID = C.CategoriaID -- Relaci�n con la tabla de marcas
            WHERE P.Stock > 0 -- Verificar que el stock sea mayor que cero
              AND C.Nombre = @NombreCategoria -- Filtrar por el nombre de la marca
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
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar los productos por marca.', 16, 1);
    END CATCH
END;
GO
