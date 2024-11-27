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
    CarritoID INT PRIMARY KEY IDENTITY,                     -- Identificador �nico del carrito
    UsuarioID INT NOT NULL UNIQUE FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- Usuario �nico con carrito activo
    TotalCarrito DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (TotalCarrito >= 0) -- Total inicializado en cero, no puede ser negativo
);

CREATE TABLE Paises (
    PaisID INT PRIMARY KEY IDENTITY (1,1),
	Codigo NVARCHAR(3) UNIQUE, -- Identificador �nico del pa�s
    Nombre NVARCHAR(100) NOT NULL,             -- Nombre del pa�s, hasta 100 caracteres
	CostoEnvio DECIMAL(10, 2) DEFAULT 0 CHECK (CostoEnvio >= 0)
);

CREATE TABLE Estados_Provincias (
    EstadoProvinciaID INT PRIMARY KEY IDENTITY (1,1),        -- Identificador �nico del estado/provincia
    PaisID INT FOREIGN KEY REFERENCES Paises(PaisID) ON DELETE CASCADE, -- FK a Paises, elimina en cascada
    Nombre NVARCHAR(100) NOT NULL                     -- Nombre del estado o provincia, hasta 100 caracteres
);

CREATE TABLE Direcciones (
    DireccionID INT PRIMARY KEY IDENTITY (1,1),                 -- Identificador �nico de la direcci�n
    UsuarioID INT NOT NULL, -- FK a Usuarios
    PaisID INT NOT NULL,     -- FK a Pa�ses
    EstadoProvinciaID INT NOT NULL, -- FK a Estados_Provincias
    DireccionLinea1 NVARCHAR(255) NOT NULL,               -- Primera l�nea de direcci�n (calle, n�mero)
    DireccionLinea2 NVARCHAR(255),                        -- Segunda l�nea de direcci�n (opcional)
    Ciudad NVARCHAR(100) NOT NULL,                        -- Ciudad
    CodigoPostal NVARCHAR(20) NOT NULL,                   -- C�digo postal
    Contacto NVARCHAR(15) NOT NULL                        -- N�mero de contacto (tel�fono)
	CONSTRAINT FK_Direcciones_Usuarios FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE,
    CONSTRAINT FK_Direcciones_Paises FOREIGN KEY (PaisID) REFERENCES Paises(PaisID) ON DELETE NO ACTION,
    CONSTRAINT FK_Direcciones_Estados FOREIGN KEY (EstadoProvinciaID) REFERENCES Estados_Provincias(EstadoProvinciaID) ON DELETE NO ACTION,
);

CREATE TABLE MetodoPago (
    MetodoPagoID INT PRIMARY KEY IDENTITY(1,1),     -- Identificador �nico del m�todo de pago
    UsuarioID INT FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- FK que referencia al usuario
    NumeroTarjetaCifrado NVARCHAR(256),            -- N�mero de la tarjeta (cifrado con AES y almacenado en base64)
    ClaveCifrado NVARCHAR(256),                    -- Clave de cifrado utilizada (cifrada de manera segura)
    NombreTitular NVARCHAR(100),                   -- Nombre del titular de la tarjeta
    FechaExpiracion DATE,                          -- Fecha de expiraci�n (para tarjetas)
    CodigoSeguridad NVARCHAR(4),                   -- C�digo de seguridad (almacenado de manera segura/encriptada)
    FechaRegistro DATETIME DEFAULT GETDATE(),      -- Fecha en la que se registr� el m�todo de pago
    Activo BIT DEFAULT 1                           -- Indica si el m�todo de pago est� activo (1) o no (0)
);

CREATE TABLE Marcas (
    MarcaID INT PRIMARY KEY IDENTITY (1,1),             -- Identificador �nico de la marca
    Nombre NVARCHAR(100) NOT NULL UNIQUE          -- Nombre de la marca, con restricci�n de unicidad
);

CREATE TABLE Categorias (
    CategoriaID INT PRIMARY KEY IDENTITY (1,1),         -- Identificador �nico de la categor�a
    Nombre NVARCHAR(100) NOT NULL UNIQUE          -- Nombre de la categor�a, con restricci�n de unicidad
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
    
    -- Definici�n de claves for�neas con nombres bonitos y ON DELETE NO ACTION
    CONSTRAINT FK_Productos_Categorias FOREIGN KEY (CategoriaID) REFERENCES Categorias(CategoriaID) ON DELETE NO ACTION,
    CONSTRAINT FK_Productos_Marcas FOREIGN KEY (MarcaID) REFERENCES Marcas(MarcaID) ON DELETE NO ACTION
);

