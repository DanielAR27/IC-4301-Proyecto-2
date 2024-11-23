USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdatePais
    @PaisID INT,                -- ID del pa�s a editar
    @Codigo NVARCHAR(3),        -- Nuevo c�digo del pa�s
    @Nombre NVARCHAR(100),      -- Nuevo nombre del pa�s
    @CostoEnvio DECIMAL(10, 2), -- Nuevo costo de env�o
    @Resultado INT OUTPUT       -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el pa�s existe
        IF NOT EXISTS (SELECT 1 FROM Paises WHERE PaisID = @PaisID)
        BEGIN
            SET @Resultado = -1; -- Pa�s no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el c�digo ya existe para otro pa�s
        IF EXISTS (SELECT 1 FROM Paises WHERE Codigo = @Codigo AND PaisID <> @PaisID)
        BEGIN
            SET @Resultado = -2; -- C�digo ya usado por otro pa�s
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

        -- Validar si el costo de env�o es NULL o menor que 0
        IF @CostoEnvio IS NULL OR @CostoEnvio < 0
        BEGIN
            SET @Resultado = -3; -- Costo de env�o inv�lido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar el pa�s
        UPDATE Paises
        SET Codigo = @Codigo,
            Nombre = @Nombre,
            CostoEnvio = @CostoEnvio
        WHERE PaisID = @PaisID;

        SET @Resultado = 0; -- Edici�n exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
