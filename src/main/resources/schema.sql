-- Script de creacion de la tabla 'cliente'
-- La base de datos 'powergym_db' se crea automaticamente por el driver JDBC
-- (parametro createDatabaseIfNotExist=true en application.properties)

CREATE TABLE IF NOT EXISTS cliente (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(50)   NOT NULL,
    apellidos       VARCHAR(100)  NOT NULL,
    dni             VARCHAR(15)   NOT NULL UNIQUE,
    entrenador      ENUM('Carlos Martín', 'Elena Vidal', 'Javier Rojas') NOT NULL,
    tipo_membresia  ENUM('mañana', 'tarde', 'completo') NOT NULL,
    fecha_alta      DATE          NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
