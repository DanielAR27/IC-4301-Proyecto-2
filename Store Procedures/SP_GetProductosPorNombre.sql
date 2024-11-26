USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosPorNombre
    @Nombre NVARCHAR(100)     -- Parte del nombre del producto a buscar
AS
BEGIN
    BEGIN TRY

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
            AND LOWER(P.Nombre) LIKE '%' + LOWER(@Nombre) + '%' -- Búsqueda en cualquier posición
        ORDER BY P.ProductoID;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener los productos por nombre.', 16, 1);
    END CATCH
END;
GO
