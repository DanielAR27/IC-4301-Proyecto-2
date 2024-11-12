USE DB_Proyecto;
GO
CREATE PROCEDURE GetMetodoPagoPorID
    @MetodoPagoID INT
AS
BEGIN
    BEGIN TRY
        -- Seleccionar todos los m�todos de pago asociados al UsuarioID proporcionado
        SELECT 
            MP.NumeroTarjetaCifrado,
            MP.ClaveCifrado,
            MP.NombreTitular,
            MP.FechaExpiracion,
            MP.CodigoSeguridad
        FROM MetodoPago MP
        WHERE MP.MetodoPagoID = @MetodoPagoID;
    END TRY
    BEGIN CATCH
        PRINT 'Ocurri� un error al obtener los m�todos de pago del usuario.';
    END CATCH
END;
GO

EXEC GetMetodoPagoPorID 1