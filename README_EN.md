# Feishu Bitable Spring Boot Starter

Spring Boot starter for Feishu Bitable (DuoWeiTable) APIs. It provides typed
request/response models, entity mapping helpers, and auto-configuration so
consumers can add a dependency, set configuration, enable the starter with
@EnableFsDwTable, and start calling APIs like they would with a database.
Chinese version: `README.md`.

## Vision

- Use Feishu Bitable as a data source and offer database-like CRUD operations
- Provide MyBatis-Plus-style querying with `DwLambdaQueryWrapper`
- Hide Open API details so developers only need dependencies + configuration

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Application Layer                        │
│  FsDwRecordHelper / FsDwTableHelper / FsDwFieldHelper       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                        Service Layer                         │
│     FsDwRecordService / FsDwTableService / FsDwFieldService │
│                      FsDwTokenService                        │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                         API Layer (HTTP Client)              │
│      FsDwRecordApi / FsDwTableApi / FsDwFieldApi             │
│              (Forest Declarative HTTP Client)                │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Feishu Bitable Open API                   │
└─────────────────────────────────────────────────────────────┘
```

### Core Components

| Component | Responsibility | Description |
|-----------|---------------|-------------|
| `@EnableFsDwTable` | Enable Auto-Configuration | Explicit enablement to avoid conflicts |
| `FsDwAutoConfiguration` | Auto-Configuration | Registers core beans and scans Forest interfaces |
| `FsDwProperties` | Configuration Properties | Binds `duoweitable.*` config entries |
| `FsDwTokenService` | Token Management | Caffeine-based high-performance caching with concurrency support and stats |
| `DwLambdaQueryWrapper` | Query Builder | MyBatis-Plus style Lambda type-safe queries |
| `FsDwRecordHelper` | Record Operation Helper | Static utility methods for convenient CRUD |
| `BitableException` | Unified Exception | Exception hierarchy with error codes |

### Annotation System

```java
@FsDwTable(name = "Table Name", tableId = "tblxxx", viewId = "vewxxx")
public class Entity {
    @FsDwTableId                    // Marks record ID field
    private String recordId;

    @FsDwTableProperty(value = "Field Name", order = 1, type = TypeEnum.TEXT)
    private String fieldName;
}
```

### HTTP Client

This project uses **Forest** as the HTTP client, compared to RestTemplate/WebClient:
- Declarative interface definitions without manual request code
- Automatic JSON serialization/deserialization
- Support for interceptors, filters, and other extensions
- Built on OkHttp for excellent performance

### Caching Strategy

**Token Cache** uses Caffeine:
- Automatic expiration (2 hours)
- Thread-safe (based on ConcurrentHashMap)
- LRU eviction policy (max 100 tokens)
- Statistics support (hit rate, etc.)
- 60-second refresh buffer

## Problems Solved

- Removes token auth, request building, and response parsing boilerplate
- Unifies field mapping and type conversion to avoid JSON/Map mismatches
- Adds database-like filtering and pagination semantics for maintainable queries

## Highlights

- Explicit enablement via @EnableFsDwTable to avoid conflicts with other
  implementations
- Annotation-driven entity mapping (`@FsDwTable` / `@FsDwTableProperty` / `@FsDwTableId`)
- `DwLambdaQueryWrapper` for conditions, sorting, pagination, and field selection
- Layered Helper + Service APIs for quick use and extensible integration
- In-memory token cache with refresh buffer to reduce auth calls
- Unified error handling (`BitableException` / `BitableErrorCode`)

## Features

- Auto-configuration with `@ForestScan` for API clients (enabled by annotation)
- Annotation-driven entity mapping (`@FsDwTable` / `@FsDwTableProperty` / `@FsDwTableId`)
- Database-like CRUD and MyBatis-Plus-style queries with `DwLambdaQueryWrapper`
- Helper APIs for record, table, and field operations
- Error handling with unified `BitableException` and `BitableErrorCode`

## Requirements

- Java 17+
- Spring Boot 3.x
- Feishu app credentials (appId, appSecret, appToken)

## Installation

Maven:

```xml
<dependency>
  <groupId>cn.bdmcom</groupId>
  <artifactId>fs-dw-table-springboot-starter</artifactId>
  <version>1.0.2</version>
