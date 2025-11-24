# TedioApp

Spring Boot application with JWT authentication and Evolution API integration.

## 🚀 Quick Start

### Prerequisites

- Docker and Docker Compose
- Java 17 (for local development)
- Maven 3.9+ (for local development)

### Running with Docker

1. **Configure environment variables:**

```bash
# Copy the example file
cp app/.env.example app/.env

# Edit app/.env with your credentials e app
```

2. **Build and run:**

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f tedioapp

# Stop services
docker-compose down
```

3. **Access the application:**

- API: http://localhost:8101
- Health Check: http://localhost:8101/actuator/health

### Running Locally

1. **Configure environment:**

```bash
cp app/.env.example app/.env
# Edit app/.env with your credentials
```

2. **Run with Maven:**

```bash
cd app
mvn spring-boot:run
```

## 🐳 Docker Commands

### Build only

```bash
docker build -t tedioapp:latest .
```

### Run standalone container

```bash
docker run -d \
  --name tedioapp \
  -p 8101:8101 \
  --env-file app/.env \
  tedioapp:latest
```

### View logs

```bash
docker-compose logs -f tedioapp
```

### Restart services

```bash
docker-compose restart tedioapp
```

### Rebuild after code changes

```bash
docker-compose up -d --build
```

## 📁 Project Structure

```
tedioapp/
├── app/                          # Application source code
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   │       └── db/migration/ # Flyway migrations
│   │   └── test/
│   ├── .env                      # Environment variables (not in git)
│   ├── .env.example              # Environment template
│   └── pom.xml                   # Maven dependencies
├── Dockerfile                    # Multi-stage Docker build
├── docker-compose.yml            # Docker Compose configuration
├── .gitignore                    # Git ignore rules
└── .dockerignore                 # Docker ignore rules
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_HOST` | PostgreSQL host | `191.252.195.25` |
| `DATABASE_PORT` | PostgreSQL port | `5432` |
| `DATABASE_NAME` | Database name | `tedioinfernal` |
| `DATABASE_USER` | Database user | `evolution` |
| `DATABASE_PASSWORD` | Database password | *required* |
| `JWT_SECRET_KEY` | JWT secret key | *required* |

### Profiles

- `default`: Local development
- `prod`: Production (used in Docker)

## 🔐 Security

- JWT authentication
- Password encryption with BCrypt
- Non-root user in Docker container
- Environment variables for sensitive data

## 📊 Health Check

The application includes health check endpoints:

```bash
# Check application health
curl http://localhost:8101/actuator/health

# Check database connection
curl http://localhost:8101/actuator/health/db
```

## 🛠️ Development

### Hot Reload (Local)

```bash
cd app
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
```

### Run Tests

```bash
cd app
mvn test
```

### Database Migrations

Flyway migrations are automatically applied on startup. Migration files are located in:

```
app/src/main/resources/db/migration/
```

## 📝 API Documentation

### Authentication

```bash
# Login
POST http://localhost:8101/api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

### Evolution Instances

```bash
# Create instance
POST http://localhost:8101/api/evolution-instance
Authorization: Bearer {token}

# List instances
GET http://localhost:8101/api/evolution-instance
Authorization: Bearer {token}

# Set webhook
POST http://localhost:8101/api/evolution-instance/{id}/webhook
Authorization: Bearer {token}
Content-Type: application/json

{
  "webhookUrl": "https://your-webhook-url.com/api/webhook"
}
```

## 🐛 Troubleshooting

### Container won't start

```bash
# Check logs
docker-compose logs tedioapp

# Check if port is already in use
netstat -an | grep 8101
```

### Database connection issues

```bash
# Check PostgreSQL container
docker-compose ps postgres

# Test database connection
docker-compose exec postgres psql -U evolution -d tedioinfernal
```

### Rebuild from scratch

```bash
# Remove all containers and volumes
docker-compose down -v

# Rebuild and start
docker-compose up -d --build
```

## 📦 Production Deployment

### Using Docker Compose

```bash
# Production deployment
docker-compose -f docker-compose.yml up -d
```

### Using Docker Swarm

```bash
# Initialize swarm
docker swarm init

# Deploy stack
docker stack deploy -c docker-compose.yml tedioapp
```

### Using Kubernetes

Convert docker-compose.yml to Kubernetes manifests:

```bash
# Install kompose
curl -L https://github.com/kubernetes/kompose/releases/download/v1.31.2/kompose-windows-amd64.exe -o kompose.exe

# Convert
kompose convert
```

## 📄 License

This project is private and proprietary.

## 👥 Authors

- TedioInfernal Team
