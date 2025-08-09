# Leave Management System

A comprehensive leave management system built with Java Spring Boot backend, React frontend, and PostgreSQL database, all containerized with Docker.

## 🚀 Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Leave Request Management**: Submit, approve, and track leave requests
- **Employee Management**: Comprehensive employee profiles and management
- **Leave Balance Tracking**: Automatic calculation and tracking of leave balances
- **Manager Approval System**: Multi-level approval workflow
- **Dashboard & Reporting**: Visual dashboard with statistics and leave overview
- **Email Notifications**: Automated email notifications for approvals/rejections
- **Responsive Design**: Modern, mobile-friendly user interface

## 🏗️ Architecture

- **Backend**: Java Spring Boot 3.2.0 with Spring Security
- **Frontend**: React 18 with TypeScript and modern CSS
- **Database**: PostgreSQL 15 with JPA/Hibernate
- **Authentication**: JWT tokens with Spring Security
- **Containerization**: Docker containers for all services

## 📁 Project Structure

```
leave-management-system/
├── backend/                 # Java Spring Boot application
│   ├── src/main/java/com/leavemanagement/
│   │   ├── config/         # Security and application configuration
│   │   ├── controller/     # REST API controllers
│   │   ├── service/        # Business logic services
│   │   ├── repository/     # Data access layer
│   │   ├── entity/         # JPA entities
│   │   ├── dto/            # Data transfer objects
│   │   └── security/       # JWT and security components
│   ├── src/main/resources/ # Configuration files
│   └── pom.xml            # Maven dependencies
├── frontend/               # React application
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── contexts/       # React contexts (Auth)
│   │   └── App.tsx         # Main application component
│   └── package.json        # Node.js dependencies
├── database/               # Database scripts and setup
├── docker/                 # Docker configuration files
└── README.md              # This file
```

## 🛠️ Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- Docker and Docker Compose
- Maven 3.6 or higher

## 🚀 Quick Start

### 1. Clone and Setup

```bash
git clone <repository-url>
cd leave-management-system
```

### 2. Start Database

```bash
cd docker/postgres
docker-compose up -d
```

### 3. Start Backend

```bash
cd ../../backend
mvn clean install
mvn spring-boot:run
```

The backend will be available at: http://localhost:8080/api

### 4. Start Frontend

```bash
cd ../frontend
npm install
npm start
```

The frontend will be available at: http://localhost:3000

## 🔐 Default Users

The system comes with pre-configured users:

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| manager | manager123 | MANAGER |
| employee | employee123 | EMPLOYEE |

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Leave Requests
- `GET /api/leave-requests` - Get all leave requests
- `POST /api/leave-requests` - Create new leave request
- `PUT /api/leave-requests/{id}/approve` - Approve leave request
- `PUT /api/leave-requests/{id}/reject` - Reject leave request

### Employees
- `GET /api/employees` - Get all employees (MANAGER/ADMIN only)
- `GET /api/employees/{id}` - Get employee by ID
- `GET /api/employees/manager/{managerId}` - Get employees by manager

### Leave Balances
- `GET /api/leave-balances/employee/{employeeId}` - Get employee leave balances
- `POST /api/leave-balances/reset-annual` - Reset annual leave balances (ADMIN only)

### Leave Types
- `GET /api/leave-types` - Get all leave types
- `GET /api/leave-types/{id}` - Get leave type by ID

## 🐳 Docker Deployment

### Build and Run All Services

```bash
# Build backend
cd backend
docker build -t leave-management-backend .

# Build frontend
cd ../frontend
docker build -t leave-management-frontend .

# Run with docker-compose
cd ..
docker-compose up -d
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the backend directory:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/leave_management
DB_USERNAME=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=your-secure-jwt-secret-key-here
JWT_EXPIRATION=86400000

# Email (optional)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### Database Configuration

The database configuration is in `backend/src/main/resources/application.yml`. Update the database URL, username, and password as needed.

## 🧪 Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## 📱 Features Overview

### For Employees
- Submit leave requests
- View leave balances
- Track request status
- View personal dashboard

### For Managers
- Approve/reject leave requests
- View team leave requests
- Manage team members
- Access team analytics

### For Administrators
- Full system access
- User management
- Leave type configuration
- System configuration

## 🚀 Deployment

### Production Deployment

1. Update environment variables for production
2. Use production-grade database
3. Configure HTTPS
4. Set up monitoring and logging
5. Use production Docker images

### Cloud Deployment

The system can be deployed to:
- AWS (ECS, RDS)
- Google Cloud (GKE, Cloud SQL)
- Azure (AKS, Azure Database)
- Heroku
- DigitalOcean

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the API endpoints

## 🎯 Roadmap

- [ ] Mobile app (React Native)
- [ ] Advanced reporting and analytics
- [ ] Integration with calendar systems
- [ ] Multi-language support
- [ ] Advanced workflow customization
- [ ] API rate limiting and monitoring
- [ ] Automated testing pipeline
- [ ] Performance optimization

---

**Built with ❤️ using Spring Boot, React, and PostgreSQL**
