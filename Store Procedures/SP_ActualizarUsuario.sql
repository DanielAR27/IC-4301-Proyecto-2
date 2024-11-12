USE DB_Proyecto;
GO
CREATE PROCEDURE ActualizarUsuario
    @UsuarioID INT,
    @Nombre NVARCHAR(50),
    @Apellido NVARCHAR(50),
    @Email NVARCHAR(100),
    @Telefono NVARCHAR(20),
    @FechaNacimiento DATE,
    @Resultado INT OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Inicializar el resultado en 0 (éxito por defecto)
        SET @Resultado = 0;

        -- Validar que ningún campo obligatorio sea NULL o esté en blanco
        IF @UsuarioID IS NULL OR 
           @Nombre IS NULL OR @Nombre = '' OR
           @Apellido IS NULL OR @Apellido = '' OR
           @Email IS NULL OR @Email = '' OR
           @Telefono IS NULL OR @Telefono = '' OR
           @FechaNacimiento IS NULL
        BEGIN
            SET @Resultado = -1; -- Código de error para campos obligatorios nulos o en blanco
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Intentar actualizar el usuario con los datos proporcionados
        UPDATE Usuarios
        SET Nombre = @Nombre,
            Apellido = @Apellido,
            Email = @Email,
            Telefono = @Telefono,
            FechaNacimiento = @FechaNacimiento
        WHERE UsuarioID = @UsuarioID;

        -- Verificar si la actualización afectó alguna fila
        IF @@ROWCOUNT = 0
        BEGIN
            SET @Resultado = -2; -- Código de error si el usuario no fue encontrado
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Confirmar la transacción
        COMMIT TRANSACTION;
        SET @Resultado = 1; -- Código para actualización exitosa

    END TRY
    BEGIN CATCH
        -- Realizar rollback si ocurre un error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Configurar un código de error genérico
        SET @Resultado = -3; -- Error en la base de datos
        RAISERROR('Ocurrió un error al actualizar el usuario. Intente de nuevo.', 16, 1);
    END CATCH
END;
GO
