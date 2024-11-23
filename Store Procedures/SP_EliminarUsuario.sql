USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE EliminarUsuario
    @UsuarioID INT,
    @Resultado INT OUTPUT -- Par�metro de salida
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Elimina al usuario
        DELETE FROM Usuarios WHERE UsuarioID = @UsuarioID;

        -- Verifica si se elimin� alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            -- Si no se elimin� ninguna fila, consideramos que no se encontr� el usuario
            ROLLBACK TRANSACTION;
            SET @Resultado = -1; -- Usuario no encontrado
            RETURN;
        END;

        COMMIT TRANSACTION;

        -- Operaci�n exitosa
        SET @Resultado = 0; -- Indica �xito
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