# Coleções Insomnia - TedioApp

## Visão Geral

As coleções foram organizadas por módulo/serviço para facilitar o gerenciamento e manutenção. Cada coleção é independente e pode ser importada separadamente no Insomnia.

## Coleções Disponíveis

### 1. **insomnia-auth.json** 🔐
**Autenticação JWT - Comum a todos os serviços**

- Login

**Uso:** Importe esta coleção primeiro e faça login para obter o JWT token. Copie o token retornado e cole na variável `jwt_token` do ambiente de cada coleção.

---

### 2. **insomnia-users.json** 👥
**Gerenciamento de Usuários**

- Create Application User (público)
- Create Integration User (público)
- Get All Users
- Get User By ID
- Get User By Email
- Update User
- Delete User

**Funcionalidades:**
- Criação de usuários APPLICATION e INTEGRATION
- CRUD completo de usuários
- Busca por ID ou email

---

### 3. **insomnia-permissions.json** 🔑
**Gerenciamento de Permissões**

- Add Permissions (incremental)
- Remove All Permissions
- Get My Permissions
- Get Available Permissions

**Funcionalidades:**
- Adicionar permissões sem remover as existentes
- Remover todas as permissões (array vazio)
- Listar permissões do usuário autenticado
- Listar permissões disponíveis: `user`, `permission`, `omnichannel`, `omnichannel-manager`, `api-manager`

---

### 4. **insomnia-owners.json** 🏢
**Gerenciamento de Proprietários de APIs**

- Create Owner
- Get All Owners
- Get Owner By ID
- Update Owner
- Delete Owner

**Funcionalidades:**
- CRUD completo de owners
- Owners são necessários para cadastrar API Authentications

---

### 5. **insomnia-api-authentications.json** 🔌
**Gerenciamento de Autenticações de APIs Externas**

- Create API Auth - OAuth2 (exemplo)
- Create API Auth - Basic (exemplo)
- Create API Auth - API Key (exemplo)
- Get All API Authentications
- Get API Auths By Owner
- Get API Auth By ID
- Update API Authentication
- Delete API Authentication
- **Test API Authentication** (executa requisição real)

**Funcionalidades:**
- Cadastro de diferentes tipos de autenticação (OAuth2, Basic, Bearer, API Key)
- Suporte a Content-Types variados (JSON, XML, Form-Urlencoded, etc)
- Request body e headers customizáveis (formato chave-valor)
- Teste de autenticação com requisição HTTP real
- Filtro por owner

---

### 6. **insomnia-evolutions.json** 🔄
**Gerenciamento de Instâncias Evolution API**

- Create Evolution
- Get All Evolutions
- Get Evolutions By Owner
- Get Evolution By ID
- Get Evolution By Nome
- Update Evolution
- Delete Evolution

**Funcionalidades:**
- CRUD completo de instâncias Evolution
- Filtro por owner
- Busca por ID ou nome
- Gerenciamento de múltiplas instâncias com URLs e API Keys

---

### 7. **insomnia-evolution-instances.json** 🔄
**Gerenciamento de Instâncias Evolution (Integração)**

- Create Evolution Instance (integra com API Evolution)
- Get All Evolution Instances
- Get Instances By Evolution
- Get Instances By User
- Get Instances By Status
- Get Evolution Instance By ID
- Get Evolution Instance By Name
- Delete Evolution Instance

**Funcionalidades:**
- CRUD completo de instâncias Evolution
- Integração automática com Evolution API ao criar
- QR Code base64 retornado quando solicitado
- Filtros por Evolution, User ou Status
- Persistência condicional (só salva se API responder com sucesso)

---

### 8. **insomnia-evolution-messages.json** 💬
**Envio de Mensagens Evolution**

- Send Text Message
- Send Text - Long Message
- Send Media - Image (PNG)
- Send Media - Image (JPEG)
- Send Media - Document (PDF)
- Send Audio Message
- Send Sticker

**Funcionalidades:**
- Envio de mensagens de texto via Evolution API
- Envio de mensagens de mídia (imagem, vídeo, documento)
- Envio de mensagens de áudio/voz
- Envio de stickers/figurinhas
- Integração automática com EvolutionInstance
- Response simplificado com status do envio
- Suporte a mensagens longas (até 4096 caracteres)

---

### 9. **insomnia-evolution-media.json** 📥
**Buscar Base64 de Mensagens de Mídia**

- Get Media Base64 - Audio Message
- Get Media Base64 - Video Message
- Get Media Base64 - Image Message
- Get Media Base64 - Document Message
- Get Media Base64 - Sticker Message

**Funcionalidades:**
- Buscar base64 de mensagens de mídia recebidas
- Suporte a áudio, vídeo, imagem, documento e sticker
- Conversão opcional para MP4 (vídeos)
- Retorna mediaType, fileName, mimetype, fileLength e base64
- Integração automática com EvolutionInstance

