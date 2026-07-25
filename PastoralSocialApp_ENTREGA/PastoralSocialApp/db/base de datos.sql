CREATE DATABASE pastoral_social;
USE pastoral_social;

CREATE TABLE tipo_asistencia (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO tipo_asistencia (nombre) VALUES
 ('Alimentos'),
 ('Arts. higiene y limpieza'),
 ('Indumentaria'),
 ('Alquiler'),
 ('Servicios (luz, agua, otros)'),
 ('Medicamentos'),
 ('Aparatos ortopédicos'),
 ('Otros');
 USE pastoral_social;

CREATE TABLE expediente (
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    numero_ficha            VARCHAR(20) NOT NULL UNIQUE,
    fecha_inicio            DATE,
    fecha_prevista_fin      DATE,
    parroquia               VARCHAR(100),
    sector_filial           VARCHAR(100),
    direccion               VARCHAR(255),
    telefono_contacto       VARCHAR(50),
    tipo_vivienda           VARCHAR(30),
    tenencia                VARCHAR(30),
    condicion_vivienda      VARCHAR(30),
    entrevistador           VARCHAR(150),
    observaciones           TEXT,
    fecha_creacion          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

USE pastoral_social;

CREATE TABLE persona (
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    expediente_id           INT NOT NULL,
    apellidos_nombres       VARCHAR(150) NOT NULL,
    documento_identidad     VARCHAR(30),
    sexo                    VARCHAR(10),
    jefatura                BOOLEAN DEFAULT FALSE,
    relacion                VARCHAR(50),
    edad                    INT,
    pais                    VARCHAR(60),
    migracion               VARCHAR(60),
    educacion               VARCHAR(60),
    salud                   VARCHAR(255),
    alarma_medica           BOOLEAN DEFAULT FALSE,
    seguro                  VARCHAR(60),

    CONSTRAINT fk_persona_expediente
        FOREIGN KEY (expediente_id) REFERENCES expediente(id)
        ON DELETE CASCADE
);
USE pastoral_social;

CREATE TABLE usuario (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    activo      BOOLEAN DEFAULT TRUE
);

INSERT INTO usuario (nombre, username, password, activo)
VALUES ('Administrador', 'admin', 'admin123', TRUE);