# API Manager - Documentação

## Visão Geral

Sistema de gerenciamento de autenticações de APIs externas, permitindo cadastrar, testar e gerenciar diferentes tipos de autenticação.

## Interceptor de Usuário

### UserContext
O sistema agora injeta automaticamente o usuário autenticado em todas as requisições protegidas através do `UserInjectionInterceptor`.

**Uso nos Controllers:**
```java
User currentUser = UserContext.getCurrentUser();
Long userId = currentUser.getId();
String email = currentUser.getEmail();
```

**Rotas excluídas do interceptor:**
- `/api/auth/**`
- `/api/user` (POST)
- `/api/user/integration` (POST)

---

## Endpoints

### 1. Owners (Proprietários)

#### Criar Owner
```http
POST /api/owner
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Empresa XYZ",
  "descricao": "Descrição da empresa"
}
```

**Resposta (201 CREATED):**
```json
{
  "id": 1,
  "nome": "Empresa XYZ",
  "descricao": "Descrição da empresa",
  "createdAt": "2024-11-20T12:00:00",
  "updatedAt": "2024-11-20T12:00:00"
}
```

#### Listar Owners
```http
GET /api/owner
Authorization: Bearer {token}
```

#### Buscar Owner por ID
```http
GET /api/owner/{id}
Authorization: Bearer {token}
```

#### Atualizar Owner
```http
PUT /api/owner/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Empresa XYZ Atualizada",
  "descricao": "Nova descrição"
}
```

#### Deletar Owner
```http
DELETE /api/owner/{id}
Authorization: Bearer {token}
```

---

### 2. API Authentications (Autenticações de API)

#### Criar Autenticação de API
```http
POST /api/register/authentication
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Auth Sistema Pagamento",
  "descricao": "Autenticação para sistema de pagamento",
  "ownerId": 1,
  "url": "https://api.exemplo.com/auth/login",
  "authenticationType": "BASIC",
  "contentType": "APPLICATION_JSON",
  "requestBody": {
    "username": "usuario_api",
    "password": "senha_api",
    "grant_type": "client_credentials"
  },
  "headers": {
    "X-API-Key": "abc123",
    "X-Client-ID": "client_001"
  }
}
```

**Resposta (201 CREATED):**
```json
{
  "id": 1,
  "nome": "Auth Sistema Pagamento",
  "descricao": "Autenticação para sistema de pagamento",
  "ownerId": 1,
  "ownerNome": "Empresa XYZ",
  "url": "https://api.exemplo.com/auth/login",
  "authenticationType": "BASIC",
  "contentType": "APPLICATION_JSON",
  "requestBody": {
    "username": "usuario_api",
    "password": "senha_api",
    "grant_type": "client_credentials"
  },
  "headers": {
    "X-API-Key": "abc123",
    "X-Client-ID": "client_001"
  },
  "createdAt": "2024-11-20T12:00:00",
  "updatedAt": "2024-11-20T12:00:00"
}
```

#### Listar Autenticações
```http
GET /api/register/authentication
Authorization: Bearer {token}
```

**Filtrar por Owner:**
```http
GET /api/register/authentication?ownerId=1
Authorization: Bearer {token}
```

#### Buscar Autenticação por ID
```http
GET /api/register/authentication/{id}
Authorization: Bearer {token}
```

#### Atualizar Autenticação
```http
PUT /api/register/authentication/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Auth Sistema Pagamento",
  "descricao": "Descrição atualizada",
  "ownerId": 1,
  "url": "https://api.exemplo.com/auth/v2/login",
  "authenticationType": "OAUTH2",
  "contentType": "APPLICATION_FORM_URLENCODED",
  "requestBody": {
    "client_id": "novo_client",
    "client_secret": "novo_secret"
  },
  "headers": {
    "Authorization": "Basic xyz789"
  }
}
```

#### Deletar Autenticação
```http
DELETE /api/register/authentication/{id}
Authorization: Bearer {token}
```

