USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetEnvioPorDireccionID
    @DireccionID INT,
    @CostoEnvio DECIMAL(10, 2) OUTPUT
AS
BEGIN
    BEGIN TRY
        -- Verificar si existe la dirección antes de intentar obtener el costo de envío
        IF EXISTS (SELECT 1 FROM Direcciones WHERE DireccionID = @DireccionID)
        BEGIN
            SELECT @CostoEnvio = P.CostoEnvio
            FROM Paises P
            JOIN Direcciones D ON P.PaisID = D.PaisID
            WHERE D.DireccionID = @DireccionID;
        END
        ELSE
        BEGIN
            -- Si la dirección no existe, establecer un valor de error
            SET @CostoEnvio = -1;
            RAISERROR('No se encontró una dirección con el ID especificado.', 16, 1);
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en caso de que ocurra un problema
        PRINT 'Ocurrió un error al obtener el costo de envío. Verifique que el DireccionID sea correcto.';
        SET @CostoEnvio = -1; -- Código de error general
    END CATCH
END;
GO
