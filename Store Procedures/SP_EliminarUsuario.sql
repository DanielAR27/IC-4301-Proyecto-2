USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE EliminarUsuario
    @UsuarioID INT,
    @Resultado INT OUTPUT -- Parámetro de salida
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Elimina al usuario
        DELETE FROM Usuarios WHERE UsuarioID = @UsuarioID;

        -- Verifica si se eliminó alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            -- Si no se eliminó ninguna fila, consideramos que no se encontró el usuario
            ROLLBACK TRANSACTION;
            SET @Resultado = -1; -- Usuario no encontrado
            RETURN;
        END;

        COMMIT TRANSACTION;

        -- Operación exitosa
        SET @Resultado = 0; -- Indica éxito
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;

        -- Manejo de errores
        SET @Resultado = -1; -- Indica un fallo
    END CATCH
END;


select *
from Descuentos

SELECT *
FROM Productos