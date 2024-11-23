USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE CrearPais
    @Codigo NVARCHAR(3),         -- Código del país
    @Nombre NVARCHAR(100),       -- Nombre del país
    @CostoEnvio DECIMAL(10, 2),  -- Costo de envío del país
    @Resultado INT OUTPUT        -- Variable de salida para el resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar si el código ya existe
        IF EXISTS (SELECT 1 FROM Paises WHERE Codigo = @Codigo)
        BEGIN
            SET @Resultado = -1; -- Código ya existe
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el nombre es NULL o está en blanco
        IF @Nombre IS NULL OR LTRIM(RTRIM(@Nombre)) = ''
        BEGIN
            SET @Resultado = -2; -- Error de validación (nombre inválido)
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Validar si el costo de envío es NULL o menor que 0
        IF @CostoEnvio IS NULL OR @CostoEnvio < 0
        BEGIN
            SET @Resultado = -2; -- Error de validación (costo inválido)
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Insertar el país
        INSERT INTO Paises (Codigo, Nombre, CostoEnvio)
        VALUES (@Codigo, @Nombre, @CostoEnvio);

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
