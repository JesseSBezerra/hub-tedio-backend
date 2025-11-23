-- Criação da tabela de evolutions
CREATE TABLE evolutions (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao VARCHAR(500),
    url VARCHAR(1000) NOT NULL,
    api_key VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_evolution_nome ON evolutions(nome);

-- Comentários
COMMENT ON TABLE evolutions IS 'Tabela de instâncias Evolution API';
COMMENT ON COLUMN evolutions.id IS 'Identificador único da evolution';
COMMENT ON COLUMN evolutions.nome IS 'Nome único da evolution';
COMMENT ON COLUMN evolutions.descricao IS 'Descrição da evolution';
COMMENT ON COLUMN evolutions.url IS 'URL da instância Evolution';
COMMENT ON COLUMN evolutions.api_key IS 'API Key para autenticação';
