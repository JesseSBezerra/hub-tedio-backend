# Evolution Message - Documentação

## Visão Geral

Módulo para envio de mensagens de texto via Evolution API. Utiliza a integração com Evolution Instance para enviar mensagens de forma simplificada através de um único endpoint.

---

## Fluxo de Envio

```
1. Request → Controller
2. Service busca EvolutionInstance
3. Service prepara request para integração
4. EvolutionTextMessageService envia via Evolution API
5. Response simplificado retornado
```

---

## Endpoint

### Enviar Mensagem de Texto

**Envia mensagem via Evolution API**

```http
POST /api/evolution/message
Authorization: Bearer {token}
Content-Type: application/json

{
  "number": "5581996349077",
  "message": "teste de envio",
  "evolutionInstanceId": 1
}
```

**Resposta (200 OK):**
```json
{
  "number": "5581996349077",
  "message": "teste de envio",
  "status": "PENDING"
}
```

---

## Campos do Request

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| number | String | Sim | Número do destinatário (com DDI e DDD) |
| message | String | Sim | Texto da mensagem (até 4096 caracteres) |
| evolutionInstanceId | Long | Sim | ID da instância Evolution configurada |

---

## Campos do Response

| Campo | Tipo | Descrição |
|-------|------|-----------|
| number | String | Número do destinatário |
| message | String | Texto da mensagem enviada |
| status | String | Status do envio (PENDING, SENT, etc) |

---

## Status Possíveis

| Status | Descrição |
|--------|-----------|
| PENDING | Mensagem pendente de envio |
| SENT | Mensagem enviada com sucesso |
| DELIVERED | Mensagem entregue ao destinatário |
| READ | Mensagem lida pelo destinatário |
| FAILED | Falha no envio da mensagem |

---

## Validações

### Campos Obrigatórios
- **number**: Não pode ser vazio
- **message**: Não pode ser vazio
- **evolutionInstanceId**: Não pode ser nulo

### Regras de Negócio
- EvolutionInstance deve existir
- EvolutionInstance deve ter Evolution vinculado
- Evolution deve ter URL e API Key configurados
- Número deve estar no formato internacional (DDI + DDD + número)

---

## Exemplos de Uso

### Exemplo 1: Mensagem Simples

```bash
curl -X POST http://localhost:8101/api/evolution/message \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "number": "5581996349077",
    "message": "teste de envio",
    "evolutionInstanceId": 1
  }'
```

**Response:**
```json
{
  "number": "5581996349077",
  "message": "teste de envio",
  "status": "PENDING"
}
```

---

### Exemplo 2: Mensagem Longa

```bash
curl -X POST http://localhost:8101/api/evolution/message \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "number": "5581996349077",
    "message": "Olá! Esta é uma mensagem de teste mais longa para verificar o envio de textos maiores. O sistema Evolution API suporta mensagens de até 4096 caracteres.",
    "evolutionInstanceId": 1
  }'
```

---

### Exemplo 3: Múltiplos Destinatários

Para enviar para múltiplos destinatários, faça múltiplas requisições:

```bash
# Destinatário 1
curl -X POST http://localhost:8101/api/evolution/message \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "number": "5581996349077",
    "message": "Mensagem para destinatário 1",
    "evolutionInstanceId": 1
  }'

# Destinatário 2
curl -X POST http://localhost:8101/api/evolution/message \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "number": "5511999887766",
    "message": "Mensagem para destinatário 2",
    "evolutionInstanceId": 1
  }'
```

---

## Integração Completa

### Camadas
```
EvolutionMessageController
    ↓
EvolutionMessageService
    ↓ (busca EvolutionInstance)
EvolutionInstanceRepository
    ↓
EvolutionTextMessageService
    ↓
EvolutionTextMessageClient (WebClient)
    ↓
Evolution API Externa
```

### Fluxo Detalhado

1. **Controller** recebe request com `number`, `message` e `evolutionInstanceId`
2. **Service** busca `EvolutionInstance` no banco de dados
3. **Service** valida se instância existe e está configurada
4. **Service** prepara `SendTextRequestDTO` para integração
5. **EvolutionTextMessageService** recebe instância e request
6. **EvolutionTextMessageClient** faz POST para Evolution API:
   - URL: `{evolution.url}/message/sendText/{instanceName}`
   - Header: `apikey: {evolution.apiKey}`
   - Body: `{ number, text }`
7. **Evolution API** processa e envia mensagem via WhatsApp
8. **Service** retorna response simplificado com status

---

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Mensagem enviada com sucesso |
| 400 | Bad Request - Erro de validação |
| 401 | Unauthorized - Token ausente ou inválido |
| 404 | Not Found - EvolutionInstance não encontrada |
| 500 | Internal Server Error - Erro na integração com Evolution API |

---

## Respostas de Erro

### Erro de Validação (400)
```json
{
  "timestamp": "2024-11-21T18:16:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "path": "/api/evolution/message",
  "errors": [
    "number: Número é obrigatório",
    "message: Mensagem é obrigatória"
  ]
}
```

### EvolutionInstance Não Encontrada (404)
```json
{
  "timestamp": "2024-11-21T18:16:00",
  "status": 404,
  "error": "Not Found",
  "message": "Evolution Instance não encontrada com ID: 999",
  "path": "/api/evolution/message"
}
```

