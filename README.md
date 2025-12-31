User Management System with Role Based Access Management

**Table of Contents**

Overview

Features

Tech Stack

Project Structure

Setup & Installation

API Documentation

Screenshots & Demo

Database Schema

Security Implementation

Key Implementation Details

**Overview**
This is an implementation of User Management System built with Spring Boot and MySQL, featuring comprehensive Role-Based Access Control (RBAC) for managing users and their permissions. The system implements JWT-based authentication, role assignment, and admin-only endpoints for user management.

The application is fully containerized with Docker and Docker Compose for easy deployment and scaling.

**Features**
Authentication & Authorization

User Registration with email validation and duplicate prevention

User Login with JWT token generation (HS512 algorithm)

Role-Based Access Control (RBAC) - Users automatically assigned DEFAULT role on registration

Admin Role Management - Manual admin setup with secure password hashing

Token-based Security - JWT authentication for protected endpoints

User Management (Admin-only)

Get All Users - List all users with their assigned roles

Get Logged-In User - Retrieve current user profile with roles

User Statistics Dashboard - View system-wide user metrics and login analytics

Data & Infrastructure

MySQL Database with proper schema design (roles, users, user_roles junction table)

Docker & Docker Compose - Complete containerization for MySQL and Spring Boot

Swagger/OpenAPI Documentation - Interactive API documentation at /swagger-ui/

Error Handling - Global exception handler with meaningful error responses

Logging - Track user creation, updates, and last login timestamps

Tech Stack
Component	Technology	Version
Backend	Spring Boot	3.2.x
Language	Java	17+
Database	MySQL	8.0
JWT	JJWT	0.12.x
Encryption	BCrypt	Spring Security
ORM	JPA/Hibernate	Spring Data
Mapping	MapStruct	Latest
Container	Docker & Docker Compose	Latest
Documentation	Swagger/OpenAPI 3.0	Latest
Build Tool	Maven	3.8.x
📁 Project Structure
text
myproject/
├── src/main/java/com/visitly/myproject/
│   ├── controller/
│   │   ├── AuthController.java           # Login & Register endpoints
│   │   ├── UserController.java           # User management (admin)
│   │   └── AdminController.java          # Admin statistics & metrics
│   │
│   ├── service/
│   │   ├── AuthService.java              # Authentication logic
│   │   ├── UserService.java              # User CRUD operations
│   │   └── RoleService.java              # Role management
│   │
│   ├── entity/
│   │   ├── User.java                     # User entity with roles
│   │   ├── Role.java                     # Role entity
│   │   └── AuditLog.java                 # Audit trail
│   │
│   ├── dto/
│   │   ├── UserResponse.java             # User response DTO with roles
│   │   ├── RoleDto.java                  # Role data transfer object
│   │   ├── AuthResponse.java             # Auth response with token
│   │   ├── LoginRequest.java             # Login request
│   │   └── RegisterRequest.java          # Registration request
│   │
│   ├── repository/
│   │   ├── UserRepository.java           # User JPA Repository
│   │   ├── RoleRepository.java           # Role JPA Repository
│   │   └── AuditLogRepository.java       # Audit log repository
│   │
│   ├── mapper/
│   │   └── UserMapper.java               # MapStruct user mapper
│   │
│   ├── security/
│   │   ├── JwtTokenProvider.java         # JWT token generation & validation
│   │   ├── JwtAuthenticationFilter.java  # JWT filter for all requests
│   │   └── SecurityConfig.java           # Spring Security configuration
│   │
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java   # Centralized error handling
│   │   ├── DuplicateEmailException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── UnauthorizedException.java
│   │
│   └── MyprojectApplication.java         # Main Spring Boot entry point
│
├── src/main/resources/
│   ├── application.yml                   # Spring configuration
│   └── db/migration/                     # Flyway migrations (optional)
│
├── docker-compose.yml                    # Docker Compose setup
├── Dockerfile                            # Spring Boot container
├── pom.xml                               # Maven dependencies
└── README.md                             # This file
Setup & Installation
Prerequisites
Docker & Docker Compose installed

