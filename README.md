# SmartRestaurantPos 🍽️

**SmartRestaurantPos** is a Restaurant Management and Point of Sale (POS) system built with **Java 21 and Spring Boot**. The project aims to simplify restaurant operations by providing a centralized REST API for managing users, restaurant information, products, orders, tables, payments, and inventory.

The system follows a layered architecture and applies object-oriented programming principles, secure authentication, and role-based authorization to create a maintainable and scalable backend application.

## 🎯 Project Objectives

- Simplify restaurant management and daily operations.
- Manage restaurant staff and access permissions.
- Organize products, categories, and restaurant tables.
- Manage customer orders and order processing.
- Support POS operations and payment management.
- Track inventory and stock movements.
- Provide structured REST APIs for future frontend integration.

## ✨ Key Features

### 1. Authentication & Authorization
- User registration and login.
- JWT-based authentication.
- Role-Based Access Control (RBAC).
- Secure password hashing.
- Token version management for token invalidation.
- Owner-controlled account registration and permissions.

### 2. Restaurant Management
- Manage restaurant information.
- Manage restaurant branches.
- Manage staff accounts and roles.

### 3. Product Management
- Create, update, and delete products.
- Organize products by categories.
- Manage product pricing and availability.

### 4. Table Management
- Manage restaurant tables.
- Track table availability and status.
- Support table-based ordering.

### 5. Order Management
- Create and manage customer orders.
- Track order status.
- Support kitchen order processing.
- Manage order details and quantities.

### 6. POS & Payment Management
- Process customer orders.
- Calculate order totals.
- Manage payment records.
- Track sales transactions.

### 7. Inventory Management
- Track stock quantities.
- Manage ingredients and supplies.
- Monitor stock movements.

### 8. Reports & Dashboard
- Monitor restaurant sales.
- View order summaries.
- Track business performance.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4.1.2 | Backend framework |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| PostgreSQL | Relational database |
| Maven | Dependency management |
| Docker & Docker Compose | Containerization |
| Postman | API testing |
| Lombok | Reduce boilerplate code |

## 🏗️ Architecture

The application follows a layered architecture:

Controller → Service → Repository → Database

- **Controller:** Handles HTTP requests and API responses.
- **Service:** Contains business logic and application rules.
- **Repository:** Handles database operations.
- **Entity:** Represents database tables.
- **DTO:** Transfers data between API layers.
- **Security:** Handles authentication and authorization.
- **Exception Handler:** Provides consistent error responses.

## 🔐 Security Design

The system uses role-based permissions to control access to restaurant resources.

Planned roles include:
- OWNER
- ADMIN
- CASHIER
- WAITER
- KITCHEN

Only the OWNER is permitted to create ADMIN and STAFF accounts through the protected registration workflow.

## 🚀 Development Status

SmartRestaurantPos is an ongoing backend development project. Features are being implemented incrementally, with a focus on secure authentication, clean architecture, database design, and reliable business logic.

## 👨‍💻 Developer

Developed by **Navin**.

GitHub: https://github.com/navin-net

