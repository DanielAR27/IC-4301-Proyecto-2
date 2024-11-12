USE DB_Proyecto;
GO
CREATE PROCEDURE UpsertDireccionUsuario
    @DireccionID INT = NULL, -- Puede ser NULL para una nueva direcci�n
    @UsuarioID INT,
    @DireccionLinea1 NVARCHAR(255),
    @DireccionLinea2 NVARCHAR(255),
    @NombrePais NVARCHAR(100),
    @EstadoProvincia NVARCHAR(100),
    @Ciudad NVARCHAR(100),
    @CodigoPostal NVARCHAR(20),
    @Contacto NVARCHAR(100),
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (�xito por defecto)
        SET @Resultado = 0;

        -- Verificar que ning�n campo obligatorio sea NULL o est� en blanco
        IF @UsuarioID IS NULL OR
           @DireccionLinea1 IS NULL OR @DireccionLinea1 = '' OR
           @DireccionLinea2 IS NULL OR @DireccionLinea2 = '' OR
           @NombrePais IS NULL OR @NombrePais = '' OR
           @EstadoProvincia IS NULL OR @EstadoProvincia = '' OR
           @Ciudad IS NULL OR @Ciudad = '' OR
           @CodigoPostal IS NULL OR @CodigoPostal = '' OR
           @Contacto IS NULL OR @Contacto = ''
        BEGIN
            SET @Resultado = -1; -- C�digo de error para campos obligatorios nulos o en blanco
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Obtener el PaisID por el nombre del pa�s
        DECLARE @PaisID INT;
        SELECT @PaisID = PaisID
        FROM Paises
        WHERE Nombre = @NombrePais;

        IF @PaisID IS NULL
        BEGIN
            SET @Resultado = -2; -- C�digo de error para pa�s no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Obtener el EstadoProvinciaID por el PaisID y nombre del estado/provincia
        DECLARE @EstadoProvinciaID INT;
        SELECT @EstadoProvinciaID = EstadoProvinciaID
        FROM Estados_Provincias
        WHERE PaisID = @PaisID AND Nombre = @EstadoProvincia;

        IF @EstadoProvinciaID IS NULL
        BEGIN
            SET @Resultado = -3; -- C�digo de error para estado/provincia no encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar si se debe hacer un INSERT o UPDATE
        IF @DireccionID IS NOT NULL
        BEGIN
            -- Actualizar direcci�n existente
            UPDATE Direcciones
            SET DireccionLinea1 = @DireccionLinea1,
                DireccionLinea2 = @DireccionLinea2,
                PaisID = @PaisID,
                EstadoProvinciaID = @EstadoProvinciaID,
                Ciudad = @Ciudad,
                CodigoPostal = @CodigoPostal,
                Contacto = @Contacto
            WHERE DireccionID = @DireccionID AND UsuarioID = @UsuarioID;

            IF @@ROWCOUNT = 0
            BEGIN
                SET @Resultado = -4; -- C�digo de error para direcci�n no encontrada o no pertenece al usuario
                ROLLBACK TRANSACTION;
                RETURN;
            END

            SET @Resultado = 0; -- C�digo para actualizaci�n exitosa
        END
        ELSE
        BEGIN
            -- Insertar nueva direcci�n
            INSERT INTO Direcciones (UsuarioID, DireccionLinea1, DireccionLinea2, PaisID, EstadoProvinciaID, Ciudad, CodigoPostal, Contacto)
            VALUES (@UsuarioID, @DireccionLinea1, @DireccionLinea2, @PaisID, @EstadoProvinciaID, @Ciudad, @CodigoPostal, @Contacto);

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