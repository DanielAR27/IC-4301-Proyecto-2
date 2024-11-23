USE DB_Proyecto;
GO
CREATE OR ALTER TRIGGER trg_UpdateTotalFactura
ON LineasFactura
AFTER UPDATE, DELETE
AS
BEGIN
    -- Recalcula el total de la factura para las facturas afectados
    UPDATE Facturas
    SET Total = (
        SELECT ISNULL(SUM(LineaTotal), 0.0)
        FROM LineasFactura
        WHERE LineasFactura.FacturaID = Facturas.FacturaID
    )
    WHERE FacturaID IN (
        SELECT DISTINCT FacturaID
        FROM (
            -- Identificar facturas afectadas por UPDATE o DELETE
            SELECT FacturaID FROM INSERTED
            UNION
            SELECT FacturaID FROM DELETED
        ) AS Afectados
    );
END;