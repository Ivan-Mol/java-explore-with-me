# java-explore-with-me


https://github.com/Ivan-Mol/java-explore-with-me/pull/3

A two-module microservice application for event detection and search, in which the functionality depends on the role (public, authorized user or administrator).

The application consists of a regular service, a main database, service view statistics, and a database for statistics. Each part is lifted in a separate docker container.

Stack:
Java 17 (Core, Collections, Optional, Stream)
Spring Boot
Hibernate
PostgreSQL
Maven
Lombok
Postman
Docker

## Application structure

Main API
The main service in which the main functions of applications are implemented
/users
/events
/Requests
/collections
/categories

Stats API
The statistics service stores the number of views and offers various samples for analyzing application performance.
/strike
/statistics
/views

## Database ERD

<img width="544" alt="db ewm" src="https://github.com/Ivan-Mol/java-explore-with-me/assets/94922468/a6b09809-1209-4ce9-8ff6-218b17f0558f">


