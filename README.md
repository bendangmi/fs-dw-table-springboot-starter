# Feishu Bitable Spring Boot Starter

飞书多维表格（DuoWeiTable）Spring Boot Starter，提供请求/响应模型、实体映射助手与自动配置。
引入依赖、配置参数并在启动类上添加 @EnableFsDwTable 后即可调用接口，像使用数据库一样使用飞书多维表格。
英文文档见 `README_EN.md`。

## 项目愿景

- 将飞书多维表格作为数据源，像操作 MySQL 等数据库一样完成数据 CRUD
- 查询能力对标 MyBatis-Plus，使用 `DwLambdaQueryWrapper` 完成条件构造
- 屏蔽底层 Open API 调用，开发者只需引入依赖并填写飞书配置

## 架构设计

### 分层架构

```
┌─────────────────────────────────────────────────────────────┐
│                        应用层 (Application)                   │
│  FsDwRecordHelper / FsDwTableHelper / FsDwFieldHelper       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                        服务层 (Service)                      │
│     FsDwRecordService / FsDwTableService / FsDwFieldService │
│                      FsDwTokenService                        │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                         API层 (HTTP Client)                  │
│      FsDwRecordApi / FsDwTableApi / FsDwFieldApi             │
│                    (Forest 声明式 HTTP 客户端)                │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      飞书多维表格 Open API                    │
└─────────────────────────────────────────────────────────────┘
```

### 核心组件

| 组件 | 职责 | 说明 |
|------|------|------|
| `@EnableFsDwTable` | 启用自动配置 | 显式启用，避免与其他实现冲突 |
| `FsDwAutoConfiguration` | 自动配置类 | 注册所有核心 Bean 并扫描 Forest 接口 |
| `FsDwProperties` | 配置属性 | 绑定 `duoweitable.*` 配置项 |
| `FsDwTokenService` | Token 管理 | 使用 Caffeine 高性能缓存，支持并发和统计 |
| `DwLambdaQueryWrapper` | 查询构造器 | MyBatis-Plus 风格的 Lambda 类型安全查询 |
| `FsDwRecordHelper` | 记录操作助手 | 静态工具方法，便捷 CRUD 操作 |
| `BitableException` | 统一异常 | 携带错误码的异常体系 |

### 注解体系

```java
@FsDwTable(name = "表名", tableId = "tblxxx", viewId = "vewxxx")
public class Entity {
    @FsDwTableId                    // 标记记录 ID 字段
    private String recordId;

    @FsDwTableProperty(value = "字段名", order = 1, type = TypeEnum.TEXT)
    private String fieldName;
}
```

### HTTP 客户端

本项目使用 **Forest** 作为 HTTP 客户端，相比 RestTemplate/WebClient：
- 声明式接口定义，无需手写请求代码
- 自动 JSON 序列化/反序列化
- 支持拦截器、过滤器等扩展机制
- 底层使用 OkHttp，性能优异

### 缓存策略

**Token 缓存** 使用 Caffeine：
- 自动过期（2小时）
- 并发安全（基于 ConcurrentHashMap）
- LRU 淘汰策略（最多 100 个 token）
- 支持统计信息（命中率等）
- 提前 60 秒刷新缓冲

## 解决的问题

- 免去 Token 鉴权、请求构造、响应解析等“胶水工作”，业务层只保留 CRUD 语义
- 统一字段映射与类型转换，减少手写 JSON/Map 与字段名不一致的风险
- 提供类数据库的条件查询与分页能力，让数据访问更易维护、可读

## 项目亮点

- 通过 @EnableFsDwTable 显式启用自动配置 + Forest 扫描，避免与其他实现冲突
- 注解驱动实体映射（`@FsDwTable` / `@FsDwTableProperty` / `@FsDwTableId`），支持元字段读取
- `DwLambdaQueryWrapper` 提供 MyBatis-Plus 风格的条件、排序、分页与字段选择
- 记录/表/字段多层级 Helper 与 Service 组合，兼顾简单调用与可扩展集成
- Token 内存缓存 + 提前刷新缓冲，降低频繁鉴权请求
- 统一错误码与断言体系（`BitableException` / `BitableErrorCode`）

## 功能特性

- 启用注解后自动配置并扫描 Forest HTTP 客户端
- 注解驱动实体映射（`@FsDwTable` / `@FsDwTableProperty` / `@FsDwTableId`），支持元字段映射
- 类数据库的 CRUD 体验，屏蔽飞书多维表格底层调用
- MyBatis-Plus 风格查询，`DwLambdaQueryWrapper` 条件构造
- 记录、数据表、字段操作的 Helper API
- 统一异常与错误码（`BitableException` / `BitableErrorCode`）

## 运行环境

- Java 17+
- Spring Boot 3.x
- 飞书应用凭证（appId、appSecret、appToken）

## 安装

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

## 配置

最小配置（Helper API 必需）：

```yaml
duoweitable:
  app-id: your-app-id
  app-secret: your-app-secret
  app-token: your-app-token
```

`application.properties` 等价写法：

```properties
duoweitable.app-id=your-app-id
duoweitable.app-secret=your-app-secret
duoweitable.app-token=your-app-token
```

Forest 客户端可选配置（仅在需要覆盖时配置）：

```yaml
forest:
  backend: okhttp3
  connect-timeout: 5000
  read-timeout: 5000
  max-retry-count: 0
```

