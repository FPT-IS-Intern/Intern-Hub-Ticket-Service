✅ Clean / Hexagonal Architecture Prompt (Agent-ready)

1. Overall principles

Architecture follows Hexagonal (Ports & Adapters).

Dependency rule:
API → Core ← Infra
Core never depends on API or Infra.

Core is framework-agnostic (no Spring, JPA, Kafka, etc).

All framework annotations live in api or infra only.

2. Module responsibilities
   🧠 Core module (Domain + Application)

Purpose: Define what the system does and business rules.

Core contains:

Domain Model.

Pure model (not JPA entity)

No @Entity, @Table, @Id, etc

Use case / Application service

Orchestrates business flow

Defines transaction boundary conceptually

Business rules, policies, invariants

Domain validation (NOT input format validation)

Ports (interfaces)

Repository

External services (mail, payment, config, event publisher, etc)

Rules:

Core only uses Java standard library (and Lombok if needed).

Core never:

Accesses DB, Kafka, Redis, HTTP

Reads config files

Uses framework annotations

Core throws domain exceptions only.

🔌 Infra module (Driven adapters)

Purpose: Answer: How can infrastructure serve the core?

Infra contains:

Implementations of core ports

Repository implementations (JPA / JDBC / Redis)

External service adapters (HTTP client, mail, payment)

Event publisher implementations (Kafka, etc)

Technical concerns:

Persistence

Serialization

Retry

Messaging

@ConfigurationProperties

Mapping:

Entity ↔ Domain Model

Infra never exposes entity to core

Rules:

Infra depends on Core.

Infra decides technology, Core decides behavior.

Infra catches technical exceptions and translates them if needed.

🌐 API module (Driving adapters)

Purpose: Answer: What can users do with the system?

API contains:

Controllers (REST / GraphQL / gRPC)

Request / Response DTO

Auth, rate limit, input validation

Mapping:

Request DTO → Command / Input Model

Use case result → Response DTO

Bean registration for use cases

Core does NOT auto-register beans

Transaction boundary applied here (e.g. @Transactional)

Rules:

API depends on Core.

API never contains business logic.

API handles:

Input validation (format, required fields)

Mapping domain errors → HTTP errors

3. Validation rules

API validation

Syntax, format, required fields

Core validation

Business rules and invariants

Never validate business logic in controller.

4. Configuration rule

Core does not know where config comes from.

Core defines a config port if needed.

Example:

Core defines interface:

interface SecretKeyProvider {
String secretKey();
}

Infra implements it using:

application.yml

environment variables

vault, etc

5. Transaction rule

Transaction scope is defined by use case.

Transaction implementation is applied by API or Infra adapter.

Repositories must not manage transactions themselves.

6. Event / Messaging rule

Core emits events via event publisher port.

Infra decides:

Kafka / RabbitMQ / Outbox

Core never depends on messaging technology.

7. Forbidden actions (hard rules)

Core importing Spring / JPA / Kafka classes ❌

Entity leaking into Core ❌

Business logic in Controller ❌

Kafka / DB calls inside Use Case ❌

Infra calling API ❌

8. Mental model for agents

Core: “What must be true?”

Infra: “How do we make it happen?”

API: “How does the outside world talk to us?”
