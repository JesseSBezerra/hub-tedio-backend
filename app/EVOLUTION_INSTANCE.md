# Evolution Instance - Documentação

## Visão Geral

Módulo para gerenciamento de instâncias Evolution API. Ao criar uma instância, o sistema automaticamente integra com a API Evolution configurada, cria a instância remotamente e persiste os dados localmente apenas em caso de sucesso.

---

## Fluxo de Criação

```
1. Request → Controller
2. Service valida dados
3. Service busca Evolution configurada
4. Service chama Evolution API (integração)
5. Evolution API cria instância
6. Service persiste APENAS se sucesso
7. Response com dados + QR Code (se solicitado)
```

---

## Endpoints

### 1. Criar Instância Evolution

**Integra automaticamente com Evolution API**

```http
POST /api/evolution-instance
Authorization: Bearer {token}
Content-Type: application/json

{
  "instanceName": "empresa-fracisco-sa",
  "qrcode": true,
  "evolutionId": 1,
  "integration": "WHATSAPP-BAILEYS"
}
```

**Resposta (201 CREATED):**
```json
{
  "id": 1,
  "instanceName": "empresa-fracisco-sa",
  "instanceId": "6462161d-4ff9-427a-92f1-0557fadabc50",
  "qrcode": true,
  "qrcodeBase64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
  "integration": "WHATSAPP-BAILEYS",
  "status": "connecting",
  "hash": "FFA41186-7D37-484F-8D89-4E88CA0B60D4",
  "evolutionId": 1,
  "evolutionNome": "Evolution Produção",
  "userId": 1,
  "userName": "João Silva",
  "createdAt": "2024-11-21T17:45:00",
  "updatedAt": "2024-11-21T17:45:00"
}
```

**Observações:**
- `qrcodeBase64` só é retornado se `qrcode=true`
- `instanceId` é gerado pela Evolution API
- Persistência só ocorre após sucesso na API Evolution

---

### 2. Listar Todas as Instâncias

```http
GET /api/evolution-instance
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "instanceName": "empresa-fracisco-sa",
    "instanceId": "6462161d-4ff9-427a-92f1-0557fadabc50",
    "qrcode": true,
    "qrcodeBase64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
    "integration": "WHATSAPP-BAILEYS",
    "status": "open",
    "hash": "FFA41186-7D37-484F-8D89-4E88CA0B60D4",
    "evolutionId": 1,
    "evolutionNome": "Evolution Produção",
    "userId": 1,
    "userName": "João Silva",
    "createdAt": "2024-11-21T17:45:00",
    "updatedAt": "2024-11-21T17:50:00"
  }
]
```

---

### 3. Filtrar Instâncias por Evolution

```http
GET /api/evolution-instance?evolutionId=1
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "instanceName": "empresa-fracisco-sa",
    "instanceId": "6462161d-4ff9-427a-92f1-0557fadabc50",
    "qrcode": true,
    "qrcodeBase64": null,
    "integration": "WHATSAPP-BAILEYS",
    "status": "open",
    "hash": "FFA41186-7D37-484F-8D89-4E88CA0B60D4",
    "evolutionId": 1,
    "evolutionNome": "Evolution Produção",
    "userId": 1,
    "userName": "João Silva",
    "createdAt": "2024-11-21T17:45:00",
    "updatedAt": "2024-11-21T17:50:00"
  }
]
```

---

### 4. Filtrar Instâncias por Usuário

```http
GET /api/evolution-instance?userId=1
Authorization: Bearer {token}
```

---

### 5. Filtrar Instâncias por Status

```http
GET /api/evolution-instance?status=open
Authorization: Bearer {token}
```

**Status possíveis:**
- `connecting` - Conectando
- `open` - Conectado
- `close` - Desconectado

---

### 6. Buscar Instância por ID

```http
GET /api/evolution-instance/1
Authorization: Bearer {token}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "instanceName": "empresa-fracisco-sa",
  "instanceId": "6462161d-4ff9-427a-92f1-0557fadabc50",
  "qrcode": true,
  "qrcodeBase64": null,
  "integration": "WHATSAPP-BAILEYS",
  "status": "open",
  "hash": "FFA41186-7D37-484F-8D89-4E88CA0B60D4",
  "evolutionId": 1,
  "evolutionNome": "Evolution Produção",
  "userId": 1,
  "userName": "João Silva",
  "createdAt": "2024-11-21T17:45:00",
  "updatedAt": "2024-11-21T17:50:00"
}
```

---

### 7. Buscar Instância por Nome

```http
GET /api/evolution-instance/name/empresa-fracisco-sa
Authorization: Bearer {token}
```

---

### 8. Deletar Instância

```http
DELETE /api/evolution-instance/1
Authorization: Bearer {token}
```

**Resposta (204 NO CONTENT)**

**Observação:** Deleta apenas do banco local, não remove da Evolution API

---

## Validações

### Campos Obrigatórios
- **instanceName**: 3-255 caracteres, único
- **qrcode**: boolean (true/false)
- **evolutionId**: ID da Evolution configurada
- **integration**: tipo de integração (ex: WHATSAPP-BAILEYS)

### Regras de Negócio
- Nome da instância não pode duplicar
- Evolution deve existir e estar configurada
- Persistência só ocorre após sucesso na API Evolution
- QR Code base64 só retorna se `qrcode=true`

---

## Estrutura do Banco de Dados

### Tabela `evolution_instances`

