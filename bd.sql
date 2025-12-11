CREATE DATABASE gametalk;

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
    topicsCount INT NOT NULL DEFAULT 0
);

-- Tabla TOPICS
CREATE TABLE topics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoryId INT NOT NULL,
    userId INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    createdAt BIGINT NOT NULL,
    repliesCount INT NOT NULL DEFAULT 0,
    viewsCount INT NOT NULL DEFAULT 0,
    lastActivity BIGINT NOT NULL,

    CONSTRAINT fk_topics_category
        FOREIGN KEY (categoryId)
        REFERENCES categories(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_topics_user
        FOREIGN KEY (userId)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Índices adicionales
CREATE INDEX idx_topics_categoryId ON topics(categoryId);
CREATE INDEX idx_topics_userId ON topics(userId);


-- ============================================
-- POBLAR BASE DE DATOS
-- ============================================

-- USERS
INSERT INTO users (email, password, username) VALUES
('admin@example.com', 'hashedpassword123', 'admin'),
('user1@example.com', 'hashedpass1', 'user1'),
('user2@example.com', 'hashedpass2', 'user2');

-- CATEGORIES
INSERT INTO categories (name, description, icon, topicsCount) VALUES
('Programación', 'Discusión sobre distintos lenguajes y frameworks.', 'code_icon', 0),
('Base de Datos', 'Consultas, modelado y rendimiento.', 'db_icon', 0),
('DevOps', 'Automatización, pipelines y despliegues.', 'devops_icon', 0);

-- TOPICS
INSERT INTO topics (categoryId, userId, title, description, createdAt, repliesCount, viewsCount, lastActivity)
VALUES
(1, 1, 'Cómo aprender Spring Boot rápido', 'Busco consejos para aprender el framework.', UNIX_TIMESTAMP() * 1000, 0, 10, UNIX_TIMESTAMP() * 1000),
(2, 2, 'Problemas con índices en MySQL', 'Tengo dudas sobre la optimización de consultas.', UNIX_TIMESTAMP() * 1000, 2, 45, UNIX_TIMESTAMP() * 1000),
(1, 3, '¿Kotlin o Java para backend?', '¿Cuál recomiendan para proyectos nuevos?', UNIX_TIMESTAMP() * 1000, 1, 22, UNIX_TIMESTAMP() * 1000);

-- Actualizar topicsCount en categories
UPDATE categories 
SET topicsCount = (SELECT COUNT(*) FROM topics WHERE categoryId = categories.id);

