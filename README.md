# 🎫 Intern-Hub-Ticket-Service

Microservice quản lý vòng đời ticket (yêu cầu hỗ trợ, hành chính) trong hệ sinh thái **Intern Hub**. Được xây dựng theo kiến trúc **Hexagonal (Ports & Adapters)** nhằm tách biệt logic nghiệp vụ khỏi infrastructure, giúp dễ dàng mở rộng và bảo trì.

---

## 🚀 Tính năng chính

| Tính năng | Mô tả |
|---|---|
| **Quản lý Ticket** | Tạo, truy vấn, phê duyệt và theo dõi trạng thái ticket |
| **Approval Workflow** | Quy trình phê duyệt ticket với idempotency key & optimistic locking |
| **Evidence Management** | Upload và liên kết bằng chứng (ảnh, tài liệu) cho từng ticket |
| **Ticket Type Admin** | Quản trị loại ticket (tạo, cấu hình) |
| **Internal API** | API nội bộ dành cho giao tiếp giữa các service trong hệ thống |
| **Event-Driven** | Sử dụng Kafka cho xử lý bất đồng bộ và giao tiếp giữa các service |
| **Transactional Outbox** | Đảm bảo tính nhất quán dữ liệu giữa database và message broker |
| **Observability** | Tích hợp OpenTelemetry cho tracing, metrics và logging |

---

## 🛠 Tech Stack

| Công nghệ | Phiên bản | Vai trò |
|---|---|---|
| **Java** | 25 | Ngôn ngữ chính, sử dụng Virtual Threads |
| **Spring Boot** | 4.0.3 | Framework nền tảng |
| **PostgreSQL** | 16 | Cơ sở dữ liệu quan hệ |
| **Apache Kafka** | — | Message broker cho event-driven |
| **Redis** | — | Caching |
| **Liquibase** | — | Quản lý migration database |
| **OpenFeign** | 5.0.1 | HTTP client cho inter-service communication |
| **MapStruct** | 1.7.0 | Object mapping |
| **SpringDoc OpenAPI** | 3.0.1 | API documentation (Swagger UI) |
| **OpenTelemetry** | — | Observability (tracing, metrics) |
| **Gradle** | Kotlin DSL | Build system |
| **Docker** | Multi-stage | Containerization |

---

## 🏗 Kiến trúc

```
Hexagonal Architecture (Ports & Adapters)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  ┌─────────────┐     ┌──────────────────┐     ┌──────────────────┐
  │   API       │────▶│      CORE        │◀────│     INFRA        │
  │ (Driving)   │     │                  │     │  (Driven)        │
  │             │     │  Domain Models   │     │                  │
  │ Controllers │     │  Use Cases       │     │  Persistence     │
  │ DTOs        │     │  Ports (in/out)  │     │  Kafka           │
  │ Mappers     │     │  Exceptions      │     │  Service Clients │
  └─────────────┘     └──────────────────┘     └──────────────────┘
                              ▲
                              │
                      ┌───────┴───────┐
                      │     APP       │
                      │  Entry Point  │
                      │  Spring Boot  │
                      │  Config       │
                      └───────────────┘
```

### Modules

| Module | Mô tả |
|---|---|
| **`core`** | Domain models, Use cases, Ports (interfaces). Không phụ thuộc framework |
| **`api`** | Driving adapters — REST Controllers, DTOs, Request/Response mappers |
| **`infra`** | Driven adapters — JPA Persistence, Kafka producers/consumers, Feign clients |
| **`app`** | Entry point Spring Boot, cấu hình ứng dụng, Liquibase changelogs |

---

## 📂 Cấu trúc dự án

```
Intern-Hub-Ticket-Service/
├── api/                          # Driving adapters
│   └── src/main/java/.../api/
│       ├── controller/           # REST controllers
│       │   ├── TicketCommandController       # Tạo, phê duyệt ticket
│       │   ├── TicketQueryController         # Truy vấn ticket
│       │   ├── EvidenceController            # Upload/truy vấn evidence
│       │   ├── AdminTicketTypeCommandController  # Quản trị loại ticket
│       │   └── internal/
│       │       └── InternalTicketCommandController  # API nội bộ
│       └── dto/                  # Request/Response DTOs
├── core/                         # Domain layer
│   └── src/main/java/.../core/
│       ├── domain/
│       │   ├── model/            # Domain models & commands
│       │   ├── event/            # Domain events
│       │   ├── usecase/          # Business use cases
│       │   └── port/             # Interfaces (in/out/repository)
│       │       ├── in/           # Driving ports
│       │       ├── out/          # Driven ports
│       │       └── repository/   # Repository ports
│       └── exception/            # Custom domain exceptions
├── infra/                        # Driven adapters
│   └── src/main/java/.../infra/
│       ├── persistence/          # JPA entities & repositories
│       ├── service/              # Port implementations
│       ├── worker/               # Kafka consumer workers
│       ├── intergration/         # External service integrations
│       ├── configuration/        # Infra-specific config
│       ├── mapper/               # Entity-Model mappers
│       ├── model/                # Infra models
│       └── utils/                # Utilities
├── app/                          # Application entry point
│   └── src/main/resources/
│       ├── application.yml       # App configuration
│       ├── logback-spring.xml    # Logging config
│       └── db/changelog/         # Liquibase migrations
├── Dockerfile                    # Multi-stage Docker build
├── docker-compose.yml            # Local infrastructure
├── build.gradle.kts              # Root Gradle config
├── settings.gradle.kts           # Module definitions
└── gradle/libs.versions.toml     # Dependency version catalog
```

