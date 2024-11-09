USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosDeUsuarioPorPagina
    @UsuarioID INT,
    @NumeroPagina INT, -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT -- Variable de salida para indicar el resultado
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- Número de página * 5 elementos por página

        -- Verificar si hay productos en la página solicitada del carrito del usuario
        IF EXISTS (
            SELECT 1
            FROM CarritoProducto CP
            WHERE CP.CarritoID = (SELECT CarritoID FROM CarritoCompras WHERE UsuarioID = @UsuarioID)
            ORDER BY CP.Linea ASC
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 5 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay al menos un producto en la página
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no se encontraron productos en la página
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico en caso de excepción
        RAISERROR('Ocurrió un error al verificar los productos del carrito.', 16, 1);
    END CATCH
END;
GO


SELECT *
FROM CarritoCompras