USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE UpsertReview
    @ReviewID INT = NULL,       -- Parámetro opcional: si es NULL, inserta; si no, actualiza
    @UsuarioID INT,             -- ID del usuario que escribe la reseña
    @ProductoID INT,            -- ID del producto reseñado
    @Calificacion INT,          -- Calificación de la reseña
    @Comentario NVARCHAR(1000), -- Comentario de la reseña
    @Resultado INT OUTPUT       -- Resultado del procedimiento
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (éxito por defecto)
        SET @Resultado = 0;

        -- Validar que la calificación esté en el rango permitido
        IF @Calificacion < 1 OR @Calificacion > 5
        BEGIN
            SET @Resultado = -1; -- Calificación fuera de rango
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Si el comentario es vacío, asignar "Sin comentarios"
        IF @Comentario = ''
        BEGIN
            SET @Comentario = 'Sin comentarios';
        END

        -- Comprobar si es un INSERT o un UPDATE basado en @ReviewID
        IF @ReviewID IS NULL
        BEGIN
            -- Insertar una nueva reseña
            INSERT INTO Reviews (UsuarioID, ProductoID, Calificacion, Comentario)
            VALUES (@UsuarioID, @ProductoID, @Calificacion, @Comentario);

            -- Verificar si la inserción fue exitosa
            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -2; -- Error inesperado en la inserción
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 1; -- Código para inserción exitosa
        END
        ELSE
        BEGIN
            -- Actualizar una reseña existente
            UPDATE Reviews
            SET UsuarioID = @UsuarioID,
                ProductoID = @ProductoID,
                Calificacion = @Calificacion,
                Comentario = @Comentario
            WHERE ReviewID = @ReviewID;

            -- Verificar si la actualización afectó alguna fila
            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -2; -- Error inesperado en la actualización o ReviewID no encontrado
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 2; -- Código para actualización exitosa
        END

        -- Confirmar la transacción
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Revertir la transacción en caso de error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        RAISERROR('Ocurrió un error al realizar el upsert de la reseña.', 16, 1);
    END CATCH
END;
GO
