CREATE PROCEDURE ObtenerDatosUsuario
    @usuarioID INT
AS
BEGIN
    SELECT Nombre, Apellido, Email, Telefono, FechaNacimiento
    FROM Usuarios
    WHERE UsuarioID = @usuarioID;
END;
