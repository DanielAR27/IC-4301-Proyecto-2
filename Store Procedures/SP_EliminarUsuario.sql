CREATE PROCEDURE EliminarUsuario
    @UsuarioID INT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Aqu� ejecutamos la eliminaci�n del usuario y cualquier otra limpieza adicional si fuera necesario
        DELETE FROM Usuarios WHERE UsuarioID = @UsuarioID;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        -- Opcional: puedes registrar el error o devolver un mensaje espec�fico si lo necesitas
        THROW;
    END CATCH;
END;


