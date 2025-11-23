-- Adiciona coluna owner_id à tabela evolutions
ALTER TABLE evolutions
ADD COLUMN owner_id BIGINT NOT NULL;

-- Adiciona foreign key para owners
ALTER TABLE evolutions
ADD CONSTRAINT fk_evolution_owner 
FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE;

-- Adiciona índice
CREATE INDEX idx_evolution_owner_id ON evolutions(owner_id);

-- Comentário
COMMENT ON COLUMN evolutions.owner_id IS 'ID do owner responsável pela evolution';
