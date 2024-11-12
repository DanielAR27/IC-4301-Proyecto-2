USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE VerificarProductosCompradosPorUsuario
    @UsuarioID INT,          -- Par�metro de entrada para el ID del usuario
    @NumeroPagina INT,       -- Par�metro de entrada para el n�mero de p�gina
    @Resultado INT OUTPUT    -- Par�metro de salida para indicar si hay datos
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- N�mero de p�gina * 10 elementos por p�gina

        -- Verificar si existen productos en la p�gina solicitada
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
        SET @Resultado = -1; -- C�digo de error gen�rico
        RAISERROR('Ocurri� un error al verificar los productos del usuario.', 16, 1);
    END CATCH
END;
GO

BEGIN
    DECLARE @Resultado INT;
    EXEC VerificarProductosCompradosPorUsuario 1, 0, @Resultado;
    PRINT 'El resultado es ' + CAST(@Resultado AS VARCHAR(10));
END;
