USE DB_Proyecto;
GO
CREATE PROCEDURE GetFacturaPorID
    @FacturaID INT
AS
BEGIN
    BEGIN TRY
        -- Seleccionar todos los métodos de pago asociados al UsuarioID proporcionado
        SELECT 
			F.FechaFactura,
			F.Total,
			F.CostoEnvio,
			F.Estado
        FROM Facturas F
        WHERE F.FacturaID = @FacturaID;
    END TRY
    BEGIN CATCH
        PRINT 'Ocurrió un error al obtener los métodos de pago del usuario.';
    END CATCH
END;
GO