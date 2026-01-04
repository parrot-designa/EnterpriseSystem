# LambdaQueryWrapper 使用指南

## 概述

`LambdaQueryWrapper` 是为 MyBatis 定制的轻量级查询构造器，提供类型安全的动态 SQL 构建能力，避免硬编码字段名，提升代码可维护性。

## 核心特性

- ✅ **类型安全**：使用 Lambda 表达式引用字段，编译期检查
- ✅ **重构友好**：IDE 支持字段重命名自动更新
- ✅ **动态条件**：根据参数值动态拼接 SQL
- ✅ **链式调用**：支持流式编程风格
- ✅ **零依赖**：无需引入 MyBatis-Plus，纯 MyBatis 也能使用

## 快速开始

### 1. 在 Mapper 中使用

```java
@Mapper
public interface UserMapper {

    // 方式一：使用 ${wrapper.sql} 替换 SQL 片段
    @Select("SELECT * FROM user ${wrapper.sql}")
    List<User> findByWrapper(@Param("wrapper") LambdaQueryWrapper<User> wrapper);

    // 方式二：使用 @SelectProvider
    @SelectProvider(type = UserSqlProvider.class, method = "findByWrapper")
    List<User> findByWrapperWithProvider(LambdaQueryWrapper<User> wrapper);

    // 方式三：在 XML 中使用
    List<User> selectByWrapper(LambdaQueryWrapper<User> wrapper);
}
```

### 2. 在 XML 中配置

```xml
<!-- UserMapper.xml -->
<mapper namespace="com.enterprisesystem.mapper.UserMapper">
    <select id="selectByWrapper" resultType="com.enterprisesystem.entity.User">
        SELECT * FROM user
        ${wrapper.sql}
    </select>
</mapper>
```

### 3. 调用示例

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> searchUsers(String name, Integer age) {
        // 创建查询构造器
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 动态添加条件
        wrapper.eq(User::getStatus, 1)              // 等于
               .like(StringUtils.isNotBlank(name), User::getName, name)  // 模糊查询（注意这里需要自己加条件判断）
               .gt(age != null, User::getAge, age)   // 大于（自定义条件判断）
               .orderByDesc(User::getCreateTime);    // 降序排序

        // 执行查询
        return userMapper.findByWrapper(wrapper);
    }
}
```

## API 方法列表

### 比较操作

| 方法 | 说明 | SQL 示例 |
|------|------|----------|
| `eq(column, value)` | 等于 | `column = ?` |
| `ne(column, value)` | 不等于 | `column != ?` |
| `gt(column, value)` | 大于 | `column > ?` |
| `ge(column, value)` | 大于等于 | `column >= ?` |
| `lt(column, value)` | 小于 | `column < ?` |
| `le(column, value)` | 小于等于 | `column <= ?` |

### 模糊查询

| 方法 | 说明 | SQL 示例 |
|------|------|----------|
| `like(column, value)` | 包含 | `column LIKE '%value%'` |
| `likeLeft(column, value)` | 左模糊 | `column LIKE '%value'` |
| `likeRight(column, value)` | 右模糊 | `column LIKE 'value%'` |

### 集合操作

| 方法 | 说明 | SQL 示例 |
|------|------|----------|
| `in(column, collection)` | IN 查询 | `column IN (?,?,?)` |
| `notIn(column, collection)` | NOT IN 查询 | `column NOT IN (?,?,?)` |
| `between(column, start, end)` | BETWEEN 查询 | `column BETWEEN ? AND ?` |

### 空值判断

| 方法 | 说明 | SQL 示例 |
|------|------|----------|
| `isNull(column)` | 为空 | `column IS NULL` |
| `isNotNull(column)` | 不为空 | `column IS NOT NULL` |

### 排序

| 方法 | 说明 | SQL 示例 |
|------|------|----------|
| `orderByAsc(column)` | 升序 | `ORDER BY column ASC` |
| `orderByDesc(column)` | 降序 | `ORDER BY column DESC` |

### 自定义

| 方法 | 说明 |
|------|------|
| `apply(sql, params...)` | 添加自定义 SQL 片段 |

## 完整使用示例

### 示例 1：基本查询

```java
// 查询年龄大于 18 岁的用户
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.gt(User::getAge, 18)
       .orderByDesc(User::getCreateTime);

List<User> users = userMapper.findByWrapper(wrapper);
// 生成 SQL: SELECT * FROM user WHERE age > ? ORDER BY create_time DESC
```

### 示例 2：多条件组合

```java
// 查询状态为 1、姓名包含 "张"、年龄在 18-30 之间的用户
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getStatus, 1)
       .like(User::getName, "张")
       .between(User::getAge, 18, 30)
       .orderByAsc(User::getId);

List<User> users = userMapper.findByWrapper(wrapper);
// 生成 SQL: SELECT * FROM user WHERE status = ? AND name LIKE ? AND age BETWEEN ? AND ? ORDER BY id ASC
```

### 示例 3：IN 查询

```java
// 查询 ID 在列表中的用户
List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L);

LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.in(User::getId, ids)
       .eq(User::getStatus, 1);

List<User> users = userMapper.findByWrapper(wrapper);
// 生成 SQL: SELECT * FROM user WHERE id IN (?,?,?,?,?) AND status = ?
```

### 示例 4：动态条件查询

```java
// 根据请求参数动态查询
public List<User> searchUsers(String name, Integer minAge, Integer maxAge, String email) {
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

    // 根据参数是否为空动态添加条件
    if (StringUtils.isNotBlank(name)) {
        wrapper.like(User::getName, name);
    }
    if (minAge != null) {
        wrapper.ge(User::getAge, minAge);
    }
    if (maxAge != null) {
        wrapper.le(User::getAge, maxAge);
    }
    if (StringUtils.isNotBlank(email)) {
        wrapper.eq(User::getEmail, email);
    }
    wrapper.eq(User::getStatus, 1)
           .orderByDesc(User::getCreateTime);

    return userMapper.findByWrapper(wrapper);
}
```

### 示例 5：自定义 SQL 片段

```java
// 使用 apply 添加自定义条件
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getStatus, 1)
       .apply("YEAR(create_time) = ?", 2024)
       .apply("MONTH(create_time) IN (1, 2, 3)");

