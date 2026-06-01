-- ============================================================
-- SCRIPT COMPLETO DE BASE DE DATOS - LIBRERÍA CACERES
-- ============================================================
-- Incluye todas las tablas necesarias para el sistema

-- ============================================================
-- 1. TABLAS BASE - PERSONAS Y CONTACTOS
-- ============================================================

CREATE TABLE Persona (
  IdPersona SERIAL PRIMARY KEY,
  Nombre VARCHAR(100) NOT NULL,
  ApellidoPaterno VARCHAR(100) NOT NULL,
  ApellidoMaterno VARCHAR(100),
  Sexo CHAR(1),
  FechaDeNacimiento DATE,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FotoPerfil TEXT
);

CREATE TABLE Contacto (
  IdContacto SERIAL PRIMARY KEY,
  Email VARCHAR(150),
  Celular VARCHAR(50),
  IdPersona INT NOT NULL,
  FOREIGN KEY (IdPersona) REFERENCES Persona(IdPersona) ON DELETE CASCADE
);

CREATE TABLE Direccion (
  IdDireccion SERIAL PRIMARY KEY,
  Pais VARCHAR(100),
  Ciudad VARCHAR(100),
  Zona VARCHAR(100),
  Direccion TEXT,
  Referencia TEXT,
  IdPersona INT NOT NULL,
  FOREIGN KEY (IdPersona) REFERENCES Persona(IdPersona) ON DELETE CASCADE
);

-- ============================================================
-- 2. TABLAS DE SEGURIDAD Y AUTENTICACIÓN
-- ============================================================

CREATE TABLE Rol (
  IdRol SERIAL PRIMARY KEY,
  Nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Permiso (
  IdPermiso SERIAL PRIMARY KEY,
  Nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Modulo (
  IdModulo SERIAL PRIMARY KEY,
  Nombre VARCHAR(100) NOT NULL,
  Descripcion VARCHAR(200),
  Icono VARCHAR(50),
  Ruta VARCHAR(100)
);

CREATE TABLE Usuario (
  IdUsuario SERIAL PRIMARY KEY,
  Username VARCHAR(50) NOT NULL UNIQUE,
  Password TEXT NOT NULL,
  Activo BOOLEAN DEFAULT TRUE,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdPersona INT NOT NULL,
  FOREIGN KEY (IdPersona) REFERENCES Persona(IdPersona) ON DELETE CASCADE
);

CREATE TABLE UsuarioRol (
  IdUsuarioRol SERIAL PRIMARY KEY,
  IdUsuario INT NOT NULL,
  IdRol INT NOT NULL,
  FechaRegistro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario) ON DELETE CASCADE,
  FOREIGN KEY (IdRol) REFERENCES Rol(IdRol) ON DELETE CASCADE,
  UNIQUE(IdUsuario, IdRol)
);

CREATE TABLE RolPermiso (
  IdRolPermiso SERIAL PRIMARY KEY,
  IdRol INT NOT NULL,
  IdPermiso INT NOT NULL,
  FOREIGN KEY (IdRol) REFERENCES Rol(IdRol) ON DELETE CASCADE,
  FOREIGN KEY (IdPermiso) REFERENCES Permiso(IdPermiso) ON DELETE CASCADE,
  UNIQUE(IdRol, IdPermiso)
);

CREATE TABLE RolAcceso (
  IdRolAcceso SERIAL PRIMARY KEY,
  IdRol INT NOT NULL,
  IdModulo INT NOT NULL,
  IdPermiso INT NOT NULL,
  FOREIGN KEY (IdRol) REFERENCES Rol(IdRol) ON DELETE CASCADE,
  FOREIGN KEY (IdModulo) REFERENCES Modulo(IdModulo) ON DELETE CASCADE,
  FOREIGN KEY (IdPermiso) REFERENCES Permiso(IdPermiso) ON DELETE CASCADE,
  UNIQUE(IdRol, IdModulo, IdPermiso)
);

CREATE TABLE TokenRecuperacion (
  IdToken SERIAL PRIMARY KEY,
  Token VARCHAR(255) NOT NULL UNIQUE,
  FechaExpiracion TIMESTAMP NOT NULL,
  Usado BOOLEAN DEFAULT FALSE,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdUsuario INT NOT NULL,
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario) ON DELETE CASCADE
);

