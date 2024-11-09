USE DB_Proyecto;
CREATE PROCEDURE ObtenerPasswordPorEmail
    @Email NVARCHAR(100),
    @Password NVARCHAR(255) OUTPUT -- Par�metro de salida para la contrase�a
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el par�metro de salida en NULL
        SET @Password = NULL;

        -- Buscar la contrase�a seg�n el email proporcionado
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

        -- Lanzar un error gen�rico en caso de error en la base de datos
        RAISERROR('Ocurri� un error. Intente de nuevo.', 16, 1);
    END CATCH
END;

/*DECLARE @ResultadoPassword NVARCHAR(255);

-- Ejecutar el procedimiento almacenado y capturar el par�metro de salida
EXEC ObtenerPasswordPorEmail 'pedroporro@gmail.com', @ResultadoPassword OUTPUT;

-- Mostrar el resultado
PRINT 'La contrase�a es: ' + ISNULL(@ResultadoPassword, 'No se encontr� el usuario');


SELECT *
FROM Usuarios */