CREATE TABLE Facturas (
    FacturaID INT PRIMARY KEY IDENTITY (1,1),                   -- Identificador �nico de la factura
    UsuarioID INT NOT NULL FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- Relaci�n con el usuario
    FechaFactura DATE DEFAULT GETDATE(),                  -- Fecha de la factura (fecha de compra)
    Total DECIMAL(10, 2) NOT NULL CHECK (Total >= 0), -- Monto total con descuentos aplicados
    CostoEnvio DECIMAL(10, 2) NOT NULL CHECK (CostoEnvio >= 0),
	Estado NVARCHAR(50) NOT NULL DEFAULT 'Pendiente'
);


CREATE TABLE LineasFactura (
    LineaFacturaID INT PRIMARY KEY IDENTITY (1,1),                         -- Identificador �nico de la l�nea de factura
    FacturaID INT NOT NULL FOREIGN KEY REFERENCES Facturas(FacturaID) ON DELETE CASCADE, -- Relaci�n con Factura
    ProductoID INT NOT NULL, -- Identificador del producto comprado
    ProductoNombre NVARCHAR(100) NOT NULL,                         -- Nombre del producto al momento de la compra
    Linea INT NOT NULL,                                            -- Orden de la l�nea en la factura
    Cantidad DECIMAL(10, 2) NOT NULL CHECK (Cantidad > 0),         -- Cantidad de producto comprado
    PrecioOriginal DECIMAL(10, 2) NOT NULL CHECK (PrecioOriginal >= 0), -- Precio del producto sin descuento
    DescuentoAplicado DECIMAL(5, 2) DEFAULT 0 CHECK (DescuentoAplicado BETWEEN 0 AND 100), -- % de descuento
    LineaTotal DECIMAL(10, 2) NOT NULL CHECK (LineaTotal >= 0)     -- Total de la l�nea con descuento aplicado
    -- Definici�n de claves for�neas con nombres bonitos y ON DELETE CASCADE
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
    ReviewID INT PRIMARY KEY IDENTITY (1,1),                    -- Identificador �nico de la rese�a
    UsuarioID INT NOT NULL FOREIGN KEY REFERENCES Usuarios(UsuarioID) ON DELETE CASCADE, -- Usuario que hizo la rese�a
    ProductoID INT NOT NULL FOREIGN KEY REFERENCES Productos(ProductoID) ON DELETE CASCADE, -- Producto que se est� rese�ando
    Calificacion INT NOT NULL CHECK (Calificacion BETWEEN 1 AND 5), -- Calificaci�n del 1 al 5
    Comentario NVARCHAR(1000),                            -- Comentario opcional de la rese�a
    FechaReview DATETIME NOT NULL DEFAULT GETDATE()       -- Fecha en que se realiz� la rese�a
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
    -- Definici�n de claves for�neas con nombres bonitos y ON DELETE CASCADE
    CONSTRAINT FK_CarritoProducto_Productos FOREIGN KEY (ProductoID) REFERENCES Productos(ProductoID) ON DELETE CASCADE,
);


-- INSERCIONES

INSERT INTO Paises (Codigo, Nombre) VALUES 
('CRC', 'Costa Rica'),
('COL', 'Colombia'),
('PAN', 'Panam�'),
('USA', 'Estados Unidos'),
('GTA', 'Guatemala');

INSERT INTO Estados_Provincias (PaisID, Nombre) VALUES 
(1, 'San Jos�'),
(1, 'Alajuela'),
(1, 'Cartago'),
(1, 'Heredia'),
(1, 'Guanacaste'),
(1, 'Puntarenas'),
(1, 'Lim�n');


INSERT INTO Estados_Provincias (PaisID, Nombre) VALUES 
(2, 'Medell�n'),
(2, 'Cartagena');

INSERT INTO Marcas(Nombre) VALUES
('Pokemarket'),
('Pikastore');

INSERT INTO Categorias(Nombre) VALUES
('Pokeballs'),
('Bayas');

INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Pokeball', 'Dispositivo b�sico para capturar Pok�mon.', 100, 5, 1, 1, 'https://i.ibb.co/pyDt2Wx/poke-ball.png'),
('Superball', 'Pokeball mejorada con mayor tasa de captura.', 600, 5, 1, 1, 'https://i.ibb.co/TPPsQb9/great-ball.png'),
('Ultraball', 'Ball avanzada con alta tasa de captura.', 1200, 5, 1, 1, 'https://i.ibb.co/3sBYwHJ/ultra-ball.png'),
('Veloz Ball', 'Ideal para capturas r�pidas al inicio del encuentro', 1000, 5, 1, 1, 'https://i.ibb.co/4Tjy7mf/quick-ball.png'),
('Honor Ball', 'Ball especial usada en ocasiones honor�ficas.', 200, 5, 1, 1, 'https://i.ibb.co/zHzgrLW/premier-ball.png'),
('Sana Ball', 'Cura los estados alterados del Pok�mon al capturarlo.', 300, 5, 1, 1, 'https://i.ibb.co/3zt7xXp/heal-ball.png'),
('Buceo Ball', 'Eficaz para capturar Pok�mon encontrados bajo el agua.', 1000, 5, 1, 1, 'https://i.ibb.co/p3wrZ16/dive-ball.png'),
('Malla Ball', 'Ideal para capturar Pok�mon de tipo Agua o Bicho.', 1000, 5, 1, 1, 'https://i.ibb.co/82gk99G/net-ball.png'),
('Nido Ball', 'M�s efectiva con Pok�mon de menor nivel.', 1000, 5, 1, 1, 'https://i.ibb.co/3YM8nzL/nest-ball.png'),
('Lujo Ball', 'Ball que aumenta la felicidad del Pok�mon capturado.', 1000, 5, 1, 1, 'https://i.ibb.co/WFm7tMc/luxury-ball.png');

INSERT INTO Productos(Nombre, Descripcion, Precio, Stock, CategoriaID, MarcaID, Imagen) VALUES
('Baya Zreza', 'Cura a un Pok�mon paralizado.', 10, 5, 2, 2, 'https://i.ibb.co/Kj6shKd/cheri-berry.png'),
('Baya Atania', 'Cura a un Pok�mon dormido.', 10, 5, 2, 2, 'https://i.ibb.co/5YCF5rD/chesto-berry.png'),
('Baya Meloc', 'Cura a un Pok�mon envenenado.', 10, 5, 2, 2, 'https://i.ibb.co/0YYs0DW/pecha-berry.png'),
('Baya Safre', 'Cura a un Pok�mon quemado.', 10, 5, 2, 2, 'https://i.ibb.co/QcsVpC9/rawst-berry.png'),
('Baya Perasi', 'Cura a un Pok�mon congelado.', 10, 5, 2, 2, 'https://i.ibb.co/rt4P6W7/aspear-berry.png'),
('Baya Zanama', 'Restaura 10 PP de un movimiento.', 10, 5, 2, 2, 'https://i.ibb.co/yBzv9ws/leppa-berry.png'),
('Baya Aranja', 'Restaura 10 PS.', 10, 5, 2, 2, ' https://i.ibb.co/dDLvT0f/oran-berry.png'),
('Baya Caquic', 'Cura a un Pok�mon confundido.', 10, 5, 2, 2, 'https://i.ibb.co/Xz7mPFf/persim-berry.png'),
('Baya Ziuela', 'Cura cualquier problema de estado.', 10, 5, 2, 2, 'https://i.ibb.co/hB1HSg5/lum-berry.png'),
('Baya Zidra', 'Restaura 30 PS o 1/4 de los PS m�ximos del Pok�mon.', 10, 5, 2, 2, 'https://i.ibb.co/GWSfttm/sitrus-berry.png');

INSERT INTO Descuentos(ProductoID, Porcentaje, FechaInicio, FechaFin) VALUES
(1, 10, GETDATE(), GETDATE() + 1);


-- Pruebas para el borrado de productos

INSERT INTO Descuentos(ProductoID, Porcentaje, FechaInicio, FechaFin) VALUES
(24, 10, GETDATE(), GETDATE() + 1);

INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario) VALUES
	(1, 24, 2, 'Me gust� pero no captura a veces.');

SELECT * FROM Productos
SELECT * FROM DESCUENTOS
SELECT * FROM Reviews
SELECT * FROM CarritoProducto
SELECT * FROM LineasFactura

INSERT INTO Reviews(UsuarioID, ProductoID, Calificacion, Comentario) VALUES
	(1, 5, 4.4, 'Me gust� pero no captura a veces.'),
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
OFFSET 2 * 10 ROWS -- N�mero de p�gina * N�mero de elementos por obtener
FETCH NEXT 10 ROWS ONLY; -- N�mero de elementos por obtener

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