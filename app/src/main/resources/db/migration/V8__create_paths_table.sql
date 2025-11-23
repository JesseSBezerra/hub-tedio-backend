-- Criação da tabela de paths
CREATE TABLE paths (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    path VARCHAR(1000) NOT NULL,
    integration_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_path_integration FOREIGN KEY (integration_id) REFERENCES integrations(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_path_nome ON paths(nome);
CREATE INDEX idx_path_integration_id ON paths(integration_id);

-- Comentários
COMMENT ON TABLE paths IS 'Tabela de paths/endpoints das integrações';
COMMENT ON COLUMN paths.id IS 'Identificador único do path';
COMMENT ON COLUMN paths.nome IS 'Nome descritivo do path';
COMMENT ON COLUMN paths.path IS 'Caminho do endpoint (ex: /users, /auth/login)';
COMMENT ON COLUMN paths.integration_id IS 'ID da integração à qual o path pertence';
