USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarBorradoSinStock
    @UsuarioID INT,
    @Resultado INT OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;
		DECLARE @CarritoID INT;

        -- 1) Obtener el CarritoID según el UsuarioID
        SELECT @CarritoID = CarritoID 
        FROM CarritoCompras 
        WHERE UsuarioID = @UsuarioID;

        -- Si no existe un carrito para el usuario, devolver -1 en @Resultado
        IF @CarritoID IS NULL
        BEGIN
            SET @Resultado = -1; -- Código de error para carrito no encontrado
            PRINT 'Error: No se ha encontrado un carrito para el usuario especificado';
            RETURN;
        END

        -- Eliminar productos cuya cantidad solicitada es mayor al stock disponible
        DELETE FROM CarritoProducto
        WHERE CarritoID = @CarritoID
          AND ProductoID IN (
              SELECT CP.ProductoID
              FROM CarritoProducto CP
              JOIN Productos P ON P.ProductoID = CP.ProductoID
              WHERE CP.CarritoID = @CarritoID
                AND CP.Cantidad > P.Stock
          );

        -- Verificar si se eliminaron productos
        IF @@ROWCOUNT > 0
			BEGIN
				-- Actualizar el TotalCarrito después de eliminar productos
				UPDATE CarritoCompras
				SET TotalCarrito = (
					SELECT SUM(LineaTotal)
					FROM CarritoProducto
					WHERE CarritoID = @CarritoID
				)
				WHERE CarritoID = @CarritoID;

				SET @Resultado = 0; -- Productos eliminados
			END
        ELSE
			BEGIN
				SET @Resultado = 1; -- No se eliminaron productos
			END
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        PRINT 'Ocurrió un error al verificar y eliminar productos sin stock.';
        SET @Resultado = -1; -- Código de error general
    END CATCH
END;
GO