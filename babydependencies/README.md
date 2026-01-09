# BabyDependencies Parent POM 使用指南

## 概述

`babydependencies` 是一个 Maven 父 POM，集中管理了项目中所有依赖的版本。它可以在任何新的或现有项目中作为 parent 使用，简化依赖管理。

## Maven 坐标

```xml
<groupId>com.enterprisesystem</groupId>
<artifactId>babydependencies</artifactId>
<version>1.0.0</version>
```

## 安装

### 本地安装

在当前项目中执行：

```bash
cd babydependencies
mvn clean install
```

这会将 POM 安装到本地 Maven 仓库（`~/.m2/repository/com/enterprisesystem/babydependencies/1.0.0/`）。

### 部署到远程仓库（可选）

如果需要与其他开发者共享，可以部署到私服：

```bash
mvn clean deploy
```

需要在 `pom.xml` 中配置分发管理：

```xml
<distributionManagement>
    <repository>
        <id>your-repo-id</id>
        <url>https://your-maven-repo.com/releases</url>
    </repository>
    <snapshotRepository>
        <id>your-snapshot-repo-id</id>
        <url>https://your-maven-repo.com/snapshots</url>
    </snapshotRepository>
</distributionManagement>
```

## 在项目中使用

### 方式一：作为父 POM（推荐）

在你的项目 `pom.xml` 中：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 引用 babydependencies 作为父 POM -->
    <parent>
        <groupId>com.enterprisesystem</groupId>
        <artifactId>babydependencies</artifactId>
        <version>1.0.0</version>
    </parent>

    <!-- 你的项目信息 -->
    <groupId>com.yourcompany</groupId>
    <artifactId>your-project</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <dependencies>
        <!-- 无需指定版本，由父 POM 管理 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- 更多依赖... -->
    </dependencies>
</project>
```

### 方式二：作为依赖导入

如果需要保留现有的父 POM（如 `spring-boot-starter-parent`），可以导入 BOM：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.enterprisesystem</groupId>
            <artifactId>babydependencies</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 管理的依赖

### 框架核心
- **Spring Boot**: 2.7.18
- **Spring Cloud**: 2021.0.5
- **Spring Cloud Alibaba**: 2021.0.5.0

### 数据库
- **MySQL Connector**: 8.0.26
- **MyBatis Spring Boot Starter**: 2.3.1

### 服务发现与配置
- **Nacos Discovery**: 通过 Spring Cloud Alibaba 管理
- **Nacos Config**: 通过 Spring Cloud Alibaba 管理

### 工具库
- **Apache Commons Lang3**: 3.19.0
- **Fastjson**: 1.2.83

### 消息队列
- **Spring Boot AMQP (RabbitMQ)**: 2.4.6

### 安全认证
- **Apache Shiro**: 1.8.0
- **JJWT**: 0.9.1

### Redis
- **Spring Data Redis Starter**: 2.7.18
- **Lettuce (Redis 客户端)**: 6.1.8.RELEASE
- **Commons Pool2**: 2.11.1
- **Redisson**: 3.17.7

### 其他
- **Spring Boot AOP**: 2.7.18

## 版本属性

所有版本号都定义在 `<properties>` 标签中，可以在子项目中覆盖：

```xml
<properties>
    <spring-boot.version>2.7.18</spring-boot.version>
    <mysql.version>8.0.26</mysql.version>
    <!-- 覆盖其他版本... -->
</properties>
```

## 使用示例

### 创建新的 Spring Boot 项目

```bash
# 使用 Maven Archetype 创建项目
mvn archetype:generate \
  -DgroupId=com.yourcompany \
  -DartifactId=your-project \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

cd your-project
```

然后修改 `pom.xml`，添加 `babydependencies` 作为父 POM。

### 验证配置

```bash
# 查看有效的 POM 配置
mvn help:effective-pom

# 查看依赖树
mvn dependency:tree
```

## 注意事项

1. **本地仓库**：确保 `babydependencies` 已安装到本地 Maven 仓库
2. **版本管理**：所有依赖版本由父 POM 统一管理，不要在子项目中重复指定版本
3. **Java 版本**：默认为 Java 8，如需修改请在子项目中覆盖 `maven.compiler.source` 和 `maven.compiler.target`
4. **编码**：默认使用 UTF-8 编码

## 更新和维护

### 更新依赖版本

在 `babydependencies/pom.xml` 中修改对应版本：

```xml
<properties>
    <spring-boot.version>2.7.19</spring-boot.version>
    <!-- 更新其他版本... -->
</properties>
```

然后重新安装：

```bash
mvn clean install
```

### 发布新版本

1. 修改 `babydependencies/pom.xml` 中的 `<version>` 标签
2. 执行 `mvn clean install` 或 `mvn clean deploy`
3. 在使用项目中更新 `<parent>` 的 `<version>` 标签

## 故障排查

### 找不到依赖

如果 Maven 无法找到 `babydependencies`：

1. 确认已执行 `mvn clean install`
2. 检查本地仓库：`ls ~/.m2/repository/com/enterprisesystem/babydependencies/`
3. 清理并重新构建：`mvn clean install -U`

### 版本冲突

如果遇到版本冲突：

1. 使用 `mvn dependency:tree` 查看依赖树
2. 使用 `mvn dependency:analyze` 分析未使用的依赖
3. 在子项目中覆盖特定版本

## 相关资源

- [Maven POM 参考](https://maven.apache.org/pom.html)
- [Spring Boot 依赖管理](https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/dependency-versions.html)
