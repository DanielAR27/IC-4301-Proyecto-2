USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdateProvincia
    @EstadoProvinciaID INT,     -- ID del estado/provincia a actualizar
    @Nombre NVARCHAR(100),      -- Nuevo nombre del estado/provincia
    @CodigoPais NVARCHAR(3),    -- C�digo del pa�s asociado
    @Resultado INT OUTPUT       -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el estado/provincia existe
        IF NOT EXISTS (SELECT 1 FROM Estados_Provincias WHERE EstadoProvinciaID = @EstadoProvinciaID)
        BEGIN
            SET @Resultado = -1; -- Estado/provincia no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Buscar el ID del pa�s basado en el c�digo
        DECLARE @PaisID INT;
        SELECT @PaisID = PaisID FROM Paises WHERE Codigo = @CodigoPais;

        -- Validar si el pa�s existe
        IF @PaisID IS NULL
        BEGIN
            SET @Resultado = -2; -- C�digo de pa�s no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre es NULL o est� en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -3; -- Nombre inv�lido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar el estado/provincia
        UPDATE Estados_Provincias
        SET Nombre = @Nombre,
            PaisID = @PaisID
        WHERE EstadoProvinciaID = @EstadoProvinciaID;

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