</dependency>
```

Gradle:

```kotlin
dependencies {
  implementation("cn.bdmcom:fs-dw-table-springboot-starter:1.0.0")
}
```

## Configuration

Minimal configuration (required for helper APIs):

```yaml
duoweitable:
  app-id: your-app-id
  app-secret: your-app-secret
  app-token: your-app-token
```

`application.properties` equivalent:

```properties
duoweitable.app-id=your-app-id
duoweitable.app-secret=your-app-secret
duoweitable.app-token=your-app-token
```

Optional Forest client tuning (only if you need overrides):

```yaml
forest:
  backend: okhttp3
  connect-timeout: 5000
  read-timeout: 5000
  max-retry-count: 0
```

## Enablement (3 steps)

1) Add dependency  
2) Configure properties  
3) Add the annotation on your Spring Boot application class

AutoConfiguration.imports auto-enable is intentionally disabled.

```java
import cn.bdmcom.annotation.EnableFsDwTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFsDwTable
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
```

## Note (projects already using Forest)

If your project already uses Forest and defines its own `@ForestScan`, make sure
to include `cn.bdmcom.core.api` in `basePackages`, otherwise the starter clients
will not be registered:

```java
import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ForestScan(basePackages = {
  "your.project.forest.clients",
  "cn.bdmcom.core.api"
})
public class ForestClientConfig {
}
```

## Quick Start

### 1) Define an entity

```java
@FsDwTable(name = "Test Table", tableId = "tblxxxxx", viewId = "vewxxxxx")
public class TestTable {
  @FsDwTableId
  private String recordId;

  @FsDwTableProperty(value = "Name", order = 1, type = TypeEnum.TEXT)
  private String name;

  @FsDwTableProperty(value = "Age", order = 2, type = TypeEnum.NUMBER)
  private Integer age;
}
```

### 2) Query records

```java
List<TestTable> records = FsDwRecordHelper.queryRecords(TestTable.class);
```

### 3) Add a record

```java
TestTable add = new TestTable();
add.setName("Alice");
add.setAge(18);
AddRecordRes res = FsDwRecordHelper.addRecord(add);
```

### 4) Batch add records

```java
List<TestTable> batch = new ArrayList<>();
for (int i = 0; i < 10; i++) {
  TestTable item = new TestTable();
  item.setName("User-" + i);
  item.setAge(18);
  batch.add(item);
}
BatchCreateRecordRes batchRes = FsDwRecordHelper.batchCreateRecords(TestTable.class, batch);
```

### 5) Update a record

```java
TestTable update = new TestTable();
update.setRecordId("recxxxxxx");
update.setName("Alice");
UpdateRecordRes res = FsDwRecordHelper.updateRecord(update);
```

### 6) Delete a record

```java
TestTable del = new TestTable();
del.setRecordId("recxxxxxx");
DeleteRecordRes res = FsDwRecordHelper.deleteRecord(del);
```

## Query With Conditions

```java
DwLambdaQueryWrapper<TestTable> wrapper = new DwLambdaQueryWrapper<>();
wrapper.eq(TestTable::getName, "Alice");
List<TestTable> list = FsDwRecordHelper.queryRecords(TestTable.class, wrapper);

wrapper = new DwLambdaQueryWrapper<>();
wrapper.between(TestTable::getAge, 18, 30);
list = FsDwRecordHelper.queryRecords(TestTable.class, wrapper);

