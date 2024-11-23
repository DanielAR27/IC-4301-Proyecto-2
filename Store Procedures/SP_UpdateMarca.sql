USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdateMarca
    @MarcaID INT,            -- ID de la marca a actualizar
    @Nombre NVARCHAR(100),   -- Nuevo nombre de la marca
    @Resultado INT OUTPUT    -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si la marca existe
        IF NOT EXISTS (SELECT 1 FROM Marcas WHERE MarcaID = @MarcaID)
        BEGIN
            SET @Resultado = -1; -- Marca no encontrada
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

        -- Validar si el nuevo nombre ya está en uso por otra marca
        IF EXISTS (SELECT 1 FROM Marcas WHERE Nombre = @Nombre AND MarcaID <> @MarcaID)
        BEGIN
            SET @Resultado = -3; -- Nombre de marca ya en uso
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar la marca
        UPDATE Marcas
        SET Nombre = @Nombre
        WHERE MarcaID = @MarcaID;

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
