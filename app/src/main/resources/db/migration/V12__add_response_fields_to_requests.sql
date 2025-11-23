-- Adiciona colunas response_fields e response_example na tabela requests
ALTER TABLE requests
ADD COLUMN response_fields JSONB,
ADD COLUMN response_example JSONB;

-- Comentários
COMMENT ON COLUMN requests.response_fields IS 'Mapeamento dos campos do response (nome -> tipo)';
COMMENT ON COLUMN requests.response_example IS 'Exemplo completo do response';
