# Spring Boot Migration Design

**Date:** 2026-04-13  
**Project:** LazyCo  
**Status:** Approved

---

## Overview

Migrate LazyCo from a manually-configured Spring Framework 7 WAR application (deployed to external Tomcat) to a Spring Boot 4.x JAR application with embedded Tomcat.

**Approach:** In-place incremental migration — modify the existing project step-by-step so the app remains buildable and runnable at every stage.

---

## Decisions

| Decision | Choice | Reason |
|---|---|---|
| Deployment | JAR with embedded Tomcat | Standard Spring Boot approach, no external Tomcat needed |
| Spring Boot version | 4.x | Aligns with existing Spring Framework 7, Spring Security 7, Hibernate ORM 7 |
| Configuration strategy | Hybrid | Auto-config for simple infra (DB, Flyway, MongoDB, async, cache), keep custom config for complex pieces (security, MVC, interceptors, RBAC) |
| Property management | Spring Boot profiles | `application.properties` with Boot conventions; local overrides in `application-local.properties` (gitignored) |
| Logging | Migrate to Logback | Spring Boot default; replicate existing Log4j2 appenders and MDC fields in `logback-spring.xml` |

---

## Section 1: pom.xml Restructuring

**Changes:**
- Change `<packaging>` from `war` to `jar`
- Add Spring Boot 4.x BOM as a `<dependencyManagement>` import (not as `<parent>`) — the project has no existing parent, and this is less disruptive than switching to a Boot parent
- Replace individual Spring starters:
  - `spring-webmvc`, `spring-core`, `spring-context`, `spring-web`, `spring-expression`, `spring-context-support`, `spring-tx`, `spring-orm` → `spring-boot-starter-web`
  - `spring-security-web`, `spring-security-config` → `spring-boot-starter-security`
  - `spring-batch-core`, `spring-batch-infrastructure` → `spring-boot-starter-batch`
  - `spring-data-jpa`, `spring-data-commons` → `spring-boot-starter-data-jpa`
  - `spring-data-mongodb` → `spring-boot-starter-data-mongodb`
  - `spring-test` → `spring-boot-starter-test`
  - Add `spring-boot-starter-cache`
- Add `spring-boot-maven-plugin` to `<build><plugins>`
- Remove `jakarta.servlet-api` (provided by embedded Tomcat)
- Remove `log4j-bom`, all `log4j-*` dependencies, explicit `slf4j-api`
- Keep unchanged: `hibernate-platform` BOM (explicit version 7.x overrides Boot default), `hibernate-core`, `hibernate-envers`, `hibernate-jcache`, `hibernate-hikaricp`, `hibernate-validator`, `HikariCP`, `c3p0`, `postgresql`, `mongodb-driver-legacy`, `jedis`, `flyway-core`, `flyway-database-postgresql`, `ehcache`, `javax.xml.bind`/JAXB, `cache-api`, `lombok`, `mapstruct`, `modelmapper`, `aspectjweaver`, `gson`, `org.json`, `jackson-*`, `jjwt-*`, `quartz`, `jobrunr`, `javamelody-core`, `snakeyaml`, `jakarta.persistence-api`, `jakarta.activation-api`, `jakarta.validation-api`, `jakarta.mail`

**Note:** Keep the explicit `hibernate-platform` BOM import *above* the Spring Boot BOM so Hibernate 7.x takes precedence over whatever Boot 4.x defaults to.

---

## Section 2: Launcher + Remove web.xml

**Add:**
```
src/main/java/com/example/lazyco/LazyCoApplication.java
```
```java
@SpringBootApplication
public class LazyCoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LazyCoApplication.class, args);
    }
}
```

**Remove:**
- `src/main/webapp/WEB-INF/web.xml`
- `src/main/webapp/index.jsp`

**Modify `BackendWebConf.java`:**
- Remove `@PropertySources` / `@PropertySource` annotation — Boot handles property loading
- Remove `PropertySourcesPlaceholderConfigurer` `@Bean` if present
- Keep everything else: `@EnableWebMvc`, `@EnableWebSecurity`, `@EnableScheduling`, `@EnableAsync`, `@EnableAspectJAutoProxy`, `@EnableTransactionManagement`, `@ComponentScan`, all MVC wiring

**Note:** `@EnableWebMvc` on `BackendWebConf` intentionally disables Boot's MVC auto-config. This is desired — all interceptors, argument resolvers, and message converters remain under explicit control.

---

## Section 3: Property Migration

**`application.properties`** is rewritten using Spring Boot conventions:

