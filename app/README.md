# TedioApp - Spring Boot Application

Aplicação Spring Boot com autenticação JWT, gerenciamento de usuários e integração com Evolution API.

## Tecnologias

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Spring Security
- Spring Boot Actuator
- PostgreSQL
- Flyway (Database Migrations)
- JWT (JSON Web Token)
- Lombok
- Maven

## Estrutura do Projeto

```
src/main/java/com/tedioinfernal/tedioapp/
├── config/              # Configurações (Security, CORS)
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects
├── entity/              # Entidades JPA
├── enums/               # Enumerações
├── exception/           # Tratamento de exceções
├── repository/          # Repositories JPA
├── security/            # Configurações de segurança JWT
└── service/             # Serviços de negócio

src/main/resources/
└── db/migration/        # Scripts SQL do Flyway
```

## Configuração

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente no arquivo `.env`:

```properties
DATABASE_HOST=191.252.195.25
DATABASE_PORT=5432
DATABASE_NAME=tedioinfernal
DATABASE_USER=evolution
DATABASE_PASSWORD=Tor1t4ma2013
JWT_SECRET_KEY=sua-chave-secreta-super-segura-mude-em-producao
```

## Módulos Disponíveis

- **Autenticação e Usuários** - Sistema de login e gerenciamento de usuários
- **Permissões** - Controle de acesso granular
- **API Manager** - Gerenciamento de autenticações de APIs externas
- **Evolution** - Gerenciamento de instâncias Evolution API
- **Evolution Instance** - Criação e gerenciamento de instâncias (integração automática)
- **Evolution Message** - Envio de mensagens de texto via Evolution API
- **Integrations** - Integrações com sistemas externos
- **Paths e Requests** - Documentação e testes de endpoints

Para documentação detalhada de cada módulo, consulte:
- `API_MANAGER.md` - API Manager
- `EVOLUTION.md` - Evolution API
- `EVOLUTION_INSTANCE.md` - Evolution Instance (Integração)
- `EVOLUTION_MESSAGE.md` - Evolution Message (Envio de mensagens)
- `FLYWAY.md` - Migrations

---

## Endpoints da API

### Autenticação

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer"
}
```

### Usuários

#### Criar Usuário Application
```http
POST /api/user
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta (201 CREATED):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "userType": "APPLICATION",
  "clientId": null,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Criar Usuário Integration
```http
POST /api/user/integration
Content-Type: application/json

{
  "nome": "Sistema Integração",
  "email": "integration@example.com",
  "password": "senha123",
  "clientId": "client-123",
  "clientSecret": "secret-456"
}
```

**Resposta (201 CREATED):**
```json
{
  "id": 2,
  "nome": "Sistema Integração",
  "email": "integration@example.com",
  "userType": "INTEGRATION",
  "clientId": "client-123",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Buscar Usuários
```http
GET /api/user
Authorization: Bearer {token}
```

Este endpoint aceita query parameters opcionais:

**Listar todos os usuários:**
```http
GET /api/user
```

**Buscar por ID:**
```http
GET /api/user?id=1
```

**Buscar por email:**
```http
GET /api/user?email=joao@example.com
```

**Resposta (200 OK) - Lista:**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "userType": "APPLICATION",
    "clientId": null,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

**Resposta (200 OK) - Usuário único (com id ou email):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "userType": "APPLICATION",
  "clientId": null,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Atualizar Usuário
```http
PUT /api/user/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@example.com",
  "password": "novaSenha123"
}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva Atualizado",
  "email": "joao.novo@example.com",
  "userType": "APPLICATION",
  "clientId": null,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T11:00:00"
}
```

#### Deletar Usuário
```http
DELETE /api/user/{id}
Authorization: Bearer {token}
```

**Resposta (204 NO CONTENT)**

### Permissões

#### Atribuir Permissões ao Usuário
```http
POST /api/permission
Authorization: Bearer {token}
Content-Type: application/json

