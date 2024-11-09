USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE ProcesarCompra
    @UsuarioID INT,
    @Total DECIMAL(10, 2),
    @Envio DECIMAL(10, 2),
    @Resultado INT OUTPUT -- Variable de resultado para indicar el estado de la operación
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION; -- Iniciar la transacción

        DECLARE @CarritoID INT;
        DECLARE @TotalCarrito DECIMAL(10, 2);
        DECLARE @VatTotal DECIMAL(10, 2);
        DECLARE @FacturaID INT; -- Declaración de la variable interna para FacturaID

        -- 1. Obtener el CarritoID y verificar que existe
        SELECT @CarritoID = CarritoID, @TotalCarrito = TotalCarrito
        FROM CarritoCompras
        WHERE UsuarioID = @UsuarioID;

        IF @CarritoID IS NULL
        BEGIN
            SET @Resultado = -1; -- Indica que el carrito no existe
            RAISERROR('El carrito no existe para el usuario proporcionado.', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- 2. Verificar que el total recibido coincida con el total del carrito
        IF @Total <> @TotalCarrito
        BEGIN
            SET @Resultado = -1; -- Indica que el total no coincide
            RAISERROR('El total proporcionado no coincide con el total del carrito.', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- 3. Calcular el IVA total (por ejemplo, 13% del total)
        SET @VatTotal = @Total * 0.13;

        -- 4. Insertar la nueva factura
        INSERT INTO Facturas (UsuarioID, Total, CostoEnvio)
        VALUES (@UsuarioID, @Total, @Envio);

        SET @FacturaID = SCOPE_IDENTITY(); -- Obtener el ID de la factura recién insertada

        -- 5. Mover productos a LineasFactura y actualizar stock
        INSERT INTO LineasFactura (FacturaID, ProductoID, ProductoNombre, Linea, Cantidad, PrecioOriginal, LineaTotal)
        SELECT 
            @FacturaID,
            CP.ProductoID,
            CP.ProductoNombre,
            CP.Linea,
            CP.Cantidad,
            CP.PrecioOriginal,
            CP.LineaTotal
        FROM CarritoProducto CP
        WHERE CP.CarritoID = @CarritoID;

        -- Actualizar el stock de los productos
        UPDATE P
        SET P.Stock = P.Stock - CP.Cantidad
        FROM Productos P
        JOIN CarritoProducto CP ON P.ProductoID = CP.ProductoID
        WHERE CP.CarritoID = @CarritoID;

        -- 6. Borrar los productos del carrito
        DELETE FROM CarritoProducto
        WHERE CarritoID = @CarritoID;

        -- 7. Actualizar el TotalCarrito en CarritoCompras
        UPDATE CarritoCompras
        SET TotalCarrito = 0
        WHERE CarritoID = @CarritoID;

        -- Indicar que la transacción fue exitosa
        SET @Resultado = 0; -- Indica éxito

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Indicar que hubo un error
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al procesar la compra.', 16, 1);
    END CATCH
END;
GO