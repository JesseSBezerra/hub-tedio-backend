# Guia de Migrations com Flyway

## O que é Flyway?

Flyway é uma ferramenta de versionamento e migração de banco de dados que permite gerenciar mudanças no schema de forma controlada e versionada.

## Configuração

### Dependências Maven

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

### Configuração no application.properties

```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true

# JPA Configuration - Importante!
spring.jpa.hibernate.ddl-auto=validate
```

**Nota:** O `ddl-auto=validate` garante que o Hibernate apenas valide o schema, sem criar ou alterar tabelas automaticamente.

## Estrutura de Diretórios

```
src/main/resources/
└── db/
    └── migration/
        ├── V1__create_users_table.sql
        ├── V2__add_user_status_column.sql
        └── V3__create_logs_table.sql
```

## Convenção de Nomenclatura

### Formato Padrão

```
V{versão}__{descrição}.sql
```

### Regras:

1. **Prefixo `V`**: Indica uma migration versionada
2. **Versão**: Número sequencial (1, 2, 3, etc.) ou formato semântico (1.0, 1.1, 2.0)
3. **Separador `__`**: Dois underscores separam a versão da descrição
4. **Descrição**: Nome descritivo em snake_case ou camelCase
5. **Extensão `.sql`**: Arquivo SQL

### Exemplos Válidos:

- `V1__create_users_table.sql`
- `V2__add_email_verification.sql`
- `V3__create_roles_table.sql`
- `V1.1__add_user_index.sql`
- `V2.0__refactor_authentication.sql`

### Exemplos Inválidos:

- ❌ `V1_create_users.sql` (apenas um underscore)
- ❌ `create_users_table.sql` (sem versão)
- ❌ `V1__create users table.sql` (espaços na descrição)

## Tipos de Migrations

### 1. Versioned Migrations (V)

Migrations versionadas que são executadas uma única vez em ordem sequencial.

```sql
-- V1__create_users_table.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
```

### 2. Repeatable Migrations (R)

Migrations que podem ser executadas múltiplas vezes (views, procedures, functions).

```sql
-- R__create_user_view.sql
CREATE OR REPLACE VIEW active_users AS
SELECT * FROM users WHERE status = 'ACTIVE';
```

### 3. Undo Migrations (U)

Migrations para reverter mudanças (requer Flyway Teams).

```sql
-- U1__create_users_table.sql
DROP TABLE users;
```

## Comandos Flyway

### Via Spring Boot

O Flyway é executado automaticamente na inicialização da aplicação quando configurado.

### Via Maven Plugin

Adicione ao `pom.xml`:

```xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>9.22.0</version>
    <configuration>
        <url>jdbc:postgresql://localhost:5432/tedioinfernal</url>
        <user>evolution</user>
        <password>Tor1t4ma2013</password>
    </configuration>
</plugin>
```

Comandos disponíveis:

```bash
# Executar migrations
mvn flyway:migrate

# Ver status das migrations
mvn flyway:info

# Validar migrations
mvn flyway:validate

# Limpar banco (CUIDADO!)
mvn flyway:clean

# Reparar histórico de migrations
mvn flyway:repair
```

## Boas Práticas

### 1. Nunca Modifique Migrations Aplicadas

❌ **Errado:**
```sql
-- V1__create_users_table.sql (já aplicada)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL -- Adicionado depois
);
```

✅ **Correto:**
```sql
-- V2__add_email_to_users.sql (nova migration)
ALTER TABLE users ADD COLUMN email VARCHAR(255) NOT NULL;
```

### 2. Use Transações

```sql
-- V3__update_user_data.sql
BEGIN;

UPDATE users SET status = 'ACTIVE' WHERE status IS NULL;
ALTER TABLE users ALTER COLUMN status SET NOT NULL;

COMMIT;
```

### 3. Seja Descritivo

❌ **Ruim:** `V2__update.sql`

✅ **Bom:** `V2__add_user_status_and_role_columns.sql`

### 4. Teste Migrations Localmente

Sempre teste suas migrations em ambiente de desenvolvimento antes de aplicar em produção.

### 5. Mantenha Migrations Pequenas

Prefira várias migrations pequenas a uma grande migration complexa.

### 6. Use IF EXISTS/IF NOT EXISTS

```sql
-- V4__add_index_safely.sql
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
```

### 7. Adicione Comentários

```sql
-- V5__add_user_preferences.sql
-- Adiciona tabela de preferências do usuário
-- Relacionamento 1:1 com users

CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    theme VARCHAR(50) DEFAULT 'light',
    language VARCHAR(10) DEFAULT 'pt-BR',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice para busca rápida por user_id
CREATE UNIQUE INDEX idx_user_preferences_user_id ON user_preferences(user_id);
```

## Rollback de Migrations

### Opção 1: Nova Migration de Reversão

```sql
-- V6__revert_user_preferences.sql
DROP TABLE IF EXISTS user_preferences CASCADE;
```

### Opção 2: Flyway Undo (Teams Edition)

```sql
-- U5__add_user_preferences.sql
DROP TABLE IF EXISTS user_preferences CASCADE;
```

## Troubleshooting

### Erro: "Migration checksum mismatch"

**Causa:** Migration foi modificada após ser aplicada.

**Solução:**
```bash
mvn flyway:repair
```

### Erro: "Found non-empty schema without metadata table"

**Causa:** Banco já tem tabelas mas sem histórico Flyway.

**Solução:** Use `baseline-on-migrate=true` no `application.properties`

### Erro: "Migration failed"

**Causa:** Erro SQL na migration.

**Solução:**
1. Corrija o SQL
2. Execute `mvn flyway:repair`
3. Execute novamente

## Tabela de Controle

Flyway cria uma tabela `flyway_schema_history` para controlar as migrations:

```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

Colunas importantes:
- `installed_rank`: Ordem de execução
- `version`: Versão da migration
- `description`: Descrição da migration
- `type`: Tipo (SQL, JDBC, etc.)
- `script`: Nome do arquivo
- `checksum`: Hash do conteúdo
- `installed_on`: Data/hora de execução
- `execution_time`: Tempo de execução em ms
- `success`: Status de sucesso

## Exemplo Completo

### V1__create_users_table.sql
```sql
-- Criação da tabela de usuários
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    client_id VARCHAR(255),
    client_secret VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);

-- Comentários
COMMENT ON TABLE users IS 'Tabela de usuários do sistema';
COMMENT ON COLUMN users.email IS 'Email único do usuário (usado para login)';
```

### V2__add_user_status.sql
```sql
-- Adiciona coluna de status ao usuário
ALTER TABLE users ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';

-- Cria índice para filtros por status
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);

-- Atualiza usuários existentes
UPDATE users SET status = 'ACTIVE' WHERE status IS NULL;

-- Adiciona constraint
ALTER TABLE users ALTER COLUMN status SET NOT NULL;
```

## Referências

- [Documentação Oficial Flyway](https://flywaydb.org/documentation/)
- [Flyway Migrations](https://flywaydb.org/documentation/concepts/migrations)
- [Spring Boot + Flyway](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
