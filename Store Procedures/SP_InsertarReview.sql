USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE InsertarReview
    @UsuarioID INT, 
    @ProductoID INT, 
    @Calificacion INT, 
    @Comentario NVARCHAR(1000),
    @Resultado INT OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Verificar si la calificación está en el rango permitido
        IF @Calificacion < 1 OR @Calificacion > 5
        BEGIN
            SET @Resultado = -1;
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Si el comentario es vacío, asignar "Sin comentarios"
        IF @Comentario = ''
        BEGIN
            SET @Comentario = 'Sin comentarios';
        END

        -- Insertar la nueva reseña
        INSERT INTO Reviews (UsuarioID, ProductoID, Calificacion, Comentario)
        VALUES (@UsuarioID, @ProductoID, @Calificacion, @Comentario);

        -- Establecer el resultado como exitoso
        SET @Resultado = 1;
        
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Manejar errores y revertir la transacción
        ROLLBACK TRANSACTION;
        SET @Resultado = -2; -- Código de error genérico
        RAISERROR('Ocurrió un error al insertar la reseña.', 16, 1);
    END CATCH
END;
GO
