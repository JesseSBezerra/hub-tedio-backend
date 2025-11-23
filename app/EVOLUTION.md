# Evolution API - Documentação

## Visão Geral

Módulo para gerenciamento de instâncias Evolution API, permitindo cadastrar e gerenciar múltiplas instâncias com suas respectivas URLs e API Keys.

---

## Endpoints

### 1. Criar Evolution

```http
POST /api/evolution
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Evolution Produção",
  "descricao": "Instância principal de produção",
  "url": "https://api.evolution.com.br",
  "apiKey": "sua-api-key-aqui",
  "ownerId": 1
}
```

**Resposta (201 CREATED):**
```json
{
  "id": 1,
  "nome": "Evolution Produção",
  "descricao": "Instância principal de produção",
  "url": "https://api.evolution.com.br",
  "apiKey": "sua-api-key-aqui",
  "ownerId": 1,
  "ownerNome": "Empresa XYZ",
  "createdAt": "2024-11-21T16:00:00",
  "updatedAt": "2024-11-21T16:00:00"
}
```

---

### 2. Listar Todas as Evolutions

```http
GET /api/evolution
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Evolution Produção",
    "descricao": "Instância principal de produção",
    "url": "https://api.evolution.com.br",
    "apiKey": "sua-api-key-aqui",
    "ownerId": 1,
    "ownerNome": "Empresa XYZ",
    "createdAt": "2024-11-21T16:00:00",
    "updatedAt": "2024-11-21T16:00:00"
  },
  {
    "id": 2,
    "nome": "Evolution Homologação",
    "descricao": "Instância de testes",
    "url": "https://api-hml.evolution.com.br",
    "apiKey": "outra-api-key",
    "ownerId": 1,
    "ownerNome": "Empresa XYZ",
    "createdAt": "2024-11-21T16:05:00",
    "updatedAt": "2024-11-21T16:05:00"
  }
]
```

---

### 3. Filtrar Evolutions por Owner

```http
GET /api/evolution?ownerId={ownerId}
Authorization: Bearer {token}
```

**Exemplo:**
```http
GET /api/evolution?ownerId=1
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Evolution Produção",
    "descricao": "Instância principal de produção",
    "url": "https://api.evolution.com.br",
    "apiKey": "sua-api-key-aqui",
    "ownerId": 1,
    "ownerNome": "Empresa XYZ",
    "createdAt": "2024-11-21T16:00:00",
    "updatedAt": "2024-11-21T16:00:00"
  }
]
```

---

### 4. Buscar Evolution por ID

```http
GET /api/evolution/{id}
Authorization: Bearer {token}
```

**Exemplo:**
```http
GET /api/evolution/1
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "Evolution Produção",
  "descricao": "Instância principal de produção",
  "url": "https://api.evolution.com.br",
  "apiKey": "sua-api-key-aqui",
  "createdAt": "2024-11-21T16:00:00",
  "updatedAt": "2024-11-21T16:00:00"
}
```

---

### 5. Buscar Evolution por Nome

```http
GET /api/evolution/nome/{nome}
Authorization: Bearer {token}
```

**Exemplo:**
```http
GET /api/evolution/nome/Evolution Produção
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "Evolution Produção",
  "descricao": "Instância principal de produção",
  "url": "https://api.evolution.com.br",
  "apiKey": "sua-api-key-aqui",
  "createdAt": "2024-11-21T16:00:00",
  "updatedAt": "2024-11-21T16:00:00"
}
```

---

### 6. Atualizar Evolution

```http
PUT /api/evolution/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Evolution Produção V2",
  "descricao": "Instância atualizada",
  "url": "https://api-v2.evolution.com.br",
  "apiKey": "nova-api-key",
  "ownerId": 1
}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "Evolution Produção V2",
  "descricao": "Instância atualizada",
  "url": "https://api-v2.evolution.com.br",
  "apiKey": "nova-api-key",
  "createdAt": "2024-11-21T16:00:00",
  "updatedAt": "2024-11-21T16:30:00"
}
```

---

### 7. Deletar Evolution

```http
DELETE /api/evolution/{id}
Authorization: Bearer {token}
```

**Resposta (204 NO CONTENT)**

---

## Validações

