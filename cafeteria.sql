-- ============================================================
--  BASE DE DATOS: Cafetería IES Francisco Ayala
-- ============================================================
CREATE DATABASE IF NOT EXISTS cafeteria_ies
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cafeteria_ies;

-- ─────────────────────────────────────────────────────────────
-- 1. Tabla raíz de usuarios (OBLIGATORIA)
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS usuarios (
    id         INT          AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    nombre     VARCHAR(100) NOT NULL,
    apellidos  VARCHAR(150) NOT NULL,
    dni        VARCHAR(9)   NOT NULL UNIQUE,
    rol        ENUM('cliente','empleado') NOT NULL DEFAULT 'cliente'
);

-- ─────────────────────────────────────────────────────────────
-- 2. Joined Table Inheritance — tablas hijas
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS clientes (
    usuario_id INT PRIMARY KEY,
    curso      VARCHAR(50),
    CONSTRAINT fk_cliente FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS empleados (
    usuario_id INT PRIMARY KEY,
    turno      ENUM('mañana','tarde') NOT NULL DEFAULT 'mañana',
    CONSTRAINT fk_empleado FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────────────────────
-- 3. Entidad principal del dominio — productos
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS productos (
    id         INT            AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100)   NOT NULL,
    descripcion VARCHAR(255),
    precio     DECIMAL(6,2)   NOT NULL,
    categoria  ENUM('bebida','comida','bocadillo','otro') NOT NULL DEFAULT 'otro',
    stock      INT            NOT NULL DEFAULT 0
);

-- ─────────────────────────────────────────────────────────────
-- 4. Relación N:M — pedidos (cabecera)
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS pedidos (
    id          INT      AUTO_INCREMENT PRIMARY KEY,
    cliente_id  INT      NOT NULL,
    fecha       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado      ENUM('pendiente','preparando','listo','entregado') NOT NULL DEFAULT 'pendiente',
    total       DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id)
        REFERENCES clientes(usuario_id) ON DELETE CASCADE
);

-- Líneas del pedido (detalle N:M entre pedido y producto)
CREATE TABLE IF NOT EXISTS lineas_pedido (
    id          INT            AUTO_INCREMENT PRIMARY KEY,
    pedido_id   INT            NOT NULL,
    producto_id INT            NOT NULL,
    cantidad    INT            NOT NULL DEFAULT 1,
    precio_unit DECIMAL(6,2)  NOT NULL,
    CONSTRAINT fk_linea_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id)   ON DELETE CASCADE,
    CONSTRAINT fk_linea_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────────────────────
-- 5. Datos de ejemplo
-- ─────────────────────────────────────────────────────────────
INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) VALUES
('admin',   '1234', 'admin@ies.es',   'Ana',   'García López',   '11111111A', 'empleado'),
('juan',    '1234', 'juan@ies.es',    'Juan',  'Martínez Ruiz',  '22222222B', 'cliente'),
('maria',   '1234', 'maria@ies.es',   'María', 'Sánchez Pérez',  '33333333C', 'cliente');

INSERT INTO empleados (usuario_id, turno) VALUES (1, 'mañana');
INSERT INTO clientes  (usuario_id, curso) VALUES (2, '2º DAM'), (3, '1º DAW');

INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES
('Café solo',      'Café espresso',             1.00, 'bebida',    100),
('Café con leche', 'Café con leche entera',     1.20, 'bebida',    100),
('Bocadillo jamón','Pan con jamón serrano',     2.50, 'bocadillo',  30),
('Croissant',      'Croissant de mantequilla',  1.50, 'comida',     40),
('Agua 500ml',     'Botella de agua mineral',   0.80, 'bebida',     80),
('Zumo naranja',   'Zumo natural de naranja',   1.80, 'bebida',     50);

USE cafeteria_ies;
SELECT * FROM usuarios;