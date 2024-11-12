USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosConDescuentoPorPagina
    @NumeroPagina INT          -- N�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Seleccionar productos con ID, nombre, precio, imagen, y una columna "DescuentoVigente" que siempre sea "S"
        SELECT P.ProductoID, P.Nombre, P.Precio, P.Imagen,
               'S' AS DescuentoVigente
        FROM Productos P
        JOIN Descuentos D ON D.ProductoID = P.ProductoID
        WHERE P.Stock > 0 -- Verificar que el stock sea mayor que cero
            AND DATEDIFF(SECOND, GETDATE(), D.FechaInicio) <= 0 
            AND DATEDIFF(SECOND, GETDATE(), D.FechaFin) >= 0
        ORDER BY P.ProductoID
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 10 ROWS ONLY; -- Obtener los pr�ximos 10 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos con descuento vigente.', 16, 1);
    END CATCH
END;
GO
