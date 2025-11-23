-- Criação da tabela de requests
CREATE TABLE requests (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    http_method VARCHAR(20) NOT NULL,
    path_id BIGINT NOT NULL,
    content_type VARCHAR(100),
    body_fields JSONB,
    header_fields JSONB,
    param_fields JSONB,
    request_example JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_request_path FOREIGN KEY (path_id) REFERENCES paths(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_request_nome ON requests(nome);
CREATE INDEX idx_request_path_id ON requests(path_id);
CREATE INDEX idx_request_http_method ON requests(http_method);

-- Comentários
COMMENT ON TABLE requests IS 'Tabela de requisições/chamadas dos paths';
COMMENT ON COLUMN requests.id IS 'Identificador único da request';
COMMENT ON COLUMN requests.nome IS 'Nome descritivo da request';
COMMENT ON COLUMN requests.http_method IS 'Método HTTP (GET, POST, PUT, DELETE, etc)';
COMMENT ON COLUMN requests.path_id IS 'ID do path ao qual a request pertence';
COMMENT ON COLUMN requests.content_type IS 'Content-Type da requisição';
COMMENT ON COLUMN requests.body_fields IS 'Campos do body (nome -> tipo)';
COMMENT ON COLUMN requests.header_fields IS 'Campos dos headers (nome -> tipo)';
COMMENT ON COLUMN requests.param_fields IS 'Campos dos parâmetros (nome -> tipo)';
COMMENT ON COLUMN requests.request_example IS 'Exemplo completo da requisição em formato JSON';
