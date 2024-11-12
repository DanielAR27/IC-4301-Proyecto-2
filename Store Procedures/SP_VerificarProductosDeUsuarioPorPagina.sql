USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosDeUsuarioPorPagina
    @UsuarioID INT,
    @NumeroPagina INT, -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT -- Variable de salida para indicar el resultado
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- N�mero de p�gina * 5 elementos por p�gina

        -- Verificar si hay productos en la p�gina solicitada del carrito del usuario
        IF EXISTS (
            SELECT 1
            FROM CarritoProducto CP
            WHERE CP.CarritoID = (SELECT CarritoID FROM CarritoCompras WHERE UsuarioID = @UsuarioID)
            ORDER BY CP.Linea ASC
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 5 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay al menos un producto en la p�gina
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron productos en la p�gina
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- C�digo de error gen�rico en caso de excepci�n
        RAISERROR('Ocurri� un error al verificar los productos del carrito.', 16, 1);
    END CATCH
END;
GO


SELECT *
FROM CarritoCompras