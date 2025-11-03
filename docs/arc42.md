# ExamByte - Architektur-Dokumentation (arc42)

## Metainformationen

- **Titel**: ExamByte - Architektur-Dokumentation (arc42)
- **Autor**: Marvin0109
- **Version**: 1.0
- **Erstellt am**: 03. Februar 2025
- **Aktualisiert am**: 03. November 2025
- **Zielgruppen**: Entwickler, Benutzer
- **Verwendete Werkzeuge**: PlantUML

## 1. Einführung und Ziele

### 1.1 Aufgabenstellung

ExamByte ist eine Webanwendung zur Verwaltung und Durchführung von Prüfungen im Programmierpraktikum. 
Sie ersetzt Ilias als System zur Zulassung für die Abschlussprüfung. Die Anwendung ermöglicht die Durchführung von Tests, 
manuelle Bewertung von Freitextaufgaben durch Korrektor:innen und eine Ergebnisauswertung für Studierende und 
Administrator:innen.

### 1.2 Qualitätsziele

- **Benutzerfreundlich:** Einfache Bedienung für Studierende, Korrektor:innen und Administrator:innen
- **Sicherheit:** Nutzung von GitHub-Authentifizierung zur sicheren Anmeldung.
- **Automatisierung:** Automatische Bewertung von Multiple-Choice-/Single-Choice-Aufgaben.
- **Skalierbarkeit:** Unterstützung mehrerer gleichzeitiger Prüfungen und Nutzer:innen.
- **Nachvollziehbarkeit:** Klare Statusübersicht über Testergebnisse und Zulassung.

### 1.3 Stakeholder

| Rolle               | Interesse                                       |
|---------------------|-------------------------------------------------|
| Studierende         | Teilnahme an Tests, Prüfungsergebnisse einsehen |
| Korrektor:innen     | Bewertung von Freitextaufgaben                  |
| Administrator:innen | Testverwaltung, Ergebnisse überprüfen           |
| Entwickler:innen    | Wartung und Weiterentwicklung                   |

## 2. Randbedingung

### 2.1 Technische Randbedingungen

| Randbedingung                | Erläuterung                                                                                                                                          |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| Hardwareausstattung          | Betrieb der Anwendung auf PC und Notebooks (solange keine zu alte Hardware im Betrieb kommt läuft die Anwendung auf jedem PC/Notebook)               |
| Betrieb auf Betriebssystemen | Jegliche Linux Distro oder auch Windows mit WSL erwünscht                                                                                            |
| Implementierung in Java      | Anwendung wurde im Java-lastigen Semester entwickelt. Entwicklung unter Version Java 21. Die Anwendung soll auch auf neuere Versionen laufen können. |
| Fremdsoftware frei verfügbar | Für die Authentifizierung ist die GitHub OAuth App nötig, welche kostenlos ist für den Eigengebrauch.                                                |

### 2.2 Organisatorisch

| Randbedingung                    | Erläuterung                                                                                                                                                                                                                                                                                                                                      |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Team                             | muz70wuc und Marvin0109                                                                                                                                                                                                                                                                                                                          |
| Zeitplan                         | Beginn der Entwicklung Anfang November 2024                                                                                                                                                                                                                                                                                                      |
| Vorgehensmodell                  | Das Projekt wurde parallel zur Vorlesung *Programmierpraktikum 2* entwickelt. Methoden wie *Domain Storytelling* wurden erst im Laufe der Veranstaltung eingeführt und standen daher zu Projektbeginn **nicht** zur Verfügung, was Auswirkungen hatte auf die Entwicklungszeit. <br/> Zur Dokumentation der Architektur kommt arc42 zum Einsatz. |
| Entwicklungswerkzeuge            | Der Entwurf war schon bekannt durch das verwenden des Ilias System im Studium. Arbeitsergebnisse sind im [Aktivitätsprotokoll](Activity_Protocol.md) gesammelt worden. Erstellung der Java-Quelltexte in IntelliJ Ultimate.                                                                                                                      |
| Versionsverwaltung               | Git, GitHub                                                                                                                                                                                                                                                                                                                                      |
| Testwerkzeuge und -prozesse      | JUnit, Integrationstests, WebMvcTests und Testcontainer für Datenbanktests.                                                                                                                                                                                                                                                                      |
| Veröffentlichung als Open Source | N/A                                                                                                                                                                                                                                                                                                                                              |

### 2.3 Konventionen

