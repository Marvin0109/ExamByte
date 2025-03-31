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
    - Java 21 & Spring Boot
    - GitHub-Account für Authentifizierung
    - Konfigurationsdatei für Rollenverwaltung (?)
    - Docker für die Datenbank
    - SSH Key für das einfachere klonen des Repository

2. **Projekt klonen mit SSH**
   ```sh
   git clone git@github.com:Marvin0109/ExamByte.git
   cd yourDirectory/exambyte-chillex

3. **Projekt starten**
   ...
   
## Dokumentation

Die ausführliche Architektur-Dokumentation findest du [hier](docs/arc42.md).
