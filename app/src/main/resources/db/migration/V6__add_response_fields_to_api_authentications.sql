-- Adiciona coluna response_fields na tabela api_authentications
ALTER TABLE api_authentications
ADD COLUMN response_fields JSONB;

-- Comentário
COMMENT ON COLUMN api_authentications.response_fields IS 'Mapeamento de campos do response (nome -> tipo)';
