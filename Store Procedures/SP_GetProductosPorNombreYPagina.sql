USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosPorNombrePorPagina
    @Nombre NVARCHAR(100),     -- Parte del nombre del producto a buscar
    @NumeroPagina INT          -- N�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Seleccionar productos con ID, nombre, precio, imagen, y si tienen descuento vigente
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
        WHERE P.Stock > 0 -- Verificar que el stock sea mayor que cero
            AND LOWER(P.Nombre) LIKE '%' + LOWER(@Nombre) + '%' -- B�squeda en cualquier posici�n
        ORDER BY P.ProductoID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los pr�ximos 10 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos por nombre.', 16, 1);
    END CATCH
END;
GO
