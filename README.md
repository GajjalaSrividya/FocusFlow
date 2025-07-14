# FocusFlow

**FocusFlow** is a full-stack to-do management web application that helps users stay organized, focused, and productive.  
It enables users to sign up, log in, manage their weekly tasks, and receive automatic daily email reminders at midnight.

---

##  Tech Stack

###  Backend
- Java 17
- Spring Boot 3.1.5
- Spring Security with JWT authentication
- JavaMailSender (for scheduled task reminder emails)

###  Database
- Apache Cassandra 4.1.9 (CQL on port 9042)

###  Frontend
- React
- Tailwind CSS

###  Build & Tools
- Maven
- Docker
- Eclipse IDE
- Git & GitHub

---

##  Running with Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/GajjalaSrividya/FocusFlow.git
cd FocusFlow

# Stop and remove any existing containers and networks (optional cleanup)
docker compose down

# Build and start all services
docker-compose up --build


Visit: http://localhost:5173/ login or sign up
Frontend: http://localhost:5173
Backend API: http://localhost:8080
  
#View Cassandra Tables
docker exec -it cassandra cqlsh
USE focusflow;
DESCRIBE tables;

#Manual Setup (Without Docker)
**backend
cd backend
# Run using Maven Wrapper
./mvnw spring-boot:run
# Or using regular Maven
mvn spring-boot:run -DskipTests

**frontend
cd frontend
npm install
npm run dev
Visit: http://localhost:5173/ login or sign up

## Features
### Authentication
- **JWT-secured login and signup** with Spring Security to ensure secure user sessions.
### Task Management
- **Weekly task planning** – Add tasks for the upcoming 7 days.
- **CRUD operations** – Create, update, delete, and mark tasks as completed.
###  Email Reminders
- **Automatic daily email at midnight** using JavaMailSender, sending the day’s tasks.
### Task Calendar View
- **Interactive calendar** to view tasks for any selected date.
### Progress Tracking *(In Progress)*
- **Daily and overall performance tracking** with visual stats (coming soon).
### Docker Integration
- **Full app containerization** – Run backend, frontend, and Cassandra DB using Docker Compose.