-- ============================================================
-- 3. TABLAS DE CLIENTES Y PROVEEDORES
-- ============================================================

CREATE TABLE Cliente (
  IdCliente SERIAL PRIMARY KEY,
  Saldo NUMERIC(12,2) DEFAULT 0.00,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdPersona INT NOT NULL,
  FOREIGN KEY (IdPersona) REFERENCES Persona(IdPersona) ON DELETE CASCADE
);

CREATE TABLE Proveedor (
  IdProveedor SERIAL PRIMARY KEY,
  Descripcion VARCHAR(150),
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdPersona INT NOT NULL,
  FOREIGN KEY (IdPersona) REFERENCES Persona(IdPersona) ON DELETE CASCADE
);

-- ============================================================
-- 4. TABLAS DE CATÁLOGO Y PRODUCTOS
-- ============================================================

CREATE TABLE Categoria (
  IdCategoria SERIAL PRIMARY KEY,
  Nombre VARCHAR(100) NOT NULL UNIQUE,
  Descripcion TEXT,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Marca (
  IdMarca SERIAL PRIMARY KEY,
  Nombre VARCHAR(100) NOT NULL UNIQUE,
  Descripcion TEXT,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE UnidadMedida (
  IdUnidad SERIAL PRIMARY KEY,
  Nombre VARCHAR(50) NOT NULL UNIQUE,
  Abreviatura VARCHAR(10),
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Producto (
  IdProducto SERIAL PRIMARY KEY,
  Codigo VARCHAR(50) NOT NULL UNIQUE,
  Nombre VARCHAR(150) NOT NULL,
  Descripcion TEXT,
  Precio NUMERIC(12,2) NOT NULL,
  Activo BOOLEAN DEFAULT TRUE,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdCategoria INT NOT NULL,
  IdMarca INT,
  FOREIGN KEY (IdCategoria) REFERENCES Categoria(IdCategoria),
  FOREIGN KEY (IdMarca) REFERENCES Marca(IdMarca)
);

CREATE TABLE ProductoUnidad (
  IdProductoUnidad SERIAL PRIMARY KEY,
  IdProducto INT NOT NULL,
  IdUnidad INT NOT NULL,
  Equivalencia INT DEFAULT 1,
  Precio NUMERIC(12,2) NOT NULL,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (IdProducto) REFERENCES Producto(IdProducto) ON DELETE CASCADE,
  FOREIGN KEY (IdUnidad) REFERENCES UnidadMedida(IdUnidad),
  UNIQUE(IdProducto, IdUnidad)
);

-- ============================================================
-- 5. TABLAS DE INVENTARIO
-- ============================================================

CREATE TABLE Inventario (
  IdInventario SERIAL PRIMARY KEY,
  Stock INT NOT NULL DEFAULT 0,
  StockMinimo INT DEFAULT 0,
  StockMaximo INT,
  FechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdProductoUnidad INT NOT NULL,
  FOREIGN KEY (IdProductoUnidad) REFERENCES ProductoUnidad(IdProductoUnidad) ON DELETE CASCADE,
  UNIQUE(IdProductoUnidad)
);

CREATE TABLE MovimientoInventario (
  IdMovimiento SERIAL PRIMARY KEY,
  Tipo VARCHAR(50) NOT NULL,
  Cantidad INT NOT NULL,
  StockAnterior INT,
  StockPosterior INT,
  Motivo VARCHAR(255),
  Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  IdProductoUnidad INT NOT NULL,
  IdUsuario INT NOT NULL,
  FOREIGN KEY (IdProductoUnidad) REFERENCES ProductoUnidad(IdProductoUnidad),
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);

-- ============================================================
-- 6. TABLAS DE SERVICIOS
-- ============================================================

CREATE TABLE Servicio (
  IdServicio SERIAL PRIMARY KEY,
  Nombre VARCHAR(100) NOT NULL UNIQUE,
  Descripcion TEXT,
  Precio NUMERIC(12,2),
  Activo BOOLEAN DEFAULT TRUE,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 7. TABLAS DE VENTAS
-- ============================================================

CREATE TABLE Venta (
  IdVenta SERIAL PRIMARY KEY,
  Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  Total NUMERIC(12,2) NOT NULL,
  Estado VARCHAR(50) DEFAULT 'Completada',
  Observacion TEXT,
  IdCliente INT NOT NULL,
  IdUsuario INT NOT NULL,
  FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);

CREATE TABLE VentaDetalle (
  IdDetalle SERIAL PRIMARY KEY,
  Cantidad INT NOT NULL,
  Precio NUMERIC(12,2) NOT NULL,
  Subtotal NUMERIC(12,2) NOT NULL,
  Descuento NUMERIC(12,2) DEFAULT 0.00,
  IdVenta INT NOT NULL,
  IdProductoUnidad INT NOT NULL,
  FOREIGN KEY (IdVenta) REFERENCES Venta(IdVenta) ON DELETE CASCADE,
  FOREIGN KEY (IdProductoUnidad) REFERENCES ProductoUnidad(IdProductoUnidad)
);

-- ============================================================
-- 8. TABLAS DE PEDIDOS
-- ============================================================

CREATE TABLE Pedido (
  IdPedido SERIAL PRIMARY KEY,
  Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FechaEntregaEstimada DATE,
  Estado VARCHAR(50) DEFAULT 'Pendiente',
  Observacion TEXT,
  IdUsuario INT NOT NULL,
  IdCliente INT NOT NULL,
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario),
  FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente)
);

CREATE TABLE PedidoDetalle (
  IdDetalle SERIAL PRIMARY KEY,
  Tipo VARCHAR(20) NOT NULL,
  Cantidad INT NOT NULL,
  Precio NUMERIC(12,2) NOT NULL,
  IdPedido INT NOT NULL,
  IdProductoUnidad INT,
  IdServicio INT,
  FOREIGN KEY (IdPedido) REFERENCES Pedido(IdPedido) ON DELETE CASCADE,
  FOREIGN KEY (IdProductoUnidad) REFERENCES ProductoUnidad(IdProductoUnidad),
  FOREIGN KEY (IdServicio) REFERENCES Servicio(IdServicio)
);

CREATE TABLE PedidoDetallePersonalizado (
  IdPersonalizado SERIAL PRIMARY KEY,
  IdDetalle INT NOT NULL,
  TipoTrabajo VARCHAR(50),
  ColorTapaFrontal VARCHAR(50),
  ColorTapaPosterior VARCHAR(50),
  CantidadHojas INT,
  Tamano VARCHAR(50),
  Observacion TEXT,
  FOREIGN KEY (IdDetalle) REFERENCES PedidoDetalle(IdDetalle) ON DELETE CASCADE
);

-- ============================================================
-- 9. TABLAS DE TIPO DE CAMBIO Y REPORTES
-- ============================================================

CREATE TABLE TipoCambio (
  IdTipoCambio SERIAL PRIMARY KEY,
  Valor NUMERIC(12,4) NOT NULL,
  Moneda VARCHAR(3) DEFAULT 'Bs',
  Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Reporte (
  IdReporte SERIAL PRIMARY KEY,
  Tipo VARCHAR(50) NOT NULL,
  FechaInicio TIMESTAMP,
  FechaFin TIMESTAMP,
  FechaGenerado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  Datos TEXT,
  IdUsuario INT NOT NULL,
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);

-- ============================================================
-- 10. TABLA DE NOTIFICACIONES
-- ============================================================

CREATE TABLE NotificacionVenta (
  IdNotificacion SERIAL PRIMARY KEY,
  IdVenta INT,
  IdPedido INT,
  Medio VARCHAR(50),
  Estado VARCHAR(50) DEFAULT 'Pendiente',
  FechaEnvio TIMESTAMP,
  FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  Mensaje TEXT,
  IdCliente INT,
  IdUsuario INT,
  FOREIGN KEY (IdVenta) REFERENCES Venta(IdVenta),
  FOREIGN KEY (IdPedido) REFERENCES Pedido(IdPedido),
  FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);

-- ============================================================
-- ÍNDICES PARA MEJORAR PERFORMANCE
-- ============================================================

-- Índices en tablas principales
CREATE INDEX idx_usuario_username ON Usuario(Username);
CREATE INDEX idx_usuario_persona ON Usuario(IdPersona);
CREATE INDEX idx_cliente_persona ON Cliente(IdPersona);
CREATE INDEX idx_proveedor_persona ON Proveedor(IdPersona);

-- Índices en productos e inventario
CREATE INDEX idx_producto_codigo ON Producto(Codigo);
CREATE INDEX idx_producto_categoria ON Producto(IdCategoria);
CREATE INDEX idx_productounidad_producto ON ProductoUnidad(IdProducto);
CREATE INDEX idx_inventario_productounidad ON Inventario(IdProductoUnidad);

-- Índices en ventas y pedidos
CREATE INDEX idx_venta_cliente ON Venta(IdCliente);
CREATE INDEX idx_venta_usuario ON Venta(IdUsuario);
CREATE INDEX idx_venta_fecha ON Venta(Fecha);
CREATE INDEX idx_ventadetalle_venta ON VentaDetalle(IdVenta);

CREATE INDEX idx_pedido_cliente ON Pedido(IdCliente);
CREATE INDEX idx_pedido_usuario ON Pedido(IdUsuario);
CREATE INDEX idx_pedido_fecha ON Pedido(Fecha);
CREATE INDEX idx_pedidodetalle_pedido ON PedidoDetalle(IdPedido);

-- Índices en seguridad
CREATE INDEX idx_usuariorol_usuario ON UsuarioRol(IdUsuario);
CREATE INDEX idx_usuariorol_rol ON UsuarioRol(IdRol);
CREATE INDEX idx_tokenrecuperacion_usuario ON TokenRecuperacion(IdUsuario);
CREATE INDEX idx_tokenrecuperacion_token ON TokenRecuperacion(Token);

-- Índices en movimientos
CREATE INDEX idx_movimiento_productounidad ON MovimientoInventario(IdProductoUnidad);
CREATE INDEX idx_movimiento_usuario ON MovimientoInventario(IdUsuario);
CREATE INDEX idx_movimiento_fecha ON MovimientoInventario(Fecha);

-- ============================================================
-- INSERCIÓN DE DATOS INICIALES
-- ============================================================

-- Roles básicos
INSERT INTO Rol (Nombre) VALUES 
  ('Administrador'),
  ('Vendedor'),
  ('Gerente'),
  ('Cliente');

-- Permisos básicos
INSERT INTO Permiso (Nombre) VALUES
  ('Ver'),
  ('Crear'),
  ('Editar'),
  ('Eliminar'),
  ('Reportes'),
  ('Configuración');

-- Módulos del sistema
INSERT INTO Modulo (Nombre, Descripcion, Icono, Ruta) VALUES
  ('Inicio', 'Dashboard Principal', 'home', '/inicio'),
  ('Usuarios', 'Gestión de Usuarios', 'users', '/usuarios'),
  ('Roles', 'Gestión de Roles', 'shield', '/roles'),
  ('Clientes', 'Gestión de Clientes', 'users', '/clientes'),
  ('Proveedores', 'Gestión de Proveedores', 'truck', '/proveedores'),
  ('Productos', 'Gestión de Productos', 'package', '/productos'),
  ('Inventario', 'Control de Inventario', 'box', '/inventario'),
  ('Ventas', 'Registro de Ventas', 'shopping-cart', '/ventas'),
  ('Pedidos', 'Gestión de Pedidos', 'clipboard', '/pedidos'),
  ('Reportes', 'Reportes del Sistema', 'chart-bar', '/reportes');

-- Categorías de productos
INSERT INTO Categoria (Nombre, Descripcion) VALUES
  ('Libros', 'Libros varios'),
  ('Cuadernos', 'Cuadernos y libretas'),
  ('Útiles Escolares', 'Útiles de escritura y escuela'),
  ('Personalizados', 'Productos personalizados');

-- Unidades de medida
INSERT INTO UnidadMedida (Nombre, Abreviatura) VALUES
  ('Unidad', 'Und'),
  ('Caja', 'Cj'),
  ('Paquete', 'Pq'),
  ('Docena', 'Doc');

-- Servicios
INSERT INTO Servicio (Nombre, Descripcion, Precio) VALUES
  ('Encuadernación', 'Servicio de encuadernación de libros', 50.00),
  ('Personalización', 'Personalización de productos', 75.00),
  ('Entrega a Domicilio', 'Envío a domicilio', 25.00);

-- Tipos de cambio
INSERT INTO TipoCambio (Valor, Moneda) VALUES
  (1.0, 'Bs'),
  (6.96, 'USD');

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
