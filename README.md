# Intern-Hub-Ticket-Service

Intern-Hub-Ticket-Service is a core component of the Intern Hub ecosystem, responsible for managing the lifecycle of support and administrative tickets. Built with a focus on scalability and maintainability, it employs Hexagonal Architecture to separate business logic from infrastructure concerns.

## 🚀 Features

- **Ticket Lifecycle Management**: Create, query, and manage various types of tickets.
- **Approval Workflow**: Integrated approval system for tickets requiring review.
- **Evidence Management**: Capability to upload and link evidence (images, documents) to tickets.
- **Asynchronous Processing**: Uses Kafka for event-driven communication and background tasks.
- **Transactional Outbox**: Ensures data consistency between the database and message broker.
- **Observability**: Ready for monitoring with OpenTelemetry and detailed logging.

## 🛠 Tech Stack

- **Java 25**: Using the latest Java features including Virtual Threads.
- **Spring Boot 3.4.3**: Robust framework for microservices.
- **PostgreSQL**: Reliable relational database for persistent storage.
- **Kafka**: High-performance distributed messaging system.
- **Liquibase**: Database schema evolution and migration management.
- **Gradle**: Modern build automation system.
- **Lombok**: Reducing boilerplate code.

## 🏗 Architecture

The project follows **Hexagonal (Ports & Adapters)** architecture principles:

- **Core**: Contains pure domain models and business use cases. Framework-agnostic.
- **API**: Driving adapters (REST controllers) that handle external communication.
- **Infra**: Driven adapters for persistence (JPA), messaging (Kafka), and external integrations.
- **App**: The Spring Boot entry point and main configuration.

## 📂 Project Structure

```text
├── api     - Driving adapters (Controllers, DTOs, Mappers)
├── core    - Domain models, Ports (Interfaces), Use cases
├── infra   - Driven adapters (Persistence, Kafka, Service Clients)
├── app     - Application entry point and configuration
└── build   - Build artifacts
```

## 🚦 Getting Started

### Prerequisites

- JDK 25
- Docker & Docker Compose
- Gradle (optional, wrapper included)

### Configuration

The application can be configured via environment variables. Key variables include:

- `SERVER_PORT`: Port for the REST API (default: 8080)
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`: PostgreSQL connection details.
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka broker addresses.
- `INTERNAL_SECRET_KEY`: Security key for internal API calls.

### Running Locally

1. **Start Infrastructure**:
   ```bash
   docker-compose up -d
   ```

2. **Run Application**:
   ```bash
   ./gradlew bootRun
   ```

## 📖 API Documentation

The service exposes RESTful endpoints under `/api/v1/tickets`. 
- `POST /api/v1/tickets`: Create a new ticket.
- `POST /api/v1/tickets/{ticketId}/approve`: Approve a ticket.
- `POST /api/v1/tickets/evidence/upload`: Upload evidence for a ticket.

(Note: API docs can be explored via Swagger if enabled in `application.yml`).

---
Developed as part of the Intern Hub Platform.
