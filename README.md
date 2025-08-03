# 🏢 Franchise Management System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-Reactive-blue.svg)](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
[![MongoDB](https://img.shields.io/badge/MongoDB-Reactive-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)

## 📋 Descripción

Sistema de gestión de franquicias desarrollado con **Spring Boot WebFlux** que implementa una arquitectura **hexagonal (Clean Architecture)** con programación **reactiva**. Permite gestionar franquicias, sucursales y productos de manera eficiente y escalable.

## 🏗️ Arquitectura

### Arquitectura Hexagonal (Clean Architecture)

```
                           🌐 MUNDO EXTERIOR
    ┌─────────────────────────────────────────────────────────┐
    │                    ENTRYPOINTS                          │
    │                   📱 REST API                           │
    │                                                         │
    │  🎯 Handlers     📋 DTOs        🚨 Exceptions           │
    │  • Franchise     • Requests     • Not Found             │
    │  • Branch        • Responses    • Validations           │
    │  • Product       • Updates      • Global Handler        │
    │                                                         │
    │              🔀 Router Configuration                    │
    └─────────────────────────────────────────────────────────┘
                                    │
                                    ▼
    ┌─────────────────────────────────────────────────────────┐
    │                  📋 CASOS DE USO                        │
    │                (Lógica de Aplicación)                   │
    │                                                         │
    │  ✨ Crear Franquicia    📊 Consultar Top Stock          │
    │  🏢 Agregar Sucursal    🔍 Obtener Franquicias          │
    │  📦 Agregar Producto    ✏️  Actualizar Nombres           │
    │  🗑️  Eliminar Producto   📈 Modificar Stock             │
    │                                                         │
    │           (11 Casos de Uso Implementados)               │
    └─────────────────────────────────────────────────────────┘
                                    │
                                    ▼
    ┌─────────────────────────────────────────────────────────┐
    │               🏛️ NÚCLEO DEL NEGOCIO                     │
    │                 (Domain Layer)                          │
    │                                                         │
    │  📊 Entidades          💎 Value Objects                 │
    │  • Franchise           • Name (con validaciones)        │
    │  • Branch              • Stock (inmutable)              │
    │  • Product                                              │
    │                                                         │
    │  ⚙️ Servicios          🏭 Factory                        │
    │  • ProductStock        • FranchiseFactory               │
    │    Service                                              │
    │                                                         │
    │              🔌 Repository Interface                    │
    └─────────────────────────────────────────────────────────┘
                                    │
                                    ▼
    ┌─────────────────────────────────────────────────────────┐
    │              🔧 INFRAESTRUCTURA                         │
    │               (Detalles Técnicos)                       │
    │                                                         │
    │  🔄 Mappers            💾 Persistencia                  │
    │  • Domain ↔ Entity     • MongoDB Entities               │
    │  • Entity ↔ DTO        • Repository Impl                │
    │  • Response Maps       • Reactive Mongo                 │
    │                                                         │
    │           🔌 Implementación de Repositorios             │
    └─────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                      🗄️ BASE DE DATOS
    ┌─────────────────────────────────────────────────────────┐
    │                    📚 MongoDB                           │
    │                (Reactive & NoSQL)                       │
    │                                                         │
    │  • franchise_system database                            │
    │  • Documentos embebidos (branches + products)           │
    │  • Streams reactivos (Mono/Flux)                        │
    └─────────────────────────────────────────────────────────┘
```

### 🔄 Flujo de una Petición

```
👤 Cliente                  🌐 API REST                 📋 Caso de Uso
    │                           │                           │
    │ POST /franchises          │                           │
    │ ────────────────────────► │ FranchiseHandler          │
    │                           │ ──────────────────────►   │ CreateFranchise
    │                           │                           │ UseCase
    │                           │                           │
    ▼                           ▼                           ▼

🏛️ Dominio                  🔧 Infraestructura         🗄️ MongoDB
    │                           │                           │
    │ Franchise + Name          │                           │
    │ ──────────────────────► │ Mapper                      │
    │                           │ ──────────────────────►   │ franchises
    │                           │ FranchiseEntity           │ collection
    │                           │                           │
    │ ◄────────────────────── │ ◄────────────────────── │
    ▼                           ▼                           ▼

    Response (Mono<FranchiseResponse>)
```

## 🚀 Funcionalidades Implementadas

### Core Features
- ✅ **Crear franquicia** (`POST /api/v1/franchises`)
- ✅ **Agregar sucursal** (`POST /api/v1/franchises/{id}/branches`)
- ✅ **Agregar producto** (`POST /api/v1/franchises/{franchiseId}/branches/{branchId}/products`)
- ✅ **Eliminar producto** (`DELETE /api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}`)
- ✅ **Modificar stock** (`PATCH /api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock`)
- ✅ **Top productos por stock** (`GET /api/v1/franchises/{franchiseId}/top-stock-products`)

### Plus Features
- ✅ **Actualizar nombre franquicia** (`PATCH /api/v1/franchises/{id}`)
- ✅ **Actualizar nombre sucursal** (`PATCH /api/v1/franchises/{franchiseId}/branches/{branchId}`)
- ✅ **Actualizar nombre producto** (`PATCH /api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}`)

## 🛠️ Stack Tecnológico

### Backend
- **Java 17+** - LTS con mejoras de performance
- **Spring Boot 3.4.0** - Framework principal
- **Spring WebFlux** - Programación reactiva
- **Spring Data Reactive MongoDB** - Persistencia reactiva
- **Project Reactor** - Reactive Streams implementation

### Testing
- **JUnit 5** - Framework de testing
- **WebTestClient** - Testing reactivo de endpoints
- **SpringBootTest** - Tests de integración
- **Cobertura 100%** de endpoints funcionales

### Tools & Libraries
- **Lombok** - Reducción de boilerplate
- **MapStruct** - Mapeo automático de objetos
- **AspectJ** - Logging transversal
- **Maven** - Gestión de dependencias

## 📊 Comparativa con Otras Arquitecturas

| Aspecto | Arquitectura Hexagonal + Reactive | Arquitectura MVC Tradicional | Microservicios |
|---------|-----------------------------------|------------------------------|----------------|
| **Escalabilidad** | ⭐⭐⭐⭐⭐ Alta (reactive) | ⭐⭐⭐ Media (blocking) | ⭐⭐⭐⭐⭐ Muy Alta |
| **Mantenibilidad** | ⭐⭐⭐⭐⭐ Excelente | ⭐⭐⭐ Buena | ⭐⭐ Compleja |
| **Testing** | ⭐⭐⭐⭐⭐ Fácil (mocks) | ⭐⭐⭐ Medio | ⭐⭐ Difícil |
| **Performance** | ⭐⭐⭐⭐⭐ Excelente | ⭐⭐⭐ Buena | ⭐⭐⭐⭐ Muy Buena |
| **Complejidad** | ⭐⭐⭐ Media | ⭐⭐⭐⭐⭐ Baja | ⭐⭐ Alta |
| **Deployment** | ⭐⭐⭐⭐⭐ Simple | ⭐⭐⭐⭐⭐ Simple | ⭐⭐ Complejo |

## 🚀 Instalación y Ejecución

### Prerrequisitos
```bash
- Java 21+
- Maven 3.8+
- MongoDB 7.0+ (para desarrollo local)
- Docker & Docker Compose (recomendado)
```

### 🏠 Ejecución Local (Desarrollo)

#### 1. Clonar el repositorio
```bash
git clone https://github.com/LPZneider/franchise-system.git
cd franchise-system
```

#### 2. Levantar MongoDB para desarrollo local
```bash
# Opción 1: Docker (Recomendado)
docker run -d --name mongodb -p 27017:27017 mongo:latest

# Opción 2: Docker con autenticación
docker run -d --name mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin123 \
  mongo:7.0
```

#### 3. Ejecutar la aplicación
```bash
# Compilar y ejecutar
mvn spring-boot:run

# O ejecutar directamente el JAR
mvn clean package
java -jar target/franchise-system-0.0.1-SNAPSHOT.jar
```

#### 4. Verificar la aplicación
```bash
curl http://localhost:8001/actuator/health
```

### 🐳 Ejecución con Docker (Producción)

#### Multi-stage Dockerfile optimizado
```dockerfile
# Multi-stage build para optimizar tamaño de imagen
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY .mvn ./.mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Imagen de producción
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/target/franchise-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8001
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8001/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Construcción de la imagen
```bash
# Construir imagen
docker build -t franchise-system:latest .

# Ejecutar contenedor individual
docker run -d \
  --name franchise-app \
  -p 8001:8001 \
  -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/franchise_system \
  franchise-system:latest
```

### 🐳 Docker Compose (Ambiente Completo)

#### Levantar todo el stack
```bash
# Levantar servicios en background
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Verificar estado de los servicios
docker-compose ps
```

#### Configuración completa (docker-compose.yml)
```yaml
version: '3.8'

services:
  mongodb:
    image: mongo:7.0
    container_name: franchise-mongodb
    restart: always
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin123
      MONGO_INITDB_DATABASE: franchise_system
    volumes:
      - mongodb_data:/data/db
      - ./init-mongo.js:/docker-entrypoint-initdb.d/init-mongo.js:ro
    networks:
      - franchise-network
    healthcheck:
      test: ["CMD", "mongosh", "--eval", "db.adminCommand('ping')"]
      interval: 10s
      timeout: 5s
      retries: 5

  franchise-app:
    build: .
    container_name: franchise-system
    restart: always
    ports:
      - "8001:8001"
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://admin:admin123@mongodb:27017/franchise_system?authSource=admin
      SPRING_PROFILES_ACTIVE: docker
      SERVER_PORT: 8001
    depends_on:
      mongodb:
        condition: service_healthy
    networks:
      - franchise-network

volumes:
  mongodb_data:
    driver: local

networks:
  franchise-network:
    driver: bridge
```

#### Comandos útiles de Docker Compose
```bash
# Levantar servicios
docker-compose up -d

# Parar servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v

# Reconstruir imágenes
docker-compose up --build

# Ver logs de un servicio específico
docker-compose logs -f franchise-app
```

### 🧪 Testing

#### Prerrequisitos para Tests
**IMPORTANTE**: Para ejecutar los tests localmente necesitas MongoDB ejecutándose:

```bash
# Levantar MongoDB para tests
docker run -d --name mongodb-test -p 27017:27017 mongo:latest

# Verificar que MongoDB esté corriendo
docker ps | grep mongodb-test
```

#### Ejecutar Tests
```bash
# Todos los tests (requiere MongoDB en puerto 27017)
mvn test

# Tests específicos
mvn test -Dtest=FranchiseHandlerTest

# Tests con perfil específico
mvn test -Dspring.profiles.active=test

# Con reporte de cobertura
mvn test jacoco:report

# Solo tests unitarios (no requieren MongoDB)
mvn test -Dtest="**/*UseCaseTest,**/*Test" -Dtest.skip.integration=true
```

### 🔧 Perfiles de Configuración

#### application.properties (Desarrollo Local)
```properties
spring.application.name=franchise-system
server.port=8001
spring.data.mongodb.uri=mongodb://localhost:27017/franchise_system
spring.data.mongodb.auto-index-creation=true
logging.level.com.nequi.franchise=DEBUG
```

#### application-docker.properties (Docker)
```properties
spring.data.mongodb.uri=mongodb://admin:admin123@mongodb:27017/franchise_system?authSource=admin
spring.data.mongodb.auto-index-creation=true
logging.level.com.nequi.franchise=INFO
server.port=8001
server.shutdown=graceful
management.endpoints.web.exposure.include=health,info,metrics
```

### 🚀 Despliegue en Producción

#### Variables de Entorno Requeridas
```bash
export SPRING_DATA_MONGODB_URI="mongodb://user:pass@host:port/database?authSource=admin"
export SERVER_PORT=8001
export SPRING_PROFILES_ACTIVE=prod
export LOGGING_LEVEL_COM_NEQUI_FRANCHISE=INFO
```

#### Health Checks
```bash
# Verificar salud de la aplicación
curl http://localhost:8001/actuator/health

# Métricas de la aplicación
curl http://localhost:8001/actuator/metrics

# Info de la aplicación
curl http://localhost:8001/actuator/info
```
