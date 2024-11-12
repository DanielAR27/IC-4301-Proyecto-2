USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetRolPorUsuarioID
	@UsuarioID INT
AS
BEGIN
    SELECT Rol
    FROM Usuarios
	Where UsuarioID = @UsuarioID
END;
GO