Java 17+ for local development

Maven 3.8+ for building

Quick Start (Docker)
bash
# 1. Clone the repository
git clone <your-repo>
cd myproject

# 2. Build and start all services
./mvnw clean package -DskipTests && \
docker-compose down && \
docker rmi myproject-app:latest 2>/dev/null || true && \
docker-compose build --no-cache && \
docker-compose up -d && \
sleep 25

# 3. Initialize admin account and roles
docker exec -i myproject-mysql mysql -uroot -pRootPass123! myprojectdb -e "
INSERT IGNORE INTO roles (name, description, created_at) VALUES 
  ('USER', 'Default user role', NOW()), 
  ('ADMIN', 'Administrator role', NOW());

INSERT IGNORE INTO users (username, email, password_hash, is_active, created_at, updated_at) VALUES 
  ('admin', 'admin@example.com', '\$2a\$10\$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, NOW(), NOW());

INSERT IGNORE INTO user_roles (user_id, role_id) 
  SELECT u.id, r.id FROM users u, roles r 
  WHERE u.email = 'admin@example.com' AND r.name = 'ADMIN';
"

# 4. Test the API
curl -s http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"Pass@123456"}' | jq .
Local Development Setup
bash
# 1. Start only MySQL
docker-compose up -d myproject-mysql

# 2. Update application.yml with local database
# spring:
#   datasource:
#     url: jdbc:mysql://localhost:3306/myprojectdb
#     username: root
#     password: RootPass123!

# 3. Run Spring Boot
./mvnw spring-boot:run

# 4. Access Swagger UI
open http://localhost:8080/swagger-ui/
📚 API Documentation
Base URL
text
http://localhost:8080/api
Authentication Endpoints
Register New User
text
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass@123"
}
Response (201 Created):

json
{
  "id": 3,
  "username": "john_doe",
  "email": "john@example.com",
  "isActive": true,
  "createdAt": "2025-12-31T12:30:45.123456",
  "roles": [
    {
      "id": 1,
      "name": "USER",
      "description": "Default user role",
      "createdAt": "2025-12-31T06:57:21"
    }
  ]
}
Login
text
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass@123"
}
Response (200 OK):

json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzY3MTc4NzAwLCJleHAiOjE3NjcyNjUxMDB9.Q1r...",
  "type": "Bearer",
  "user": {
    "id": 3,
    "username": "john_doe",
    "email": "john@example.com",
    "isActive": true,
    "createdAt": "2025-12-31T12:30:45.123456"
  }
}
User Endpoints (Admin-only)
Get All Users
text
GET /admin/users
Authorization: Bearer {JWT_TOKEN}
Response (200 OK):

json
[
  {
    "id": 1,
    "username": "john",
    "email": "john@example.com",
    "isActive": true,
    "createdAt": "2025-12-31T06:50:04.676467",
    "updatedAt": "2025-12-31T07:20:32.468484",
    "lastLoginAt": "2025-12-31T07:20:32.467932",
    "roles": []
  },
  {
    "id": 2,
    "username": "admin",
    "email": "admin@example.com",
    "isActive": true,
    "createdAt": "2025-12-31T06:57:21",
    "roles": [
      {
        "id": 2,
        "name": "ADMIN",
        "description": "Administrator role",
        "createdAt": "2025-12-31T06:57:21"
      }
    ]
  }
]
Get Current User Profile
text
GET /users/me
Authorization: Bearer {JWT_TOKEN}
Response (200 OK):

json
{
  "id": 2,
  "username": "john_doe",
  "email": "john@example.com",
  "isActive": true,
  "createdAt": "2025-12-31T12:30:45.123456"
}
Admin Dashboard Endpoints
Get User Statistics
text
GET /admin/stats
Authorization: Bearer {ADMIN_JWT_TOKEN}
Response (200 OK):

