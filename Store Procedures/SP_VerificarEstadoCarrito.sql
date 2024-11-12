USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarEstadoCarrito
    @UsuarioID INT,
    @Resultado INT OUTPUT -- Variable de salida para indicar el estado de la verificación
AS
BEGIN
    BEGIN TRY
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

        -- 2) Verificar la diferencia entre las líneas del CarritoProducto y las líneas con los precios y descuentos actuales
        IF EXISTS (
            SELECT CP.ProductoID, P.Nombre, P.Precio,
                   CASE 
                       WHEN D.ProductoID IS NOT NULL THEN D.Porcentaje 
                       ELSE 0 
                   END AS Descuento
            FROM CarritoProducto CP
            LEFT JOIN Productos P ON P.ProductoID = CP.ProductoID
            LEFT JOIN (
                SELECT ProductoID, Porcentaje, FechaInicio, FechaFin,
                       ROW_NUMBER() OVER (PARTITION BY ProductoID ORDER BY FechaInicio ASC) AS RowNum
                FROM Descuentos
                WHERE DATEDIFF(SECOND, GETDATE(), FechaInicio) <= 0
                  AND DATEDIFF(SECOND, GETDATE(), FechaFin) >= 0
            ) D ON D.ProductoID = CP.ProductoID AND D.RowNum = 1
            WHERE CP.CarritoID = @CarritoID
            EXCEPT
            SELECT CP.ProductoID, CP.ProductoNombre, CP.PrecioOriginal, CP.DescuentoAplicado
            FROM CarritoProducto CP
            WHERE CP.CarritoID = @CarritoID
        )
        BEGIN
            -- Actualizar las líneas del carrito a los valores actuales
            UPDATE CP
            SET
				CP.ProductoNombre = P.Nombre,
				CP.PrecioOriginal = P.Precio,
                CP.DescuentoAplicado = ISNULL(D.Porcentaje, 0),
                CP.LineaTotal = CP.Cantidad * P.Precio - (CP.Cantidad * P.Precio * ISNULL(D.Porcentaje, 0) * 0.01)
            FROM CarritoProducto CP
            JOIN Productos P ON P.ProductoID = CP.ProductoID
            LEFT JOIN (
                SELECT ProductoID, Porcentaje, FechaInicio, FechaFin,
                       ROW_NUMBER() OVER (PARTITION BY ProductoID ORDER BY FechaInicio ASC) AS RowNum
                FROM Descuentos
                WHERE DATEDIFF(SECOND, GETDATE(), FechaInicio) <= 0
                  AND DATEDIFF(SECOND, GETDATE(), FechaFin) >= 0
            ) D ON D.ProductoID = CP.ProductoID AND D.RowNum = 1
            WHERE CP.CarritoID = @CarritoID;

			-- Actualizar el TotalCarrito con la suma de LineaTotal de CarritoProducto
			UPDATE CarritoCompras
			SET TotalCarrito = (
				SELECT SUM(LineaTotal)
				FROM CarritoProducto
				WHERE CarritoID = @CarritoID
			)
			WHERE CarritoID = @CarritoID;


            -- Devolver 0 si se realizó la actualización
            SET @Resultado = 0;
        END
        ELSE
        BEGIN
            -- No se necesita actualizar, devolver 1
            SET @Resultado = 1;
        END

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        PRINT 'Ocurrió un error al verificar y actualizar los descuentos en el carrito.';
        SET @Resultado = -1; -- Código de error general
    END CATCH
END;
GO

BEGIN 
    DECLARE @Resultado INT;
    EXEC VerificarEstadoCarrito 1, @Resultado OUTPUT
    SELECT @Resultado AS Resultado;
END;