{
  "permissions": ["user", "permission", "omnichannel", "omnichannel-manager", "api-manager"]
}
```

**Comportamento:**
- **Adiciona novas permissões** mantendo as existentes (não remove permissões já vinculadas)
- **Ignora permissões duplicadas** (não gera erro se a permissão já existe)
- **Remove todas as permissões** se enviar array vazio: `{"permissions": []}`

**Exemplos:**

*Adicionar primeira permissão:*
```json
{"permissions": ["user"]}
// Resultado: ["user"]
```

*Adicionar mais permissões (mantém as existentes):*
```json
{"permissions": ["permission", "omnichannel"]}
// Resultado: ["user", "permission", "omnichannel"]
```

*Tentar adicionar permissão existente (não dá erro):*
```json
{"permissions": ["user"]}
// Resultado: ["user", "permission", "omnichannel"] (sem alteração)
```

*Remover todas as permissões:*
```json
{"permissions": []}
// Resultado: []
```

**Resposta (200 OK):**
```json
{
  "userId": 1,
  "userName": "João Silva",
  "userEmail": "joao@example.com",
  "permissions": ["user", "permission", "omnichannel", "omnichannel-manager", "api-manager"]
}
```

#### Buscar Permissões do Usuário
```http
GET /api/permission
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
{
  "userId": 1,
  "userName": "João Silva",
  "userEmail": "joao@example.com",
  "permissions": ["user", "omnichannel"]
}
```

#### Listar Permissões Disponíveis
```http
GET /api/permission/available
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "user",
    "description": "Permissão para gerenciar usuários",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  {
    "id": 2,
    "name": "permission",
    "description": "Permissão para gerenciar permissões",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  {
    "id": 3,
    "name": "omnichannel",
    "description": "Permissão para acessar omnichannel",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  {
    "id": 4,
    "name": "api-manager",
    "description": "Permissão para gerenciar APIs",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  {
    "id": 5,
    "name": "omnichannel-manager",
    "description": "Permissão para gerenciar omnichannel",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

## Tipos de Usuário

### APPLICATION
- Usuário padrão da aplicação
- Campos obrigatórios: nome, email, password

### INTEGRATION
- Usuário para integrações externas
- Campos obrigatórios: nome, email, password, clientId, clientSecret
- ClientSecret é criptografado no banco de dados

## Segurança

- Senhas são criptografadas usando BCrypt
- Autenticação via JWT (JSON Web Token)
- Token expira em 1 hora (3600000ms)
- Endpoints públicos (sem autenticação):
  - `POST /api/auth/login` - Login
  - `POST /api/user` - Criar usuário application
  - `POST /api/user/integration` - Criar usuário integration
  - `GET /actuator/health` - Health check
  - `GET /actuator/info` - Informações da aplicação
- Endpoints protegidos (requerem JWT):
  - `GET /api/user` - Listar usuários
  - `GET /api/user/{id}` - Buscar usuário por ID
  - `PUT /api/user/{id}` - Atualizar usuário
  - `DELETE /api/user/{id}` - Deletar usuário
  - Todos os endpoints do Actuator exceto health e info

## Como Executar

### Pré-requisitos
- Java 17
- Maven 3.6+
- PostgreSQL

### Passos

1. Clone o repositório
2. Configure as variáveis de ambiente
3. Execute o comando:

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8101`

## Tratamento de Erros

A API retorna respostas de erro padronizadas:

```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem de erro detalhada",
  "path": "/api/user",
  "errors": ["campo: mensagem de validação"]
}
```

### Códigos de Status HTTP

- **200 OK**: Requisição bem-sucedida
- **201 CREATED**: Recurso criado com sucesso
- **204 NO CONTENT**: Recurso deletado com sucesso
- **400 BAD REQUEST**: Erro de validação ou requisição inválida
- **401 UNAUTHORIZED**: Credenciais inválidas ou token ausente/inválido
- **500 INTERNAL SERVER ERROR**: Erro interno do servidor

## Banco de Dados

### Migrations com Flyway

A aplicação utiliza **Flyway** para gerenciar as migrations do banco de dados. As migrations são executadas automaticamente na inicialização da aplicação.

#### Estrutura da Tabela Users

A migration `V1__create_users_table.sql` cria a tabela `users` com a seguinte estrutura:

```sql
CREATE TABLE users (
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

-- Índices criados:
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);
```

#### Configuração do Flyway

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true
```

#### Criando Novas Migrations

Para criar uma nova migration, adicione um arquivo SQL em `src/main/resources/db/migration/` seguindo o padrão:

```
V{versão}__{descrição}.sql
```

Exemplo: `V2__add_user_status_column.sql`

**Importante:** 
- O Hibernate está configurado com `ddl-auto=validate`, ou seja, apenas valida o schema
- Todas as alterações no banco devem ser feitas via migrations Flyway
- As migrations são versionadas e executadas em ordem

## Spring Boot Actuator

O Spring Boot Actuator fornece endpoints para monitoramento e gerenciamento da aplicação.

### Endpoints Disponíveis

#### Públicos (sem autenticação)

**Health Check:**
```http
GET /actuator/health
```

Resposta:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

**Application Info:**
```http
GET /actuator/info
```

Resposta:
```json
{
  "app": {
    "name": "TedioApp",
    "description": "Spring Boot Application with JWT Authentication",
    "version": "1.0.0",
    "encoding": "UTF-8",
    "java": {
      "version": "17"
    }
  }
}
```

#### Protegidos (requerem autenticação JWT)

**Métricas da Aplicação:**
```http
GET /actuator/metrics
Authorization: Bearer {token}
```

**Métricas Específicas:**
```http
GET /actuator/metrics/jvm.memory.used
GET /actuator/metrics/http.server.requests
GET /actuator/metrics/system.cpu.usage
Authorization: Bearer {token}
```

**Variáveis de Ambiente:**
```http
GET /actuator/env
Authorization: Bearer {token}
```

**Status do Flyway:**
```http
GET /actuator/flyway
Authorization: Bearer {token}
```

**Loggers:**
```http
GET /actuator/loggers
GET /actuator/loggers/com.tedioinfernal
Authorization: Bearer {token}
```

**Thread Dump:**
```http
GET /actuator/threaddump
Authorization: Bearer {token}
```

**Heap Dump:**
```http
GET /actuator/heapdump
Authorization: Bearer {token}
```

### Configuração

```properties
# Endpoints expostos
management.endpoints.web.exposure.include=health,info,metrics,env,flyway,loggers,threaddump,heapdump

# Detalhes de health apenas para usuários autenticados
management.endpoint.health.show-details=when-authorized

# Métricas habilitadas
management.metrics.enable.jvm=true
management.metrics.enable.process=true
management.metrics.enable.system=true
```

### Segurança dos Endpoints

- `/actuator/health` e `/actuator/info`: **Públicos**
- Demais endpoints: **Requerem autenticação JWT**

## Logs

A aplicação gera logs detalhados para:
- Operações de criação, atualização e deleção de usuários
- Tentativas de login
- Erros de validação e autenticação
- Erros internos do servidor
