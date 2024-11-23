USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE CrearProvincia
    @Nombre NVARCHAR(100),       -- Nombre de la provincia
    @CodigoPais NVARCHAR(3),     -- Código del país asociado
    @Resultado INT OUTPUT        -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Buscar el ID del país basado en el código
        DECLARE @PaisID INT;
        SELECT @PaisID = PaisID FROM Paises WHERE Codigo = @CodigoPais;

        -- Validar si el país existe
        IF @PaisID IS NULL
        BEGIN
            SET @Resultado = -1; -- Código de país no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre de la provincia es NULL o está en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -2; -- Nombre inválido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Insertar la nueva provincia
        INSERT INTO Estados_Provincias (PaisID, Nombre)
        VALUES (@PaisID, @Nombre);

        SET @Resultado = 0; -- Inserción exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar excepciones
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Lanzar un error genérico en caso de error en la base de datos
        RAISERROR('Ocurrió un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
