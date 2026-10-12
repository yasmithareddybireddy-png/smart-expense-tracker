-- Create database if not exists (run as a privileged MySQL user)
CREATE DATABASE IF NOT EXISTS expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE expense_tracker;

-- Tables will be created by JPA (hibernate.ddl-auto=update), but you can use explicit SQL if preferred.
