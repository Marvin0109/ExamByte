<p align="center">
    <img src="src/main/resources/static/public/pictures/exambyteLogo.png" alt="ExamByte Logo" width="400">
</p>

# ExamByte

## Overview

> [!TIP]
> German version of README check [here.](README.de.md)

- [What is ExamByte?](#what-is-exambyte)
- [Features](#features)
- [Installation (Linux)](#installation-linux)
- [Usage and Demo](#usage-and-demo)
- [Architecture](#architecture)
- [Documentation](#documentation)
- [Contributors](#contributors)

> [!NOTE]
> Development of ExamByte officially ended on **March 24, 2026**.
>
> After extensive development, the current version has been declared final and
> no further features or changes will be implemented.
> Open or planned enhancements will no longer be pursued.
>
> The related development branch `feature/review-lock` was never merged and has therefore been discarded.
> The current state of the project is considered complete.
>
> Final code review has been completed. For more details and possible future ideas, check [To do](docs/todo.md).

## What is ExamByte?

ExamByte is a web application for conducting and grading tests in programming lab courses.
It replaces ILIAS as the testing platform for exam admission and provides:

- **Automatic grading** of multiple-choice questions
- **Manual review** of free-text tasks by reviewers
- **Management of test results** for students and organizers
- **User management** with GitHub authentication

## Features

- **Test management**: Create, preview, and conduct tests
- **Test evaluation**: Automatic grading of multiple-choice questions and manual review of free-text tasks
- **Admission status**: Students can track their progress and current admission status
- **Result overview**: Organizers have access to a complete overview of test results
- **Export feature**: Download test results as CSV files

## Local development (Linux)

### Requirements
- Java 21
- A GitHub account for authentication
- Docker for the database
- SSH setup for cloning the repository (optional)
- Add the required credentials to a `.env` file (use `.env.example` as a template)
- Create `application-local.yaml` by using the template `application-local.example.yaml`

### Cloning repository via SSH
```
$ git clone git@github.com:Marvin0109/ExamByte.git
```

For local development, PostgreSQL can be run in Docker while the Spring Boot application runs directly on the host
machine. 

Start the database container first:
```
$ docker compose up -d exambyteDB
```

Then start the application with the `local` Spring profile:
```
$ ./gradlew bootRun --args='--spring.profiles.active=local'
```

The application will then be available at `http://localhost:8080`.

### Shutting down the application
Stop the local Spring Boot application with `Ctrl+C`.

Then stop the Docker containers:
```
$ docker compose down
```

To also delete the database volume, use `-v`:
```
$ docker compose down -v
```

## Running the complete application with Docker

To run both the Spring Boot application and PostgreSQL in Docker:
```
$ ./gradlew clean bootJar
$ docker compose up -d --build
```

## Usage and Demo

> [!NOTE]
>
> The screenshots show the core features of ExamByte as of March 18, 2026.
>
> More screenshots [here](src/main/resources/static/public/demo).

### Creating tests

![Creating tests](src/main/resources/static/public/demo/createExam_1.png)

### Conducting tests

![Conducting tests](src/main/resources/static/public/demo/submitExam.png)

### Reviewing student submission

![Reviewing student submission](src/main/resources/static/public/demo/review.png)

### Results overview

![Results overview](src/main/resources/static/public/demo/showReview.png)

## Architecture

### Data flow and API calls
```mermaid
flowchart TD
    n1[Controller] -->|Request| n2[Service]
    n2 -->|Process Task| n3[Repository]
    n3 -->|Read/Write| n4[(Database)]
    n4 -->|Data| n3
    n3 -->|Result| n2
    n2 -->|Response| n1

    style n1 fill:#FF914D,stroke:#000,color:#000000
    style n2 fill:#7ED957,stroke:#000,color:#000000
    style n3 fill:#0097B2,stroke:#000,color:#000000
    style n4 fill:#CB6CE6,stroke:#000,color:#000000
```

### Dependencies of layers (Onion architecture)
```mermaid
flowchart TD
    classDef web fill:#f9f,stroke:#333,stroke-width:2px;
    classDef application fill:#bbf,stroke:#333,stroke-width:2px;
    classDef infrastructure fill:#bfb,stroke:#333,stroke-width:2px;
    classDef domain fill:#ffeb99,stroke:#333,stroke-width:2px;

%% Layer
    subgraph Web [Web Layer]
        direction TB
        Controller[Controller]:::web
    end

    subgraph Application [Application Layer]
        direction TB
        Service[Business Logic / Services]:::application
    end

    subgraph Infrastructure [Infrastructure Layer]
        direction TB
        Repository[Repository / DB Access]:::infrastructure
    end

    subgraph Domain [Domain Layer]
        direction TB
        Entity[Model / Core Logic]:::domain
    end

%% Flow
    Controller
    Service
    Service
    Repository
    Repository
    Entity
    Controller
    Application
    Service["Business Logic / Services"]
    Infrastructure
    Repository["Repository / DB Access"]
    Domain
    style Controller color:#000000
    style Service color:#000000
    style Repository color:#000000
    style Entity color:#000000
    Web --- Application
    Application --- Infrastructure
    Infrastructure --- Domain
```

## Documentation

- [arc42 architecture documentation](docs/arc42-architecture.md)
- [Activity log](docs/activity-log.md)
- [To do](docs/todo.md)
- [Styleguide](docs/style-guide.md)

## Contributors

- Marvin0109 - Main development and maintenance
- muz70wuc - Early-stage co-development

[Back to overview.](#overview)