json
{
  "totalUsers": 5,
  "activeUsers": 5,
  "lastLoginTimes": {
    "1": "2025-12-31T07:05:14.788658",
    "2": "2025-12-31T07:02:17.132988"
  },
  "timestamp": "2025-12-31T07:08:23.477981"
}
Screenshots & Demo
1. Docker Desktop - Running Containers
Docker Containers Running
All containers (MySQL, Spring Boot app) are healthy and running.

<img width="1280" height="832" alt="Screenshot 2025-12-31 at 2 15 50 PM" src="https://github.com/user-attachments/assets/eeec750b-78ff-442e-a560-e90031321844" />


2. Postman - User Registration
Register User Endpoint
Successful user registration with automatic USER role assignment (201 Created).
<img width="1280" height="832" alt="Screenshot 2025-12-31 at 11 59 03 AM" src="https://github.com/user-attachments/assets/a69f2ebd-5a27-4dc2-bfe1-c11fd99580a5" />


4. Postman - User Login
Login Endpoint
JWT token generation on successful login with user details in response.
<img width="1280" height="832" alt="Screenshot 2025-12-31 at 2 13 40 PM" src="https://github.com/user-attachments/assets/8c447e11-ebe3-4ce9-9845-c236eea8e5ba" />


6. Swagger UI - API Documentation
Swagger UI - Authentication
Interactive API documentation with all endpoints and schemas.
UI.pdf

9. Postman - Get All Users (Admin)
Get All Users Response
Fetching all users with roles using Bearer token authentication.
<img width="1280" height="832" alt="Screenshot 2025-12-31 at 2 13 35 PM" src="https://github.com/user-attachments/assets/aa8563a2-8b40-4b8e-b668-f627fa459450" />


11. Postman - Admin Login
Admin Login
Admin user login returning JWT token for protected endpoint access.
<img width="1280" height="832" alt="Screenshot 2025-12-31 at 2 13 40 PM" src="https://github.com/user-attachments/assets/9e839bd2-01f6-47dc-870d-d9c0140e7f70" />


13. Postman - Get Current User
Get Current User
Authenticated user retrieving their own profile information.
<img width="1280" height="832" alt="Screenshot 2025-12-31 at 11 59 46 AM" src="https://github.com/user-attachments/assets/5b4b423a-7e0a-468f-95c9-25d2f57c6585" />


15. Postman - New User Registration
Register New User
Creating new test user with 201 Created response.
<img width="1280" height="832" alt="Screenshot 2025-12-31 at 11 59 03 AM" src="https://github.com/user-attachments/assets/5431e79e-8447-4973-8aca-301bc2d9b7a4" />


17. Postman - User Login After Registration
New User Login
Successfully logging in with newly registered credentials.

Database Schema
Users Table
sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  last_login_at TIMESTAMP NULL
);
Roles Table
sql
CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
User-Roles Junction Table (Many-to-Many)
sql
CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
Default Roles:

USER - Default role assigned to all new users

ADMIN - Administrator with access to user management endpoints

🔐 Security Implementation
JWT Token Details
Algorithm: HS512 (HMAC with SHA-512)

Token Type: Bearer Token

Claims:

sub - User email (subject)

iat - Issued at timestamp

exp - Expiration timestamp (24 hours)

Secret Key: Stored in application.yml (change in production!)

Password Security
Hashing: BCrypt with strength 10

Salt: Auto-generated per password

Comparison: Secure comparison using PasswordEncoder.matches()

Endpoint Security
java
// Authentication Filter intercepts all requests
JwtAuthenticationFilter applies JWT validation

// Protected endpoints require valid Bearer token
Authorization: Bearer {valid_jwt_token}

// Role-based endpoint protection
@Secured("ROLE_ADMIN") - Admin-only endpoints
Example JWT Token (Decoded)
json
Header: {
  "alg": "HS512"
}

Payload: {
  "sub": "admin@example.com",
  "iat": 1767167655,
  "exp": 1767254055
}

Signature: Q1rtZE4tQoRjIsf3AbJNFNIHLjZTmHi2f_v-_BYIjWpwz...
🔧 Key Implementation Details
1. Automatic User Role Assignment
When a user registers, the USER role is automatically assigned:

