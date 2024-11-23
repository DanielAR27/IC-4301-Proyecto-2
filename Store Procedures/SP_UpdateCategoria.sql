USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdateCategoria
    @CategoriaID INT,        -- ID de la categor�a a actualizar
    @Nombre NVARCHAR(100),   -- Nuevo nombre de la categor�a
    @Resultado INT OUTPUT    -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si la categor�a existe
        IF NOT EXISTS (SELECT 1 FROM Categorias WHERE CategoriaID = @CategoriaID)
        BEGIN
            SET @Resultado = -1; -- Categor�a no encontrada
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre es NULL o est� en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -2; -- Nombre inv�lido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nuevo nombre ya est� en uso por otra categor�a
        IF EXISTS (SELECT 1 FROM Categorias WHERE Nombre = @Nombre AND CategoriaID <> @CategoriaID)
        BEGIN
            SET @Resultado = -3; -- Nombre de categor�a ya en uso
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar la categor�a
        UPDATE Categorias
        SET Nombre = @Nombre
        WHERE CategoriaID = @CategoriaID;

        SET @Resultado = 0; -- Edici�n exitosa

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

SELECT * FROM Categorias
