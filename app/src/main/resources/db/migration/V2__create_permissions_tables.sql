-- Criação da tabela de permissões
CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela de relacionamento user_permissions
CREATE TABLE user_permissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_permissions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_permission UNIQUE (user_id, permission_id)
);

-- Índices para melhor performance
CREATE INDEX idx_user_permissions_user_id ON user_permissions(user_id);
CREATE INDEX idx_user_permissions_permission_id ON user_permissions(permission_id);

-- Inserção das permissões padrão
INSERT INTO permissions (name, description) VALUES 
    ('user', 'Permissão para gerenciar usuários'),
    ('permission', 'Permissão para gerenciar permissões'),
    ('omnichannel', 'Permissão para acessar omnichannel');

-- Comentários nas tabelas
COMMENT ON TABLE permissions IS 'Tabela de permissões do sistema';
COMMENT ON TABLE user_permissions IS 'Tabela de relacionamento entre usuários e permissões';
COMMENT ON COLUMN permissions.name IS 'Nome único da permissão';
COMMENT ON COLUMN permissions.description IS 'Descrição da permissão';
COMMENT ON COLUMN user_permissions.user_id IS 'ID do usuário';
COMMENT ON COLUMN user_permissions.permission_id IS 'ID da permissão';
