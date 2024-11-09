USE DB_Proyecto;
CREATE PROCEDURE ObtenerPasswordPorEmail
    @Email NVARCHAR(100),
    @Password NVARCHAR(255) OUTPUT -- Parámetro de salida para la contraseña
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el parámetro de salida en NULL
        SET @Password = NULL;

        -- Buscar la contraseña según el email proporcionado
        SELECT @Password = U.Password
        FROM Usuarios U
        WHERE U.Email = @Email;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Realizar rollback si ocurre un error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Lanzar un error genérico en caso de error en la base de datos
        RAISERROR('Ocurrió un error. Intente de nuevo.', 16, 1);
    END CATCH
END;

/*DECLARE @ResultadoPassword NVARCHAR(255);

-- Ejecutar el procedimiento almacenado y capturar el parámetro de salida
EXEC ObtenerPasswordPorEmail 'pedroporro@gmail.com', @ResultadoPassword OUTPUT;

-- Mostrar el resultado
PRINT 'La contraseña es: ' + ISNULL(@ResultadoPassword, 'No se encontró el usuario');


SELECT *
FROM Usuarios */