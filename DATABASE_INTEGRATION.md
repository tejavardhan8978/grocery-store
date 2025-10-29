# Grocery Store Application - Database Integration

## Overview
The application has been transformed from using hardcoded values to a database-driven architecture using Spring Data JPA and H2 database.

## Key Changes Made

### 1. Repository Layer
- **UserRepository**: Interface extending JpaRepository for user database operations
  - `findByEmail()` - Find user by email address
  - `findByEmailAndPassword()` - Authenticate user with email/password
  - `existsByEmail()` - Check if email already exists
  - Custom queries for finding admin and employee users

### 2. Service Layer
- **UserService**: Business logic layer for user operations
  - User authentication
  - User registration with validation
  - Guest user creation
  - Email existence checking
- **DataInitializer**: Populates database with sample data on startup

### 3. Controller Updates
- **HomeController**: Now uses session-based user management
- **LoginController**: Implements real database authentication with session management
- **RegisterController**: Complete user registration with validation and auto-login

### 4. Model Updates
- **User**: Made serializable for session storage, added proper JPA annotations

## Sample Users Created on Startup

The application automatically creates these test users:

1. **Regular User**
   - Email: `john.doe@example.com`
   - Password: `password123`

2. **Admin User**
   - Email: `admin@grocerystore.com`
   - Password: `admin123`

3. **Employee User**
   - Email: `jane.smith@grocerystore.com`
   - Password: `employee123`

## Features Implemented

### Authentication & Session Management
- Real database-based user authentication
- Session management for logged-in users
- Guest user support for non-authenticated sessions
- Secure logout with session cleanup

### User Registration
- Complete registration form handling
- Email uniqueness validation
- Required field validation
- Automatic login after successful registration
- Error handling and user feedback

### Database Integration
- H2 in-memory database with file persistence
- JPA/Hibernate for ORM
- Automatic schema generation
- Database console access at `/h2-console`

## Testing the Application

1. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Access the application**
   - Home page: http://localhost:8081/
   - Login page: http://localhost:8081/login
   - Register page: http://localhost:8081/register
   - H2 Console: http://localhost:8081/h2-console

3. **Test Login**
   - Use any of the sample users listed above
   - Try invalid credentials to test error handling

4. **Test Registration**
   - Register a new user with unique email
   - Try registering with existing email to test validation

5. **Test Session Management**
   - Login and navigate around the application
   - Logout and verify session cleanup

## Database Access

- **Console URL**: http://localhost:8081/h2-console
- **JDBC URL**: `jdbc:h2:file:./db_test/database;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH`
- **Username**: (leave empty)
- **Password**: (leave empty)

## Next Steps

To make this a full RESTful API, consider:
1. Adding `@RestController` classes that return JSON
2. Implementing proper REST endpoints (GET /api/users, POST /api/users, etc.)
3. Adding JWT-based authentication
4. Implementing proper error responses and HTTP status codes
5. Adding API documentation with Swagger/OpenAPI