---

## 📡 API Endpoints

Base path: `/ticket`

### Ticket Commands

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/ticket` | Tạo ticket mới | `@Authenticated` |
| `POST` | `/ticket/{ticketId}/approve` | Phê duyệt ticket | `REVIEW:ticket` |

### Ticket Queries

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `GET` | `/ticket/all?page=&size=` | Lấy tất cả ticket (phân trang) | `READ:ticket` |
| `GET` | `/ticket/pending?page=&size=` | Lấy ticket đang chờ duyệt | `READ:ticket` |
| `GET` | `/ticket/{ticketId}` | Xem chi tiết ticket | `READ:ticket` |

### Evidence

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/ticket/{ticketId}/evidences` | Upload evidence cho ticket | `UPDATE:ticket` |
| `GET` | `/ticket/{ticketId}/evidences` | Lấy danh sách evidence | `READ:ticket` |

### Admin — Ticket Types

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/ticket/ticket-types` | Tạo loại ticket mới | `CREATE:ticket-type` |

### Internal API

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/ticket/internal` | Tạo ticket (internal) | `@Internal` |
| `GET` | `/ticket/internal/{ticketId}` | Xem chi tiết ticket (internal) | `@Internal` |

> 📖 **Swagger UI** có sẵn tại: `/swagger-ui.html` (khi `SWAGGER_ENABLED=true`)

---

## 🚦 Bắt đầu

### Yêu cầu

- **JDK 25** (Eclipse Temurin recommended)
- **Docker & Docker Compose**
- Gradle wrapper đã bao gồm trong dự án

### Biến môi trường

| Biến | Mặc định | Mô tả |
|---|---|---|
| `SERVER_PORT` | `8080` | Port REST API |
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `postgres` | Tên database |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `password` | Database password |
| `SCHEMA_NAME` | `public` | Database schema |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka broker addresses |
| `INTERNAL_SECRET_KEY` | `defaultSecretValue` | Secret key cho internal API |
| `GATEWAY_URL` | `http://localhost:8765` | URL Gateway service |
| `SWAGGER_ENABLED` | `true` | Bật/tắt Swagger UI |
| `DEPLOY_ENV` | `dev` | Môi trường deploy |

### Chạy local

1. **Khởi động PostgreSQL với Docker Compose:**
   ```bash
   docker-compose up -d
   ```

2. **Chạy ứng dụng:**
   ```bash
   ./gradlew :app:bootRun
   ```

3. **Truy cập API docs:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

### Build Docker image

```bash
docker build -t ticket-service .
docker run -p 8080:8080 ticket-service
```

---

## 🧪 Testing

```bash
# Chạy toàn bộ tests
./gradlew test

# Chạy test cho module cụ thể
./gradlew :core:test
./gradlew :api:test
./gradlew :infra:test
```

---

## 📦 Build

```bash
# Build toàn bộ project
./gradlew build

# Build bootJar cho deployment
./gradlew :app:bootJar
```

File JAR output: `app/build/libs/ticket-service.jar`

---

## 🔗 Intern Hub Ecosystem

Service này sử dụng các thư viện chung của hệ sinh thái Intern Hub:

| Thư viện | Phiên bản | Mô tả |
|---|---|---|
| [Intern-Hub-Common-Library](https://github.com/FPT-IS-Intern/Intern-Hub-Common-Library) | 2.1.1 | DTO chung, response wrapper, utilities |
| [Intern-Hub-Security-Starter](https://github.com/FPT-IS-Intern/Intern-Hub-Security-Starter) | 1.0.9 | Authentication, authorization, permission annotations |
| [Intern-Hub-Audit-Client](https://github.com/FPT-IS-Intern/Intern-Hub-Audit-Client) | 1.0.0 | Audit logging client |

---

*Developed as part of the **Intern Hub Platform** — FPT IS Intern*
