CREATE PROCEDURE EliminarUsuario
    @UsuarioID INT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Aquí ejecutamos la eliminación del usuario y cualquier otra limpieza adicional si fuera necesario
        DELETE FROM Usuarios WHERE UsuarioID = @UsuarioID;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        -- Opcional: puedes registrar el error o devolver un mensaje específico si lo necesitas
        THROW;
    END CATCH;
END;


