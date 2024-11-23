USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdateDescuento
    @DescuentoID INT, -- ID del descuento a actualizar
    @NombreProducto NVARCHAR(100), -- Nombre del producto
    @PorcentajeDescuento DECIMAL(5, 2), -- Porcentaje del descuento
    @FechaInicio DATE, -- Fecha de inicio del descuento
    @FechaFin DATE, -- Fecha de fin del descuento
    @Resultado INT OUTPUT -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @ProductoID INT;

        -- Validar si el DescuentoID es v�lido
        IF NOT EXISTS (SELECT 1 FROM Descuentos WHERE DescuentoID = @DescuentoID)
        BEGIN
            SET @Resultado = -1; -- Error: Descuento no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre del producto es NULL o est� en blanco
        IF @NombreProducto IS NULL OR LTRIM(RTRIM(@NombreProducto)) = ''
        BEGIN
            SET @Resultado = -2; -- Error: Nombre del producto inv�lido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el porcentaje es NULL o menor que 0
        IF @PorcentajeDescuento IS NULL OR @PorcentajeDescuento <= 0
        BEGIN
            SET @Resultado = -2; -- Error: Porcentaje de descuento inv�lido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si la fecha de inicio es NULL o si la fecha de fin es NULL
        IF @FechaInicio IS NULL OR @FechaFin IS NULL
        BEGIN
            SET @Resultado = -2; -- Error: Fechas inv�lidas
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si la fecha de fin es menor que la fecha de inicio
        IF @FechaFin < @FechaInicio
        BEGIN
            SET @Resultado = -3; -- Error: Fecha de fin no puede ser menor que la fecha de inicio
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar si el producto existe y obtener el ProductoID
        SELECT @ProductoID = ProductoID
        FROM Productos
        WHERE Nombre = @NombreProducto;

        IF @ProductoID IS NULL
        BEGIN
            SET @Resultado = -4; -- Error: Producto no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar si ya existen descuentos para el ProductoID en el rango de fechas (excepto el descuento actual)
        IF EXISTS (
            SELECT 1
            FROM Descuentos
            WHERE ProductoID = @ProductoID
              AND DescuentoID <> @DescuentoID
              AND (
                    (@FechaInicio BETWEEN FechaInicio AND FechaFin)
                 OR (@FechaFin BETWEEN FechaInicio AND FechaFin)
                 OR (FechaInicio BETWEEN @FechaInicio AND @FechaFin)
                 OR (FechaFin BETWEEN @FechaInicio AND @FechaFin)
                 )
        )
        BEGIN
            SET @Resultado = -5; -- Error: Ya hay un descuento para este producto en este rango de fechas
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar el descuento
        UPDATE Descuentos
        SET ProductoID = @ProductoID,
            Porcentaje = @PorcentajeDescuento,
            FechaInicio = @FechaInicio,
            FechaFin = @FechaFin
        WHERE DescuentoID = @DescuentoID;

        SET @Resultado = 0; -- Actualizaci�n exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Lanzar un error gen�rico en caso de error en la base de datos
        RAISERROR('Ocurri� un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
