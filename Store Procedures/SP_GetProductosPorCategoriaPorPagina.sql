USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosPorCategoriaPorPagina
    @NombreCategoria NVARCHAR(100), -- Par�metro de entrada para el nombre de la marca
    @NumeroPagina INT -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Seleccionar los 10 elementos correspondientes a la p�gina con los detalles de categor�a y marca
        SELECT P.ProductoID, P.Nombre, P.Precio, P.Imagen, 
            CASE 
                WHEN EXISTS (
                    SELECT 1 
                    FROM Descuentos D 
                    WHERE D.ProductoID = P.ProductoID
                      AND DATEDIFF(SECOND, GETDATE(), D.FechaInicio) <= 0 
                      AND DATEDIFF(SECOND, GETDATE(), D.FechaFin) >= 0
                ) THEN 'S' 
                ELSE 'N' 
            END AS DescuentoVigente
        FROM Productos P
        JOIN Categorias C ON P.CategoriaID = C.CategoriaID -- Relaci�n con la tabla de marcas
        WHERE P.Stock > 0 -- Verificar que el stock sea mayor que cero
          AND C.Nombre = @NombreCategoria -- Filtrar por el nombre de la marca
        ORDER BY P.ProductoID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los pr�ximos 10 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos por marca.', 16, 1);
    END CATCH
END;
GO
