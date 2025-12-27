# PromptDB Auth Service

A comprehensive authentication and authorization microservice built with Spring Boot and React. This project provides JWT-based authentication, user management, and company administration functionality.

## Overview

The Auth Service is the entry layer for the PromptDB system, handling:
- User authentication and login
- JWT token generation and management
- User and company management
- Role-based access control
- Bearer token management
- Health check monitoring

## Project Structure

```
auth/
├── src/
│   ├── main/
│   │   ├── java/com/promptdb/auth/          # Backend Java code
│   │   │   ├── controllers/                  # REST API endpoints
│   │   │   ├── services/                     # Business logic
│   │   │   ├── models/                       # Entity models
│   │   │   ├── repository/                   # Data access layer
│   │   │   ├── filter/                       # JWT authentication filters
│   │   │   ├── config/                       # Spring configuration
│   │   │   ├── dto/                          # Data transfer objects
│   │   │   ├── exceptions/                   # Custom exceptions
│   │   │   ├── exceptionHandlers/            # Global exception handling
│   │   │   ├── authenticatonProviders/       # Authentication providers
│   │   │   ├── token/                        # Token utilities
│   │   │   └── utils/                        # Utility classes
│   │   └── resources/
│   │       ├── application.yaml              # Spring configuration
│   │       ├── logback.xml                   # Logging configuration
│   │       ├── db/migration/                 # Flyway database migrations
│   │       └── templates/                    # Email templates
│   ├── frontend/
│   │   └── auth-web/                         # React TypeScript frontend
│   │       ├── src/                          # React components and pages
│   │       ├── public/                       # Static assets
│   │       ├── package.json                  # Frontend dependencies
│   │       └── vite.config.ts               # Vite configuration
│   └── test/
│       └── java/                             # Unit and integration tests
├── docker/
│   └── nginx/                                # Nginx reverse proxy
├── pom.xml                                   # Maven configuration
└── logs/                                     # Application logs

```

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.3.5
- **Language**: Java 17
- **Security**: Spring Security with JWT
- **Database**: MySQL 9.0
- **ORM**: Spring Data JPA
- **Migrations**: Flyway
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18.3.1
- **Language**: TypeScript
- **Build Tool**: Vite
- **UI Libraries**: Material-UI (MUI), React Bootstrap
- **HTTP Client**: Axios
- **Routing**: React Router v7
- **Icons**: React Icons
- **Styling**: Emotion (CSS-in-JS)

### DevOps
- **Containerization**: Docker
- **Reverse Proxy**: Nginx

## Getting Started

### Prerequisites
- Java 17+
- Node.js 16+ (for frontend)
- MySQL 5.7+ or higher
- Maven 3.6+
- Docker (optional)

### Backend Setup

1. **Configure Database**
   - Update `src/main/resources/application.yaml` with your MySQL credentials
   - Default connection: `jdbc:mysql://0.0.0.0:3306/auth`

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   The server will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to Frontend Directory**
   ```bash
   cd src/frontend/auth-web
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Development Server**
   ```bash
   npm run dev
   ```
   The frontend will be available at `http://localhost:5173`

4. **Build for Production**
   ```bash
   npm run build
   ```

## API Endpoints

### Authentication
- `POST /auth/login` - User login
- `POST /auth/logout` - User logout
- `POST /auth/refresh` - Refresh JWT tokens

### Users
- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create new user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### Companies
- `GET /companies` - Get all companies
- `GET /companies/{id}` - Get company by ID
- `POST /companies` - Create new company
- `PUT /companies/{id}` - Update company
- `DELETE /companies/{id}` - Delete company

### Health Check
- `GET /health` - Service health status

## Configuration

### Application Properties
The application uses `application.yaml` for configuration:

```yaml
spring:
  application:
    name: auth
  datasource:
    url: jdbc:mysql://host:port/database
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8080
```

### Environment Variables
Configure the following environment variables for production:
- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret
- `JWT_EXPIRATION` - Token expiration time

## Authentication Flow

1. **Login Request**: User submits credentials to `/auth/login`
2. **Credential Validation**: Spring Security validates against stored credentials
3. **Token Generation**: JWT tokens (access and refresh) are generated
4. **Cookie Setup**: Access token is stored in HTTP-only cookie
5. **Response**: User receives tokens and cookie for subsequent requests
6. **Subsequent Requests**: JWTAuthorizationFilter validates token on each request

## Spring Security Setup

### Security Configuration Overview

The application implements a stateless JWT-based authentication system using Spring Security. The `SecurityConfig` class provides comprehensive security configuration with the following features:

#### Core Components

1. **Password Encoding**
   - Uses BCrypt password encoding with strength 12
   - `PasswordEncoder` bean configured for secure password hashing

2. **Authentication Provider**
   - Custom `AuthDaoAuthenticationProvider` handles credential validation
   - Integrates with `AuthUserDetailsService` for user lookup
   - Validates passwords using BCrypt encoder

3. **Authentication Manager**
   - `ProviderManager` manages the authentication flow
   - Single provider configuration for username/password authentication

