USE DB_Proyecto;

CREATE TABLE Usuarios (
    UsuarioID INT PRIMARY KEY IDENTITY (1, 1),       
    Nombre NVARCHAR(100) NOT NULL,             
    Apellido NVARCHAR(100) NOT NULL,           
    Email NVARCHAR(100) UNIQUE NOT NULL,
    Password NVARCHAR(255) NOT NULL,        
    Telefono NVARCHAR(15),
    FechaNacimiento DATE,                    
    FechaRegistro DATE DEFAULT GETDATE(),
    Rol NVARCHAR(50) NOT NULL      
);


CREATE TABLE Marcas (
    MarcaID INT PRIMARY KEY IDENTITY (1,1),             -- Identificador único de la marca
    Nombre NVARCHAR(100) NOT NULL UNIQUE          -- Nombre de la marca, con restricción de unicidad
);

CREATE TABLE Categorias (
    CategoriaID INT PRIMARY KEY IDENTITY (1,1),         -- Identificador único de la categoría
    Nombre NVARCHAR(100) NOT NULL UNIQUE          -- Nombre de la categoría, con restricción de unicidad
);

CREATE TABLE Productos (
    ProductoID INT PRIMARY KEY IDENTITY (1,1),
    Nombre NVARCHAR(100) NOT NULL,
    Descripcion NVARCHAR(500),
    Precio DECIMAL(10, 2) NOT NULL,
    Stock DECIMAL(10, 2) NOT NULL CHECK (Stock >= 0),
    CategoriaID INT NOT NULL,
    MarcaID INT NOT NULL,
    CalificacionPromedio DECIMAL(3, 2) DEFAULT 0.0 CHECK (CalificacionPromedio BETWEEN 0 AND 5), -- Rango de 0.00 a 5.00
    FechaAgregado DATE DEFAULT GETDATE(),
    Imagen NVARCHAR(255) NOT NULL,
    
    -- Definición de claves foráneas con nombres bonitos y ON DELETE NO ACTION
    CONSTRAINT FK_Productos_Categorias FOREIGN KEY (CategoriaID) REFERENCES Categorias(CategoriaID) ON DELETE NO ACTION,
    CONSTRAINT FK_Productos_Marcas FOREIGN KEY (MarcaID) REFERENCES Marcas(MarcaID) ON DELETE NO ACTION
);

CREATE TABLE CarritoCompras (
    CarritoID INT PRIMARY KEY IDENTITY,                     -- Identificador único del carrito
    UsuarioID INT NOT NULL UNIQUE FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- Usuario único con carrito activo
    TotalCarrito DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (TotalCarrito >= 0) -- Total inicializado en cero, no puede ser negativo
);

CREATE TABLE Paises (
    PaisID INT PRIMARY KEY IDENTITY (1,1),
	Codigo NVARCHAR(3) UNIQUE, -- Identificador único del país
    Nombre NVARCHAR(100) NOT NULL,             -- Nombre del país, hasta 100 caracteres
	CostoEnvio DECIMAL(10, 2) DEFAULT 0 CHECK (CostoEnvio >= 0)
);

CREATE TABLE Estados_Provincias (
    EstadoProvinciaID INT PRIMARY KEY IDENTITY (1,1),        -- Identificador único del estado/provincia
    PaisID INT FOREIGN KEY REFERENCES Paises(PaisID) ON DELETE CASCADE, -- FK a Paises, elimina en cascada
    Nombre NVARCHAR(100) NOT NULL                     -- Nombre del estado o provincia, hasta 100 caracteres
);

CREATE TABLE Direcciones (
    DireccionID INT PRIMARY KEY IDENTITY (1,1),                 -- Identificador único de la dirección
    UsuarioID INT NOT NULL, -- FK a Usuarios
    PaisID INT NOT NULL,     -- FK a Países
    EstadoProvinciaID INT NOT NULL, -- FK a Estados_Provincias
    DireccionLinea1 NVARCHAR(255) NOT NULL,               -- Primera línea de dirección (calle, número)
    DireccionLinea2 NVARCHAR(255),                        -- Segunda línea de dirección (opcional)
    Ciudad NVARCHAR(100) NOT NULL,                        -- Ciudad
    CodigoPostal NVARCHAR(20) NOT NULL,                   -- Código postal
    Contacto NVARCHAR(15) NOT NULL                        -- Número de contacto (teléfono)
	CONSTRAINT FK_Direcciones_Usuarios FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE,
    CONSTRAINT FK_Direcciones_Paises FOREIGN KEY (PaisID) REFERENCES Paises(PaisID) ON DELETE NO ACTION,
    CONSTRAINT FK_Direcciones_Estados FOREIGN KEY (EstadoProvinciaID) REFERENCES Estados_Provincias(EstadoProvinciaID) ON DELETE NO ACTION,
);

