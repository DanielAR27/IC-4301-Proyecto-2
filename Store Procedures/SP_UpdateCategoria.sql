USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdateCategoria
    @CategoriaID INT,        -- ID de la categoría a actualizar
    @Nombre NVARCHAR(100),   -- Nuevo nombre de la categoría
    @Resultado INT OUTPUT    -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si la categoría existe
        IF NOT EXISTS (SELECT 1 FROM Categorias WHERE CategoriaID = @CategoriaID)
        BEGIN
            SET @Resultado = -1; -- Categoría no encontrada
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre es NULL o está en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -2; -- Nombre inválido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nuevo nombre ya está en uso por otra categoría
        IF EXISTS (SELECT 1 FROM Categorias WHERE Nombre = @Nombre AND CategoriaID <> @CategoriaID)
        BEGIN
            SET @Resultado = -3; -- Nombre de categoría ya en uso
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar la categoría
        UPDATE Categorias
        SET Nombre = @Nombre
        WHERE CategoriaID = @CategoriaID;

        SET @Resultado = 0; -- Edición exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Lanzar un error genérico en caso de error en la base de datos
        RAISERROR('Ocurrió un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO

SELECT * FROM Categorias
