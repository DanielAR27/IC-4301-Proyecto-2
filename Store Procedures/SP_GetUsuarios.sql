USE [DB_Proyecto]
GO
CREATE PROCEDURE GetUsuarios
AS
BEGIN
    BEGIN TRANSACTION;
    BEGIN TRY
        SELECT UsuarioID, Nombre, Apellido, Rol
        FROM Usuarios;
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO
