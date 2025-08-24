# E-commerce Project

This is a full-stack **E-commerce application** with a **React frontend** and **Spring Boot backend**, using **PostgreSQL** as the database. The project supports features such as product management, user authentication, cart and order handling, and payment integration.  

---

## Features

- User authentication with JWT
- Product catalog with categories and search
- Shopping cart and checkout
- Payment integration with PayPal
- Image upload using Cloudinary
- RESTful API with Spring Boot
- React frontend with modern UI

---

## Tech Stack

- **Frontend:** React, JavaScript, CSS
- **Backend:** Spring Boot, Java
- **Database:** PostgreSQL
- **Authentication:** JWT
- **Payments:** PayPal
- **Cloud Storage:** Cloudinary

---

## Environment Variables

Create a `.env` file in the root of your backend project (and frontend if needed). Below is an **example `.env`**:

```env
# ------------------------
# Cloudinary Configuration
# ------------------------
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret

# ------------------------
# PayPal Configuration
# ------------------------
PAYPAL_CLIENT_ID=your-client-id
PAYPAL_CLIENT_SECRET=your-client-secret
PAYPAL_MODE=sandbox

# ------------------------
# JWT Configuration
# ------------------------
JWT_SECRET=your-jwt-secret
JWT_EXPIRATION=43200000   # 12 hours in milliseconds

Appliation properties example.

# ------------------------
# Spring Application
# ------------------------
spring.application.name=server
server.port=5000
server.servlet.session.cookie.same-site=none

# ------------------------
# PostgreSQL Database Configuration
# ------------------------
spring.datasource.url=jdbc:postgresql://localhost:5432/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.datasource.driver-class-name=org.postgresql.Driver

# ------------------------
# JPA / Hibernate
# ------------------------
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

