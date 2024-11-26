USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarReviewsParaUsuario
    @UsuarioID INT,       -- Parámetro de entrada para el ID del usuario
    @NumeroPagina INT,    -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT -- Parámetro de salida para indicar si hay reseñas del usuario
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Verificar si existen reseñas del usuario en la página solicitada
        IF EXISTS (
            SELECT R.ReviewID
            FROM Reviews R
            WHERE R.UsuarioID = @UsuarioID
            ORDER BY R.ReviewID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 10 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay reseñas del usuario en la página
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron reseñas del usuario en la página
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar las reseñas del usuario.', 16, 1);
    END CATCH
END;
GO
