-- Adiciona coluna webhook_url na tabela evolution_instances
ALTER TABLE evolution_instances
ADD COLUMN webhook_url VARCHAR(500);

-- Adiciona comentário na coluna
COMMENT ON COLUMN evolution_instances.webhook_url IS 'URL do webhook para receber eventos do WhatsApp';
