USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE CrearCategoria
    @Nombre NVARCHAR(100), -- Nombre de la categor�a
    @Resultado INT OUTPUT   -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el nombre de la categor�a ya existe
        IF EXISTS (SELECT 1 FROM Categorias WHERE Nombre = @Nombre)
        BEGIN
            SET @Resultado = -1; -- Nombre de la categor�a ya existe
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

        -- Insertar la nueva categor�a
        INSERT INTO Categorias (Nombre)
        VALUES (@Nombre);

        SET @Resultado = 0; -- Inserci�n exitosa

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
