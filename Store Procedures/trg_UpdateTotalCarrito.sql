USE DB_Proyecto;
GO
CREATE OR ALTER TRIGGER trg_UpdateTotalCarrito
ON CarritoProducto
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    -- Recalcula el total del carrito para los carritos afectados
    UPDATE CarritoCompras
    SET TotalCarrito = (
        SELECT ISNULL(SUM(LineaTotal), 0.0)
        FROM CarritoProducto
        WHERE CarritoProducto.CarritoID = CarritoCompras.CarritoID
    )
    WHERE CarritoID IN (
        SELECT DISTINCT CarritoID
        FROM (
            -- Identificar carritos afectados por INSERT, UPDATE o DELETE
            SELECT CarritoID FROM INSERTED
            UNION
            SELECT CarritoID FROM DELETED
        ) AS Afectados
    );
END;