| Konvention                 | Erläuterung                                                                                                                                                                                                           |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Architekturdokumentation   | Dieses Dokument stellt die Architektur der Software dar und befindet sich in der Version 1.0, welche die erste vollständige und stabile Version ist.                                                                  |
| Kodierrichtlinien für Java | Java Format nach Google-Java-Format, geprüft mit Hilfe von in der IDE eingebautem Google-Java-Format Plugin                                                                                                           |
| Sprache (DE vs EN)         | Benennung von Dingen (Komponenten, Schnittstellen) in Diagrammen und Texten innerhalb dieser (deutschen) arc42-Architekturdokumentation in Deutsch. Objekte wie Rollen in Java auf deutsch, alles andere auf Englisch |

Alles Weitere an Konventionen: [Styleguide hier](STYLEGUIDE.md)

## 3 Kontextabgrenzung

### 3.1 Fachlicher Kontext

![Exam Process Diagram](../src/main/resources/static/public/pictures/ExamByteProzess.png)

*Abbildung 1: Diagramm des Prüfungsprozesses*

## 4 Lösungssicht

### 4.1 Architekturübersicht

Die Anwendung folgt einer klassischen **Client-Server-Architektur:**

- **Frontend:** Web-App für Studierende, Korrektor:innen und Administrator:innen
- **Backend:** REST-API mit Spring Boot
- **Datenbank:** PostgreSQL für Test- und Benutzerverwaltung

### 4.2 Hauptkomponenten

- **Benutzermanagement:** Rollenverwaltung, GitHub-Login
- **Testverwaltung:** Erstellung, Bearbeitung und Veröffentlichung von Tests
- **Bewertungssystem:** Automatische MC-/SC-Bewertung, manuelle Freitextbewertung
- **Ergebnisanzeige:** Visualisierung der Testergebnisse für alle Beteiligten

## 5 Bausteinsicht

- **Controller-Schicht** (Spring MVC): Bearbeitung von Anfragen
- **Service-Schicht:** Geschäftslogik und Validierung
- **Datenbank-Schicht:** Speicherung und Abruf von Daten

## 6 Laufzeitsicht

### 6.1 Erstellung eines Tests

1. Administrator:innen (besser gesagt: Professor:innen) erstellen einen Test.
2. Sie füllen das Testformular aus mit Fragestellungen, Antwortmöglichkeiten, Punkte, ...
3. Sie setzen die relevanten Zeiten fest (Startzeit, Frist, Veröffentlichung der Ergebnisse).
4. Am Ende erhalten Sie eine Übersicht in Form einer CSV-Datei. (**STILL IN PROGRESS**)

### 6.2 Testdurchführung

1. Studierende melden sich mit GitHub an.
2. Sie öffnen einen aktiven Test.
3. Sie beantworten die Fragen.
4. Nach Ablauf der Testzeit wird die Bearbeitung gesperrt.
5. Der Zulassungsstatus ist immer einsehbar und wird aktualisiert.

### 6.3 Bewertung eines Tests

1. MC-/SC-Fragen werden automatisch bewertet.
2. Freitextantworten werden Korrektor:innen zugewiesen.
3. Korrektor:innen bewerten die Antworten und geben Feedback.

### 6.4 Zulassungsstatus

Nach einer bestimmten Anzahl an Tests (12 Tests) wird der Zulassungsstatus aktualisiert und gibt bekannt, ob die 
Zulassung erreicht wurde oder nicht.

## 7 Verteilungssicht

- **Server:** Webserver mit Spring Boot Backend
- **Datenbank:** PostgreSQL-Datenbank
- **Frontend:** Browserbasierte Webanwendung

## 8 Qualitätsszenarien

| Qualitätsziel  | Beispiel                                                                     |
|----------------|------------------------------------------------------------------------------|
| Sicherheit     | Nur angemeldete Benutzer können Tests durchführen                            |
| Skalierbarkeit | Mehrere Studierende können parallel Tests bearbeiten (**STILL IN PROGRESS**) |
| Verfügbarkeit  | Server muss während Tests online sein                                        |

## 9 Risiken und technische Schulden

- **Mögliche Risiken:**
  - Überlastung der Server bei hoher Nutzerzahl.
  - Sicherheit von GitHub OAuth-Anmeldedaten.

## 10 Glossar

| Begriff             | Bedeutung                                                                                                                         |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| MC                  | Multiple-Choice                                                                                                                   |
| Administrator:innen | Da es sich um eine Anwendung zwischen Studierende und Professoren handelt, ist hier der Administrator:in mit Professor:in gemeint |
| OAuth               | Offenes Authentifizierungsprotokoll                                                                                               |
| SC                  | Single-Choice                                                                                                                     |

