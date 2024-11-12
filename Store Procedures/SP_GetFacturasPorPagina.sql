USE DB_Proyecto;
GO

CREATE OR ALTER PROCEDURE GetFacturasPorPagina
    @UsuarioID INT,
    @NumeroPagina INT -- Par�metro para la paginaci�n
AS
BEGIN
    BEGIN TRY
        -- Calcular el n�mero de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 5; -- N�mero de p�gina * 5 elementos por p�gina

        -- Seleccionar las 5 facturas correspondientes a la p�gina
        SELECT 
            FacturaID,
            FechaFactura,
            Total,
            CostoEnvio
        FROM 
            Facturas
        WHERE 
            UsuarioID = @UsuarioID
        ORDER BY 
            FacturaID ASC
        OFFSET @FilasAOmitir ROWS
        FETCH NEXT 5 ROWS ONLY; -- Obtener los pr�ximos 5 elementos

    END TRY
    BEGIN CATCH
        -- Manejar errores que puedan ocurrir
        RAISERROR('Ocurri� un error al obtener las facturas del usuario.', 16, 1);
    END CATCH
END;
GO

-- Ejemplo de ejecuci�n
EXEC GetFacturasPorPagina @UsuarioID = 1, @NumeroPagina = 0;


SELECT LF.LineaFacturaID, LF.ProductoID, LF.ProductoNombre,
	P.Descripcion, LF.Cantidad,
	LF.PrecioOriginal, LF.DescuentoAplicado, LF.LineaTotal,
	P.Imagen
FROM LineasFactura LF
LEFT JOIN Productos P ON LF.ProductoID= P.ProductoID
WHERE LF.FacturaID = 1