### Campos Obrigatórios
- **nome**: 3-255 caracteres, único
- **url**: máximo 1000 caracteres
- **apiKey**: máximo 500 caracteres
- **ownerId**: ID do owner responsável

### Campos Opcionais
- **descricao**: máximo 500 caracteres

---

## Estrutura do Banco de Dados

### Tabela `evolutions`

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | BIGSERIAL | PRIMARY KEY |
| nome | VARCHAR(255) | NOT NULL, UNIQUE |
| descricao | VARCHAR(500) | NULL |
| url | VARCHAR(1000) | NOT NULL |
| api_key | VARCHAR(500) | NOT NULL |
| owner_id | BIGINT | NOT NULL, FK → owners(id) |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

**Índices:**
- `idx_evolution_nome` em `nome`
- `idx_evolution_owner_id` em `owner_id`

---

## Exemplos de Uso

### Exemplo 1: Cadastrar Múltiplas Instâncias

```bash
# Instância de Produção
curl -X POST http://localhost:8101/api/evolution \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Evolution Produção",
    "descricao": "Instância principal",
    "url": "https://api.evolution.com.br",
    "apiKey": "prod-api-key-123",
    "ownerId": 1
  }'

# Instância de Homologação
curl -X POST http://localhost:8101/api/evolution \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Evolution Homologação",
    "descricao": "Ambiente de testes",
    "url": "https://api-hml.evolution.com.br",
    "apiKey": "hml-api-key-456",
    "ownerId": 1
  }'
```

### Exemplo 2: Listar e Buscar

```bash
# Listar todas
curl -X GET http://localhost:8101/api/evolution \
  -H "Authorization: Bearer {token}"

# Buscar por ID
curl -X GET http://localhost:8101/api/evolution/1 \
  -H "Authorization: Bearer {token}"

# Buscar por nome
curl -X GET http://localhost:8101/api/evolution/nome/Evolution%20Produção \
  -H "Authorization: Bearer {token}"
```

### Exemplo 3: Atualizar API Key

```bash
curl -X PUT http://localhost:8101/api/evolution/1 \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Evolution Produção",
    "descricao": "Instância principal",
    "url": "https://api.evolution.com.br",
    "apiKey": "nova-api-key-789"
  }'
```

---

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Evolution criada com sucesso |
| 204 | No Content - Evolution deletada com sucesso |
| 400 | Bad Request - Erro de validação |
| 401 | Unauthorized - Token ausente ou inválido |
| 404 | Not Found - Evolution não encontrada |
| 500 | Internal Server Error - Erro interno |

---

## Respostas de Erro

### Erro de Validação (400)
```json
{
  "timestamp": "2024-11-21T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "path": "/api/evolution",
  "errors": [
    "nome: Nome é obrigatório",
    "url: URL é obrigatória"
  ]
}
```

### Evolution Já Existe (400)
```json
{
  "timestamp": "2024-11-21T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Evolution com nome 'Evolution Produção' já existe",
  "path": "/api/evolution"
}
```

### Evolution Não Encontrada (404)
```json
{
  "timestamp": "2024-11-21T16:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Evolution não encontrada com ID: 999",
  "path": "/api/evolution/999"
}
```

---

## Segurança

- **Autenticação**: Todos os endpoints requerem JWT token válido
- **API Key**: Armazenada em texto plano (considere criptografia em produção)
- **Logs**: Todas as operações são registradas com ID do usuário

---

## Migration

**Arquivo:** `V14__create_evolutions_table.sql`

A migration cria a tabela `evolutions` com todos os campos necessários e índices otimizados.

---

## Integração com Outros Módulos

Este módulo pode ser integrado com:
- **Integrations**: Usar Evolution como fonte de autenticação
- **API Manager**: Gerenciar chamadas para Evolution API
- **Requests**: Testar endpoints da Evolution

---

## Próximos Passos

1. Execute a aplicação para aplicar a migration
2. Faça login e obtenha o JWT token
3. Cadastre suas instâncias Evolution
4. Use as instâncias em outros módulos do sistema

---

## Suporte

Para mais informações, consulte:
- `README.md` - Documentação geral
- `API_MANAGER.md` - Gerenciamento de APIs
