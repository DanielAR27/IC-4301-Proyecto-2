USE DB_Proyecto;
GO

CREATE PROCEDURE UpdateProducto
    @ProductoID INT,
    @Nombre NVARCHAR(100),
    @Descripcion NVARCHAR(500),
    @Precio DECIMAL(10, 2),
    @Stock INT,
    @CategoriaID INT,
    @MarcaID INT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Verificar si el producto existe
        IF NOT EXISTS (SELECT 1 FROM Productos WHERE ProductoID = @ProductoID)
        BEGIN
            RAISERROR ('El producto no existe.', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar que la categoría exista
        IF NOT EXISTS (SELECT 1 FROM Categorias WHERE CategoriaID = @CategoriaID)
        BEGIN
            RAISERROR ('La categoría no existe.', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar que la marca exista
        IF NOT EXISTS (SELECT 1 FROM Marcas WHERE MarcaID = @MarcaID)
        BEGIN
            RAISERROR ('La marca no existe.', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar el producto
        UPDATE Productos
        SET 
            Nombre = @Nombre,
            Descripcion = @Descripcion,
            Precio = @Precio,
            Stock = @Stock,
            CategoriaID = @CategoriaID,
            MarcaID = @MarcaID
        WHERE ProductoID = @ProductoID;

        COMMIT TRANSACTION;

        PRINT 'Producto actualizado exitosamente.';
    END TRY
    BEGIN CATCH
        -- Manejar errores y realizar ROLLBACK en caso de fallo
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Lanza el error con el mensaje original
        DECLARE @ErrorMessage NVARCHAR(4000), @ErrorSeverity INT, @ErrorState INT;
        SELECT 
            @ErrorMessage = ERROR_MESSAGE(), 
            @ErrorSeverity = ERROR_SEVERITY(), 
            @ErrorState = ERROR_STATE();

        RAISERROR (@ErrorMessage, @ErrorSeverity, @ErrorState);
    END CATCH
END;
GO
