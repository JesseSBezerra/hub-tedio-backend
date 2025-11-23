-- Criação da tabela de autenticações de API
CREATE TABLE api_authentications (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao VARCHAR(500),
    owner_id BIGINT NOT NULL,
    url VARCHAR(1000) NOT NULL,
    authentication_type VARCHAR(50) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    request_body JSONB,
    headers JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_api_auth_owner FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_api_auth_nome ON api_authentications(nome);
CREATE INDEX idx_api_auth_owner_id ON api_authentications(owner_id);
CREATE INDEX idx_api_auth_type ON api_authentications(authentication_type);

-- Comentários
COMMENT ON TABLE api_authentications IS 'Tabela de configurações de autenticação de APIs';
COMMENT ON COLUMN api_authentications.id IS 'Identificador único da autenticação';
COMMENT ON COLUMN api_authentications.nome IS 'Nome único da configuração de autenticação';
COMMENT ON COLUMN api_authentications.owner_id IS 'ID do owner responsável';
COMMENT ON COLUMN api_authentications.url IS 'URL da API para autenticação';
COMMENT ON COLUMN api_authentications.authentication_type IS 'Tipo de autenticação (OAUTH2, BASIC, BEARER, API_KEY, NONE)';
COMMENT ON COLUMN api_authentications.content_type IS 'Content-Type da requisição';
COMMENT ON COLUMN api_authentications.request_body IS 'Corpo da requisição em formato JSON (chave-valor)';
COMMENT ON COLUMN api_authentications.headers IS 'Headers da requisição em formato JSON (chave-valor)';
