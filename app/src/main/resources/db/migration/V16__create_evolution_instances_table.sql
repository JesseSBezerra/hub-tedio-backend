-- Criação da tabela de evolution_instances
CREATE TABLE evolution_instances (
    id BIGSERIAL PRIMARY KEY,
    instance_name VARCHAR(255) NOT NULL,
    instance_id VARCHAR(255),
    qrcode BOOLEAN NOT NULL DEFAULT false,
    qrcode_base64 TEXT,
    integration VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    hash VARCHAR(255),
    evolution_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_evolution_instance_evolution FOREIGN KEY (evolution_id) REFERENCES evolutions(id) ON DELETE CASCADE,
    CONSTRAINT fk_evolution_instance_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_evolution_instance_name ON evolution_instances(instance_name);
CREATE INDEX idx_evolution_instance_evolution_id ON evolution_instances(evolution_id);
CREATE INDEX idx_evolution_instance_user_id ON evolution_instances(user_id);
CREATE INDEX idx_evolution_instance_status ON evolution_instances(status);

-- Comentários
COMMENT ON TABLE evolution_instances IS 'Tabela de instâncias Evolution criadas';
COMMENT ON COLUMN evolution_instances.id IS 'Identificador único da instância';
COMMENT ON COLUMN evolution_instances.instance_name IS 'Nome da instância no Evolution';
COMMENT ON COLUMN evolution_instances.instance_id IS 'ID da instância retornado pelo Evolution';
COMMENT ON COLUMN evolution_instances.qrcode IS 'Se deve gerar QR Code';
COMMENT ON COLUMN evolution_instances.qrcode_base64 IS 'QR Code em base64';
COMMENT ON COLUMN evolution_instances.integration IS 'Tipo de integração (WHATSAPP-BAILEYS, etc)';
COMMENT ON COLUMN evolution_instances.status IS 'Status da instância (connecting, open, close)';
COMMENT ON COLUMN evolution_instances.hash IS 'Hash retornado pelo Evolution';
COMMENT ON COLUMN evolution_instances.evolution_id IS 'ID da Evolution utilizada';
COMMENT ON COLUMN evolution_instances.user_id IS 'ID do usuário que criou';
