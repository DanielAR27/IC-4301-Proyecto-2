USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE GetMarcasConID
AS
BEGIN
    -- Iniciar la transacción
    BEGIN TRANSACTION;

    BEGIN TRY
        -- Consulta para obtener MarcaID y Nombre
        SELECT MarcaID, Nombre
        FROM Marcas
        ORDER BY MarcaID;

        -- Confirmar la transacción si todo es exitoso
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Revertir la transacción en caso de error
        ROLLBACK TRANSACTION;

        -- Propagar el error para el manejo del cliente
        THROW;
    END CATCH
END;
GO
