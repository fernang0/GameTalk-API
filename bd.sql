CREATE DATABASE IF NOT EXISTS gametalk;
USE gametalk;

-- ============================================
-- CREACIÓN DE TABLAS
-- ============================================

-- Tabla USERS
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla CATEGORIES
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    icon VARCHAR(100) NOT NULL,
    topics_count INT NOT NULL DEFAULT 0
);

-- Tabla TOPICS
CREATE TABLE topics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    created_at BIGINT NOT NULL,
    replies_count INT NOT NULL DEFAULT 0,
    views_count INT NOT NULL DEFAULT 0,
    last_activity BIGINT NOT NULL,

    CONSTRAINT fk_topics_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_topics_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Índices adicionales
CREATE INDEX idx_topics_category_id ON topics(category_id);
CREATE INDEX idx_topics_user_id ON topics(user_id);


-- ============================================
-- POBLAR BASE DE DATOS
-- ============================================

-- USERS
INSERT INTO users (email, password, username) VALUES
('admin@example.com', 'hashedpassword123', 'admin'),
('user1@example.com', 'hashedpass1', 'user1'),
('user2@example.com', 'hashedpass2', 'user2');

-- CATEGORIES
INSERT INTO categories (name, description, icon, topics_count) VALUES
('Acción', 'Juegos de acción, shooters y aventuras trepidantes.', 'action_icon', 0),
('RPG', 'Juegos de rol, aventuras épicas y mundos abiertos.', 'rpg_icon', 0),
('Estrategia', 'Juegos de estrategia en tiempo real y por turnos.', 'strategy_icon', 0),
('Deportes', 'Juegos de deportes, simuladores y competencias.', 'sports_icon', 0),
('Multijugador', 'Discusiones sobre juegos online y competitivos.', 'multiplayer_icon', 0);

-- TOPICS
INSERT INTO topics (category_id, user_id, title, description, created_at, replies_count, views_count, last_activity)
VALUES
(1, 1, '¿Vale la pena Cyberpunk 2077 en 2025?', 'Después de todos los parches y actualizaciones, ¿creen que ahora sí vale la pena jugarlo?', UNIX_TIMESTAMP() * 1000, 0, 10, UNIX_TIMESTAMP() * 1000),
(2, 2, 'Mejores builds para Elden Ring', 'Comparto mis mejores builds para principiantes y veteranos en Elden Ring.', UNIX_TIMESTAMP() * 1000, 5, 45, UNIX_TIMESTAMP() * 1000),
(5, 3, 'Toxicidad en League of Legends', '¿Cómo lidian con los jugadores tóxicos? Necesito consejos.', UNIX_TIMESTAMP() * 1000, 12, 78, UNIX_TIMESTAMP() * 1000),
(3, 1, 'Civilization VI vs VII', '¿Cuál prefieren y por qué? Análisis completo de ambos juegos.', UNIX_TIMESTAMP() * 1000, 3, 22, UNIX_TIMESTAMP() * 1000),
(4, 2, 'EA Sports FC 25 - Opiniones', 'Primeras impresiones del nuevo juego de fútbol de EA.', UNIX_TIMESTAMP() * 1000, 8, 56, UNIX_TIMESTAMP() * 1000);

-- Actualizar topics_count en categories
UPDATE categories 
SET topics_count = (SELECT COUNT(*) FROM topics WHERE category_id = categories.id);

