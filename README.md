**BudgetBlitz** is a personal finance management application built with Java, Spring Boot, MySql designed to help users efficiently track their **income**, **expenses**, and **savings**, while providing clear financial insights through categorization and data visualization.

---

## Table of Contents
- [Features](#features)
- [Database Design](#database-design)
- [Tech Stack](#tech-stack)
- [Configuration](#configuration)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [API Documentation](#-api-documentation)
- [Deployment (Sevalla Cloud)](#-deployment-sevalla-cloud)

---

## Features

### User Management
- Secure **user registration** and **authentication**  
- Personalized dashboard and financial records for each user  
- Role-based access control using **Spring Security** (e.g., `ROLE_USER`)

### Income & Expense Tracking
- Add, update, and delete **income** and **expense** entries  
- Track amount, date, description, and category  
- Manage multiple income sources and expense types

### Categorization & Analysis
- Classify expenses by category (e.g., groceries, rent, utilities)  
- View aggregated totals by category for better insights  

### Financial Overview & Visualization
- Display total income, total expenses, and savings  
- Generate **monthly and yearly summaries**  
- Basic charts for financial trend analysis  

### JWT Authentication
- Secure endpoints using **JWT tokens** (RSA keys)  
- Access and refresh token management for user sessions  

### Validation & Error Handling
- Input validation with **Jakarta Validation**  
- Global exception handling with descriptive error messages

---

### Database Design:
<img width="887" height="822" alt="budget_blitz-erd" src="https://github.com/user-attachments/assets/8035b0ba-de2a-41e3-9bcc-9e1ca9c874ca" />

---

## Tech Stack

| Layer | Technology |
|-------|-------------|
| **Backend Framework** | Spring Boot (v3+) |
| **Security** | Spring Security with JWT (RSA keys) |
| **Database** | MySQL (JPA / Hibernate ORM) |
| **Build Tool** | Maven |
| **Validation** | Jakarta Bean Validation |
| **Documentation** | Swagger / Springdoc OpenAPI |
| **Deployment** | Sevalla Cloud |
| **Java Version** | Java 17 |

---

## Configuration

### `application.properties`
```properties
spring.profiles.active=dev
spring.config.import=optional:file:${spring.profiles.active}.env[.properties]
server.servlet.context-path=/api/v1/
logging.level.org.springframework.boot.autoconfigure=ERROR
logging.level.org.springframework.security=DEBUG
app.java.version=@java.version@
```

### `application-dev.properties`
```properties
server.port=8077
spring.datasource.url=jdbc:mysql://localhost:3306/budgetblitz
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

app.security.jwt.private-key=${PRIVATE_KEY}
app.security.jwt.public-key=${PUBLIC_KEY}
```

### `application-prod.properties`
```properties
server.port=${PORT:8078}
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

app.security.jwt.private-key=${PRIVATE_KEY}
app.security.jwt.public-key=${PUBLIC_KEY}
```

---

### Getting Started
#### Prerequisites
Before you begin, ensure you have the following installed:

- Java 17 or later
- Maven 3.8+
- MySQL 8.0+

#### Installation
Clone the repository:
```
git clone https://github.com/Osama-Salih/BudgetBlitz.git
cd budget-blitz
```

---

#### Running the Application
Start the project using:
```
mvn spring-boot:run
```
Or build and run:
```
mvn clean package
java -jar target/budget-blitz-api-0.0.1-SNAPSHOT.jar
```

---

### API Documentation
Once the server is running, visit:  
- **Local:** [http://localhost:8077/swagger-ui/index.html](http://localhost:8077/swagger-ui/index.html)  
- **Production:** `https://<your-deployed-domain>/swagger-ui/index.html`

---

### Deployment (Sevalla Cloud)

**1. Add the following environment variables in your Sevalla dashboard:**
```
  PORT
  DB_URL
  DB_USERNAME
  DB_PASSWORD
  PUBLIC_KEY
  PRIVATE_KEY
```

**2. Build and deploy the project:**  
```bash
mvn clean package
```

The API will be accessible at [https://budgetblitz-dgi2p.sevalla.app](https://budgetblitz-dgi2p.sevalla.app)
Happy coding.