---

### 10. **insomnia-actuator.json** 📊
**Monitoramento e Métricas**

- Health Check (público)
- Application Info (público)
- Metrics List
- JVM Memory Metrics
- HTTP Requests Metrics
- Flyway Status
- Environment
- Loggers

**Funcionalidades:**
- Monitoramento de saúde da aplicação
- Métricas JVM e HTTP
- Status das migrations
- Configuração de logs

---

## Como Usar

### Passo 1: Importar Coleções

1. Abra o Insomnia
2. Clique em **Application** → **Preferences** → **Data** → **Import Data**
3. Selecione **From File**
4. Importe os arquivos JSON na ordem:
   - `insomnia-auth.json` (primeiro)
   - Demais coleções conforme necessidade

### Passo 2: Configurar Ambiente

Cada coleção possui um ambiente com as variáveis:
- `base_url`: `http://localhost:8101` (padrão)
- `jwt_token`: `` (vazio inicialmente)

### Passo 3: Autenticar

1. Na coleção **TedioApp - Authentication**
2. Execute a requisição **Login**
3. Copie o token retornado no campo `token`
4. Cole na variável `jwt_token` do ambiente de cada coleção que você usar

### Passo 4: Usar os Endpoints

Agora você pode usar todos os endpoints protegidos. O token será automaticamente incluído no header `Authorization: Bearer {{ _.jwt_token }}`.

---

## Fluxo Recomendado

### Primeiro Uso

1. **Criar usuário** (Users → Create Application User)
2. **Fazer login** (Authentication → Login)
3. **Copiar token** e colar em `jwt_token`
4. **Atribuir permissões** (Permissions → Add Permissions)
5. **Criar owner** (Owners → Create Owner)
6. **Cadastrar autenticação de API** (API Authentications → Create API Auth)
7. **Testar autenticação** (API Authentications → Test API Authentication)

### Uso Diário

1. **Login** para obter novo token
2. Usar endpoints conforme necessidade

---

## Variáveis de Ambiente

### Globais (todas as coleções)
```json
{
  "base_url": "http://localhost:8101",
  "jwt_token": ""
}
```

### Como Alterar

1. Clique no dropdown de ambiente (canto superior esquerdo)
2. Selecione **Manage Environments**
3. Edite as variáveis conforme necessário

---

## Exemplos de Uso

### Exemplo 1: Criar e Testar Autenticação OAuth2

```
1. Login → obter token
2. Create Owner → criar "Google APIs" (ownerId: 1)
3. Create API Auth - OAuth2 → cadastrar autenticação
4. Test API Authentication → testar conexão
```

### Exemplo 2: Gerenciar Permissões

```
1. Login → obter token
2. Get Available Permissions → ver permissões disponíveis
3. Add Permissions → adicionar ["user", "api-manager"]
4. Get My Permissions → confirmar permissões
5. Add Permissions → adicionar ["omnichannel"] (incremental)
6. Remove All Permissions → limpar tudo
```

---

## Tipos de Autenticação Suportados

### OAuth2
```json
{
  "authenticationType": "OAUTH2",
  "contentType": "APPLICATION_FORM_URLENCODED",
  "requestBody": {
    "grant_type": "client_credentials",
    "client_id": "...",
    "client_secret": "..."
  }
}
```

### Basic
```json
{
  "authenticationType": "BASIC",
  "contentType": "APPLICATION_JSON",
  "requestBody": {
    "username": "admin",
    "password": "senha"
  },
  "headers": {
    "Authorization": "Basic base64..."
  }
}
```

### API Key
```json
{
  "authenticationType": "API_KEY",
  "headers": {
    "X-API-Key": "sua_key",
    "X-API-Secret": "seu_secret"
  }
}
```

---

## Troubleshooting

### Token Expirado
- **Erro:** 401 Unauthorized
- **Solução:** Faça login novamente e atualize o `jwt_token`

### Permissão Negada
- **Erro:** 403 Forbidden
- **Solução:** Verifique se o usuário tem as permissões necessárias

### Owner não encontrado
- **Erro:** "Owner não encontrado"
- **Solução:** Crie um owner antes de cadastrar API Authentication

---

## Dicas

1. **Organize por workspace:** Crie um workspace no Insomnia para cada ambiente (dev, staging, prod)
2. **Use variáveis:** Aproveite as variáveis de ambiente para facilitar testes
3. **Teste incremental:** Teste as permissões adicionando uma por vez
4. **Monitore:** Use o Actuator para verificar saúde da aplicação
5. **Backup:** Exporte suas coleções periodicamente

---

## Suporte

Para mais informações, consulte:
- `README.md` - Documentação geral da API
- `API_MANAGER.md` - Documentação detalhada do API Manager
- `FLYWAY.md` - Documentação das migrations
