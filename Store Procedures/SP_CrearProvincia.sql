USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE CrearProvincia
    @Nombre NVARCHAR(100),       -- Nombre de la provincia
    @CodigoPais NVARCHAR(3),     -- C�digo del pa�s asociado
    @Resultado INT OUTPUT        -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Buscar el ID del pa�s basado en el c�digo
        DECLARE @PaisID INT;
        SELECT @PaisID = PaisID FROM Paises WHERE Codigo = @CodigoPais;

        -- Validar si el pa�s existe
        IF @PaisID IS NULL
        BEGIN
            SET @Resultado = -1; -- C�digo de pa�s no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre de la provincia es NULL o est� en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -2; -- Nombre inv�lido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Insertar la nueva provincia
        INSERT INTO Estados_Provincias (PaisID, Nombre)
        VALUES (@PaisID, @Nombre);

        SET @Resultado = 0; -- Inserci�n exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar excepciones
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Lanzar un error gen�rico en caso de error en la base de datos
        RAISERROR('Ocurri� un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
