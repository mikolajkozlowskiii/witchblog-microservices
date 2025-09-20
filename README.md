#  Witchblog 2.0
This is the backend part of the Witchblog project, built as a microservices architecture with event-driven communication. The entire process is orchestrated by the orchestrator microservice, supporting both WebSocket connections for real-time frontend communication and REST API calls.

## Frontend

The frontend application can be accessed at: [Frontend Repository](https://github.com/mpszkudlarek/witch-blog)
## API Communication

The application works with both **WebSocket** and standard **REST API** calls.

For REST API examples, check the `/collections` folder which contains Postman collections with sample requests and responses.

## Succesfull Basic Application Flow

Here are the simple steps for a successful divination process:

1. **Register User** - Create a user with unique ID and personal details (name, date of birth, favorite color, relationship status, favorite number)
2. **BLIK Payment** - User pays for divination for the created process for specific user 
3. **Generate Divination** - If payment is successful, it triggers an event to generate divination:
    - 3.1) Random tarot cards are generated with meanings and possible reversing cards
    - 3.2) Build prompt for divination based on user info and drawn tarot cards
    - 3.3) Call LLM (Large Language Model) and get the divination answer

    
Administrators can check profit. The system calculates profit by subtracting LLM token costs from total payments received from users.

**Note**: For a more detailed BPMN process flow, check the diagrams folder.


## Architecture Diagram

![Architecture Diagram](diagrams/architecture.svg)


## Setup

### First Step: Generate OpenAI API Key

Before starting the application, you need to generate an API key from OpenAI:

1. Go to [OpenAI Platform API Keys](https://platform.openai.com/api-keys)
2. Generate a new API key
3. Export the environment variable:
```bash
export OPENAI_API_KEY=your_api_key_here
```


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