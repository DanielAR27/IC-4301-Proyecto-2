USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE CrearMarca
    @Nombre NVARCHAR(100), -- Nombre de la marca
    @Resultado INT OUTPUT   -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el nombre de la marca ya existe
        IF EXISTS (SELECT 1 FROM Marcas WHERE Nombre = @Nombre)
        BEGIN
            SET @Resultado = -1; -- Nombre de la marca ya existe
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

        -- Insertar la nueva marca
        INSERT INTO Marcas (Nombre)
        VALUES (@Nombre);

        SET @Resultado = 0; -- Inserción exitosa

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
