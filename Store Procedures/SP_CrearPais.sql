USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE CrearPais
    @Codigo NVARCHAR(3),         -- C�digo del pa�s
    @Nombre NVARCHAR(100),       -- Nombre del pa�s
    @CostoEnvio DECIMAL(10, 2),  -- Costo de env�o del pa�s
    @Resultado INT OUTPUT        -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el c�digo ya existe
        IF EXISTS (SELECT 1 FROM Paises WHERE Codigo = @Codigo)
        BEGIN
            SET @Resultado = -1; -- C�digo ya existe
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre es NULL o est� en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -2; -- Error de validaci�n (nombre inv�lido)
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el costo de env�o es NULL o menor que 0
        IF @CostoEnvio IS NULL OR @CostoEnvio < 0
        BEGIN
            SET @Resultado = -2; -- Error de validaci�n (costo inv�lido)
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Insertar el pa�s
        INSERT INTO Paises (Codigo, Nombre, CostoEnvio)
        VALUES (@Codigo, @Nombre, @CostoEnvio);

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
