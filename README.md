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

## Installation & Nutzung

1. **Voraussetzungen**
    - Java 21
    - Github-Account für Authentifizierung
    - Docker für die Datenbank
    - SSH Key für das Klonen des Repository
    - Einsetzen der Credentials in `example.env`

2. **Projekt klonen mit SSH**
   ```
   $ git clone git@github.com:Marvin0109/ExamByte.git

3. **Container starten**
   ```
   $ docker compose up -d
   
4. **Jar File bauen und starten**
   ```
   $ ./gradlew build
   $ java -jar build/libs/exambyte-chillex-0.0.1-SNAPSHOT.jar
   
5. **Runterfahren (`strg+c`) und Container mit Volumes löschen**
   ```
   $ docker compose down -v
   
## Dokumentation

Die ausführliche Architektur-Dokumentation finden Sie [hier](docs/arc42.md).

## Troubleshooting

### Testcontainer-Tests funktionieren nicht

Temporärer Workaround:
```
$ echo api.version=1.44 >> ~/.docker-java.properties
```

Quellen:

- [Stackoverflow: Docker-Error about client api version](https://stackoverflow.com/questions/79817033/sudden-docker-error-about-client-api-version)
- [Github: Testcontainer-Java issues](https://github.com/testcontainers/testcontainers-java/issues/11212#issuecomment-3516573631)

