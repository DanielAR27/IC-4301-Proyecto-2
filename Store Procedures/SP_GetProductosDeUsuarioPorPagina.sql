USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosDeUsuarioPorPagina
    @UsuarioID INT,
    @NumeroPagina INT -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- N�mero de p�gina * 5 elementos por p�gina

        -- Seleccionar los productos del carrito del usuario, con detalles de la imagen y paginaci�n
        SELECT CP.CarritoProductoID ,CP.ProductoID, CP.ProductoNombre, P.Descripcion, CP.Cantidad, CP.PrecioOriginal, CP.DescuentoAplicado, CP.LineaTotal, P.Imagen
        FROM CarritoProducto CP
        LEFT JOIN Productos P ON P.ProductoID = CP.ProductoID
        WHERE CP.CarritoID = (SELECT CarritoID FROM CarritoCompras WHERE UsuarioID = @UsuarioID)
        ORDER BY CP.Linea ASC
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 5 ROWS ONLY; -- Obtener los pr�ximos 5 elementos

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos del carrito del usuario.', 16, 1);
    END CATCH
END;
GO
