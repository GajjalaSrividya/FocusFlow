# 🚀 FocusFlow

**FocusFlow** is a full-stack to-do management application that helps users stay organized and productive. It supports login/signup, lets you manage your weekly tasks, and sends daily task reminder emails at midnight.

---

## 🛠️ Tech Stack

### 🔧 Backend
- Spring Boot 3.1.5 (Java 17)
- Spring Security with JWT
- JavaMailSender for email reminders

### 🗄️ Database
- Apache Cassandra 4.1.9 (port `9042`)

### 💻 Frontend
- React + Tailwind CSS

### ⚙️ Tools
- Docker
- Git & GitHub

---

## 🐳 Run FocusFlow with Docker (Recommended)

> ⚠️ Make sure [Docker](https://www.docker.com/products/docker-desktop/) is installed and running on your system.

### 1. Clone the Repo
git clone https://github.com/GajjalaSrividya/FocusFlow.git
cd FocusFlow
cp .env.example .env
Edit .env and provide your Gmail credentials (used for sending emails)
# start the application
docker compose up
http://localhost:5173/signup
http://localhost:5173/login

##View Tables in Cassandra
docker exec -it cassandra cqlsh
USE focusflow;
DESCRIBE TABLES;
