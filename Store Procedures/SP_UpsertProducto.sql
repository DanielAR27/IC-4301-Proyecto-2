USE DB_Proyecto;
GO
CREATE PROCEDURE UpsertProducto
    @ProductoID INT = NULL, -- Puede ser NULL para un nuevo producto
    @Nombre NVARCHAR(100),
    @Descripcion NVARCHAR(500),
    @Precio DECIMAL(10, 2),
    @Stock DECIMAL(10, 2),
    @Categoria NVARCHAR(100),
    @Marca NVARCHAR(100),
    @Imagen NVARCHAR(255),
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (éxito por defecto)
        SET @Resultado = 0;

        -- Verificar que ningún campo obligatorio sea NULL o esté en blanco
        IF @Nombre IS NULL OR @Nombre = '' OR
           @Precio IS NULL OR @Precio <= 0 OR
           @Stock IS NULL OR @Stock < 0 OR
           @Categoria IS NULL OR @Categoria = '' OR
           @Marca IS NULL OR @Marca = '' OR
           @Imagen IS NULL OR @Imagen = ''
        BEGIN
            SET @Resultado = -1; -- Código de error para campos obligatorios nulos o en blanco
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Obtener el CategoriaID por el nombre de la categoría
        DECLARE @CategoriaID INT;
        SELECT @CategoriaID = CategoriaID
        FROM Categorias
        WHERE Nombre = @Categoria;

        IF @CategoriaID IS NULL
        BEGIN
            SET @Resultado = -2; -- Código de error para categoría no encontrada
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Obtener el MarcaID por el nombre de la marca
        DECLARE @MarcaID INT;
        SELECT @MarcaID = MarcaID
        FROM Marcas
        WHERE Nombre = @Marca;

        IF @MarcaID IS NULL
        BEGIN
            SET @Resultado = -3; -- Código de error para marca no encontrada
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar si se debe hacer un INSERT o UPDATE
        IF @ProductoID IS NOT NULL
        BEGIN
            -- Actualizar producto existente
            UPDATE Productos
            SET Nombre = @Nombre,
                Descripcion = @Descripcion,
                Precio = @Precio,
                Stock = @Stock,
                CategoriaID = @CategoriaID,
                MarcaID = @MarcaID,
                Imagen = @Imagen
            WHERE ProductoID = @ProductoID;

            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -4; -- Código de error para producto no encontrado
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 0; -- Código para actualización exitosa
        END
        ELSE
        BEGIN
            -- Insertar nuevo producto
            INSERT INTO Productos (Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen)
            VALUES (@Nombre, @Descripcion, @Precio, @Stock, @CategoriaID, @MarcaID, @Imagen);

            SET @Resultado = 1; -- Código para inserción exitosa
        END

        -- Confirmar la transacción
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Realizar rollback si ocurre un error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Lanzar un error genérico en caso de error en la base de datos
        RAISERROR('Ocurrió un error al procesar el producto. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO