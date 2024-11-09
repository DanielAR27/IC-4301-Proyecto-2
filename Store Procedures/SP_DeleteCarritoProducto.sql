USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeleteCarritoProducto
    @UsuarioID INT,
    @CarritoProductoID INT,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacci�n

        DECLARE @CarritoID INT;

        -- Obtener el CarritoID seg�n el UsuarioID
        SELECT @CarritoID = CarritoID
        FROM CarritoCompras
        WHERE UsuarioID = @UsuarioID;

        -- Verificar si el CarritoID fue encontrado
        IF @CarritoID IS NULL
        BEGIN
            SET @Resultado = -1; -- C�digo de error para carrito no encontrado
            ROLLBACK TRANSACTION; -- Revertir la transacci�n
            RETURN;
        END

        -- Eliminar el producto del carrito
        DELETE FROM CarritoProducto
        WHERE CarritoID = @CarritoID AND CarritoProductoID = @CarritoProductoID;

        -- Verificar si la eliminaci�n afect� alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            -- Actualizar el TotalCarrito en CarritoCompras
            UPDATE CarritoCompras
            SET TotalCarrito = (
                SELECT ISNULL(SUM(LineaTotal), 0)
                FROM CarritoProducto
                WHERE CarritoID = @CarritoID
            )
            WHERE CarritoID = @CarritoID;

            SET @Resultado = 0; -- Indica �xito en la eliminaci�n
            COMMIT TRANSACTION; -- Confirmar la transacci�n
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Indica que no se encontr� el producto en el carrito
            ROLLBACK TRANSACTION; -- Revertir la transacci�n
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacci�n en caso de error

        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Error al intentar eliminar el producto del carrito. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
