USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetRolPorUsuarioID
    @UsuarioID INT,
    @Resultado NVARCHAR(50) OUTPUT
AS
BEGIN
    SELECT @Resultado = Rol
    FROM Usuarios
    WHERE UsuarioID = @UsuarioID;
END;
GO

UPDATE Usuarios SET Rol = 'Administrador' where UsuarioID = 1;