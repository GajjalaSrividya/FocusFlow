# FocusFlow
**FocusFlow** is a to-do management web application that helps users stay organized, focused, and productive. It allows users to sign up, log in, manage their weekly tasks, and receive automatic email reminders every day at midnight.

# Tech Stack

# Backend
- Spring Boot 3.1.5
- Spring Security+JWT authentication
- JavaMailSender (for daily task email reminders)
- Java 17

# Database
- Apache Cassandra 4.1.9(cqlsh host: 9042 )

# Frontend
- React
-Tailwind css

# Build & Tools
- Maven
- Eclipse IDE
- Docker
- Git & GitHub

** Running everything with docker
# Clone the repo
git clone https://github.com/GajjalsSrividya/focusflow.git
cd focusflow
#stop and remove all containers,networks
docker compose down
# Start all services
docker-compose up --build

Visit: http://localhost:5173/ login or signup
>  Frontend runs at: [http://localhost:5173]
>  Backend API runs at: [http://localhost:8080]
  
  #see tables 
  docker exec -it cassandra cqlsh
  use focusflow

 # Manual Setup (Without Docker)
  docker start cassandra-db
docker exec -it cassandra-db cqlsh
USE focusflow;
DESCRIBE tables;

**backend
cd backend
# Run with Maven Wrapper
./mvnw spring-boot:run
# or regular Maven
mvn spring-boot:run -DskipTests

**frontend
cd frontend
npm install
npm run dev
Visit: http://localhost:5173/ login or signup


