# Docker Compose - Testes Locais

Este docker-compose usa a imagem **latest** do Docker Hub para testes locais.

## 🚀 Como Usar

### 1. Certifique-se que o arquivo `.env` está configurado

```bash
# Verifique se app/.env existe e está configurado
cat .env
```

### 2. Pull da imagem latest

```bash
docker pull jessebezerra/tediobackend:latest
```

### 3. Inicie o container

```bash
# Na pasta app/
docker-compose up -d
```

### 4. Verifique os logs

```bash
docker-compose logs -f tedioapp
```

### 5. Teste a aplicação

```bash
# Health check
curl http://localhost:8101/actuator/health

# Login
curl -X POST http://localhost:8101/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'
```

## 🛠️ Comandos Úteis

```bash
# Parar container
docker-compose down

# Reiniciar
docker-compose restart

# Ver logs
docker-compose logs -f

# Atualizar para versão mais recente
docker-compose pull
docker-compose up -d

# Remover tudo (incluindo volumes)
docker-compose down -v
```

## 🔄 Atualizar Imagem

Quando uma nova versão for publicada no Docker Hub:

```bash
# Pull da nova versão
docker-compose pull

# Recrie o container
docker-compose up -d --force-recreate
```

## 📊 Verificar Status

```bash
# Status dos containers
docker-compose ps

# Logs em tempo real
docker-compose logs -f tedioapp

# Entrar no container
docker-compose exec tedioapp sh
```

## 🐛 Troubleshooting

### Container não inicia

```bash
# Verifique os logs
docker-compose logs tedioapp

# Verifique se a porta está em uso
netstat -an | grep 8101
```

### Erro de conexão com banco de dados

```bash
# Verifique as variáveis de ambiente
docker-compose exec tedioapp env | grep DATABASE
```

### Forçar recriação

```bash
docker-compose down
docker-compose pull
docker-compose up -d --force-recreate
```

## 🔐 Variáveis de Ambiente

O docker-compose usa as variáveis do arquivo `.env`:

- `DATABASE_HOST` - Host do PostgreSQL
- `DATABASE_PORT` - Porta do PostgreSQL
- `DATABASE_NAME` - Nome do banco
- `DATABASE_USER` - Usuário do banco
- `DATABASE_PASSWORD` - Senha do banco
- `JWT_SECRET_KEY` - Chave secreta JWT

## 📝 Notas

- Este arquivo **não está no Git** (está no .gitignore)
- Usa a imagem `jessebezerra/tediobackend:latest` do Docker Hub
- Conecta ao banco de dados externo (não cria PostgreSQL local)
- Porta exposta: `8101`
