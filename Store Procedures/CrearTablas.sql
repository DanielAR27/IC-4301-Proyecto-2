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
('Pikastore');

INSERT INTO Categorias(Nombre) VALUES
('Pokeballs'),
('Bayas');

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

INSERT INTO Descuentos(ProductoID, Porcentaje, FechaInicio, FechaFin) VALUES
(1, 10, GETDATE(), GETDATE() + 1);


-- Pruebas para el borrado de productos

INSERT INTO Descuentos(ProductoID, Porcentaje, FechaInicio, FechaFin) VALUES
(24, 10, GETDATE(), GETDATE() + 1);

INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario) VALUES
	(1, 24, 2, 'Me gustó pero no captura a veces.');

SELECT * FROM Productos
SELECT * FROM DESCUENTOS
SELECT * FROM Reviews
SELECT * FROM CarritoProducto
SELECT * FROM LineasFactura

INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario) VALUES
	(1, 5, 4.4, 'Me gustó pero no captura a veces.'),
	(1, 5, 3, 'Mejoren su rango de captura.');



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

SELECT CC.CarritoID
FROM CarritoCompras CC
WHERE CC.UsuarioID = 1

SELECT *
FROM CarritoProducto