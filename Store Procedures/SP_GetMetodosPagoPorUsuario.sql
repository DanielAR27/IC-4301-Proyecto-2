USE DB_Proyecto;
GO
CREATE PROCEDURE GetMetodosPagoPorUsuario
    @UsuarioID INT
AS
BEGIN
    BEGIN TRY
        -- Seleccionar todos los métodos de pago asociados al UsuarioID proporcionado
        SELECT 
            MP.MetodoPagoID,
            MP.NumeroTarjetaCifrado,
            MP.ClaveCifrado,
            MP.NombreTitular,
            MP.FechaExpiracion,
            MP.CodigoSeguridad
        FROM MetodoPago MP
        WHERE MP.UsuarioID = @UsuarioID;
    END TRY
    BEGIN CATCH
        PRINT 'Ocurrió un error al obtener los métodos de pago del usuario.';
    END CATCH
END;
GO


EXEC GetMetodosPagoPorUsuario 1