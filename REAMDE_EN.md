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
  <version>1.0.0</version>
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
  @Resource
  private FsDwRecordService recordService;

  public QueryRecordRes query(String appId, String appSecret, String appToken, String tableId, QueryRecordReq req) {
    return recordService.queryRecord(appId, appSecret, appToken, tableId, req);
  }
}
```

## Error Handling

All helper/service methods throw `BitableException` on failures. Check
`BitableErrorCode` for consistent error codes and messages.

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
