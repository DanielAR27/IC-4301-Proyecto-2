USE DB_Proyecto;
GO
ALTER PROCEDURE UpsertCarritoProducto
    @UsuarioID INT,
    @ProductoID INT,
    @ProductoNombre NVARCHAR(100),
    @Cantidad INT,
    @PrecioOriginal DECIMAL(10, 2),
    @Resultado INT OUTPUT -- Variable de salida para indicar el tipo de operaci�n
AS
BEGIN
    BEGIN TRY
        DECLARE @CarritoID INT;
        DECLARE @Linea INT;
        DECLARE @DescuentoAplicado DECIMAL(5, 2) = 0;
        DECLARE @LineaTotal DECIMAL(10, 2);

        BEGIN TRANSACTION;

        -- 1) Obtener el CarritoID seg�n el UsuarioID
        SELECT @CarritoID = CarritoID 
        FROM CarritoCompras 
        WHERE UsuarioID = @UsuarioID;

        -- Si no existe un carrito para el usuario, devolver -1 en @Resultado
        IF @CarritoID IS NULL
        BEGIN
            SET @Resultado = -1; -- C�digo de error para carrito no encontrado
            PRINT 'Error: No se ha encontrado un carrito para el usuario especificado';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- 2) Ver cu�ntas l�neas existen en el Carrito y calcular la nueva l�nea
        SELECT @Linea = COUNT(*) + 1 
        FROM CarritoProducto 
        WHERE CarritoID = @CarritoID;

        -- 3) Verificar si hay un descuento actual para el ProductoID
        SELECT TOP 1 @DescuentoAplicado = D.Porcentaje
        FROM Descuentos D
        WHERE D.ProductoID = @ProductoID
            AND DATEDIFF(MINUTE, GETDATE(), D.FechaInicio) <= 0
            AND DATEDIFF(MINUTE, GETDATE(), D.FechaFin) >= 0
        ORDER BY D.FechaInicio ASC;

        -- Calcular el total de la l�nea con descuento aplicado si corresponde
        SET @LineaTotal = @Cantidad * @PrecioOriginal - (@Cantidad * @PrecioOriginal * @DescuentoAplicado * 0.01);

        -- 4) Realizar un UPSERT en CarritoProducto
        IF EXISTS (SELECT 1 FROM CarritoProducto WHERE CarritoID = @CarritoID AND ProductoID = @ProductoID)
        BEGIN
            -- Update si el producto ya existe en el carrito del usuario
            UPDATE CarritoProducto
            SET Cantidad = @Cantidad,
                PrecioOriginal = @PrecioOriginal,
                DescuentoAplicado = @DescuentoAplicado,
                LineaTotal = @LineaTotal,
                ProductoNombre = @ProductoNombre
            WHERE CarritoID = @CarritoID AND ProductoID = @ProductoID;

            SET @Resultado = 0; -- C�digo para update exitoso
        END
        ELSE
        BEGIN
            -- Insert si el producto no existe en el carrito del usuario
            INSERT INTO CarritoProducto (CarritoID, Linea, ProductoID, ProductoNombre, Cantidad, PrecioOriginal, DescuentoAplicado, LineaTotal)
            VALUES (@CarritoID, @Linea, @ProductoID, @ProductoNombre, @Cantidad, @PrecioOriginal, @DescuentoAplicado, @LineaTotal);

            SET @Resultado = 1; -- C�digo para insert exitoso
        END
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Capturar el error del trigger
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Manejar el error espec�fico del trigger
        IF ERROR_NUMBER() = 50001
        BEGIN
            SET @Resultado = -2; -- C�digo para error de stock insuficiente
			PRINT 'Error: La cantidad solicita supera al stock disponible.';
			RETURN;
        END
    END CATCH
END;
GO

DECLARE @Resultado INT; -- Declarar la variable correctamente
EXEC UpsertCarritoProducto 1, 3, 'Ultraball', 1, 1200, @Resultado OUTPUT; -- Ejecutar el procedimiento con OUTPUT
SELECT @Resultado AS Resultado; -- Ver el resultado de la ejecuci�n

SELECT * FROM CarritoCompras
