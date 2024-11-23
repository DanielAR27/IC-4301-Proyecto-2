USE DB_Proyecto;
GO
CREATE PROCEDURE DeleteDescuento
    @DescuentoID INT
AS
BEGIN
    BEGIN TRANSACTION;
    BEGIN TRY
        DELETE FROM Descuentos
        WHERE DescuentoID = @DescuentoID;
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH;
END;