#### Testar Autenticação
```http
POST /api/register/authentication/test/{nome}
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "statusCode": 200,
  "statusMessage": "OK",
  "responseHeaders": {
    "content-type": "application/json",
    "content-length": "256"
  },
  "responseBody": "{\"access_token\":\"eyJhbGc...\",\"token_type\":\"Bearer\"}",
  "errorMessage": null,
  "responseTimeMs": 342
}
```

**Resposta em caso de erro:**
```json
{
  "success": false,
  "statusCode": 0,
  "statusMessage": null,
  "responseHeaders": null,
  "responseBody": null,
  "errorMessage": "Connection refused",
  "responseTimeMs": 150
}
```

---

## Enums

### AuthenticationType
- `OAUTH2` - Autenticação OAuth 2.0
- `BASIC` - Autenticação Basic (username:password)
- `BEARER` - Token Bearer
- `API_KEY` - API Key
- `NONE` - Sem autenticação

### ContentType
- `APPLICATION_JSON` - application/json
- `APPLICATION_XML` - application/xml
- `APPLICATION_FORM_URLENCODED` - application/x-www-form-urlencoded
- `MULTIPART_FORM_DATA` - multipart/form-data
- `TEXT_PLAIN` - text/plain

---

## Estrutura de Dados

### Request Body e Headers
Ambos os campos `requestBody` e `headers` aceitam objetos JSON no formato chave-valor:

```json
{
  "requestBody": {
    "chave1": "valor1",
    "chave2": "valor2",
    "chave3": 123,
    "chave4": true
  },
  "headers": {
    "Authorization": "Bearer token",
    "X-Custom-Header": "valor"
  }
}
```

---

## Banco de Dados

### Tabela `owners`
- `id` - BIGSERIAL PRIMARY KEY
- `nome` - VARCHAR(255) NOT NULL
- `descricao` - VARCHAR(500)
- `created_at` - TIMESTAMP
- `updated_at` - TIMESTAMP

### Tabela `api_authentications`
- `id` - BIGSERIAL PRIMARY KEY
- `nome` - VARCHAR(255) NOT NULL UNIQUE
- `descricao` - VARCHAR(500)
- `owner_id` - BIGINT NOT NULL (FK → owners)
- `url` - VARCHAR(1000) NOT NULL
- `authentication_type` - VARCHAR(50) NOT NULL
- `content_type` - VARCHAR(100) NOT NULL
- `request_body` - JSONB
- `headers` - JSONB
- `created_at` - TIMESTAMP
- `updated_at` - TIMESTAMP

---

## Exemplos de Uso

### Exemplo 1: Autenticação OAuth2
```json
{
  "nome": "OAuth2 Google",
  "ownerId": 1,
  "url": "https://oauth2.googleapis.com/token",
  "authenticationType": "OAUTH2",
  "contentType": "APPLICATION_FORM_URLENCODED",
  "requestBody": {
    "grant_type": "client_credentials",
    "client_id": "seu_client_id",
    "client_secret": "seu_client_secret",
    "scope": "https://www.googleapis.com/auth/drive"
  }
}
```

### Exemplo 2: Autenticação Basic
```json
{
  "nome": "Basic Auth API",
  "ownerId": 1,
  "url": "https://api.exemplo.com/login",
  "authenticationType": "BASIC",
  "contentType": "APPLICATION_JSON",
  "requestBody": {
    "username": "admin",
    "password": "senha123"
  },
  "headers": {
    "Authorization": "Basic YWRtaW46c2VuaGExMjM="
  }
}
```

### Exemplo 3: API Key
```json
{
  "nome": "API Key Auth",
  "ownerId": 1,
  "url": "https://api.exemplo.com/data",
  "authenticationType": "API_KEY",
  "contentType": "APPLICATION_JSON",
  "headers": {
    "X-API-Key": "sua_api_key_aqui",
    "X-API-Secret": "seu_secret_aqui"
  }
}
```

---

## Migrations

- **V4__create_owners_table.sql** - Cria tabela de owners
- **V5__create_api_authentications_table.sql** - Cria tabela de autenticações de API

Execute `mvn spring-boot:run` para aplicar as migrations automaticamente.