| Old key pattern | New key |
|---|---|
| `${env.db.url}` | `spring.datasource.url` |
| `${env.db.username}` | `spring.datasource.username` |
| `${env.db.password}` | `spring.datasource.password` |
| `${env.hikari.*}` | `spring.datasource.hikari.*` |
| `${env.mongo.uri}` | `spring.data.mongodb.uri` |
| `${env.flyway.*}` | `spring.flyway.*` |
| `${env.mail.*}` | `spring.mail.*` |
| `${env.cache.*}` | `spring.cache.*` |

**Create:** `src/main/resources/application-local.properties`  
Contains actual local values (DB credentials, pool sizes, etc.)  
Add to `.gitignore`.

**Profile activation:**  
Set `SPRING_PROFILES_ACTIVE=local` as OS env var or in IDE run configuration.

**Unchanged:**  
App-specific custom property keys (non-infra) keep their names.

---

## Section 4: Auto-config Replacements (Hybrid)

### Delete — replaced by Boot auto-config + properties

| Class | Replaced by |
|---|---|
| `PostgresConfig.java` | `spring.datasource.*` + `spring.jpa.*` properties |
| `TransactionManagerConfig.java` | Boot auto-configures `JpaTransactionManager` |
| `FlywayConfig.java` | `spring.flyway.*` properties |
| `MongoConfig.java` | `spring.data.mongodb.*` properties |
| `AsyncExecutorConf/` | `spring.task.execution.*` properties |

### Keep — too custom for auto-config

| Class | Reason |
|---|---|
| `BackendWebConf.java` | Custom MVC, interceptors, argument resolvers, message converters |
| `SecurityConfig.java` + `SecurityBeansInjector.java` | Custom JWT authentication flow, RBAC |
| `Cache/` config | Keep `ehcache.xml` + cache region config; add `spring.cache.type=jcache` property |
| `QuartzScheduler/` | Custom Quartz job definitions |
| `DatabaseConf/ColumnOrderIntegrator.java` | Custom Hibernate column order integrator |
| `Email/` | Custom SMTP config |
| `Crypto/` | BouncyCastle — no Boot equivalent |
| `RateLimiter/` | Bucket4j config — no Boot equivalent |
| `GosnConf/`, `AspectConf/` | Custom beans |

---

## Section 5: Logging Migration (Log4j2 → Logback)

**Remove from pom.xml:** `log4j-bom`, `log4j-core`, `log4j-api`, `log4j-slf4j-impl`, explicit `slf4j-api`  
`spring-boot-starter-logging` (pulled in transitively) provides Logback + SLF4J.

**Delete:** `src/main/conf/log4j2.xml`

**Create:** `src/main/resources/logback-spring.xml`  
- Map all 10 rolling file appenders 1:1 from `log4j2.xml`
- MDC fields (`userId`, `requestId`, `sessionId`) via `%X{userId}` pattern — identical semantics in Logback
- Console appender scoped to `local` profile via `<springProfile name="local">`

**Call sites:** Must be migrated. Any class using `org.apache.logging.log4j.LogManager` / `org.apache.logging.log4j.Logger` directly will fail to compile once `log4j-api` is removed from the classpath. Each such class needs:
- `import org.apache.logging.log4j.LogManager;` → `import org.slf4j.LoggerFactory;`
- `import org.apache.logging.log4j.Logger;` → `import org.slf4j.Logger;`
- `LogManager.getLogger(Foo.class)` → `LoggerFactory.getLogger(Foo.class)`

This is a mechanical find-and-replace across all source files. Log method calls (`logger.info()`, `logger.debug()`, etc.) are identical in SLF4J and need no changes.

---

## Migration Order

Execute in this order — verify `mvn clean package` passes after each step:

1. `pom.xml` — add Boot BOM, swap starters, change packaging, remove log4j
2. Add `LazyCoApplication.java`, remove `web.xml` + `index.jsp`, clean `BackendWebConf.java`
3. Migrate properties — rewrite `application.properties`, create `application-local.properties`
4. Delete auto-config classes (`PostgresConfig`, `TransactionManagerConfig`, `FlywayConfig`, `MongoConfig`, `AsyncExecutorConf`)
5. Create `logback-spring.xml`, remove `log4j2.xml`
6. Run and smoke-test embedded Tomcat: `mvn spring-boot:run`

---

## What Does NOT Change

- All entity, DTO, DAO, service, controller classes
- MapStruct mappers
- All interceptors and RBAC logic
- Security/JWT implementation
- Custom argument resolvers
- Quartz/JobRunr job definitions
- Flyway migration scripts
- EhCache `ehcache.xml`
- All business logic
