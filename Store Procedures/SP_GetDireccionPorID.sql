CREATE PROCEDURE GetDireccionPorID
    @DireccionID INT
AS
BEGIN
    BEGIN TRY
        -- Seleccionar todas las direcciones asociadas al UsuarioID proporcionado
        SELECT 
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
        WHERE D.DireccionID = @DireccionID;
    END TRY
    BEGIN CATCH
        PRINT 'Ocurri� un error al obtener las direcciones del usuario.';
    END CATCH
END;

EXEC GetDireccionPorID 1

EXEC GetDireccionesPorUsuario 1
