# 05 — Infra Module

## Role

The **infra** module is the **driven adapter**. It implements all core port interfaces using concrete technologies: JPA, Redis, Kafka, Feign, password hashers, configuration bindings.

**Package**: `com.intern.hub.auth.infra`

## Rules

- Every class here implements a **core port interface**
- JPA entities **never leak into core** — always map to domain models via MapStruct
- `@Transactional` belongs here (or in the API layer), never in core
- Annotate implementations with `@Service`, `@Component`, or `@Repository`

## Package Layout

```
infra/
├── persistence/
│   ├── entity/             # JPA @Entity classes
│   │   └── converter/      # Custom JPA converters (JSON)
│   └── repository/
│       ├── jpa/            # Spring Data JPA interfaces
│       └── impl/           # Core port implementations wrapping JPA repos
├── service/                # Core port implementations (Redis, Kafka, Feign, etc.)
├── configuration/          # @ConfigurationProperties, @Configuration beans
├── mapper/                 # MapStruct mappers (Entity ↔ Domain Model)
├── model/                  # Infra-only models (e.g., Feign response models)
├── feign/                  # Feign client interfaces
├── intergration/           # Password hasher implementations (typo — keep as-is)
└── utils/                  # Utility classes (TokenHelper, OtpHelper)
```

## Dependencies

```kotlin
dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.custom.libraries)
    implementation(libs.bundles.spring.boot.database)   // JPA + PostgreSQL + Liquibase
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.starter.feign)
    implementation(libs.spring.boot.starter.kafka)
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
    implementation("commons-fileupload:commons-fileupload:1.6.0")
    implementation("org.apache.commons:commons-pool2:2.13.1")
}
```

## How to Create a JPA Entity

```java
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "my_table")
public class MyEntity extends AuditEntity {   // AuditEntity from security-starter

    @Id
    Long id;                         // Snowflake ID — never auto-increment

    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)     // enums stored as uppercase strings
    @Builder.Default
    @Column(nullable = false)
    MyStatus status = MyStatus.ACTIVE;

    @JdbcTypeCode(SqlTypes.JSON)     // for JSONB columns
    @Column(columnDefinition = "jsonb")
    @Convert(converter = JpaConverterJson.class)
    Map<String, Object> metadata;

    @Version                         // optimistic locking
    @Column(nullable = false)
    Integer version;
}
```

## How to Create a JPA Repository

```java
public interface MyEntityJpaRepository extends JpaRepository<MyEntity, Long> {
    MyEntity findByName(String name);
    boolean existsByName(String name);
    List<MyEntity> findAllByStatus(MyStatus status);
}
```

## How to Create a MapStruct Mapper

```java
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyMapper {

    MyModel toModel(MyEntity entity);
    MyEntity toEntity(MyModel model);
    List<MyModel> toModels(List<MyEntity> entities);

    @AfterMapping
    default void mapAuditFields(MyModel model, @MappingTarget MyEntity entity) {
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setVersion(model.getVersion());
    }
}
```

The `@AfterMapping` is needed because `AuditEntity` fields don't auto-map when going Model → Entity.

## How to Implement a Repository Port

```java
@Component
public class MyRepositoryImpl implements MyRepository {   // implements core port

    private final MyEntityJpaRepository jpaRepository;
    private final MyMapper mapper;

    public MyRepositoryImpl(MyEntityJpaRepository jpaRepository, MyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public MyModel findById(Long id) {
        return mapper.toModel(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public MyModel save(MyModel model) {
        return mapper.toModel(jpaRepository.save(mapper.toEntity(model)));
    }
}
```

Pattern: JPA repo → Entity → Mapper → Domain Model. Core never sees entities.

## How to Implement a Redis-Backed Port

```java
@Service
@RequiredArgsConstructor
public class MyCacheImpl implements MyCache {   // implements core port

    private static final String KEY_PREFIX = "my-feature:";
    private final StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(KEY_PREFIX + key, value, Duration.ofMinutes(30));
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + key));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(KEY_PREFIX + key);
    }
}
```

**Important**: All Redis keys are auto-prefixed with `auth:` by `RedisConfiguration`. So `my-feature:abc` becomes `auth:my-feature:abc` in Redis.

## How to Implement a Kafka Producer Port

```java
@Service
public class MyEventServiceImpl implements MyEventService {   // implements core port

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Snowflake snowflake;

    // constructor ...

    @Override
    public void publishEvent(Long entityId, String data) {
        kafkaTemplate.send("auth.my-entity.event",
            new MyEventPayload(snowflake.next(), entityId, data));
    }

    private record MyEventPayload(Long eventId, Long entityId, String data) {}
}
```

Topic naming pattern: `{service}.{entity}.{event-type}`

## How to Add a Kafka Consumer

```java
@Component
@RequiredArgsConstructor
public class MyEventListener {

    @KafkaListener(topics = "some.topic.name", groupId = "auth-service")
    public void handle(String message) {
        // Parse and process
    }
}
```

`KafkaConsumerConfiguration` is already set up. Currently no active consumers exist.

## How to Create a Feign Client

```java
@FeignClient(name = "my-service", url = "${feign.client.config.my-service.url}")
public interface MyServiceFeignClient {
    @GetMapping("/my-service/internal/some-resource/{id}")
    ResponseApi<MyFeignResponse> getById(@PathVariable("id") Long id);
}
```

Then add URL config in `application.yml`:
```yaml
feign:
  client:
    config:
      my-service:
        url: ${MY_SERVICE_URL:http://localhost:8082}
```

`FeignConfiguration` automatically adds `X-Internal-Secret` header to **all** outgoing Feign requests.

## How to Implement a Config Port

```java
@Getter @Setter
@Component
@ConfigurationProperties(prefix = "my-feature")
public class MyFeatureProperties implements MyFeatureConfig {   // implements core port
    private int maxRetries = 3;
    private long timeoutSeconds = 300;
}
```

Then add values in `application.yml`:
```yaml
my-feature:
  max-retries: 3
  timeout-seconds: 300
```

## How to Implement a Password Hasher

```java
@Component("myPasswordHasher")
public class MyPasswordHasher implements PasswordHasher {
    @Override
    public String hash(String rawPassword) { /* ... */ }
    @Override
    public boolean verify(String rawPassword, String hashedPassword) { /* ... */ }
    @Override
    public String algorithm() { return "my-algorithm"; }
}
```

Inject with `@Qualifier("myPasswordHasher")`. Primary hasher is `bcryptPasswordHasher`.

## Known Inconsistencies

- Package `intergration` is a typo for `integration` — don't rename it, don't copy the typo for new packages
- `VerificationRequestStoreImpl` uses custom string serialization instead of JSON — use JSON for new stores
- Inline dependency versions in `build.gradle.kts` (`commons-fileupload`, `commons-pool2`) — should be in version catalog
