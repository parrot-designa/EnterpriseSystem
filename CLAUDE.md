# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **multi-module Maven enterprise application** built with Spring Boot 2.7.18 and Java 8. The project follows a modular architecture with clear separation of concerns across different business domains.

## Architecture

### Module Structure

The project is organized into these Maven modules:

1. **babystart** - Main application entry point (`BabyStartApplication.java`)
   - Aggregates all modules and provides unified startup
   - Contains `@SpringBootApplication` with component scanning for `com.enterprisesystem` package
   - MyBatis mapper scanning for `com.enterprisesystem.babymain.mapper`

2. **babymain** - Core business logic (Seller/商家 management)
   - REST endpoints at `/api/v3/seller` (`SellerController.java`)
   - Business logic in `SellerService` and `SellerServiceImpl`
   - Domain objects in `SellerDomain` and `SellerDomainManager`
   - MyBatis data access with SQL annotations in `SellerMapper`
   - Database: MySQL with `seller` table (see `schema.sql`)

3. **babysecure** - User management implementation
   - REST endpoints at `/api/v1/users` (`UserController.java`)
   - Comprehensive unit tests with Mockito (`UserServiceTest.java`)
   - Uses JPA repository pattern (`UserRepository`)

4. **babysecure-api** - API definitions and constants
   - Contains `SecureApiConstants.java` with API path constants
   - Provides interface definitions for the secure module

5. **babycommon** - Shared utilities and common components
   - `APIResult.java` - Standard API response wrapper
   - `ApiConstants.java` - Common API path constants
   - `BeanParam.java` - Custom annotations

### Key Design Patterns

- **Layered Architecture**: Controller → Service → Mapper/Repository
- **Domain-Driven Design**: Domain classes with domain managers
- **API Versioning**: `/api/v1/`, `/api/v2/`, `/api/v3/`, `/api/v4/` endpoints
- **Standardized Responses**: All APIs return `APIResult<T>` wrapper
- **MyBatis Data Access**: Annotation-based SQL mapping with snake_case to camelCase conversion

## Development Commands

### Build and Run

```bash
# Build all modules from root
mvn clean install

# Build specific module (e.g., babymain)
cd babymain && mvn clean install

# Run the application
cd babystart && mvn spring-boot:run

# Package as executable JAR
mvn clean package
```

### Testing

```bash
# Run all tests
mvn test

# Run tests for specific module
cd babysecure && mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with test coverage
mvn clean test jacoco:report
```

### Database

- **Database**: MySQL 8.0.26
- **Connection**: `jdbc:mysql://localhost:3306/wjb`
- **Credentials**: root/19970920 (configured in `babymain/src/main/resources/application.yml`)
- **Schema Initialization**: Automatic via `schema.sql` files
- **Table**: `seller` table with auto-increment ID, code, name, timestamps

### Development Setup

1. **Prerequisites**:
   - Java 8 JDK
   - Maven 3.6+
   - MySQL 8.0.26 running on localhost:3306
   - Database `wjb` created

2. **IDE Configuration**:
   - IntelliJ IDEA recommended (`.idea/` directory included)
   - Enable annotation processing for Lombok
   - Configure Spring Boot run configuration for `BabyStartApplication`

## API Structure

### Endpoint Patterns

- **User Management**: `/api/v1/users/**` (babysecure module)
- **Seller Management**: `/api/v3/seller/**` (babymain module)
- **Response Format**: All endpoints return `APIResult<T>` with `code`, `message`, `data`, `timestamp`

### Example API Call

```bash
# Get all sellers with pagination
GET /api/v3/seller/list?page=1&size=10

# Create new seller
POST /api/v3/seller/create
Content-Type: application/json
{
  "code": "SELLER001",
  "name": "Test Seller"
}
```

## Code Conventions

### Naming Patterns

- **Controllers**: `*Controller.java` (REST endpoints)
- **Services**: `*Service.java` (interface) and `*ServiceImpl.java` (implementation)
- **Mappers**: `*Mapper.java` (MyBatis data access)
- **Domain**: `*Domain.java` and `*DomainManager.java`
- **Models**: `*Entity.java` (database entities), `*Dto.java` (data transfer objects)

### Package Structure

```
com.enterprisesystem.{module}/
├── controller/     # REST controllers
├── service/        # Service interfaces
├── impl/           # Service implementations
├── domain/         # Domain objects
├── mapper/         # MyBatis mappers
├── model/          # DTOs and entities
├── config/         # Configuration classes
└── constant/       # Constants
```

## Important Files

- **Root POM**: `/pom.xml` - Parent project configuration
- **Main Application**: `/babystart/src/main/java/com/enterprisesystem/babystart/BabyStartApplication.java`
- **Database Config**: `/babymain/src/main/resources/application.yml`
- **Database Schema**: `/babymain/src/main/resources/schema.sql`
- **API Response Wrapper**: `/babycommon/src/main/java/com/enterprisesystem/babycommon/entity/APIResult.java`

## Testing Strategy

- **Unit Tests**: JUnit 5 with Mockito (see `UserServiceTest.java` for example)
- **Test Location**: `src/test/java/` in each module
- **Mocking**: Use `@Mock` and `@InjectMocks` annotations
- **Test Structure**: Given-When-Then pattern with comprehensive assertions

## Frontend Integration

- **Frontend Directory**: `/react-antd-admin/` (currently empty)
- **Planned**: React admin interface with Ant Design
- **API Integration**: Will consume the REST endpoints from backend modules

## Common Development Tasks

### Adding New Module

1. Create new directory with `pom.xml`
2. Add module to root `pom.xml` `<modules>` section
3. Follow existing package structure patterns
4. Add dependencies in `babystart/pom.xml` if needed by main app

### Adding New API Endpoint

1. Create controller in appropriate module
2. Implement service layer with business logic
3. Add data access via MyBatis mapper
4. Use `APIResult<T>` for responses
5. Follow versioning pattern (`/api/vX/`)

### Database Changes

1. Update `schema.sql` in relevant module
2. Create/update MyBatis mapper with SQL annotations
3. Update entity classes in `model/` package
4. Test with existing data access patterns

## Troubleshooting

### Common Issues

1. **Database Connection**: Verify MySQL is running on localhost:3306 with `wjb` database
2. **Lombok Annotations**: Ensure IDE has annotation processing enabled
3. **Module Dependencies**: Check `pom.xml` files for correct version references
4. **MyBatis Mapper Scanning**: Verify `@MapperScan` in `BabyStartApplication` includes correct package

### Logging

- MyBatis SQL logging: `logging.level.com.enterprisesystem.babymain.mapper: debug`
- Configure in `application.yml` for development debugging