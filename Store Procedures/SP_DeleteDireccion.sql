USE DB_Proyecto;
GO
ALTER PROCEDURE DeleteDireccion
    @DireccionID INT,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
		BEGIN TRANSACTION; -- Iniciar la transacci�n
        -- Verificar si la direcci�n existe
        IF NOT EXISTS (SELECT 1 FROM Direcciones WHERE DireccionID = @DireccionID)
        BEGIN
            SET @Resultado = -1; -- C�digo de error para direcci�n no encontrada
            RETURN;
        END

        -- Eliminar la direcci�n
        DELETE FROM Direcciones
        WHERE DireccionID = @DireccionID;

        -- Verificar si la eliminaci�n afect� alguna fila
        IF @@ROWCOUNT > 0
        BEGIN
            SET @Resultado = 0; -- Indica �xito en la eliminaci�n
			COMMIT TRANSACTION; -- Confirmar la transacci�n
        END
        ELSE
        BEGIN
            SET @Resultado = -1; -- Indica que no se encontr� la direcci�n (por precauci�n)
			ROLLBACK TRANSACTION; -- Revertir la transacci�n
        END
    END TRY
    BEGIN CATCH
        -- Manejar errores en la base de datos
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION; -- Revertir la transacci�n en caso de error

        -- Manejar errores en la base de datos
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Error al intentar eliminar la direcci�n. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