java
@Transactional
public UserResponse register(RegisterRequest request) {
    // ... validation ...
    
    // Assign default USER role
    Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("USER role not found"));
    user.setRoles(new HashSet<>());
    user.getRoles().add(userRole);
    
    User savedUser = userRepository.save(user);
    return mapToUserResponse(savedUser);
}
2. JWT Token Generation
java
public String generateToken(String email) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
    
    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .compact();
}
3. Role-Based DTO Mapping
Roles are properly mapped to DTOs using MapStruct:

java
@Named("rolesToRoleDtos")
default Set<RoleDto> rolesToRoleDtos(Set<Role> roles) {
    if (roles == null || roles.isEmpty()) {
        return Set.of();
    }
    return roles.stream()
            .map(role -> RoleDto.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .createdAt(role.getCreatedAt())
                    .build())
            .collect(Collectors.toSet());
}
4. Global Exception Handling
Centralized error handling with meaningful responses:

java
@ExceptionHandler(DuplicateEmailException.class)
public ResponseEntity<ErrorResponse> handleDuplicateEmail(
        DuplicateEmailException ex) {
    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("Email already registered", 409));
}
5. Login Timestamp Tracking
User's last login is automatically updated:

java
public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    
    // Update last login timestamp
    user.setLastLoginAt(LocalDateTime.now());
    userRepository.save(user);
    
    String token = jwtTokenProvider.generateToken(user.getEmail());
    return AuthResponse.builder()
            .token(token)
            .type("Bearer")
            .user(mapToUserResponse(user))
            .build();
}
🧪 Testing the Application
Using cURL
Register a user:

bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username":"testuser",
    "email":"test@example.com",
    "password":"Pass@123456"
  }'
Login:

bash
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password"}' | jq -r '.token')

echo "Token: $ADMIN_TOKEN"
Get all users (admin only):

bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
Get current user:

bash
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
📊 Performance & Scalability
Database Indexes: User email indexed for fast lookups

Connection Pooling: HikariCP with configurable pool size

Transactional Consistency: @Transactional on critical operations

JWT Stateless: No session storage required

Horizontal Scaling: Stateless design supports load balancing

🚢 Production Deployment
Environment Variables
bash
JAVA_OPTS="-Dspring.profiles.active=prod"
DB_URL=jdbc:mysql://db-server:3306/myprojectdb
DB_USER=prod_user
DB_PASSWORD=strong_password
JWT_SECRET=production_secret_key_256_bits_minimum
Docker Deployment
bash
docker run -d \
  --name myproject-api \
  -p 8080:8080 \
  -e DB_URL=jdbc:mysql://db:3306/myprojectdb \
  -e DB_USER=prod_user \
  -e DB_PASSWORD=secure_password \
  myproject-app:latest

Assignment Completion Checklist

User Registration

Email validation

Duplicate email prevention

Automatic USER role assignment

Password hashing with BCrypt

201 Created response

User Login

Email & password validation

JWT token generation (HS512)

User profile in response

Last login tracking

200 OK response

Admin User Management

Get all users endpoint

Role-based access control

Bearer token authentication

Admin-only ROLE_ADMIN requirement

Complete user details with roles

Role Management

Multiple role support (USER, ADMIN)

Role assignment on registration

Role inclusion in user responses

Role-based endpoint access

Security

BCrypt password hashing

JWT authentication (HS512)

Spring Security configuration

Global exception handling

Secure token validation

API Documentation

Swagger/OpenAPI setup

Interactive documentation

Request/response schemas

Authentication examples

Endpoint descriptions

Database Design

Normalized schema

Foreign key relationships

Many-to-many user-roles

Timestamp tracking

Index optimization

Docker & Deployment

Docker Compose setup

MySQL containerization

Spring Boot image

Health checks

Please find below the architecture for Jwt authentication: -
<img width="2940" height="1838" alt="image" src="https://github.com/user-attachments/assets/b13ae34d-4f79-4712-afeb-78ad1fe9b417" />

Hope you like my implementation ☺️

Best Regards,
Divyanshi Sharma



