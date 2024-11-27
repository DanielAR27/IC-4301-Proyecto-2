USE DB_Proyecto;
CREATE OR ALTER PROCEDURE CrearUsuario
    @Nombre NVARCHAR(100),
    @Apellido NVARCHAR(100),
    @Email NVARCHAR(100),
    @Password NVARCHAR(255),
    @Telefono NVARCHAR(15),
    @FechaNacimiento DATE,
    @Resultado INT OUTPUT -- Variable de resultado
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (�xito por defecto)
        SET @Resultado = 0;

        -- Verificar que ning�n campo obligatorio sea NULL o est� en blanco
        IF @Nombre IS NULL OR @Nombre = '' OR
           @Apellido IS NULL OR @Apellido = '' OR
           @Email IS NULL OR @Email = '' OR
           @Password IS NULL OR @Password = ''
        BEGIN
            SET @Resultado = -1; -- C�digo de error para campos obligatorios nulos o en blanco
            ROLLBACK TRANSACTION;
            RETURN;
        END

		-- Verificar si el correo ya existe en la base de datos
        IF EXISTS (SELECT 1 FROM Usuarios WHERE Email = @Email)
        BEGIN
            SET @Resultado = -2; -- C�digo de error para correo ya existente
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Intentar insertar el usuario en la tabla
        INSERT INTO Usuarios (Nombre, Apellido, Email, Password, Telefono, FechaNacimiento, Rol)
        VALUES (@Nombre, @Apellido, @Email, @Password, @Telefono, @FechaNacimiento, 'Cliente');

        -- Obtener el ID del usuario reci�n creado
        DECLARE @NuevoUsuarioID INT;
        SET @NuevoUsuarioID = SCOPE_IDENTITY();

       -- Insertar un nuevo carrito para el usuario reci�n creado
        INSERT INTO CarritoCompras (UsuarioID)
        VALUES (@NuevoUsuarioID); -- Inicializar TotalCarrito en 0

        -- Confirmar la transacci�n si la inserci�n es exitosa
        SET @Resultado = 0; -- Indica �xito
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


/*EXEC CrearUsuario 'Pedro', 'Porro', 'pedronavas@gmail.com', '1234', '91314513', '2024-11-01', 0

SELECT *
FROM Usuarios

SELECT *
FROM CarritoCompras

DELETE 
FROM Usuarios*/