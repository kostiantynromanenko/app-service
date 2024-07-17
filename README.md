# Application - Backend
This is the backend part of the system, built using Java Spring Boot and Java 17. It facilitates creating, storing, and managing game metadata, as well as generating and managing docker-compose files for PostgreSQL databases, and storing them in S3 buckets using MinIO.

## Requirements
- Java 17
- Gradle 7.2

## Dependencies
- MongoDB
- MinIO

# Configuration
The application requires the following environment variables to be set:
- Mongo:
  - `MONGO_DB` - the name of the MongoDB database
  - `MONGO_HOST` - the host of the MongoDB instance
  - `MONGO_PORT` - the port of the MongoDB instance
  - `MONGO_USERNAME` - the username for the MongoDB instance
- MinIO:
  - `MINIO_ENDPOINT` - the URL of the MinIO instance
  - `MINIO_ACCESS_KEY` - the access key for the MinIO instance
  - `MINIO_SECRET_KEY` - the secret key for the MinIO instance

## Installation

- Build the project
```bash
./gradlew build
```
- Run the application
```bash
./gradlew bootRun
```

Alternatively, you can run the application using Docker Compose. The `docker-compose.yml` file is located in the root directory of the project.
It will start the application, MongoDB, and MinIO.