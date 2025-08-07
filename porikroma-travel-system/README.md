# Porikroma Travel Management System

A comprehensive travel management system that addresses the pain points of both solo and group travelers. Built with Spring Boot backend, Java Swing frontend, and MySQL database.

## Features

### Core Features
- **User Authentication System**
  - Email/password authentication
  - Google OAuth integration
  - Password recovery and email verification
  - JWT-based security

- **Trip Management**
  - Solo travel mode
  - Group travel mode with coordination features
  - Trip planning and itinerary management
  - Budget planning and expense tracking

- **AI-Powered Recommendations**
  - Destination suggestions based on preferences
  - Budget-aware recommendations
  - Weather and safety information

- **Booking System**
  - Unified booking interface
  - Transportation, accommodation, and activities
  - Budget-aware filtering

- **Group Coordination**
  - Group messaging and polls
  - Member management and roles
  - Shared expense tracking

- **Safety Features**
  - Emergency contact system
  - "I feel unsafe" button
  - Location sharing capabilities

- **Review and Feedback System**
  - Two-way rating system
  - Community-driven reviews
  - Verified feedback

## Technology Stack

### Backend
- **Spring Boot 3.2.0** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access
- **MySQL 8.0** - Database
- **JWT** - Token-based authentication
- **Maven** - Build tool

### Frontend
- **Java Swing** - Desktop UI framework
- **FlatLaf** - Modern look and feel
- **OkHttp** - HTTP client
- **Jackson** - JSON processing
- **MigLayout** - Layout manager

### Key Dependencies
- Spring Boot Starter Web, Security, Data JPA
- MySQL Connector
- JWT (jjwt)
- OpenAPI/Swagger documentation
- Lombok for code generation
- MapStruct for DTO mapping

## Project Structure

```
porikroma-travel-system/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/com/porikroma/
│   │   ├── controller/     # REST controllers
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Data access
│   │   ├── model/          # Entity classes
│   │   ├── security/       # Security configuration
│   │   ├── dto/            # Data transfer objects
│   │   └── config/         # Configuration classes
│   ├── src/main/resources/
│   │   ├── application.yml # Configuration
│   │   └── data.sql       # Initial data
│   └── pom.xml            # Maven configuration
├── frontend/               # Java Swing frontend
│   ├── src/main/java/com/porikroma/
│   │   ├── ui/            # Swing UI components
│   │   ├── service/       # API service classes
│   │   ├── model/         # Data models
│   │   ├── util/          # Utility classes
│   │   └── config/        # Configuration
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml            # Maven configuration
├── database/              # Database scripts
└── docs/                  # Documentation
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd porikroma-travel-system
```

### 2. Database Setup
```sql
-- Create database
CREATE DATABASE porikroma_db;

-- Create user (optional)
CREATE USER 'porikroma_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON porikroma_db.* TO 'porikroma_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Backend Configuration

Edit `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/porikroma_db
    username: your_username
    password: your_password
    
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your_google_client_id
            client-secret: your_google_client_secret
            
  mail:
    username: your_email@gmail.com
    password: your_app_password

jwt:
  secret: your_jwt_secret_key

external-apis:
  weather:
    api-key: your_weather_api_key
  maps:
    api-key: your_maps_api_key
```

### 4. Build and Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 5. Build and Run Frontend

```bash
cd frontend
mvn clean compile
mvn exec:java -Dexec.mainClass="com.porikroma.PorikromaApp"
```

Or create executable JAR:
```bash
mvn clean package
java -jar target/porikroma-frontend-1.0.0-jar-with-dependencies.jar
```

## API Documentation

Once the backend is running, access the Swagger UI at:
`http://localhost:8080/swagger-ui.html`

## Key Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration
- `GET /api/auth/verify-email` - Email verification
- `POST /api/auth/forgot-password` - Password reset request
- `POST /api/auth/reset-password` - Password reset

### User Management
- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update user profile
- `GET /api/user/trips` - Get user trips

### Trip Management
- `GET /api/trips` - Get trips
- `POST /api/trips` - Create trip
- `PUT /api/trips/{id}` - Update trip
- `DELETE /api/trips/{id}` - Delete trip

### Group Management
- `GET /api/groups` - Get user groups
- `POST /api/groups` - Create group
- `POST /api/groups/{id}/join` - Join group

## Configuration

### Environment Variables

Backend supports the following environment variables:

- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT secret key
- `GOOGLE_CLIENT_ID` - Google OAuth client ID
- `GOOGLE_CLIENT_SECRET` - Google OAuth client secret
- `MAIL_USERNAME` - Email username
- `MAIL_PASSWORD` - Email password
- `OPENAI_API_KEY` - OpenAI API key for AI features

### External API Keys Required

1. **Google OAuth** - For Google login
2. **OpenWeatherMap** - For weather data
3. **Google Maps** - For location services
4. **OpenAI** - For AI recommendations
5. **Email Service** - Gmail SMTP or similar

## Development

### Running in Development Mode

Backend:
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Frontend:
```bash
cd frontend
mvn compile exec:java
```

### Building for Production

Backend:
```bash
cd backend
mvn clean package -Pprod
```

Frontend:
```bash
cd frontend
mvn clean package
```

## Testing

Run backend tests:
```bash
cd backend
mvn test
```

## Security Features

- JWT-based authentication
- Password encryption with BCrypt
- OAuth2 integration
- CORS configuration
- Input validation
- SQL injection prevention
- XSS protection

## Database Schema

The application uses the following main entities:
- `users` - User accounts and profiles
- `roles` - User roles (USER, ADMIN, etc.)
- `trips` - Trip information
- `travel_groups` - Group travel coordination
- `bookings` - Reservations and bookings
- `expenses` - Expense tracking
- `reviews` - User reviews and ratings

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions:
- Create an issue in the repository
- Contact the development team

## Future Enhancements

- Mobile app version
- Real-time notifications
- Advanced AI features
- Integration with more booking platforms
- Offline mode support
- Multi-language support