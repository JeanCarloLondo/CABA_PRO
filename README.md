# CABA Pro - Comprehensive Basketball Arbitration Management System

## Project Overview
CABA Pro is a web-based system designed to centralize and automate the daily operations of a basketball arbitration institution. It replaces manual processes such as spreadsheets, WhatsApp groups, and phone calls with an efficient and scalable platform.

The system provides tools for managing referee profiles, assigning roles in scheduled games, handling official communication, and generating payment reports with precision and transparency.

## Main Features

- Referee Management: create and update referee profiles (photo, role, ranking, FIBA/Division level, etc.).

- Game Scheduling: register and calendar matches.

- Assignment Lifecycle: assign referees to matches, notify them, track accept/reject responses.

- Role-based Security: access control for ADMIN and REFEREE users.

- Financial Reports: define fees per ranking/tournament and generate monthly PDF liquidation reports per referee.

- Statistics: dashboards with insights such as match acceptance rates, top 5 referees, and active tournaments.

## Team Members

- Jean Carlo Londoño Ocampo

- Alejandro Garcés Ramírez

- María Alejandra Ortiz Correa

## Technologies

- Java 17+

- Spring Boot (MVC, Security)

- Spring Data JPA (Repositories)

- Thymeleaf (UI templating)

- H2 Database (default dev mode)

- iText (lowagie) for PDF export

- Maven build system

## Prerequisites

- JDK 17+ (java -version)

- Maven (mvn -v)

- Git

- (Optional for production) MySQL/Postgres database

## Installation & Quickstart

### Clone the repository

git clone https://github.com/your-org/cabapro.git

cd cabapro


### Build the project

mvn clean package -DskipTests

### Run in development mode

mvn spring-boot:run

### Run the packaged JAR

java -jar target/*.jar

### Access the application

Login: http://localhost:8080/login

H2 console (dev only): http://localhost:8080/h2-console

Default JDBC URL:

jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

## Configuration (Dev / Prod)

Dev (H2 in-memory, default)

spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

spring.datasource.username=sa

spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true

## Interesting Features (Beyond CRUD)

- PDF Liquidation Reports — automated monthly reports per referee, exported as PDFs.

- Statistics / KPIs — real-time dashboard with match acceptance rates, top referees, and tournament stats.

- Smart Search — partial, case-insensitive search by email in referee lists.

- Role-based Routing — post-login redirection based on user role, ensuring protected navigation.