#### Security Filter Chain

The security filter chain implements a stateless, JWT-based approach:

- **CSRF Protection**: Disabled (appropriate for stateless JWT APIs)
- **Session Management**: `STATELESS` - no server-side sessions
- **CORS Configuration**: 
  - Allows requests from all origins with credentials
  - Supports GET, POST, PUT, DELETE, OPTIONS methods
  - All headers allowed

#### Authentication Flow

1. **JWTAuthenticationFilter**
   - Extends `UsernamePasswordAuthenticationFilter`
   - Intercepts `/auth/login` endpoint
   - Extracts credentials from JSON request body
   - Validates credentials through `AuthenticationManager`
   - Generates JWT tokens on successful authentication
   - Creates HTTP-only cookies for tokens

2. **JWTAuthorizationFilter**
   - Applied before `JWTAuthenticationFilter`
   - Validates JWT token on each request
   - Extracts user information from token
   - Sets authentication context for the request

#### Endpoint Security

- **Excluded Endpoints**: Public access for login, health check, and error pages
- **Protected Endpoints**: Require valid JWT token in Authorization header or cookies
- **OPTIONS Requests**: Allowed for all CORS preflight requests
- **Error Handling**: Proper HTTP status codes for forbidden and unauthorized access

#### Key Security Features

- BCrypt password hashing with configurable strength
- JWT token-based stateless authentication
- HTTP-only cookies to prevent XSS attacks
- CORS properly configured for cross-origin requests
- Custom authentication provider for domain-specific logic
- Global exception handling for security exceptions

## Project Classes & Architecture

### Models (Entity Classes)

#### Core Entities

- **BaseModel**: Abstract base class with common fields (id, createdAt, updatedAt)
- **UserModel**: Main user entity implementing Spring Security's `UserDetails` interface
  - Fields: name, age, username, email, password
  - Relationship: Many-to-One with CompanyModel
  - Security: Implements user authority and account status methods
  - Tracking: Failed login count for security monitoring

- **CompanyModel**: Represents organizations/companies
  - One-to-Many relationship with UserModel
  - Manages company metadata and associations

- **RoleModel**: Defines roles for authorization
  - Used in role-based access control (RBAC)
  - Associated with UserModel through UserRolesModel

- **UserRolesModel**: Junction table for User-Role relationship
  - Implements many-to-many relationship between users and roles

- **BearerTokenModel**: Represents issued JWT tokens
  - Tracks token metadata and validity
  - Enables token revocation/blacklisting

- **ClientModel**: Client/service accounts for API integration
  - Represents third-party clients accessing the system

- **UserPrincipalModel**: Enhanced principal containing user context
  - Used in Spring Security for storing additional user information

### Services

#### Authentication & Security Services

- **AuthUserDetailsService**: Implements Spring's `UserDetailsService`
  - Loads user details from database by username
  - Integrates with Spring Security authentication
  - Throws `UsernameNotFoundException` for invalid users

- **JWTService**: Manages JWT token operations
  - Generates access and refresh tokens
  - Validates token signatures and expiration
  - Extracts claims from tokens

- **BearerTokenService**: Manages issued bearer tokens
  - Tracks active tokens in the system
  - Handles token revocation and blacklisting
  - Supports token validation across services

#### Business Logic Services

- **UserServices**: User management and operations
  - Create, read, update, delete user operations
  - User search and filtering
  - User profile management

- **CompanyService**: Company management operations
  - CRUD operations for companies
  - Company hierarchy and relationships
  - User-company associations

### Controllers (REST Endpoints)

- **AuthController**: Authentication endpoints
  - POST `/auth/login` - User login with credentials
  - POST `/auth/logout` - User logout
  - POST `/auth/refresh` - Token refresh

- **UserController**: User management endpoints
  - GET `/users` - List all users
  - GET `/users/{id}` - Get specific user
  - POST `/users` - Create new user
  - PUT `/users/{id}` - Update user
  - DELETE `/users/{id}` - Delete user

- **CompanyController**: Company management endpoints
  - GET `/companies` - List all companies
  - GET `/companies/{id}` - Get specific company
  - POST `/companies` - Create new company
  - PUT `/companies/{id}` - Update company
  - DELETE `/companies/{id}` - Delete company

- **HealthCheck**: System health monitoring
  - GET `/health` - Returns application health status

- **DefaultController** (error/): Handles error routing

### Filters (Security Filters)

- **JWTAuthenticationFilter**: 
  - Custom authentication filter extending `UsernamePasswordAuthenticationFilter`
  - Processes login requests at `/auth/login`
  - Generates JWT tokens and HTTP-only cookies
  - Returns user login response with token metadata

- **JWTAuthorizationFilter**:
  - Pre-filter for request authorization
  - Validates JWT tokens from Authorization headers
  - Extracts user context and sets authentication
  - Allows unauthenticated access to public endpoints

### Authentication Providers

