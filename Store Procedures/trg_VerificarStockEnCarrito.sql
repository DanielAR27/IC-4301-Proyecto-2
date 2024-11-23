USE DB_Proyecto;
GO
CREATE TRIGGER VerificarStockEnCarrito
ON CarritoProducto
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Verificar si la cantidad de producto en el carrito excede el stock disponible
    IF EXISTS (
        SELECT 1
        FROM INSERTED I
        JOIN Productos P ON I.ProductoID = P.ProductoID
        WHERE I.Cantidad > P.Stock
    )
    BEGIN
        -- Lanzar un error para que sea capturado por el procedimiento
        THROW 50001, 'Error: La cantidad solicitada excede el stock disponible.', 1;
    END
END;
GO