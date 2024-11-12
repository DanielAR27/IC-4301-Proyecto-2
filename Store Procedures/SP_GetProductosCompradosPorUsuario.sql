USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosCompradosPorUsuario
    @UsuarioID INT,          -- Parámetro de entrada para el ID del usuario
    @NumeroPagina INT        -- Parámetro de entrada para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Seleccionar productos distintos con ID, nombre, precio e imagen
		SELECT LF.ProductoID, P.Nombre, P.Precio, P.Imagen
		FROM LineasFactura LF
		LEFT JOIN Facturas F ON F.FacturaID = LF.FacturaID
		LEFT JOIN Productos P ON P.ProductoID = LF.ProductoID
		WHERE F.UsuarioID = @UsuarioID
		GROUP BY LF.ProductoID, P.Nombre, P.Precio, P.Imagen
		ORDER BY LF.ProductoID
		OFFSET @FilasAOmitir ROWS
		FETCH NEXT 10 ROWS ONLY; -- Obtener los próximos 10 elementos
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener los productos del usuario.', 16, 1);
    END CATCH
END;
GO

EXEC GetProductosCompradosPorUsuario 1, 0

