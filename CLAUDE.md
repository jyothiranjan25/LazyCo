# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
# Build the project
mvn clean install

# Run tests only
mvn test

# Run a single test class
mvn test -Dtest=ClassName

# Run integration tests
mvn verify

# Apply code formatting (Google Java Format — run before committing)
mvn spotless:apply

# Check formatting without applying
mvn spotless:check

# Package as WAR
mvn package
```

The project deploys as a WAR file to a servlet container (Tomcat). There is no embedded server; deploy `target/LazyCo.war` to a running Tomcat instance.

**JobRunr dashboard** runs on port 8002 when the application is running.

## Project Structure & Architecture

**Stack:** Java 22, Spring Framework 7 (MVC, Security, Data), Hibernate ORM 7, PostgreSQL (primary), MongoDB (secondary), EhCache 3, Flyway, Log4j2

**Entry point:** `BackendWebConf.java` — the main Spring `@Configuration` class wiring MVC, Security, Scheduling, Async, Transactions, and AOP.

**Layered architecture per module:**
```
entities/{ModuleName}/
├── {Entity}.java          — JPA entity (extends AbstractModel or AbstractRBACModel)
├── {Entity}DTO.java       — API contract DTO
├── I{Entity}DAO.java      — DAO interface (extends IAbstractDAO)
├── {Entity}DAOImpl.java   — Hibernate Criteria-based implementation
├── I{Entity}Service.java  — Service interface (extends IAbstractService)
├── {Entity}ServiceImpl.java
└── {Entity}Controller.java — REST controller (extends AbstractController)
```

**Request lifecycle:**
`DispatcherServlet → Filters (RequestCache → RequestProcessing → Security) → Interceptors (RateLimit → RestController → Login → Role) → Controller → Service → DAO`

**core/ package** contains all infrastructure:
- `WebMVC/` — Spring config, security, JWT, interceptors, filters, RBAC
- `DatabaseConf/` — PostgreSQL, MongoDB, Flyway, HikariCP, transaction config
- `AbstractClasses/` — Base classes all entities/DTOs/DAOs/controllers extend
- `Cache/` — EhCache configuration wiring
- `QuartzScheduler/` — Scheduled job definitions
- `Email/` — SMTP email service
- `RateLimiter/` — Bucket4j rate limiting
- `Crypto/` — BouncyCastle encryption utilities

## Key Conventions

**Entities** extend `AbstractModel` (base fields: id, createdAt, updatedAt) or `AbstractRBACModel` (adds ownership/role fields). Hibernate Envers audits all entity changes — audit tables get `_AUD` suffix.

**DAOs** use a custom Criteria Builder wrapper (not JPQL/HQL). Look at existing DAO implementations before writing queries.

**DTOs** are mapped via MapStruct (compile-time). Add `@Mapper` interfaces alongside DTOs; MapStruct generates implementations during `mvn compile`.

**RBAC** is enforced in `RoleControllerInterceptor` via `@EnableWebSecurity`. Public endpoints are registered in `EndpointRegistry.java`.

**Caching** uses Hibernate second-level cache + query cache (EhCache). Entity caching strategy is `READ_WRITE`.

## Database

- **Migrations:** Flyway, located at `src/main/dbResources/migration/`. Files follow `V{n}__{description}.sql` naming. Flyway auto-validates on startup.
- **Schema auto-update:** Hibernate `hbm2ddl.auto` is set in the `env` file (default: `update` for local dev).
- **Local setup:** PostgreSQL on `localhost:5432`, database name `lazyCo`.

## Configuration

- `src/main/resources/application.properties` — main config (references `${env.*}` variables)
- `env` file — local environment overrides (HikariCP pool, DB credentials, etc.)
- `src/main/conf/ehcache.xml` — cache region definitions
- `src/main/conf/log4j2.xml` — logging (10 rolling file appenders, MDC includes userId/requestId/sessionId)

## Testing

Tests are under `src/test/java/com/example/lazyco/`. Integration tests extend the Spring test context and hit a real PostgreSQL database — do not mock the database layer.
