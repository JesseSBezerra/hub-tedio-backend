-- Adiciona coluna description na tabela integrations
ALTER TABLE integrations
ADD COLUMN description VARCHAR(500);

-- Comentário
COMMENT ON COLUMN integrations.description IS 'Descrição da integração';
