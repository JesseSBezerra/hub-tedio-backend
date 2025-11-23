-- Adiciona coluna authentication_id na tabela integrations
ALTER TABLE integrations
ADD COLUMN authentication_id BIGINT;

-- Adiciona foreign key
ALTER TABLE integrations
ADD CONSTRAINT fk_integration_authentication 
FOREIGN KEY (authentication_id) REFERENCES api_authentications(id) ON DELETE SET NULL;

-- Adiciona índice
CREATE INDEX idx_integration_authentication_id ON integrations(authentication_id);

-- Comentário
COMMENT ON COLUMN integrations.authentication_id IS 'ID da autenticação vinculada à integração';
