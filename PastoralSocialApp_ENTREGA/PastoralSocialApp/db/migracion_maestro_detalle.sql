-- ============================================================================
--  Migración: catálogos de Parroquia / Sector-Filial y relaciones maestro-detalle
--  Base de datos: pastoral_social  (MySQL)
--
--  Es NO DESTRUCTIVA:
--    * No elimina columnas ni datos existentes.
--    * Las columnas de texto libre antiguas (expediente.parroquia y
--      expediente.sector_filial) se conservan por compatibilidad; quedan en
--      desuso a favor de las nuevas llaves foráneas.
--
--  Nota: la aplicación usa hibernate.hbm2ddl.auto=update, por lo que estas
--  tablas y columnas se crean automáticamente al iniciar el módulo y el
--  catálogo se siembra desde el código (ParroquiaDAO.sembrarSiVacio()). Este
--  script se ofrece para aplicar/documentar los cambios de forma explícita.
-- ============================================================================

USE pastoral_social;

-- 1) Catálogo maestro de parroquias --------------------------------------------
CREATE TABLE IF NOT EXISTS parroquia (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL UNIQUE
);

-- 2) Catálogo detalle de sectores / filiales (cada sector es de una parroquia) -
CREATE TABLE IF NOT EXISTS sector_filial (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    parroquia_id  INT NOT NULL,
    CONSTRAINT fk_sector_parroquia
        FOREIGN KEY (parroquia_id) REFERENCES parroquia(id)
        ON DELETE CASCADE
);

-- 3) Nuevas llaves foráneas en el expediente (maestro Caso Social) -------------
--    Se agregan como NULL para no afectar filas existentes.
ALTER TABLE expediente
    ADD COLUMN parroquia_id     INT NULL AFTER fecha_prevista_fin,
    ADD COLUMN sector_filial_id INT NULL AFTER parroquia_id;

ALTER TABLE expediente
    ADD CONSTRAINT fk_expediente_parroquia
        FOREIGN KEY (parroquia_id) REFERENCES parroquia(id),
    ADD CONSTRAINT fk_expediente_sector
        FOREIGN KEY (sector_filial_id) REFERENCES sector_filial(id);

-- 4) Datos semilla del catálogo (idempotente: no duplica si ya existen) --------
INSERT INTO parroquia (nombre) VALUES
    ('Parroquia Catedral'),
    ('Parroquia San José'),
    ('Parroquia Nuestra Señora del Carmen'),
    ('Parroquia San Antonio'),
    ('Parroquia Inmaculada Concepción')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO sector_filial (nombre, parroquia_id)
SELECT s.nombre, p.id
FROM parroquia p
JOIN (
    SELECT 'Parroquia Catedral' AS parroquia, 'Centro' AS nombre UNION ALL
    SELECT 'Parroquia Catedral', 'Barrio Norte' UNION ALL
    SELECT 'Parroquia Catedral', 'Barrio Sur' UNION ALL
    SELECT 'Parroquia San José', 'San José este' UNION ALL
    SELECT 'Parroquia San José', 'San José oeste' UNION ALL
    SELECT 'Parroquia San José', 'La Colina' UNION ALL
    SELECT 'Parroquia Nuestra Señora del Carmen', 'El Carmen' UNION ALL
    SELECT 'Parroquia Nuestra Señora del Carmen', 'Los Ángeles' UNION ALL
    SELECT 'Parroquia Nuestra Señora del Carmen', 'La Trinidad' UNION ALL
    SELECT 'Parroquia San Antonio', 'San Antonio' UNION ALL
    SELECT 'Parroquia San Antonio', 'El Progreso' UNION ALL
    SELECT 'Parroquia San Antonio', 'Las Palmas' UNION ALL
    SELECT 'Parroquia Inmaculada Concepción', 'El Rosario' UNION ALL
    SELECT 'Parroquia Inmaculada Concepción', 'San Rafael' UNION ALL
    SELECT 'Parroquia Inmaculada Concepción', 'La Esperanza'
) s ON s.parroquia = p.nombre
WHERE NOT EXISTS (
    SELECT 1 FROM sector_filial sf
    WHERE sf.nombre = s.nombre AND sf.parroquia_id = p.id
);
