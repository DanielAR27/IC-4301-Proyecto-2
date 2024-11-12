USE DB_Proyecto;
GO
CREATE OR PROCEDURE ObtenerTotalCarrito
    @UsuarioID INT,
    @TotalCarrito DECIMAL(10, 2) OUTPUT -- Variable de salida para el total del carrito
AS
BEGIN
    BEGIN TRY
        -- Obtener el total del carrito asociado al UsuarioID
        SELECT @TotalCarrito = TotalCarrito
        FROM CarritoCompras
        WHERE UsuarioID = @UsuarioID;

        -- Si no se encontró ningún carrito, lanzar un error
        IF @TotalCarrito IS NULL
        BEGIN
            SET @TotalCarrito = 0.00; -- Devolver 0 en la variable de salida si no hay carrito
            RAISERROR('No se encontró un carrito para el usuario especificado.', 16, 1);
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en caso de que ocurra un problema
        SET @TotalCarrito = 0.00; -- Asegurarse de que la variable de salida tenga un valor seguro
        RAISERROR('Ocurrió un error al obtener el total del carrito.', 16, 1);
    END CATCH
END;
GO