## 启用方式（3 步）

1) 引入依赖  
2) 配置参数  
3) 启动类加注解（不加注解不会加载 Starter，已禁用 AutoConfiguration.imports 自动装配）

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

## 注意事项（已使用 Forest 的项目）

如果你的项目已经使用了 Forest，并且自己加了 `@ForestScan`，请把 `cn.bdmcom.core.api` 加到
`basePackages`，否则 Starter 的接口不会被扫描到：

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

## 快速开始

### 1) 定义实体

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

### 2) 查询记录

```java
List<TestTable> records = FsDwRecordHelper.queryRecords(TestTable.class);
```

### 3) 新增记录

```java
TestTable add = new TestTable();
add.setName("Alice");
add.setAge(18);
AddRecordRes res = FsDwRecordHelper.addRecord(add);
```

### 4) 批量新增记录

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

### 5) 更新记录

```java
TestTable update = new TestTable();
update.setRecordId("recxxxxxx");
update.setName("Alice");
UpdateRecordRes res = FsDwRecordHelper.updateRecord(update);
```

### 6) 删除记录

```java
TestTable del = new TestTable();
del.setRecordId("recxxxxxx");
DeleteRecordRes res = FsDwRecordHelper.deleteRecord(del);
```

## 条件查询

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

## 数据表操作

```java
CreateTableRes created = FsDwTableHelper.createTable(TestTable.class);
List<ListTableRes.TableItem> all = FsDwTableHelper.listAllTables();
UpdateTableRes updated = FsDwTableHelper.updateTable(TestTable.class);
DeleteTableRes deleted = FsDwTableHelper.deleteTable(TestTable.class);
```

## 字段操作

```java
List<TableFieldListRes.TableField> fields = FsDwFieldHelper.listFields("tblxxxxx", "vewxxxxx");
AddFieldRes created = FsDwFieldHelper.createField(TestTable.class, new AddFieldReq("New Field", TypeEnum.TEXT));
UpdateFieldRes updated = FsDwFieldHelper.updateFieldByName(TestTable.class, "New Field",
    new UpdateFieldReq("New Field Name", TypeEnum.TEXT));
DeleteFieldRes deleted = FsDwFieldHelper.deleteFieldByName(TestTable.class, "New Field Name");
```

## 直接使用 Service Bean

如果希望使用 Spring 管理的 Service：

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

## 异常与错误码

所有 Helper/Service 方法在失败时会抛出 `BitableException`。
错误码与信息请参考 `BitableErrorCode`。

## 注意事项

### 1. 分页查询的性能限制

飞书多维表格的分页机制与传统的数据库分页（如 MySQL 的 OFFSET）不同：

- **顺序分页**：飞书使用 `page_token` 机制，必须从第一页开始顺序获取
- **性能影响**：查询第 N 页需要发起 N 次请求，对于大结果集会有性能问题
- **建议**：
  - 尽量使用查询条件缩小结果集范围
  - 避免跳转到很深的页码（如直接查第 1000 页）
  - 如果需要大数据量处理，考虑使用全量导出或其他方式

### 2. 双 API 设计说明

本项目提供了两种使用方式：

**静态 Helper API**：
```java
FsDwRecordHelper.queryRecords(TestTable.class);
```
- 优点：简洁便捷，无需注入
- 缺点：静态方法难以 mock，不利于单元测试

**Service Bean API**：
```java
@Autowired
private FsDwRecordService recordService;
```
- 优点：可测试、可 AOP 拦截、支持多租户
- 缺点：需要显式注入

**建议**：简单项目用 Helper，复杂项目或多租户场景用 Service。

### 3. 与现有 Forest 项目的集成

如果你的项目已经使用了 Forest 并自定义了 `@ForestScan`，需要确保将本项目的 API 包加入扫描路径：

```java
@ForestScan(basePackages = {
    "your.project.forest.clients",
    "cn.bdmcom.core.api"  // 必须包含
})
```

### 4. Lambda 查询的性能考虑

`DwLambdaQueryWrapper` 使用反射解析 Lambda 表达式获取字段名：
- 首次解析会有性能开销，字段名映射会被缓存
- 频繁创建新的 Wrapper 实例会有一定开销
- 建议复用 Wrapper 实例或使用字符串字段名

### 5. Token 缓存管理

Token 默认缓存 2 小时（提前 60 秒刷新），如需手动清除：

```java
@Autowired
private FsDwTokenService tokenService;

// 清除指定 token
tokenService.evictToken(appId, appSecret);

// 清除所有 token
tokenService.evictAll();

// 获取缓存统计
CacheStats stats = tokenService.getCacheStats();
```

### 6. 类型转换限制

飞书多维表格的字段类型与 Java 类型映射有限制：
- 日期时间：自动转换为 `LocalDateTime`/`LocalDate`
- 多选/单选：返回文本内容或数组
- 复杂嵌套结构可能需要手动处理

## 发布到 Maven Central

发布前请更新 `pom.xml` 元信息：

- `url`、`scm`、`developers`
- `groupId` 所有权与发布版本号

签名构建：

```sh
mvn -P release -DskipTests clean verify
```

发布需要配置 Sonatype 账号与 GPG 密钥，请根据官方流程完成发布。

## 许可证

Apache License, Version 2.0
