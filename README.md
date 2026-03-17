<p align="center">
    <img src="src/main/resources/static/public/pictures/exambyteLogo.png" alt="ExamByte Logo" width="400">
</p>

# ExamByte

ExamByte ist eine Webanwendung zur Durchführung und Bewertung von Tests im Programmierpraktikum. 
Es ersetzt ILIAS als Testsystem für die Klausurzulassung und ermöglicht:

- **Automatische Bewertung** von Multiple-Choice-Fragen
- **Manuelle Korrektur** von Freitextaufgaben durch Korrektor:innen
- **Verwaltung von Testergebnissen** für Studierende und Organisator:innen
- **Benutzerverwaltung** mit GitHub-Authentifizierung

## Funktionen

- **Testverwaltung**: Erstellen, Vorschau und Durchführung von Tests
- **Testbewertung**: Automatische Bewertung von MC-Fragen, manuelle Bewertung von Freitextaufgaben
- **Zulassungsstatus**: Studierende sehen ihren Fortschritt und den aktuellen Zulassungsstand
- **Ergebnisübersichten**: Organisator:innen haben eine Gesamtübersicht der Testergebnisse
- **Exportfunktion**: Testergebnisse als CSV-Datei herunterladen

## Installation

### Voraussetzungen
- Java 21
- Github-Account für Authentifizierung
- Docker für die Datenbank
- Einrichtung von SSH für das Klonen des Repository (optional)
- Einsetzen der Credentials in einer `.env`-Datei (verwende hier für `.env.example`)

### Projekt klonen mit SSH
```
$ git clone git@github.com:Marvin0109/ExamByte.git
```

### Container starten
> [!IMPORTANT]
> Docker Container **immer** vor der Ausführung der `.jar` starten, ansonsten wird die Anwendung nicht starten.
```
$ docker compose up -d
```
   
### JAR-File bauen und starten
```
$ ./gradlew build
$ java -jar build/libs/exambyte-chillex-0.0.1-SNAPSHOT.jar
```
   
> [!WARNING]
> Wenn Testcontainer nicht die Docker API-Version `1.44` erkennen tut, werden die Integrationstests fehlschlagen.
> Workaround:
> - Docker aktualisieren
> - Spring Boot aktualisieren
> - Temporär die API-Version manuell setzen (**langfristig nicht empfohlen**)
>   ```
>   $ echo api.version=1.44 >> ~/.docker-java.properties
>   ```
>   
>   Quellen:
> - [Stackoverflow: Docker-Error about client api version](https://stackoverflow.com/questions/79817033/sudden-docker-error-about-client-api-version)
> - [Github: Testcontainer-Java issues](https://github.com/testcontainers/testcontainers-java/issues/11212#issuecomment-3516573631)
   
### Runterfahren
```
$ ^C # Verwende strg+c
$ docker compose down
```

> [!TIP]
> Falls auch die DB-Daten gelöscht werden sollen, verwende Argument `-v`
> ```
> $ docker compose down -v # Volumes werden gelöscht
> ```

## Nutzung

<video src="src/main/resources/static/public/demo/Login.mp4" width="500" autoplay muted loop></video>

Restliche Demos folgen ...

## Architektur

### Datenfluss und API-Calls
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

### Abhängigkeiten der Layers (Onion Architektur)

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

## Dokumentation

- [arc42-Architekturdokumentation](docs/arc42.md)
- [Aktivitätsprotokoll](docs/Activity_Protocol.md)
- [TODO](docs/TO_DO.md)
- [Styleguide](docs/STYLEGUIDE.md)

## Mitwirkende

- Marvin0109 - Hauptentwicklung und Wartung
- muz70wuc - Mitentwicklung in der Anfangsphase

