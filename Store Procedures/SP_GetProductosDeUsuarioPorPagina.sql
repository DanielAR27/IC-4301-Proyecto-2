USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosDeUsuarioPorPagina
    @UsuarioID INT,
    @NumeroPagina INT -- Parámetro de entrada para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- Número de página * 5 elementos por página

        -- Seleccionar los productos del carrito del usuario, con detalles de la imagen y paginación
        SELECT CP.CarritoProductoID ,CP.ProductoID, CP.ProductoNombre, P.Descripcion, CP.Cantidad, CP.PrecioOriginal, CP.DescuentoAplicado, CP.LineaTotal, P.Imagen
        FROM CarritoProducto CP
        LEFT JOIN Productos P ON P.ProductoID = CP.ProductoID
        WHERE CP.CarritoID = (SELECT CarritoID FROM CarritoCompras WHERE UsuarioID = @UsuarioID)
        ORDER BY CP.Linea ASC
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 5 ROWS ONLY; -- Obtener los próximos 5 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener los productos del carrito del usuario.', 16, 1);
    END CATCH
END;
GO