List<User> users = userMapper.findByWrapper(wrapper);
// 生成 SQL: SELECT * FROM user WHERE status = ? AND YEAR(create_time) = ? AND MONTH(create_time) IN (1, 2, 3)
```

### 示例 6：分页查询

```java
// 结合分页使用
public PageResult<User> pageUsers(int page, int size, String keyword) {
    // 计算偏移量
    int offset = (page - 1) * size;

    // 构建查询条件
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.isNotBlank(keyword)) {
        wrapper.like(User::getName, keyword)
               .or()
               .like(User::getEmail, keyword);
    }
    wrapper.eq(User::getStatus, 1)
           .orderByDesc(User::getCreateTime());

    // 使用 apply 添加分页
    wrapper.apply("LIMIT ?, ?", offset, size);

    List<User> records = userMapper.findByWrapper(wrapper);
    int total = countByWrapper(wrapper); // 需要单独实现计数

    return new PageResult<>(records, total, page, size);
}
```

## 字段命名规则

### 驼峰转下划线

工具类会自动将实体类的驼峰命名转换为数据库的下划线命名：

| 实体类字段名 | 数据库字段名 |
|-------------|-------------|
| `userName` | `user_name` |
| `createTime` | `create_time` |
| `emailAddr` | `email_addr` |
| `isActive` | `is_active` |

### 自定义字段映射

如果实体类字段名与数据库字段名不遵循驼峰转下划线规则，可以使用 `LambdaUtils` 手动指定：

```java
// 使用 LambdaUtils 手动获取字段名
String columnName = LambdaUtils.getFieldName(User::getUserName, true); // user_name
```

## 注意事项

### 1. SQL 注入防护

使用 `#{wrapper.sql}` 而不是 `${wrapper.sql}` 可以防止 SQL 注入（视具体场景而定）：

```java
// 推荐：参数化查询
@Select("SELECT * FROM user WHERE id = #{id}")
User findById(Long id);

// 警惕：SQL 拼接（wrapper 本身已经做了参数化处理）
@Select("SELECT * FROM user ${wrapper.sql}")
List<User> findByWrapper(@Param("wrapper") LambdaQueryWrapper<User> wrapper);
```

### 2. 条件判断

当前版本的方法参数都是直接传入的，如果需要动态条件，需要手动判断：

```java
// ❌ 错误：如果 name 为 null，会生成 user_name LIKE '%%' 的无效条件
wrapper.like(User::getName, null);

// ✅ 正确：先判断再添加条件
if (StringUtils.isNotBlank(name)) {
    wrapper.like(User::getName, name);
}
```

### 3. 性能优化

```java
// ✅ 推荐：将常用查询方法封装到 Service 层
public List<User> findActiveUsers() {
    return userMapper.findByWrapper(
        new LambdaQueryWrapper<User>()
            .eq(User::getStatus, 1)
            .orderByDesc(User::getCreateTime)
    );
}
```

### 4. 字段类型限制

当前实现依赖标准的 getter 方法，实体类必须符合 JavaBean 规范：

```java
public class User {
    private Long id;
    private String name;

    // 必须提供标准的 getter 方法
    public Long getId() { return id; }
    public String getName() { return name; }
}
```

## 与 MyBatis-Plus 对比

| 特性 | LambdaQueryWrapper | MyBatis-Plus LambdaQueryWrapper |
|------|-------------------|--------------------------------|
| 依赖 | 无额外依赖 | 需要 MyBatis-Plus |
| 大小 | 轻量（3个类） | 较大（整个框架） |
| 功能 | 基础查询条件 | 更丰富的条件和方法 |
| 学习成本 | 低 | 中 |
| 适用场景 | 纯 MyBatis 项目 | 已使用 MyBatis-Plus |

## 常见问题

### Q1: 为什么 SQL 是 `${wrapper.sql}` 而不是 `#{wrapper.sql}`？

A: 因为 `wrapper.sql` 生成的是 SQL 片段（如 `WHERE name LIKE ?`），需要直接拼接到 SQL 中。参数值已经通过 `getParams()` 单独传递，因此不存在 SQL 注入风险。

### Q2: 如何获取生成的 SQL 和参数？

A: 可以调用对应的方法查看：

```java
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getId, 1)
       .like(User::getName, "张");

System.out.println("SQL: " + wrapper.getSql());
System.out.println("参数: " + wrapper.getParams());

// 输出:
// SQL:  WHERE id = ? AND name LIKE ?
// 参数: [1, %张%]
```

### Q3: 如何实现 OR 条件？

A: 当前版本暂不支持链式 OR，可以使用 `apply` 方法：

```java
wrapper.eq(User::getStatus, 1)
       .apply("(name LIKE ? OR email LIKE ?)", "%keyword%", "%keyword%");
```

### Q4: 支持嵌套条件吗？

A: 可以通过 `apply` 方法实现嵌套：

```java
wrapper.apply("(status = ? OR (status = ? AND create_time > ?))", 1, 2, "2024-01-01");
```

## 更新日志

- **v1.0.0** (2026-01-04)
  - 初始版本
  - 支持基本比较、模糊、IN、BETWEEN 等查询
  - 支持排序和自定义 SQL

## 贡献

如有问题或建议，欢迎提 Issue。