wrapper = new DwLambdaQueryWrapper<>();
wrapper.pageSize(20);
wrapper.pageNo(2);
list = FsDwRecordHelper.queryRecords(TestTable.class, wrapper);
```

## Table Operations

```java
CreateTableRes created = FsDwTableHelper.createTable(TestTable.class);
List<ListTableRes.TableItem> all = FsDwTableHelper.listAllTables();
UpdateTableRes updated = FsDwTableHelper.updateTable(TestTable.class);
DeleteTableRes deleted = FsDwTableHelper.deleteTable(TestTable.class);
```

## Field Operations

```java
List<TableFieldListRes.TableField> fields = FsDwFieldHelper.listFields("tblxxxxx", "vewxxxxx");
AddFieldRes created = FsDwFieldHelper.createField(TestTable.class, new AddFieldReq("New Field", TypeEnum.TEXT));
UpdateFieldRes updated = FsDwFieldHelper.updateFieldByName(TestTable.class, "New Field",
    new UpdateFieldReq("New Field Name", TypeEnum.TEXT));
DeleteFieldRes deleted = FsDwFieldHelper.deleteFieldByName(TestTable.class, "New Field Name");
```

## Using Service Beans Directly

If you prefer Spring-managed services:

```java
@Service
public class DemoService {
  @Autowired
  private FsDwRecordService recordService;

  public QueryRecordRes query(String appId, String appSecret, String appToken, String tableId, QueryRecordReq req) {
    return recordService.queryRecord(appId, appSecret, appToken, tableId, req);
  }
}
```

## Error Handling

All helper/service methods throw `BitableException` on failures. Check
`BitableErrorCode` for consistent error codes and messages.

## Important Notes

### 1. Pagination Performance

Feishu Bitable's pagination mechanism differs from traditional database pagination (e.g., MySQL OFFSET):

- **Sequential Pagination**: Feishu uses `page_token` mechanism requiring sequential fetch from page 1
- **Performance Impact**: Querying page N requires N requests, which is problematic for large result sets
- **Recommendations**:
  - Use query conditions to narrow down result sets
  - Avoid jumping to deep pages (e.g., directly querying page 1000)
  - Consider full export for large data processing

### 2. Dual API Design

This project provides two usage patterns:

**Static Helper API**:
```java
FsDwRecordHelper.queryRecords(TestTable.class);
```
- Pros: Concise, no injection needed
- Cons: Hard to mock, not unit-test friendly

**Service Bean API**:
```java
@Autowired
private FsDwRecordService recordService;
```
- Pros: Testable, AOP-capable, multi-tenant support
- Cons: Requires explicit injection

**Recommendation**: Use Helper for simple projects, Service for complex/multi-tenant scenarios.

### 3. Integration with Existing Forest Projects

If your project already uses Forest with custom `@ForestScan`, ensure this project's API package is included:

```java
@ForestScan(basePackages = {
    "your.project.forest.clients",
    "cn.bdmcom.core.api"  // Must include
})
```

### 4. Lambda Query Performance

`DwLambdaQueryWrapper` uses reflection to parse Lambda expressions for field names:
- First-time parsing has overhead; field name mappings are cached
- Frequent Wrapper instance creation has some overhead
- Consider reusing Wrapper instances or using string field names

### 5. Token Cache Management

Tokens are cached for 2 hours by default (with 60-second refresh buffer). Manual eviction:

```java
@Autowired
private FsDwTokenService tokenService;

// Evict specific token
tokenService.evictToken(appId, appSecret);

// Evict all tokens
tokenService.evictAll();

// Get cache statistics
CacheStats stats = tokenService.getCacheStats();
```

### 6. Type Conversion Limitations

Feishu Bitable field type to Java type mapping has limitations:
- Date/Time: Auto-converted to `LocalDateTime`/`LocalDate`
- Multi-select/Single-select: Returns text content or arrays
- Complex nested structures may require manual handling

## Publishing (Maven Central)

Before publishing, update the metadata in `pom.xml`:

- `url`, `scm`, and `developers` sections
- `groupId` ownership and release `version`

To sign artifacts:

```sh
mvn -P release -DskipTests clean verify
```

You also need Sonatype credentials and GPG keys configured in your Maven
settings. Consult Maven Central publishing docs for your preferred workflow.

## License

Apache License, Version 2.0