CREATE TABLE MetodoPago (
    MetodoPagoID INT PRIMARY KEY IDENTITY(1,1),     -- Identificador único del método de pago
    UsuarioID INT FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- FK que referencia al usuario
    NumeroTarjetaCifrado NVARCHAR(256),            -- Número de la tarjeta (cifrado con AES y almacenado en base64)
    ClaveCifrado NVARCHAR(256),                    -- Clave de cifrado utilizada (cifrada de manera segura)
    NombreTitular NVARCHAR(100),                   -- Nombre del titular de la tarjeta
    FechaExpiracion DATE,                          -- Fecha de expiración (para tarjetas)
    CodigoSeguridad NVARCHAR(4),                   -- Código de seguridad (almacenado de manera segura/encriptada)
    FechaRegistro DATETIME DEFAULT GETDATE(),      -- Fecha en la que se registró el método de pago
    Activo BIT DEFAULT 1                           -- Indica si el método de pago está activo (1) o no (0)
);

CREATE TABLE Facturas (
    FacturaID INT PRIMARY KEY IDENTITY (1,1),                   -- Identificador único de la factura
    UsuarioID INT NOT NULL FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- Relación con el usuario
    FechaFactura DATE DEFAULT GETDATE(),                  -- Fecha de la factura (fecha de compra)
    Total DECIMAL(10, 2) NOT NULL CHECK (Total >= 0), -- Monto total con descuentos aplicados
    CostoEnvio DECIMAL(10, 2) NOT NULL CHECK (CostoEnvio >= 0),
	Estado NVARCHAR(50) NOT NULL DEFAULT 'Pendiente'
);


CREATE TABLE LineasFactura (
    LineaFacturaID INT PRIMARY KEY IDENTITY (1,1),                         -- Identificador único de la línea de factura
    FacturaID INT NOT NULL FOREIGN KEY REFERENCES Facturas(FacturaID) ON DELETE CASCADE, -- Relación con Factura
    ProductoID INT NOT NULL, -- Identificador del producto comprado
    ProductoNombre NVARCHAR(100) NOT NULL,                         -- Nombre del producto al momento de la compra
    Linea INT NOT NULL,                                            -- Orden de la línea en la factura
    Cantidad DECIMAL(10, 2) NOT NULL CHECK (Cantidad > 0),         -- Cantidad de producto comprado
    PrecioOriginal DECIMAL(10, 2) NOT NULL CHECK (PrecioOriginal >= 0), -- Precio del producto sin descuento
    DescuentoAplicado DECIMAL(5, 2) DEFAULT 0 CHECK (DescuentoAplicado BETWEEN 0 AND 100), -- % de descuento
    LineaTotal DECIMAL(10, 2) NOT NULL CHECK (LineaTotal >= 0)     -- Total de la línea con descuento aplicado
    -- Definición de claves foráneas con nombres bonitos y ON DELETE CASCADE
    CONSTRAINT FK_LineasFactura_Productos FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE NO ACTION
);

CREATE TABLE Descuentos (
    DescuentoID INT PRIMARY KEY IDENTITY (1,1),
    ProductoID INT FOREIGN KEY REFERENCES Productos(ProductoID) ON DELETE CASCADE,
    Porcentaje DECIMAL(5, 2) CHECK (Porcentaje BETWEEN 0 AND 100), 
    FechaInicio DATETIME NOT NULL,
    FechaFin DATETIME NOT NULL,
    CONSTRAINT CK_FechaFin CHECK (FechaFin > FechaInicio),
    CONSTRAINT UQ_Producto_Fecha UNIQUE (ProductoID, FechaInicio, FechaFin)
);


CREATE TABLE Reviews (
    ReviewID INT PRIMARY KEY IDENTITY (1,1),                    -- Identificador único de la reseña
    UsuarioID INT NOT NULL FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- Usuario que hizo la reseña
    ProductoID INT NOT NULL FOREIGN KEY REFERENCES Productos(ProductoID) ON DELETE CASCADE, -- Producto que se está reseñando
    Calificacion INT NOT NULL CHECK (Calificacion BETWEEN 1 AND 5), -- Calificación del 1 al 5
    Comentario NVARCHAR(1000),                            -- Comentario opcional de la reseña
    FechaReview DATETIME NOT NULL DEFAULT GETDATE()       -- Fecha en que se realizó la reseña
);

CREATE TABLE CarritoProducto (
    CarritoProductoID INT PRIMARY KEY IDENTITY (1,1),
    CarritoID INT FOREIGN KEY REFERENCES CarritoCompras(CarritoID) ON DELETE CASCADE, 
    ProductoID INT NOT NULL,
    ProductoNombre NVARCHAR(100) NOT NULL,                          -- Nombre del producto en el momento de agregarlo
    Linea INT NOT NULL,                                             -- Orden del producto en el carrito    
    Cantidad DECIMAL(10, 2) NOT NULL CHECK (Cantidad > 0),          -- Cantidad del producto, no negativa
    PrecioOriginal DECIMAL(10, 2) NOT NULL,                         -- Precio sin descuento
	DescuentoAplicado DECIMAL(5, 2) NOT NULL,						-- Descuento aplicado
    LineaTotal DECIMAL(10, 2) NOT NULL,                             -- Cantidad * PrecioOriginal	
    -- Definición de claves foráneas con nombres bonitos y ON DELETE CASCADE
    CONSTRAINT FK_CarritoProducto_Productos FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE,
);

