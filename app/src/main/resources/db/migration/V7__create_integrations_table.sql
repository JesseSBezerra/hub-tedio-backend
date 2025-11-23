-- Criação da tabela de integrações
CREATE TABLE integrations (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    base_url VARCHAR(1000) NOT NULL,
    owner_id BIGINT NOT NULL,
    headers JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_integration_owner FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_integration_nome ON integrations(nome);
CREATE INDEX idx_integration_owner_id ON integrations(owner_id);

-- Comentários
COMMENT ON TABLE integrations IS 'Tabela de integrações com sistemas externos';
COMMENT ON COLUMN integrations.id IS 'Identificador único da integração';
COMMENT ON COLUMN integrations.nome IS 'Nome único da integração';
COMMENT ON COLUMN integrations.base_url IS 'URL base da integração';
COMMENT ON COLUMN integrations.owner_id IS 'ID do owner responsável';
COMMENT ON COLUMN integrations.headers IS 'Headers padrão da integração em formato JSON (chave-valor)';
