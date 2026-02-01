# 🚀 Issue Tracker API (Spring Boot · API-First Backend)

An API-first Issue Tracking backend built with Spring Boot and MySQL, focused on
business rule enforcement, clean REST design, and explicit authorization logic.

⚠️ No UI by design  
⚠️ No JWT / Spring Security  
⚠️ Postman is the client  

This project prioritizes correctness, clarity, and backend discipline over feature count.

---

## ✨ Key Highlights

- Clean REST APIs with proper HTTP semantics
- Explicit ownership & authorization (without frameworks)
- Strict issue lifecycle validation
- Logical delete using ARCHIVED state
- DTO-driven API contracts (entities never exposed)
- JPQL projections with pagination & filtering
- Centralized, predictable error handling

---

## 🧱 Tech Stack

Backend:
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate

Database:
- MySQL

Tools:
- Maven
- IntelliJ IDEA
- Postman
- Git

---

## 📂 Project Structure

com.samyak.api
 ├── controller
 │    └── api
 ├── service
 │    └── impl
 ├── repository
 ├── entity
 ├── dto
 ├── exception
 ├── exception.handler
 ├── util
 └── config

This structure enforces separation of concerns and mirrors real-world backend projects.

---

## 🧩 Domain Overview

### User
Used only for ownership and assignment logic.

Fields:
- id
- name
- email
- status (ACTIVE / INACTIVE)
- createdAt
- updatedAt

### Issue
Represents a bug / task / issue.

Fields:
- id
- title
- description
- status (OPEN / IN_PROGRESS / RESOLVED / ARCHIVED)
- priority (LOW / MEDIUM / HIGH)
- createdBy (userId)
- assignedTo (userId)
- createdAt
- updatedAt

Notes:
- Issues are never deleted
- ARCHIVED is a terminal, read-only state

---

## 🔐 Authorization Model (No JWT)

There is no authentication framework.

User identity is passed explicitly via request header:

X-USER-ID: <userId>

Authorization rules are enforced directly in the service layer, making the logic:
- Transparent
- Testable
- Easy to explain in interviews

---

## 🔄 Issue Lifecycle Rules

OPEN        -> IN_PROGRESS
IN_PROGRESS -> RESOLVED
RESOLVED    -> (no changes allowed)
ARCHIVED    -> (no changes allowed)

Invalid transitions return:
400 BAD REQUEST

---

## 🌐 API Endpoints

Base path:
/api/issues

Endpoints:
- POST   /api/issues              -> Create issue
- GET    /api/issues              -> List issues (pagination + filters)
- GET    /api/issues/{id}          -> Get issue by ID
- PUT    /api/issues/{id}/assign   -> Assign issue
- PUT    /api/issues/{id}/status   -> Update issue status
- POST   /api/issues/{id}/archive  -> Archive issue

---

## 📦 DTO-Driven Design

- Controllers communicate only via DTOs
- Entities remain internal to the service layer
- JPQL uses DTO projections for list APIs

This prevents accidental data leaks and keeps APIs stable.

---

## ❗ Error Handling

All errors follow a single, predictable format:

{
  "errorCode": "ACCESS_DENIED",
  "message": "Only creator can archive issue"
}

Handled via a global exception handler.

---

## 🧪 Testing

- No UI
- No automated tests (intentional)
- Entire API tested using Postman
- Includes success, failure, and edge-case scenarios

Postman acts as the API client.

---

## ❓ Why No UI?

This project focuses on backend correctness.

Using Postman forces:
- Explicit error handling
- Proper HTTP status usage
- Clear API contracts
- Realistic client-server interaction

---

## 🧠 What This Project Demonstrates

- API-first backend thinking
- Ownership enforcement without security frameworks
- State-machine style business logic
- Enterprise-style error handling
- Clean layering and discipline

---

## 🚀 Getting Started

Prerequisites:
- Java 17
- MySQL
- Maven

Run:
mvn spring-boot:run

Database must exist before startup.

---

## 👤 Author

Samyak  
B.Tech IT Student  
Backend-focused | Spring Boot | API Design

---

## 📝 License

This project is for learning and demonstration purposes.
