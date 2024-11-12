USE DB_Proyecto;
GO
CREATE OR ALTER PROCEDURE GetProductosOrdenadosPorReviews
    @Orden NVARCHAR(11),    -- Parámetro para indicar el orden ("Ascendente" o "Descendente")
    @NumeroPagina INT       -- Parámetro para el número de página
AS
BEGIN
    BEGIN TRY
        -- Calcular el número de filas a omitir
        DECLARE @FilasAOmitir INT;
        SET @FilasAOmitir = @NumeroPagina * 10; -- Número de página * 10 elementos por página

        -- Construir la consulta de selección de productos con el orden dinámico
        DECLARE @SQL NVARCHAR(MAX);
        
        SET @SQL = '
            SELECT P.ProductoID, P.Nombre, P.Precio, P.Imagen,
				CASE WHEN EXISTS (SELECT 1
								  FROM Descuentos D
								  WHERE D.productoID = P.ProductoID
									AND DATEDIFF(SECOND, GETDATE(), D.FechaInicio) <= 0
									AND DATEDIFF(SECOND, GETDATE(), D.FechaFin) >= 0
								  ) THEN ''S''
								  ELSE ''N''
				END AS DescuentoVigente
            FROM Productos P
            WHERE P.Stock > 0
            ORDER BY P.CalificacionPromedio ' + CASE 
                WHEN @Orden = 'Ascendente' THEN 'ASC' 
                WHEN @Orden = 'Descendente' THEN 'DESC' 
                ELSE 'ASC'  -- Valor por defecto en caso de entrada no válida
            END + '
            OFFSET ' + CAST(@FilasAOmitir AS NVARCHAR(10)) + ' ROWS
            FETCH NEXT 10 ROWS ONLY;'
        
        -- Ejecutar la consulta dinámica
        EXEC sp_executesql @SQL;

    END TRY
    BEGIN CATCH
        -- Manejar cualquier error que ocurra
        RAISERROR('Ocurrió un error al obtener los productos ordenados por precio.', 16, 1);
    END CATCH
END;
GO