ALTER PROCEDURE GetDireccionesPorUsuario
    @UsuarioID INT
AS
BEGIN
    BEGIN TRY
        -- Seleccionar todas las direcciones asociadas al UsuarioID proporcionado
        SELECT 
			D.DireccionID, 
            P.Nombre AS PaisNombre,
            EP.Nombre AS EstadoProvinciaNombre,
            D.DireccionLinea1,
            D.DireccionLinea2,
            D.Ciudad,
            D.CodigoPostal,
            D.Contacto
        FROM Direcciones D
        JOIN Paises P ON D.PaisID = P.PaisID
        JOIN Estados_Provincias EP ON D.EstadoProvinciaID = EP.EstadoProvinciaID
        WHERE D.UsuarioID = @UsuarioID;
    END TRY
    BEGIN CATCH
        PRINT 'Ocurrió un error al obtener las direcciones del usuario.';
    END CATCH
END;

SELECT *
FROM Usuarios


EXEC GetDireccionesPorUsuario 1
