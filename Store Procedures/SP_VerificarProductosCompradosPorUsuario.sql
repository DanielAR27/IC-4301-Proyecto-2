USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosCompradosPorUsuario
    @UsuarioID INT,          -- Parámetro de entrada para el ID del usuario
    @NumeroPagina INT,       -- Parámetro de entrada para el número de página
    @Resultado INT OUTPUT    -- Parámetro de salida para indicar si hay datos
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Verificar si existen productos en la página solicitada
        IF EXISTS (
            SELECT 1
            FROM LineasFactura LF
            LEFT JOIN Facturas F ON F.FacturaID = LF.FacturaID
            LEFT JOIN Productos P ON P.ProductoID = LF.ProductoID
            WHERE F.UsuarioID = @UsuarioID
            GROUP BY LF.ProductoID, P.Nombre, P.Precio, P.Imagen
            ORDER BY LF.ProductoID
            OFFSET @FilasAOmitir ROWS
            FETCH NEXT 10 ROWS ONLY
        )
        BEGIN
            SET @Resultado = 1; -- Indica que hay datos
        END
        ELSE
        BEGIN
            SET @Resultado = 0; -- Indica que no hay datos
        END
    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        SET @Resultado = -1; -- Código de error genérico
        RAISERROR('Ocurrió un error al verificar los productos del usuario.', 16, 1);
    END CATCH
END;
GO

BEGIN
    DECLARE @Resultado INT;
    EXEC VerificarProductosCompradosPorUsuario 1, 0, @Resultado;
    PRINT 'El resultado es ' + CAST(@Resultado AS VARCHAR(10));
END;