- **AuthDaoAuthenticationProvider**: Custom authentication provider
  - Validates username/password credentials
  - Uses `AuthUserDetailsService` for user lookup
  - Validates passwords with BCrypt encoder
  - Handles authentication exceptions

### Utilities & Helpers

- **JWTUtils**: JWT token generation and validation
  - Creates JWT tokens with custom claims
  - Validates token signatures
  - Extracts claims (username, roles, expiration)
  - Supports both access and refresh tokens

- **PasswordEncryptor** (Interface): Abstract password encryption contract
  - Defines password encryption/validation interface

- **BCryptPasswordEncryptorImpl**: BCrypt password encryption implementation
  - Implements `PasswordEncryptor` interface
  - Uses Spring Security's `BCryptPasswordEncoder`
  - Provides symmetric encryption for passwords

### DTOs (Data Transfer Objects)

#### Request DTOs

- **UserLoginRequest**: Login credentials
  - Fields: username, password

- **CreateCompanyRequest**: Company creation payload
  - Company details for registration

- **UserUpdateRequestDTO**: User update payload
  - Partial user information for updates

#### Response DTOs

- **UserLoginResponseDTO**: Login success response
  - Contains JWT tokens and user information
  - Includes token type and expiration details

- **UserInfoResponseDTO**: User information response
  - Public user profile data

- **CurrentUserInfoResponseDTO**: Current authenticated user info
  - Extended user information for logged-in user

- **JwtDto**: JWT token details
  - Token string, type, expiration

- **HealthCheckResponse**: System health status
  - Service status and metadata

- **ApiResponse**: Generic success response wrapper
  - Standardized format for API responses

- **ApiFailureResponse**: Generic error response wrapper
  - Standardized format for error responses

### Exception Handling

- **BaseException**: Abstract base exception class
  - Foundation for custom exceptions
  - Structured error information

- **AuthException**: Authentication-specific exceptions
  - Login failures, token validation errors
  - Invalid credentials

- **ErrorCodes**: Enumeration of error codes
  - Standardized error codes for the system
  - Maps to HTTP status codes and messages

- **ControllerExceptionHandler**: Global exception handler
  - Catches and processes exceptions across controllers
  - Returns standardized API error responses
  - Maps exceptions to appropriate HTTP status codes

### Security Token Management

- **JWTAuthenticationToken**: Custom authentication token
  - Extends Spring Security's authentication token
  - Carries JWT token information through filter chain

### Repository & Data Access

- **UserRepository**: User data access interface
  - Spring Data JPA repository for UserModel
  - Custom query methods for user lookup

- **CompanyRepository**: Company data access interface
  - Spring Data JPA repository for CompanyModel
  - Company-specific queries

- **BearerTokenRepository**: Token data access interface
  - Manages bearer token records
  - Token validity checks and revocation

- **UserRepoImpl**: Custom user repository implementation
  - Implements complex user queries
  - Custom filtering and search logic

## Database Migrations

Database migrations are managed using Flyway and stored in `src/main/resources/db/migration/`. Migrations are automatically applied on application startup.

## Testing

Run the test suite:
```bash
mvn test
```

Test coverage report available at `target/site/jacoco/index.html`

### Test Categories
- Unit tests for services and utilities
- Integration tests for filters and controllers
- Controller tests for REST endpoints
- Exception handling tests

## Logging

Logging is configured using Logback. Logs are written to:
- `logs/auth.log` - Main application log
- `logs/auth-rolling.log` - Rolling log file

Adjust log levels in `src/main/resources/logback.xml`

## Docker Deployment

Build and run with Docker:

```bash
# Build Docker image
docker build -t promptdb-auth:latest .

# Run container
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://mysql:3306/auth \
  -e DB_USERNAME=amit \
  -e DB_PASSWORD=password \
  promptdb-auth:latest
```

Nginx reverse proxy configuration available in `docker/nginx/`

## Project Conventions

- **Package Structure**: Organized by feature/layer (controllers, services, models, etc.)
- **REST API**: RESTful endpoints with proper HTTP methods
- **Error Handling**: Global exception handler with standardized API responses
- **DTO Pattern**: Data transfer objects for request/response handling
- **JWT Security**: Token-based stateless authentication

## Known Issues & Notes

- Update `"qwerty"` hardcoded value in login response if needed
- JWT cookie configuration set for 24-hour expiration
- HTTP-only cookies enabled for security
- Database DDL auto-generation disabled (migrations managed via Flyway)

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests and linting
4. Submit a pull request

For frontend: Run `npm run lint` before committing
For backend: Ensure all tests pass with `mvn test`

## License

This project is part of the PromptDB suite.

## Support

For issues and questions, please contact the development team or create an issue in the repository.

## Roadmap

- [ ] OAuth2 integration
- [ ] Social login providers
- [ ] Two-factor authentication (2FA)
- [ ] Role-based access control (RBAC) enhancement
- [ ] API key management
- [ ] Session management improvements
- [ ] Audit logging

---

**Version**: 0.0.1-SNAPSHOT  
**Last Updated**: December 2025