-- INSERCIONES

--Insert para la tabla usuarios 
--La contraseña es 'password' para todos
INSERT INTO Usuarios (Nombre, Apellido, Email, Password, Telefono, FechaNacimiento, Rol) VALUES
('Rónald', 'Gómez', 'bala@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '22334466', '1975-01-24', 'Administrador'),
('Erick', 'Lonnis', 'erick.lonnis@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12345690', '1965-09-09', 'Administrador'),
('Jervis', 'Drummond', 'jervis.drummond@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12567891', '1973-09-08', 'Cliente'),
('Luis', 'Marín', 'luis.marin@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12345892', '1975-05-10', 'Cliente'),
('Mauricio', 'Wright', 'mauricio.wright@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12345693', '1971-12-20', 'Cliente'),
('Gilberto', 'Martínez', 'gilberto.martinez@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12567894', '1980-10-01', 'Cliente'),
('Wilmer', 'López', 'wilmer.lopez@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12345675', '1972-08-03', 'Cliente'),
('Rolando', 'Fonseca', 'rolando.fonseca@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '14567896', '1975-06-06', 'Cliente'),
('Mauricio', 'Solís', 'mauricio.solis@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '12567897', '1973-03-13', 'Cliente'),
('Paulo', 'Wanchope', 'paulo.wanchope@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '14567898', '1976-07-31', 'Cliente'),
('Walter', 'Centeno', 'walter.centeno@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '14567899', '1975-10-06', 'Cliente'),
('Winston', 'Parks', 'winston.parks@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '34556677', '1982-10-21', 'Cliente'),
('Froylán', 'Ledezma', 'cachorro@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '44667788', '1981-03-11', 'Cliente'),
('Bryan', 'Ruiz', 'comadreja@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '55668899', '1968-08-15', 'Cliente'),
('Harold', 'Wallace', 'harold.wallace@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '66789900', '1976-09-07', 'Cliente'),
('Steven', 'Bryce', 'steven.bryce@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '77889011', '1978-08-16', 'Cliente'),
('Hernán', 'Medford', 'hernan.medford@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '88901122', '1968-05-23', 'Cliente'),
('Álvaro', 'Mesén', 'alvaro.mesen@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '99012233', '1973-12-02', 'Cliente'),
('Rodrigo', 'Cordero', 'rodrigo.cordero@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '11023344', '1974-04-19', 'Cliente'),
('William', 'Sunsing', 'william.sunsing@gmail.com', '$2a$10$TUCGwO5kaHz1XxjxIhIuneKCKl.3wmc/06nkFR1zyluNA6N/LcjFu', '22034455', '1977-05-22', 'Cliente');

-- Insertar los carritos
INSERT INTO CarritoCompras(UsuarioID, TotalCarrito) VALUES
(1, 0),
(2, 0),
(3, 0),
(4, 0),
(5, 0),
(6, 0),
(7, 0),
(8, 0),
(9, 0),
(10, 0),
(11, 0),
(12, 0),
(13, 0),
(14, 0),
(15, 0),
(16, 0),
(17, 0),
(18, 0),
(19, 0),
(20, 0);

-- Insertar los países.
INSERT INTO Paises (Codigo, Nombre) VALUES 
('CRC', 'Costa Rica'),
('COL', 'Colombia'),
('PAN', 'Panamá'),
('USA', 'Estados Unidos'),
('GTA', 'Guatemala');

INSERT INTO Estados_Provincias (PaisID, Nombre) VALUES 
(1, 'San José'),
(1, 'Alajuela'),
(1, 'Cartago'),
(1, 'Heredia'),
(1, 'Guanacaste'),
(1, 'Puntarenas'),
(1, 'Limón');


INSERT INTO Estados_Provincias (PaisID, Nombre) VALUES 
(2, 'Medellín'),
(2, 'Cartagena');


INSERT INTO Marcas(Nombre) VALUES
('Pokemarket'),
('Pikastore'),
('Evolutronics'),
('PokeHealing'),
('PokeTrainer'),
('Silph S.A.'),
('WildCatcher'),
('BerryWorld'),
('PowerUp Inc.'),
('Pokestar'),
('Rocket'),
('Aether');


-- Categorías finales (sin duplicar 'Bayas' ni 'Medicinas')
INSERT INTO Categorias(Nombre) VALUES
('Pokeballs'),               
('Bayas'),                 
('Piedras Evolutivas'),      
('Equipamiento de Batalla'), 
('Objetos Curativos'),      
('Fósiles y Reliquias'),   
('Decoración y Estilo'),     
('MT/MO y Mejoras'),       
('Herramientas Útiles'),     
('Objetos Especiales');      


--INSERTS DE PRODUCTOS

--Insert de Pokebolas
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Pokeball', 'Dispositivo básico para capturar Pokémon.', 100, 5, 1, 1, 'https://i.ibb.co/pyDt2Wx/poke-ball.png'),
('Superball', 'Pokeball mejorada con mayor tasa de captura.', 600, 5, 1, 1, 'https://i.ibb.co/TPPsQb9/great-ball.png'),
('Ultraball', 'Ball avanzada con alta tasa de captura.', 1200, 5, 1, 1, 'https://i.ibb.co/3sBYwHJ/ultra-ball.png'),
('Veloz Ball', 'Ideal para capturas rápidas al inicio del encuentro', 1000, 5, 1, 1, 'https://i.ibb.co/4Tjy7mf/quick-ball.png'),
('Honor Ball', 'Ball especial usada en ocasiones honoríficas.', 200, 5, 1, 1, 'https://i.ibb.co/zHzgrLW/premier-ball.png'),
('Sana Ball', 'Cura los estados alterados del Pokémon al capturarlo.', 300, 5, 1, 1, 'https://i.ibb.co/3zt7xXp/heal-ball.png'),
('Buceo Ball', 'Eficaz para capturar Pokémon encontrados bajo el agua.', 1000, 5, 1, 1, 'https://i.ibb.co/p3wrZ16/dive-ball.png'),
('Malla Ball', 'Ideal para capturar Pokémon de tipo Agua o Bicho.', 1000, 5, 1, 1, 'https://i.ibb.co/82gk99G/net-ball.png'),
('Nido Ball', 'Más efectiva con Pokémon de menor nivel.', 1000, 5, 1, 1, 'https://i.ibb.co/3YM8nzL/nest-ball.png'),
('Lujo Ball', 'Ball que aumenta la felicidad del Pokémon capturado.', 1000, 5, 1, 1, 'https://i.ibb.co/WFm7tMc/luxury-ball.png');

--Insert de Bayas
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Baya Zreza', 'Cura a un Pokémon paralizado.', 10, 5, 2, 2, 'https://i.ibb.co/Kj6shKd/cheri-berry.png'),
('Baya Atania', 'Cura a un Pokémon dormido.', 10, 5, 2, 2, 'https://i.ibb.co/5YCF5rD/chesto-berry.png'),
('Baya Meloc', 'Cura a un Pokémon envenenado.', 10, 5, 2, 2, 'https://i.ibb.co/0YYs0DW/pecha-berry.png'),
('Baya Safre', 'Cura a un Pokémon quemado.', 10, 5, 2, 2, 'https://i.ibb.co/QcsVpC9/rawst-berry.png'),
('Baya Perasi', 'Cura a un Pokémon congelado.', 10, 5, 2, 2, 'https://i.ibb.co/rt4P6W7/aspear-berry.png'),
('Baya Zanama', 'Restaura 10 PP de un movimiento.', 10, 5, 2, 2, 'https://i.ibb.co/yBzv9ws/leppa-berry.png'),
('Baya Aranja', 'Restaura 10 PS.', 10, 5, 2, 2, ' https://i.ibb.co/dDLvT0f/oran-berry.png'),
('Baya Caquic', 'Cura a un Pokémon confundido.', 10, 5, 2, 2, 'https://i.ibb.co/Xz7mPFf/persim-berry.png'),
('Baya Ziuela', 'Cura cualquier problema de estado.', 10, 5, 2, 2, 'https://i.ibb.co/hB1HSg5/lum-berry.png'),
('Baya Zidra', 'Restaura 30 PS o 1/4 de los PS máximos del Pokémon.', 10, 5, 2, 2, 'https://i.ibb.co/GWSfttm/sitrus-berry.png');

--Insert de medicinas
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Poción', 'Restaura 20 PS de un Pokémon.', 200, 10, 5, 4, 'https://i.ibb.co/nCL0hnX/potion.png'),
('Superpoción', 'Restaura 50 PS de un Pokémon.', 700, 10, 5, 4, 'https://i.ibb.co/0fcM2DK/super-potion.png'),
('Hiperpoción', 'Restaura 120 PS de un Pokémon.', 1500, 10, 5, 4, 'https://i.ibb.co/pQW40YP/hyper-potion.png'),
('Antídoto', 'Cura el envenenamiento de un Pokémon.', 100, 10, 5, 4, 'https://i.ibb.co/fvG4s74/antidote.png'),
('Quemacura', 'Cura las quemaduras de un Pokémon.', 250, 10, 5, 4, 'https://i.ibb.co/K7462t1/burn-heal.png'),
('Despertar', 'Cura el sueño de un Pokémon.', 200, 10, 3, 1, 'https://i.ibb.co/CBM8jTF/awakening.png'),
('Hielo Cura', 'Cura el congelamiento de un Pokémon.', 200, 10, 3, 4, 'https://i.ibb.co/mCnyhNc/ice-heal.png'),
('Revivir', 'Revive a un Pokémon debilitado con la mitad de sus PS.', 1500, 5, 5, 3, 'https://i.ibb.co/2gP25qt/revive.png'),
('Revivir Máx', 'Revive a un Pokémon debilitado con todos sus PS.', 3000, 5, 5, 2, 'https://i.ibb.co/6JXRf9z/max-revive.png'),
('Cura Total', 'Cura todos los problemas de estado de un Pokémon.', 600, 10, 5, 9, 'https://i.ibb.co/rmNXq3Y/full-heal.png');

--Inserts de piedras evolutivas
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Piedra Lunar', 'Permite evolucionar Pokémon especiales.', 2500, 5, 3, 9, 'https://i.ibb.co/m6RkJF2/moon-stone.png'),
('Piedra Fuego', 'Permite evolucionar Pokémon tipo fuego.', 2500, 5, 3, 7, 'https://i.ibb.co/Pgq9XVQ/fire-stone.png'),
('Piedra Trueno', 'Permite evolucionar Pokémon eléctricos.', 2500, 5, 3, 9, 'https://i.ibb.co/WHhMQKT/thunder-stone.png'),
('Piedra Agua', 'Permite evolucionar Pokémon acuáticos.', 2500, 5, 3, 9, 'https://i.ibb.co/mtXqJHX/water-stone.png'),
('Piedra Hoja', 'Permite evolucionar Pokémon tipo planta.', 2500, 5, 3, 10, 'https://i.ibb.co/RTC4MVb/leaf-stone.png');

--Inserts de equipamientos de batalla
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Cinta Elección', 'Aumenta un atributo pero restringe ataques.', 3000, 10, 4, 1, 'https://i.ibb.co/cJ1PfKg/choice-band.png'),
('Banda Focus', 'Previene un golpe fatal una vez.', 2500, 15, 4, 2, 'https://i.ibb.co/pZn0H1n/focus-band.png'),
('Cinturón Experto', 'Mejora ataques efectivos.', 3500, 10, 4, 3, 'https://i.ibb.co/F7JKrkF/expert-belt.png'),
('Gafas Oscuras', 'Aumenta la potencia de ataques oscuros.', 2000, 20, 4, 4, 'https://i.ibb.co/s53m2MV/black-glasses.png'),
('Cinturón Negro', 'Mejora ataques tipo lucha.', 2500, 15, 4, 5, 'https://i.ibb.co/x6ZvwsB/black-belt.png');

--Inserts de fósiles y reliquias
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Fósil Domo', 'Fósil antiguo de Pokémon extintos.', 4000, 5, 6, 1, 'https://i.ibb.co/sRG5Thv/dome-fossil.png'),
('Fósil Garra', 'Permite revivir un Pokémon extinto.', 4000, 5, 6, 2, 'https://i.ibb.co/C69CY08/claw-fossil.png'),
('Trozo Estrella', 'Objeto de alto valor comercial.', 3000, 10, 6, 3, 'https://i.ibb.co/Wz7fgFH/star-piece.png'),
('Pepita', 'Objeto raro con alto valor.', 5000, 5, 6, 4, 'https://i.ibb.co/ngMX8T7/nugget.png'),
('Perla Grande', 'Perla valiosa para vender.', 4000, 10, 6, 5, 'https://i.ibb.co/xX814Xw/big-pearl.png');

--Inserts de decoración y estilo
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Globo Helio', 'Flota para decoración de bases.', 1500, 20, 7, 6, 'https://i.ibb.co/h8fnz7T/air-balloon.png'),
('Cartel Pokémon', 'Decoración temática.', 1200, 15, 7, 7, 'https://i.ibb.co/W66JwyV/pink-scarf.png'),
('Lámpara Lunar', 'Ilumina bases con estilo.', 2500, 10, 7, 8, 'https://i.ibb.co/gTDsm71/dusk-stone.png'),
('Cama Secreta', 'Mueble para decorar bases.', 3000, 5, 7, 9, 'https://i.ibb.co/jbwZWMp/yellow-flute.png'),
('Tienda Acuática', 'Decoración especial para bases.', 3500, 5, 7, 10, 'https://i.ibb.co/3NJqXMt/blue-scarf.png');

--Inserts de máquinas y mejoras
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('MT Llamarada', 'Enseña el ataque Llamarada.', 5000, 5, 8, 1, 'https://i.ibb.co/Cs3kQGm/flame-plate.png'),
('MT Hidrobomba', 'Enseña el ataque Hidrobomba.', 5000, 5, 8, 2, 'https://i.ibb.co/mBKS3ZX/splash-plate.png'),
('MT Psicorrayo', 'Enseña el ataque Psicorrayo.', 5000, 5, 8, 3, 'https://i.ibb.co/q5sPLqJ/mind-plate.png'),
('Proteína', 'Aumenta ataque base del Pokémon.', 9800, 5, 8, 4, 'https://i.ibb.co/PGtpJ2N/protein.png'),
('Carburante', 'Aumenta velocidad base.', 9800, 5, 8, 5, 'https://i.ibb.co/WswYZLk/carbos.png');

--Inserts de herramientas útiles
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Cuerda Huida', 'Permite escapar de cuevas.', 300, 50, 9, 6, 'https://i.ibb.co/nrXRh9f/escape-rope.png'),
('Campana Alivio', 'Aumenta la amistad del Pokémon.', 2500, 20, 9, 7, 'https://i.ibb.co/jWWvR4T/soothe-bell.png'),
('Piedra Eterna', 'Previene evolución.', 2000, 15, 9, 8, 'https://i.ibb.co/dtvc0G3/everstone.png'),
('Bola Humo', 'Facilita escapar de combates.', 1000, 25, 9, 9, 'https://i.ibb.co/hsX4zcN/smoke-ball.png'),
('Amuleto Moneda', 'Duplica las ganancias tras combate.', 5000, 10, 9, 10, 'https://i.ibb.co/w6WYvkg/amulet-coin.png');

--Inserts de productos especiales
INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Ceniza Sagrada', 'Revive y restaura PS al máximo para todo el equipo.', 5000, 5, 10, 1, 'https://i.ibb.co/hgVJc69/sacred-ash.png'),
('Caramelo Raro', 'Aumenta un nivel del Pokémon al consumirlo.', 2000, 20, 10, 2, 'https://i.ibb.co/NSkK1yW/rare-candy.png'),
('Hueso Raro', 'Objeto valioso que se puede vender a buen precio.', 3000, 10, 10, 3, 'https://i.ibb.co/vjC0BkJ/rare-bone.png'),
('Cola Skitty', 'Facilita escapar de combates contra Pokémon salvajes.', 1500, 30, 10, 6, 'https://i.ibb.co/L9VJTJr/fluffy-tail.png'),
('Roca del Rey', 'Da posibilidad de causar retroceso.', 1200, 15, 10, 5, 'https://i.ibb.co/sVRqhjg/kings-rock.png');


-- Inserts de pedidos para 18 clientes (dos pedidos por cliente)
INSERT INTO Facturas (UsuarioID, FechaFactura, Total, CostoEnvio, Estado) VALUES
(3, '2024-01-15', 3000, 150, 'Entregado'),
(3, '2024-02-10', 4200, 200, 'Entregado'),
(4, '2024-03-05', 1800, 100, 'Pendiente'),
(4, '2024-03-15', 2500, 120, 'Entregado'),
(5, '2024-04-08', 5600, 250, 'Entregado'),
(5, '2024-05-22', 1200, 80,  'Entregado'),
(6, '2024-06-10', 4800, 220, 'Entregado'),
(6, '2024-07-01', 3600, 180, 'Entregado'),
(7, '2024-08-20', 1900, 100, 'Entregado'),
(7, '2024-09-10', 6000, 300, 'Entregado'),
(8, '2024-09-15', 1500, 90,  'Pendiente'),
(8, '2024-10-12', 2200, 110, 'Pendiente'),
(9, '2024-11-02', 4700, 210, 'Entregado'),
(9, '2024-11-15', 3300, 170, 'Pendiente'),
(10, '2024-11-01', 4000, 200, 'Entregado'),
(10, '2024-11-15', 2800, 150, 'Entregado'),
(11, '2024-06-25', 3200, 150, 'Entregado'),
(11, '2024-07-30', 4200, 200, 'Pendiente'),
(12, '2024-08-05', 3900, 190, 'Entregado'),
(12, '2024-09-10', 4500, 220, 'Pendiente'),
(13, '2024-10-01', 5200, 260, 'Entregado'),
(13, '2024-11-11', 3500, 180, 'Entregado'),
(14, '2024-11-05', 4300, 210, 'Entregado'),
(14, '2024-11-20', 2900, 140, 'Entregado'),
(15, '2024-06-10', 5000, 250, 'Pendiente'),
(15, '2024-06-30', 2700, 130, 'Entregado'),
(16, '2024-07-15', 2100, 110, 'Entregado'),
(16, '2024-08-20', 4900, 240, 'Pendiente'),
(17, '2024-09-25', 3400, 170, 'Entregado'),
(17, '2024-10-05', 4100, 200, 'Entregado'),
(18, '2024-06-10', 3200, 120, 'Pendiente'),
(18, '2024-06-20', 4800, 150, 'Entregado'),
(19, '2024-07-05', 3900, 130, 'Pendiente'),
(19, '2024-07-18', 4500, 180, 'Entregado'),
(20, '2024-08-12', 5200, 250, 'Entregado'),
(20, '2024-08-25', 4100, 200, 'Entregado');


-- Detalles de los pedidos
INSERT INTO LineasFactura (FacturaID, ProductoID, ProductoNombre, Linea, Cantidad, PrecioOriginal, DescuentoAplicado, LineaTotal) VALUES
(1, 1, 'Pokeball', 1, 2, 100, 0, 200),
(1, 2, 'Superball', 2, 1, 600, 10, 540),
(1, 3, 'Ultraball', 3, 1, 1200, 0, 1200),
(2, 10, 'Lujo Ball', 1, 3, 1000, 0, 3000),
(2, 12, 'Baya Atania', 2, 2, 10, 0, 20),
(3, 13, 'Baya Meloc', 1, 4, 10, 0, 40),
(3, 25, 'Quemacura', 2, 3, 250, 5, 712.50),
(4, 31, 'Piedra Lunar', 1, 1, 2500, 10, 2250),
(5, 35, 'Piedra Hoja', 1, 2, 2500, 0, 5000),
(5, 41, 'Fósil Domo', 2, 1, 4000, 0, 4000),
(5, 43, 'Trozo Estrella', 3, 3, 3000, 0, 9000),
(5, 36, 'Cinta Elección', 4, 2, 3000, 5, 5700),
(6, 39, 'Gafas Oscuras', 1, 4, 2000, 10, 7200),
(7, 33, 'Piedra Trueno', 1, 1, 2500, 0, 2500),
(7, 46, 'Globo Helio', 2, 2, 1500, 0, 3000),
(8, 51, 'MT Llamarada', 1, 3, 5000, 0, 15000),
(8, 54, 'Proteína', 2, 1, 9800, 10, 8820),
(9, 62, 'Caramelo Raro', 1, 1, 2000, 0, 2000),
(9, 56, 'Cuerda Huida', 2, 2, 300, 5, 570),
(9, 61, 'Ceniza Sagrada', 3, 1, 5000, 0, 5000),
(10, 65, 'Roca del Rey', 1, 2, 1200, 0, 2400),
(10, 1, 'Pokeball', 2, 5, 100, 0, 500),
(10, 10, 'Lujo Ball', 3, 2, 1000, 10, 1800),
(10, 3, 'Ultraball', 4, 3, 1200, 5, 3420),
(10, 12, 'Baya Atania', 5, 10, 10, 0, 100),
(10, 25, 'Quemacura', 6, 6, 250, 0, 500),
(10, 31, 'Piedra Lunar', 7, 1, 2500, 10, 2250),
(10, 35, 'Piedra Hoja', 8, 3, 2500, 0, 7500),
(11, 41, 'Fósil Domo', 1, 2, 4000, 5, 7600),
(11, 36, 'Cinta Elección', 2, 1, 3000, 0, 3000),
(11, 39, 'Gafas Oscuras', 3, 5, 2000, 10, 9000),
(12, 33, 'Piedra Trueno', 1, 2, 2500, 0, 5000),
(13, 46, 'Globo Helio', 1, 4, 1500, 0, 6000),
(13, 51, 'MT Llamarada', 2, 1, 5000, 0, 5000),
(14, 62, 'Caramelo Raro', 1, 2, 2000, 5, 3800),
(14, 56, 'Cuerda Huida', 2, 10, 300, 0, 3000),
(14, 60, 'Amuleto Moneda', 3, 2, 5000, 10, 9000),
(14, 61, 'Ceniza Sagrada', 4, 1, 5000, 0, 5000),
(15, 65, 'Roca del Rey', 1, 2, 1200, 0, 2400),
(15, 21, 'Poción', 2, 3, 200, 0, 600),
(15, 22, 'Superpoción', 3, 4, 700, 5, 2660),
(15, 23, 'Hiperpoción', 4, 2, 1500, 0, 3000),
(15, 25, 'Quemacura', 5, 1, 250, 0, 250),
(15, 26, 'Despertar', 6, 1, 200, 0, 200),
(16, 27, 'Hielo Cura', 1, 1, 200, 0, 200),
(17, 28, 'Revivir', 1, 2, 1500, 10, 2700),
(17, 31, 'Piedra Lunar', 2, 1, 2500, 0, 2500),
(18, 34, 'Piedra Agua', 1, 3, 2500, 0, 7500),
(18, 41, 'Fósil Domo', 2, 2, 4000, 0, 8000),
(18, 36, 'Cinta Elección', 3, 1, 3000, 0, 3000),
(18, 46, 'Globo Helio', 4, 4, 1500, 10, 5400),
(18, 1, 'Pokeball', 5, 3, 100, 0, 300),
(18, 11, 'Baya Zreza', 6, 5, 10, 0, 50),
(18, 13, 'Baya Meloc', 7, 8, 10, 0, 80),
(19, 36, 'Cinta Elección', 1, 1, 3000, 5, 2850),
(19, 3, 'Ultraball', 2, 2, 1200, 10, 2160),
(20, 25, 'Quemacura', 1, 4, 250, 0, 1000),
(21, 4, 'Veloz Ball', 1, 5, 1000, 0, 5000),
(21, 48, 'Lámpara Lunar', 2, 1, 2500, 0, 2500),
(21, 9, 'Nido Ball', 3, 6, 1000, 10, 5400),
(21, 33, 'Piedra Trueno', 4, 1, 2500, 5, 2375),
(21, 14, 'Baya Safre', 5, 10, 10, 0, 100),
(22, 39, 'Gafas Oscuras', 1, 2, 2000, 0, 4000),
(22, 6, 'Sana Ball', 2, 4, 300, 0, 1200),
(23, 65, 'Roca del Rey', 1, 2, 1200, 0, 2400),
(23, 10, 'Lujo Ball', 2, 2, 1000, 10, 1800),
(23, 56, 'Cuerda Huida', 3, 5, 300, 0, 1500),
(24, 34, 'Piedra Agua', 1, 2, 2500, 5, 4750),
(24, 61, 'Ceniza Sagrada', 2, 1, 5000, 10, 4500),
(25, 7, 'Buceo Ball', 1, 3, 1000, 0, 3000),
(26, 21, 'Poción', 1, 6, 200, 0, 1200),
(27, 41, 'Fósil Domo', 1, 1, 4000, 10, 3600),
(28, 52, 'MT Hidrobomba', 1, 1, 5000, 5, 4750),
(28, 28, 'Revivir', 2, 1, 1500, 0, 1500),
(28, 31, 'Piedra Lunar', 3, 3, 2500, 10, 6750),
(28, 63, 'Hueso Raro', 4, 4, 2000, 5, 7600),
(28, 60, 'Amuleto Moneda', 5, 2, 5000, 0, 10000),
(28, 38, 'Cinturón Experto', 6, 1, 3500, 0, 3500),
(29, 35, 'Piedra Hoja', 1, 2, 2500, 0, 5000),
(30, 8, 'Malla Ball', 1, 5, 1000, 0, 5000),
(30, 22, 'Superpoción', 2, 4, 700, 0, 2800),
(31, 1, 'Pokeball', 1, 10, 100, 5, 950), 
(31, 57, 'Campana Alivio', 2, 2, 2000, 0, 4000), 
(31, 4, 'Veloz Ball', 3, 3, 1000, 10, 2700), 
(32, 31, 'Piedra Lunar', 1, 1, 2500, 0, 2500), 
(33, 21, 'Poción', 1, 6, 200, 0, 1200), 
(33, 38, 'Cinturón Experto', 2, 2, 3500, 5, 6650),
(34, 11, 'Baya Zreza', 1, 15, 10, 0, 150),
(34, 60, 'Amuleto Moneda', 2, 1, 5000, 10, 4500), 
(34, 6, 'Sana Ball', 3, 5, 300, 0, 1500), 
(35, 41, 'Fósil Domo', 1, 1, 4000, 0, 4000),
(35, 14, 'Baya Safre', 2, 10, 10, 5, 95),
(36, 61, 'Ceniza Sagrada', 1, 1, 5000, 5, 4750),
(36, 3, 'Ultraball', 2, 2, 1200, 10, 2160), 
(36, 28, 'Revivir', 3, 1, 1500, 0, 1500),
(36, 48, 'Lámpara Lunar', 4, 1, 2500, 0, 2500); 



INSERT INTO Descuentos(ProductoID, Porcentaje, FechaInicio, FechaFin) VALUES
(1, 10, GETDATE(), GETDATE() + 1);


-- Pruebas para el borrado de productos

INSERT INTO Descuentos(ProductoID, Porcentaje, FechaInicio, FechaFin) VALUES
(24, 10, GETDATE(), GETDATE() + 1);

INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario) VALUES
	(1, 24, 2, 'Me gustó pero no captura a veces.');

SELECT * FROM Usuarios
SELECT * FROM CarritoCompras
SELECT * FROM Productos
SELECT * FROM DESCUENTOS
SELECT * FROM Reviews
SELECT * FROM CarritoProducto
SELECT * FROM LineasFactura
SELECT * FROM CATEGORIAS

INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario) VALUES
	(1, 4, 4.4, 'Me gustó pero no captura a veces.'),
	(1, 4, 3, 'Mejoren su rango de captura.');



SELECT *
FROM Descuentos

SELECT *
FROM Productos

SELECT P.ProductoID, P.Nombre, P.Descripcion, P.Precio, P.Stock, C.Nombre Categoria, M.Nombre Marca, P.CalificacionPromedio, P.Imagen
FROM  Productos P
LEFT JOIN Marcas M ON P.MarcaID = M.MarcaID
LEFT JOIN Categorias C on P.CategoriaID = C.CategoriaID
ORDER BY ProductoID
OFFSET 2 * 10 ROWS -- Número de página * Número de elementos por obtener
FETCH NEXT 10 ROWS ONLY; -- Número de elementos por obtener

SELECT *
FROM Marcas
ORDER BY MarcaID

SELECT *
FROM Categorias

SELECT * FROM Descuentos;

SELECT * FROM Usuarios;

UPDATE Usuarios
SET Password = '$2a$10$NCmjSayz80nRpvvhz4rO3epmCcJbRUcXaBbu7wKXKoABkuUBksKNO';


SELECT * FROM CarritoCompras

INSERT INTO Usuarios (Nombre, Apellido, Email, Password, Telefono, FechaNacimiento, Rol) VALUES
('Ronaldo', 'Nazario', 'nazario@gmail.com', '$2a$10$Si0xmEyimutLME2x2jLQCuQqVzwkpMeYpDQHpB43IPrpCmdLkNds', '22334466', '1975-01-24', 'Administrador');

DELETE FROM Usuarios;

EXEC CrearUsuario 'Pedro', 'Navas', '4590', '1234', '91314513', '2024-11-01', 0

SELECT *
FROM Productos

SELECT *
FROM CarritoProducto CP
WHERE CP.ProductoID = 64

SELECT *
FROM LineasFactura LF
WHERE LF.ProductoID = 64