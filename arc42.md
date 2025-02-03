# ExamByte - Architektur-Dokumentation (arc42)

## 1. Einführung und Ziele

### 1.1 Aufgabenstellung

ExamByte ist eine Webanwendung zur Verwaltung und Durchführung von Prüfungen im Programmierpraktikum. 
Sie ersetzt Ilias als System zur Zulassung für die Abschlussprüfung. Die Anwendung ermöglicht die Durchführung von Tests, 
manuelle Bewertung von Freitextaufgaben durch Korrektor:innen und eine Ergebnisauswertung für Studierende und 
Administrator:innen.

### 1.2 Qualitätsziele

- **Benutzerfreundlich:** Einfache Bedienung für Studierende, Korrektor:innen und Administrator:innen
- **Sicherheit:** Nutzung von GitHub-Authentifizierung zur sicheren Anmeldung.
- **Automatisierung:** Automatische Bewertung von Multiple-Choice-Aufgaben.
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
| Betrieb auf Betriebssystemen | Jegliche Linux Distro oder auch Windows erwünscht                                                                                                    |
| Implementierung in Java      | Anwendung wurde im Java-lastigen Semester entwickelt. Entwicklung unter Version Java 21. Die Anwendung soll auch auf neuere Versionen laufen können. |
| Fremdsoftware frei verfügbar | Für die Authentifizierung ist die GitHub OAuth App nötig, welche kostenlos ist für den Eigengebrauch.                                                |

### 2.2 Organisatorisch

| Randbedingung                          | Erläuterung                                                                                                                                                                                     |
|----------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Team                                   | muz70wuc und Marvin0109                                                                                                                                                                         |
| Zeitplan                               | Beginn der Entwicklung Anfang November 2024                                                                                                                                                     |
| Vorgehensmodell                        | Entwicklung risikogetrieben, iterativ und inkrementell. Zur Dokumentation der Architektur kommt arc42 zum Einsatz.                                                                              |
| Entwicklungswerkzeuge                  | Der Entwurf war schon bekannt durch das verwenden des Ilias System im Studium. Arbeitsergebnisse in Activity_Protocol.md gesammelt worden. Erstellung der Java-Quelltexte in IntelliJ Ultimate. |
| Konfigurations- und Versionsverwaltung | Git bei GitHub                                                                                                                                                                                  |
| Testwerkzeuge und -prozesse            | JUnit im Annotationsstil sowohl für inhaltliche Richtigkeit als auch für Integrationstests. Testcontainer für Datenbanktests.                                                                   |
| Veröffentlichung als Open Source       | N/A                                                                                                                                                                                             |

### 2.3 Konventionen

| Konvention                 | Erläuterung                                                                                                                                                                                                           |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Architekturdokumentation   | Terminologie und Gliederung nach dem deutschen arc42-Template in der Version 6.0                                                                                                                                      |
| Kodierrichtlinien für Java | Java Format nach Google-Java-Format, geprüft mit Hilfe von in der IDE eingebautem Google-Java-Format Plugin                                                                                                           |
| Sprache (DE vs EN)         | Benennung von Dingen (Komponenten, Schnittstellen) in Diagrammen und Texten innerhalb dieser (deutschen) arc42-Architekturdokumentation in Deutsch. Objekte wie Rollen in Java auf deutsch, alles andere auf Englisch |

## 3 Kontextabgrenzung

### 3.1 Fachlicher Kontext

