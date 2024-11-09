USE DB_Proyecto;
CREATE PROCEDURE GetUsuarioIDPorEmail
    @Email NVARCHAR(100),      -- Parámetro de entrada para el email
    @UsuarioID INT OUTPUT      -- Parámetro de salida para el ID del usuario
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el parámetro de salida en NULL
        SET @UsuarioID = NULL;

        -- Obtener el ID del usuario basado en el email
        SELECT @UsuarioID = UsuarioID
        FROM Usuarios
        WHERE Email = @Email;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Realizar rollback si ocurre un error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Lanzar un error genérico en caso de error en la base de datos
        RAISERROR('Ocurrió un error al obtener el ID del usuario.', 16, 1);
    END CATCH
END;


SELECT *
FROM Usuarios