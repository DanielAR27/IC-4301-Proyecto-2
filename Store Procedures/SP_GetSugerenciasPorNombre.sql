USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetSugerenciasPorNombre
    @Nombre NVARCHAR(100)     -- Parte del nombre del producto a buscar
AS
BEGIN
    BEGIN TRY

        -- Seleccionar productos con ID, nombre, precio, imagen, y si tienen descuento vigente
        SELECT P.Nombre
        FROM Productos P
        WHERE P.Stock > 0 -- Verificar que el stock sea mayor que cero
            AND LOWER(P.Nombre) LIKE '%' + LOWER(@Nombre) + '%' -- B�squeda en cualquier posici�n
        ORDER BY P.ProductoID;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos por nombre.', 16, 1);
    END CATCH
END;
GO