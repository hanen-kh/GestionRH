# GestionRH

REST API for human resources management: employees, departments, leave requests, bonuses, users, and roles. Built with Spring Boot, secured with JWT, backed by PostgreSQL, and delivered through a Jenkins DevSecOps pipeline (SonarQube, Trivy, Docker).

## Features

- Employee and department management
- Leave (congé) requests with a validation workflow
- Bonus (prime) management
- User and role management with JWT authentication
- Interactive API documentation (Swagger UI)

## Tech stack

| Area | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.3.1, Spring Security, Spring Data JPA |
| Database | PostgreSQL (H2 for tests) |
| Build | Maven (wrapper included) |
| API docs | Springdoc OpenAPI |
| Tooling | Lombok |
| Delivery | Docker, Jenkins |
| Quality and security | SonarQube, Trivy, Hadolint |

## Architecture

The application follows a layered architecture:

```text
Controllers  →  Services  →  Repositories  →  PostgreSQL
     ↑              ↑
    DTOs         Entities
```

```text
GestionRH/
├── src/
│   ├── main/
│   │   ├── java/intern/gestionrh/
│   │   │   ├── Config          Security and application configuration
│   │   │   ├── Controllers     REST endpoints
│   │   │   ├── dto             Request and response objects
│   │   │   ├── Entities        JPA entities
│   │   │   ├── Repositories    Data access
│   │   │   ├── Services        Business logic
│   │   │   └── GestionRhApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                   Unit tests
├── Dockerfile
├── Jenkinsfile                 CI/CD pipeline
├── pom.xml
├── Entities.uml                Data model diagram
└── README.Docker.md            Docker details
```

## Prerequisites

- Java 17
- PostgreSQL (or Docker)
- Maven, or the included `mvnw` wrapper

## Configuration

Database settings live in `src/main/resources/application.properties`. Any property can be overridden with an environment variable, for example:

```bash
export SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5432/gestionrh'
export SPRING_DATASOURCE_USERNAME='your-db-user'
export SPRING_DATASOURCE_PASSWORD='your-db-password'
```

Do not commit real credentials or JWT secrets.

## Run locally

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`. Once started, the interactive API documentation is available at `/swagger-ui/index.html` on the application port.

## Run with Docker

Build the image yourself:

```bash
docker build -t gestionrh .
docker run -p 8080:8080 gestionrh
```

or pull the one published by the pipeline from Docker Hub (`khmilett/devsecops`, tagged `latest` and with the Jenkins build number). See [README.Docker.md](README.Docker.md) for details.

## Tests

```bash
./mvnw test
```

Tests use JUnit 5, Mockito, Spring Boot Test, and Spring Security Test, and run against an in-memory H2 database, so no PostgreSQL instance is required.

## CI/CD pipeline

The [`Jenkinsfile`](Jenkinsfile) defines a declarative pipeline that builds, tests, analyzes, scans, and publishes the application. Security checks run at every level: source code, dependencies, secrets, Dockerfile, and final image.

### Pipeline options

- Timestamps in the console log
- No concurrent builds of the same job
- 30-minute global timeout
- Only the last 10 builds are kept

### Stages

| # | Stage | What it does | Fails the build? |
| --- | --- | --- | --- |
| 1 | Checkout | Fetches the source code from SCM | Yes |
| 2 | Build & Unit Tests | Runs `mvn clean verify`; unit test results are published as JUnit reports | Yes |
| 3 | SonarQube Analysis (SAST) | Runs `mvn sonar:sonar` for project key `gestionrh`: static analysis for bugs, vulnerabilities, and code smells | Yes |
| 4 | Quality Gate | Waits up to 5 minutes for the SonarQube quality gate result | Yes, aborts if the gate fails |
| 5 | Scan Source (deps + secrets) | Trivy filesystem scan for vulnerable dependencies and leaked secrets (`HIGH`, `CRITICAL`, unfixed issues ignored) | No, report only |
| 6 | Lint Dockerfile | Hadolint checks Dockerfile best practices | No, warnings only |
| 7 | Build Docker Image | Builds the image tagged with the build number and `latest` | Yes |
| 8 | Scan Docker Image (Trivy) | Writes a JSON report, then scans the image for `HIGH` and `CRITICAL` vulnerabilities that have a fix | Yes |
| 9 | Push to Registry | Logs in and pushes both tags to Docker Hub, only on the `main` branch | Yes |

After every run (success or failure), the pipeline archives `trivy-report.json` and the built JAR, logs out of Docker, removes the local images, and cleans the workspace.

### Jenkins setup

For the pipeline to run, Jenkins needs:

- **Tools** named `Maven-3.9` and `JDK-17` (Manage Jenkins → Tools)
- **Plugins:** Pipeline, SonarQube Scanner, JUnit, Credentials Binding
- **SonarQube server** registered under the name `sonarqube` (Manage Jenkins → System), with a webhook pointing to `<jenkins-url>/sonarqube-webhook/` so the Quality Gate stage receives its result
- **Credentials:** a username/password entry with ID `dockerhub-credentials` (Docker Hub user and access token)
- **On the agent:** Docker, [Trivy](https://trivy.dev), and [Hadolint](https://github.com/hadolint/hadolint) installed
- **A multibranch pipeline job**, since the push stage depends on the branch name (`main`)
