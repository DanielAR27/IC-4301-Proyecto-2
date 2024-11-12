USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosCompradosPorUsuario
    @UsuarioID INT,          -- Par�metro de entrada para el ID del usuario
    @NumeroPagina INT        -- Par�metro de entrada para el n�mero de p�gina
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Seleccionar productos distintos con ID, nombre, precio e imagen
		SELECT LF.ProductoID, P.Nombre, P.Precio, P.Imagen
		FROM LineasFactura LF
		LEFT JOIN Facturas F ON F.FacturaID = LF.FacturaID
		LEFT JOIN Productos P ON P.ProductoID = LF.ProductoID
		WHERE F.UsuarioID = @UsuarioID
		GROUP BY LF.ProductoID, P.Nombre, P.Precio, P.Imagen
		ORDER BY LF.ProductoID
		OFFSET @FilasAOmitir ROWS
		FETCH NEXT 10 ROWS ONLY; -- Obtener los pr�ximos 10 elementos
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurri� un error al obtener los productos del usuario.', 16, 1);
    END CATCH
END;
GO

EXEC GetProductosCompradosPorUsuario 1, 0

