#  Witchblog 2.0

## Setup

### First Step: Generate OpenAI API Key

Before starting the application, you need to generate an API key from OpenAI:

1. Go to [OpenAI Platform API Keys](https://platform.openai.com/api-keys)
2. Generate a new API key
3. Export the environment variable:
```bash
export OPENAI_API_KEY=your_api_key_here
```

## Frontend

The frontend application can be accessed at: [Frontend Repository](https://github.com/mpszkudlarek/witch-blog)
## API Communication

The application works with both **WebSocket** and standard **REST API** calls.

For REST API examples, check the `/collections` folder which contains Postman collections with sample requests and responses.

##  Kafka (KRaft Mode — No Zookeeper)

We use **Apache Kafka in KRaft mode** (i.e., without Zookeeper) via the official Bitnami Docker image.

### 1. Create a Docker network:

```bash
docker network create kafka-net
```

### 2. Start Kafka in KRaft mode
```bash
docker run -d --name kafka \
  --network kafka-net \
  -e KAFKA_CFG_PROCESS_ROLES=broker,controller \
  -e KAFKA_CFG_NODE_ID=1 \
  -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  -p 9092:9092 \
  bitnami/kafka:latest
```

## Spring Boot — Local Profile

To run the application with a local profile (`application-local.yml`), add the following VM option:
```bash
-Dspring.profiles.active=local
```

## Project Startup Instructions

Before starting the services, you **must build the project** to ensure all common modules are available.

### 1. Build the project from the root directory:

```bash
mvn clean install
```
This will install the common-module and any shared dependencies to your local Maven repository.

## 2. Start Microservices in the Following Order

1. **registry-service** — Service discovery via Eureka
2. **gateway-service** — API Gateway for routing requests
3. **orchestrator-service** — Core logic for managing processes and communication
4. **payment-service** — Handles payment flow and Kafka event publishing (dummy BLIK logic)
5. **divination-service** — Creates divination by integration with LLM, based on user info and payement process
6. **management-service** — Calculates balance based on payments and the tokens spent to generate divination