| Campo | Tipo | Restrições |
|-------|------|------------|
| id | BIGSERIAL | PRIMARY KEY |
| instance_name | VARCHAR(255) | NOT NULL |
| instance_id | VARCHAR(255) | NULL (preenchido após API) |
| qrcode | BOOLEAN | NOT NULL |
| qrcode_base64 | TEXT | NULL |
| integration | VARCHAR(100) | NOT NULL |
| status | VARCHAR(50) | NULL |
| hash | VARCHAR(255) | NULL |
| evolution_id | BIGINT | NOT NULL, FK → evolutions(id) |
| user_id | BIGINT | NOT NULL, FK → users(id) |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

**Índices:**
- `idx_evolution_instance_name` em `instance_name`
- `idx_evolution_instance_evolution_id` em `evolution_id`
- `idx_evolution_instance_user_id` em `user_id`
- `idx_evolution_instance_status` em `status`

---

## Integração com Evolution API

### Endpoint Chamado
```
POST {evolution.url}/instance/create
Headers:
  Content-Type: application/json
  apikey: {evolution.apiKey}
Body:
  {
    "instanceName": "empresa-fracisco-sa",
    "qrcode": true,
    "integration": "WHATSAPP-BAILEYS"
  }
```

### Response da Evolution API
```json
{
  "instance": {
    "instanceName": "empresa-fracisco-sa",
    "instanceId": "6462161d-4ff9-427a-92f1-0557fadabc50",
    "integration": "WHATSAPP-BAILEYS",
    "status": "connecting"
  },
  "hash": "FFA41186-7D37-484F-8D89-4E88CA0B60D4",
  "qrcode": {
    "code": "2@5/7qvJhAw0...",
    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."
  }
}
```

---

## Exemplos de Uso

### Exemplo 1: Criar Instância com QR Code

```bash
curl -X POST http://localhost:8101/api/evolution-instance \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "instanceName": "empresa-fracisco-sa",
    "qrcode": true,
    "evolutionId": 1,
    "integration": "WHATSAPP-BAILEYS"
  }'
```

### Exemplo 2: Criar Instância sem QR Code

```bash
curl -X POST http://localhost:8101/api/evolution-instance \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "instanceName": "empresa-teste",
    "qrcode": false,
    "evolutionId": 1,
    "integration": "WHATSAPP-BAILEYS"
  }'
```

### Exemplo 3: Listar Instâncias de uma Evolution

```bash
curl -X GET "http://localhost:8101/api/evolution-instance?evolutionId=1" \
  -H "Authorization: Bearer {token}"
```

### Exemplo 4: Listar Instâncias por Status

```bash
curl -X GET "http://localhost:8101/api/evolution-instance?status=open" \
  -H "Authorization: Bearer {token}"
```

---

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 201 | Created - Instância criada com sucesso |
| 200 | OK - Requisição bem-sucedida |
| 204 | No Content - Instância deletada com sucesso |
| 400 | Bad Request - Erro de validação |
| 401 | Unauthorized - Token ausente ou inválido |
| 404 | Not Found - Instância não encontrada |
| 500 | Internal Server Error - Erro na integração com Evolution API |

---

## Respostas de Erro

### Erro de Validação (400)
```json
{
  "timestamp": "2024-11-21T17:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "path": "/api/evolution-instance",
  "errors": [
    "instanceName: Nome da instância é obrigatório",
    "evolutionId: Evolution ID é obrigatório"
  ]
}
```

### Nome Duplicado (400)
```json
{
  "timestamp": "2024-11-21T17:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Já existe uma instância com o nome: empresa-fracisco-sa",
  "path": "/api/evolution-instance"
}
```

### Evolution Não Encontrada (404)
```json
{
  "timestamp": "2024-11-21T17:45:00",
  "status": 404,
  "error": "Not Found",
  "message": "Evolution não encontrada com ID: 999",
  "path": "/api/evolution-instance"
}
```

### Erro na API Evolution (500)
```json
{
  "timestamp": "2024-11-21T17:45:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Falha ao criar instância Evolution: Connection timeout",
  "path": "/api/evolution-instance"
}
```

---

## Segurança

- **Autenticação**: Todos os endpoints requerem JWT token válido
- **Rastreabilidade**: Cada instância vinculada ao usuário criador
- **Logs**: Todas as operações registradas com ID do usuário
- **API Key**: Armazenada na Evolution, não exposta na response

---

## Migration

**Arquivo:** `V16__create_evolution_instances_table.sql`

A migration cria a tabela `evolution_instances` com todos os campos necessários, foreign keys e índices otimizados.

---

## Arquitetura

### Camadas
```
Controller (EvolutionInstanceController)
    ↓
Service (EvolutionInstanceService)
    ↓
Integration Service (EvolutionApiIntegrationService)
    ↓
Client (EvolutionInstanceClient - WebClient)
    ↓
Evolution API Externa
```

### Persistência Condicional
```
1. Valida dados localmente
2. Chama Evolution API
3. SE sucesso:
   - Persiste no banco
   - Retorna response com dados
4. SE erro:
   - NÃO persiste
   - Lança exceção com mensagem
```

---

## Próximos Passos

1. Execute a aplicação para aplicar a migration
2. Cadastre uma Evolution (módulo Evolution)
3. Crie instâncias usando a Evolution cadastrada
4. Monitore os logs para acompanhar a integração
5. Use o QR Code retornado para conectar o WhatsApp

---

## Suporte

Para mais informações, consulte:
- `README.md` - Documentação geral
- `EVOLUTION.md` - Módulo Evolution
- `API_MANAGER.md` - Gerenciamento de APIs