### Erro na API Evolution (500)
```json
{
  "timestamp": "2024-11-21T18:16:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Falha ao enviar mensagem: Connection timeout",
  "path": "/api/evolution/message"
}
```

---

## Formato do Número

O número deve estar no formato internacional:

- **DDI**: Código do país (55 para Brasil)
- **DDD**: Código da área (81 para Recife, 11 para São Paulo, etc)
- **Número**: Número do telefone (8 ou 9 dígitos)

**Exemplos válidos:**
- `5581996349077` (Recife - 9 dígitos)
- `5511999887766` (São Paulo - 9 dígitos)
- `558132211234` (Recife - 8 dígitos fixo)

**Formato incorreto:**
- `81996349077` (falta DDI)
- `996349077` (falta DDI e DDD)
- `+5581996349077` (não usar símbolo +)

---

## Segurança

- **Autenticação**: Todos os endpoints requerem JWT token válido
- **Rastreabilidade**: Logs registram ID do usuário em todas operações
- **API Key**: Armazenada na Evolution, não exposta na response
- **Validações**: Campos obrigatórios e formato de número

---

## Limitações

- **Tamanho da mensagem**: Máximo 4096 caracteres
- **Tipo de mensagem**: Apenas texto simples (sem formatação)
- **Destinatário**: Um por requisição
- **Timeout**: 30 segundos para resposta da Evolution API

---

## Fluxo Completo de Uso

### 1. Pré-requisitos

Antes de enviar mensagens, você precisa:

**a) Ter um usuário autenticado:**
```bash
POST /api/auth/login
{
  "email": "seu@email.com",
  "password": "senha123"
}
```

**b) Cadastrar uma Evolution:**
```bash
POST /api/evolution
{
  "nome": "Evolution Produção",
  "descricao": "Instância principal",
  "url": "http://191.252.195.25:9010",
  "apiKey": "6a1b023f-4e5c-48d7-b9a1-f3c5b8d2e4f0",
  "ownerId": 1
}
```

**c) Criar uma EvolutionInstance:**
```bash
POST /api/evolution-instance
{
  "instanceName": "empresa-fracisco-sa",
  "qrcode": true,
  "evolutionId": 1,
  "integration": "WHATSAPP-BAILEYS"
}
```

**d) Conectar o WhatsApp usando o QR Code retornado**

### 2. Enviar Mensagem

Agora você pode enviar mensagens:

```bash
POST /api/evolution/message
{
  "number": "5581996349077",
  "message": "Olá! Esta é uma mensagem de teste.",
  "evolutionInstanceId": 1
}
```

---

## Logs

O sistema registra logs detalhados em cada etapa:

```
INFO  - POST /api/evolution/message - Sending message by user ID: 1
INFO  - Sending message to 5581996349077 via Evolution Instance ID: 1
INFO  - Using Evolution Instance: empresa-fracisco-sa (Evolution: Evolution Produção)
INFO  - Calling Evolution API to send message...
INFO  - Sending text message to 5581996349077 via instance: empresa-fracisco-sa
INFO  - Text message sent successfully. Message ID: 3EB091A3574CBF8AE35591A3C30082AB7B2F4886, Status: PENDING
INFO  - Message sent successfully. Message ID: 3EB091A3574CBF8AE35591A3C30082AB7B2F4886, Status: PENDING
```

---

## Monitoramento

Para monitorar o envio de mensagens:

1. **Logs da aplicação**: Verifique os logs para acompanhar cada envio
2. **Evolution API**: Acesse o painel da Evolution para ver status das mensagens
3. **WhatsApp**: Verifique o aplicativo para confirmar recebimento

---

## Troubleshooting

### Mensagem não enviada

**Problema:** Response retorna mas mensagem não chega

**Possíveis causas:**
- WhatsApp desconectado (verificar QR Code)
- Número inválido ou bloqueado
- Instância Evolution com problemas

**Solução:**
1. Verificar status da instância no Evolution
2. Reconectar WhatsApp se necessário
3. Validar formato do número

### Erro de timeout

**Problema:** Timeout ao chamar Evolution API

**Possíveis causas:**
- Evolution API fora do ar
- Rede lenta ou instável
- URL incorreta

**Solução:**
1. Verificar se Evolution API está acessível
2. Validar URL configurada na Evolution
3. Aumentar timeout se necessário (WebClientConfig)

### EvolutionInstance não encontrada

**Problema:** Erro 404 ao buscar instância

**Possíveis causas:**
- ID incorreto
- Instância deletada
- Banco de dados inconsistente

**Solução:**
1. Listar instâncias disponíveis: `GET /api/evolution-instance`
2. Verificar ID correto
3. Criar nova instância se necessário

---

## Próximos Passos

1. Execute a aplicação
2. Cadastre Evolution e EvolutionInstance
3. Conecte o WhatsApp via QR Code
4. Envie mensagens de teste
5. Monitore os logs e status

---

## Suporte

Para mais informações, consulte:
- `README.md` - Documentação geral
- `EVOLUTION.md` - Módulo Evolution
- `EVOLUTION_INSTANCE.md` - Módulo Evolution Instance
- `doc/INSOMNIA_COLLECTIONS.md` - Collections Insomnia
