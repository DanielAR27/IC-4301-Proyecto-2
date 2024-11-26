USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarReviewsParaUsuario
    @UsuarioID INT,       -- Par�metro de entrada para el ID del usuario
    @NumeroPagina INT,    -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT -- Par�metro de salida para indicar si hay rese�as del usuario
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Verificar si existen rese�as del usuario en la p�gina solicitada
        IF EXISTS (
            SELECT R.ReviewID
            FROM Reviews R
            WHERE R.UsuarioID = @UsuarioID
            ORDER BY R.ReviewID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 10 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay rese�as del usuario en la p�gina
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron rese�as del usuario en la p�gina
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar las rese�as del usuario.', 16, 1);
    END CATCH
END;
GO
