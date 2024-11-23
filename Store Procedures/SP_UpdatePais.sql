USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE UpdatePais
    @PaisID INT,                -- ID del país a editar
    @Codigo NVARCHAR(3),        -- Nuevo código del país
    @Nombre NVARCHAR(100),      -- Nuevo nombre del país
    @CostoEnvio DECIMAL(10, 2), -- Nuevo costo de envío
    @Resultado INT OUTPUT       -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el país existe
        IF NOT EXISTS (SELECT 1 FROM Paises WHERE PaisID = @PaisID)
        BEGIN
            SET @Resultado = -1; -- País no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el código ya existe para otro país
        IF EXISTS (SELECT 1 FROM Paises WHERE Codigo = @Codigo AND PaisID <> @PaisID)
        BEGIN
            SET @Resultado = -2; -- Código ya usado por otro país
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre es NULL o está en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -3; -- Nombre inválido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el costo de envío es NULL o menor que 0
        IF @CostoEnvio IS NULL OR @CostoEnvio < 0
        BEGIN
            SET @Resultado = -3; -- Costo de envío inválido
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Actualizar el país
        UPDATE Paises
        SET Codigo = @Codigo,
            Nombre = @Nombre,
            CostoEnvio = @CostoEnvio
        WHERE PaisID = @PaisID;

        SET @Resultado = 0; -- Edición exitosa

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Código de error genérico
        RAISERROR('Ocurrió un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
