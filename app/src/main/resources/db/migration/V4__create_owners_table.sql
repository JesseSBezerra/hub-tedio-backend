-- Criação da tabela de owners
CREATE TABLE owners (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para busca por nome
CREATE INDEX idx_owners_nome ON owners(nome);

-- Comentários
COMMENT ON TABLE owners IS 'Tabela de proprietários/donos de APIs';
COMMENT ON COLUMN owners.id IS 'Identificador único do owner';
COMMENT ON COLUMN owners.nome IS 'Nome do owner';
COMMENT ON COLUMN owners.descricao IS 'Descrição do owner';
