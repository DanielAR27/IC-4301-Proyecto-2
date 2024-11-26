USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE UpsertReview
    @ReviewID INT = NULL,       -- Par�metro opcional: si es NULL, inserta; si no, actualiza
    @UsuarioID INT,             -- ID del usuario que escribe la rese�a
    @ProductoID INT,            -- ID del producto rese�ado
    @Calificacion INT,          -- Calificaci�n de la rese�a
    @Comentario NVARCHAR(1000), -- Comentario de la rese�a
    @Resultado INT OUTPUT       -- Resultado del procedimiento
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (�xito por defecto)
        SET @Resultado = 0;

        -- Validar que la calificaci�n est� en el rango permitido
        IF @Calificacion < 1 OR @Calificacion > 5
        BEGIN
            SET @Resultado = -1; -- Calificaci�n fuera de rango
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Si el comentario es vac�o, asignar "Sin comentarios"
        IF @Comentario = ''
        BEGIN
            SET @Comentario = 'Sin comentarios';
        END

        -- Comprobar si es un INSERT o un UPDATE basado en @ReviewID
        IF @ReviewID IS NULL
        BEGIN
            -- Insertar una nueva rese�a
            INSERT INTO Reviews (UsuarioID, ProductoID, Calificacion, Comentario)
            VALUES (@UsuarioID, @ProductoID, @Calificacion, @Comentario);

            -- Verificar si la inserci�n fue exitosa
            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -2; -- Error inesperado en la inserci�n
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 1; -- C�digo para inserci�n exitosa
        END
        ELSE
        BEGIN
            -- Actualizar una rese�a existente
            UPDATE Reviews
            SET UsuarioID = @UsuarioID,
                ProductoID = @ProductoID,
                Calificacion = @Calificacion,
                Comentario = @Comentario
            WHERE ReviewID = @ReviewID;

            -- Verificar si la actualizaci�n afect� alguna fila
            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -2; -- Error inesperado en la actualizaci�n o ReviewID no encontrado
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 2; -- C�digo para actualizaci�n exitosa
        END

        -- Confirmar la transacci�n
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Revertir la transacci�n en caso de error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        RAISERROR('Ocurri� un error al realizar el upsert de la rese�a.', 16, 1);
    END CATCH
END;
GO
