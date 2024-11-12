USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE DeleteCarritoProducto
    @UsuarioID INT,
    @CarritoProductoID INT,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacción

        DECLARE @CarritoID INT;

        -- Obtener el CarritoID según el UsuarioID
        SELECT @CarritoID = CarritoID
        FROM CarritoCompras
        WHERE UsuarioID = @UsuarioID;

        -- Verificar si el CarritoID fue encontrado
        IF @CarritoID IS NULL
        BEGIN
            SET @Resultado = -1; -- Código de error para carrito no encontrado
            ROLLBACK TRANSACTION; -- Revertir la transacción
            RETURN;
        END

        -- Eliminar el producto del carrito
        DELETE FROM CarritoProducto
        WHERE CarritoID = @CarritoID AND CarritoProductoID = @CarritoProductoID;

        -- Verificar si la eliminación afectó alguna fila
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

            SET @Resultado = 0; -- Indica éxito en la eliminación
            COMMIT TRANSACTION; -- Confirmar la transacción
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Indica que no se encontró el producto en el carrito
            ROLLBACK TRANSACTION; -- Revertir la transacción
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacción en caso de error

        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Error al intentar eliminar el producto del carrito. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
