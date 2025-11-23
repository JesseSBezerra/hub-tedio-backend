-- Migration: Create users table
-- Author: TedioApp
-- Date: 2024-11-18

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    client_id VARCHAR(255),
    client_secret VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX idx_users_email ON users(email);

-- Create index on user_type for filtering
CREATE INDEX idx_users_user_type ON users(user_type);

-- Add comments to table and columns
COMMENT ON TABLE users IS 'Tabela de usuários do sistema - suporta tipos APPLICATION e INTEGRATION';
COMMENT ON COLUMN users.id IS 'Identificador único do usuário';
COMMENT ON COLUMN users.nome IS 'Nome completo do usuário';
COMMENT ON COLUMN users.email IS 'Email único do usuário (usado para login)';
COMMENT ON COLUMN users.password IS 'Senha criptografada com BCrypt';
COMMENT ON COLUMN users.user_type IS 'Tipo de usuário: APPLICATION ou INTEGRATION';
COMMENT ON COLUMN users.client_id IS 'ID do cliente (apenas para usuários INTEGRATION)';
COMMENT ON COLUMN users.client_secret IS 'Secret do cliente criptografado (apenas para usuários INTEGRATION)';
COMMENT ON COLUMN users.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN users.updated_at IS 'Data e hora da última atualização do registro';
