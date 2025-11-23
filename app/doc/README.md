# Documentação da API - TedioApp

Esta pasta contém a documentação e collections para testar a API TedioApp.

## Insomnia Collection

### Como Importar

1. Abra o Insomnia
2. Clique em **Application** > **Preferences** > **Data** > **Import Data**
3. Selecione o arquivo `insomnia-collection.json`
4. A collection "TedioApp API" será importada com todas as requisições

### Estrutura da Collection

A collection está organizada em 4 grupos:

#### 1. Authentication
- **Login**: Autentica um usuário e retorna token JWT

#### 2. Users
- **Create Application User**: Cria usuário tipo APPLICATION
- **Create Integration User**: Cria usuário tipo INTEGRATION
- **Get All Users**: Lista todos os usuários
- **Get User By ID (Query Param)**: Busca usuário por ID via `?id=1`
- **Get User By Email (Query Param)**: Busca usuário por email via `?email=user@example.com`
- **Update User**: Atualiza dados do usuário
- **Delete User**: Remove um usuário

#### 3. Permissions
- **Assign Permissions**: Atribui permissões ao usuário autenticado
- **Get User Permissions**: Busca permissões do usuário autenticado
- **Get Available Permissions**: Lista todas as permissões disponíveis no sistema

#### 4. Actuator
- **Health Check**: Verifica saúde da aplicação
- **Application Info**: Informações da aplicação
- **Metrics**: Lista todas as métricas
- **JVM Memory Metrics**: Métricas de memória
- **Flyway Status**: Status das migrations
- **Environment**: Variáveis de ambiente

### Variáveis de Ambiente

A collection usa as seguintes variáveis:

```json
{
  "base_url": "http://localhost:8101",
  "jwt_token": ""
}
```

#### Como Configurar o Token JWT

1. Execute a requisição **Login** na pasta Authentication
2. Copie o token da resposta (campo `token`)
3. Clique no ambiente (canto superior esquerdo)
4. Cole o token no campo `jwt_token`
5. Salve

Agora todas as requisições autenticadas usarão automaticamente o token!

### Fluxo de Teste Recomendado

#### 1. Criar Usuário Application
```http
POST /api/user
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

#### 2. Fazer Login
```http
POST /api/auth/login
{
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer"
}
```

#### 3. Configurar Token
- Copie o valor do campo `token`
- Cole na variável `jwt_token` do ambiente

#### 4. Testar Endpoints Autenticados
Agora você pode testar todos os endpoints que requerem autenticação:
- Get All Users
- Get User By ID
- Update User
- Delete User
- Actuator Metrics
- Etc.

### Exemplos de Requisições

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

#### Buscar Usuário por ID
```http
GET /api/user?id=1
Authorization: Bearer {seu-token}
```

#### Buscar Usuário por Email
```http
GET /api/user?email=joao@example.com
Authorization: Bearer {seu-token}
```

#### Listar Todos os Usuários
```http
GET /api/user
Authorization: Bearer {seu-token}
```

#### Atualizar Usuário
```http
PUT /api/user/1
Authorization: Bearer {seu-token}
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@example.com",
  "password": "novaSenha123"
}
```

#### Verificar Health
```http
GET /actuator/health
```

#### Ver Métricas JVM
```http
GET /actuator/metrics/jvm.memory.used
Authorization: Bearer {seu-token}
```

### Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Recurso deletado com sucesso |
| 400 | Bad Request - Erro de validação |
| 401 | Unauthorized - Credenciais inválidas ou token ausente |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro interno |

### Respostas de Erro

#### Erro de Validação (400)
```json
{
  "timestamp": "2024-11-18T22:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Erro de validação nos campos",
  "path": "/api/user",
  "errors": [
    "email: Email deve ser válido",
    "password: Password deve ter no mínimo 6 caracteres"
  ]
}
```

#### Erro de Autenticação (401)
```json
{
  "timestamp": "2024-11-18T22:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Email ou senha inválidos",
  "path": "/api/auth/login"
}
```

### Dicas

1. **Token Expirado**: O token JWT expira em **1 hora** (3600000ms). Se receber erro 401, faça login novamente.

2. **Endpoints Públicos** (sem autenticação): 
   - `POST /api/auth/login` - Login
   - `POST /api/user` - Criar usuário application
   - `POST /api/user/integration` - Criar usuário integration
   - `GET /actuator/health` - Health check
   - `GET /actuator/info` - Informações da aplicação

3. **Endpoints Protegidos** (requerem JWT no header):
   ```
   Authorization: Bearer {token}
   ```
   - `GET /api/user` - Listar usuários
   - `GET /api/user/{id}` - Buscar usuário
   - `PUT /api/user/{id}` - Atualizar usuário
   - `DELETE /api/user/{id}` - Deletar usuário
   - Todos os endpoints do Actuator (exceto health e info)

4. **Validações**:
   - Email deve ser válido
   - Password mínimo 6 caracteres
   - Nome entre 3 e 100 caracteres

5. **Tipos de Usuário**:
   - **APPLICATION**: Usuário padrão (nome, email, password)
   - **INTEGRATION**: Usuário para integrações (+ clientId, clientSecret)

### Testando com cURL

Se preferir usar cURL:

```bash
# Criar usuário
curl -X POST http://localhost:8101/api/user \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'

# Login
curl -X POST http://localhost:8101/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'

# Listar usuários (com token)
curl -X GET http://localhost:8101/api/user \
  -H "Authorization: Bearer {seu-token}"

# Health check
curl http://localhost:8101/actuator/health
```

### Troubleshooting

**Problema**: Erro 401 em endpoints autenticados
- **Solução**: Verifique se o token está configurado na variável `jwt_token`

**Problema**: Erro de conexão
- **Solução**: Verifique se a aplicação está rodando em `http://localhost:8101`

**Problema**: Erro 400 "Email já cadastrado"
- **Solução**: Use um email diferente ou delete o usuário existente

**Problema**: Token expirado
- **Solução**: Faça login novamente para obter um novo token

## Postman Collection

Se preferir usar Postman, você pode importar o mesmo arquivo JSON ou criar manualmente as requisições seguindo a documentação acima.

## Swagger/OpenAPI

Para adicionar documentação Swagger no futuro, adicione a dependência `springdoc-openapi-starter-webmvc-ui` ao projeto.

## Suporte

Para mais informações, consulte o README principal do projeto.
