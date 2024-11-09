USE DB_Proyecto;
GO
CREATE PROCEDURE UpsertMetodoPago
    @MetodoPagoID INT = NULL, -- Puede ser NULL para un nuevo m�todo de pago
    @UsuarioID INT,
    @NumeroTarjetaCifrado NVARCHAR(256),
    @ClaveCifrado NVARCHAR(256),
    @NombreTitular NVARCHAR(100),
    @FechaExpiracion DATE,
    @CodigoSeguridad NVARCHAR(4),
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (�xito por defecto)
        SET @Resultado = 0;

        -- Verificar que ning�n campo obligatorio sea NULL o est� en blanco
        IF @UsuarioID IS NULL OR
           @NumeroTarjetaCifrado IS NULL OR @NumeroTarjetaCifrado = '' OR
           @ClaveCifrado IS NULL OR @ClaveCifrado = '' OR
           @NombreTitular IS NULL OR @NombreTitular = '' OR
           @FechaExpiracion IS NULL OR
           @CodigoSeguridad IS NULL OR @CodigoSeguridad = ''
        BEGIN
            SET @Resultado = -1; -- C�digo de error para campos obligatorios nulos o en blanco
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar si se debe hacer un INSERT o UPDATE
        IF @MetodoPagoID IS NOT NULL
        BEGIN
            -- Actualizar m�todo de pago existente
            UPDATE MetodoPago
            SET NumeroTarjetaCifrado = @NumeroTarjetaCifrado,
                ClaveCifrado = @ClaveCifrado,
                NombreTitular = @NombreTitular,
                FechaExpiracion = @FechaExpiracion,
                CodigoSeguridad = @CodigoSeguridad
            WHERE MetodoPagoID = @MetodoPagoID AND UsuarioID = @UsuarioID;

            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -2; -- C�digo de error para m�todo de pago no encontrado o no pertenece al usuario
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 0; -- C�digo para actualizaci�n exitosa
        END
        ELSE
        BEGIN
            -- Insertar nuevo m�todo de pago
            INSERT INTO MetodoPago (UsuarioID, NumeroTarjetaCifrado, ClaveCifrado, NombreTitular, FechaExpiracion, CodigoSeguridad)
            VALUES (@UsuarioID, @NumeroTarjetaCifrado, @ClaveCifrado, @NombreTitular, @FechaExpiracion, @CodigoSeguridad);

            SET @Resultado = 1; -- C�digo para inserci�n exitosa
        END

        -- Confirmar la transacci�n
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Realizar rollback si ocurre un error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Lanzar un error gen�rico en caso de error en la base de datos
        RAISERROR('Ocurri� un error. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO