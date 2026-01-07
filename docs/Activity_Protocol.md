# Aktueller Stand

Weitere Details zum Projekt und den Aufgaben findest du in der TO_DO.md.

*Hinweis*: Aktuell aktive Teilnehmer sind leider nur zwei (seit dem 11.02.25 ist Marvin0109 der alleinige Entwickler der 
Anwendung), vom Rest kam keine Rückmeldung trotz mehrmaliger Nachfragen.

---

## Aktivitätsprotokoll



### 07.11.24

**Bereich**: Frontend / Backend / Tests
- Erstellung von `index.html` und `contact.html`
- Notwendige Controller erstellt
- Tests geschrieben, die bei `gradle:build` fehlschlagen, aber manuell funktionen

---

### 08.11.24

**Bereich**: Frontend / Fehlerbehandlung
- Login-Popup implementiert
- 405-Post-Error-Page erstellt
- Weitere kleine Extras ergänzt

---

### 13.11.24

**Bereich**: Frontend / Struktur
- Navigationslogik von HTML-Seiten getrennt 
- `scripts.js` eingebunden

---

### 14.11.24

**Bereich**: Security
- OAuth2-Login implementiert

---

### 20.11.24

**Bereich**: Security / Frontend
- Admin Rolle hinzugefügt
- Problem: `loadUser()` wird nicht aufgerufen (Ursache unbekannt)
- 403-Error-Page erstellt
- `exams.html` angelegt
- Weitere Extras ergänzt

---

### 22.11.24

**Bereich**: Security / Tests
- Browser-Neustart notwendig, damit Rollenänderung greifen
- Benutzername in `loadUser()` temporär manuell gesetzt
- Tests für WebController und Authentifizierung geschrieben

---

### 24.11.24

**Bereich**: Security
- Logout-Funktion hinzugefügt
- Problem: Login-Daten werden beim Logout nicht vollständig gelöscht

---

### 28.11.24

**Bereich**: Architektur / Tests
- Projekt auf Onion-Architektur umgestellt  
- `IndexTest` zunächst fehlerhaft, kurze Zeit später behoben

---

### 17.12.2024

**Bereich**: Architektur / Tests
- Test zur Onion-Architektur geschrieben, jedoch fehlgeschlagen

---

### 07.01.2025
**Bereich**: Dokumentation
- Allgemeine Dokumentation erstellt

---

### 08.01.2025
**Bereich**: Dokumentation
- Styleguide hinzugefügt

---

### 09.01.2025
**Bereich**: Infrastruktur / Tests
- `docker-compose.yml` erstellt
- (Noch nicht funktionierender) Datenbanktest angelegt
- Gradle-Dependencies aktualisiert und dokumentiert
- `application.yml` setting

---

### 14.01.2025
**Bereich**: Datenbank / Domain
- SQL-Tabellen für User, Antwort und Frage erstellt
- Zugehörige Aggregate vorerst fertiggestellt

---

### 16.01.2025
**Bereich**: Architektur / Tests
- Onion-Architektur überarbeitet
- Architekturtest angepasst (schlägt erwartungsgemäß fehl)

---

### 21.01.2025
**Bereich**: Architektur / Persistenz
- Onion-Architektur in `domain` verlagert
- Mapper- und Entity-Klassen in `persistence` erstellt
- Bugfix in `ExamByteApplication.java` (`scanBasePackages`)
- Data-Ordner wird nun lokal gespeichert (nicht committen)

---

### 23.01.2025
**Bereich**: Datenbank / Tests
- Bugfix bei Serververbindung
- Professor- und Student-Repositories ergänzt
- Zugehörige Mapper erstellt
- Erster Test zum Datenaustausch zwischen Server-DB und lokalem System (noch nicht erfolgreich)

---


### 27.01.2025
**Bereich**: Persistenz / Tests
- Separate Klassen und Packages für JPA und JDBC
- JDBC-Test erstellt und erfolgreich ausgeführt
- Service-Klasse (implementiert Repository) angelegt 
- SQL Datei in vier Dateien aufgeteilt
- Hintergrund: Spring Data, Woche 11

---

### 29.01.2025
**Bereich**: Datenbank / Tests
- Flyway-Migration-Fehler behoben (separate Skripte für vier Tabellen)
- `DataBaseTest` schlägt weiterhin fehl (`No Bean Type Found`)
- Klassenverschiebungen analog zu VL Woche 12 (ohne Erfolg)

---

### 30.01.2025
**Bereich**: Infrastruktur / Refactoring
- JPA und H2 entfernt
- Testcontainers als einzige Testdatenbank verwendet
- Bean-Type-Error behoben
- Klassen umbenannt und verschoben
- UUID eingeführt (noch nicht vollständig getestet)

---

### 31.01.2025
**Bereich**: Domain / Tests
- Exam-Entität hinzugefügt
- Tests für Datenbankrelationen gestartet

---

### 01.02.2025
**Bereich**: Datenbank / Tests
- Vollständige Datenbanklogik umgesetzt
- Integrations- und Unit-Tests erstellt und ausgeführt
- `README`-Inhalte zu `Activity_Protocol` verschoben

---

### 02.02.2025
**Bereich**: Architektur / Refactoring
- Onion-Architektur und Tests überarbeitet
- Toten Code entfernt
- Neue Packages angelegt
- Builder-Pattern eingeführt

---

### 03.02.2025
**Bereich**: Service / Dokumentation
- Service-Klassen ergänzt
- arc42-Dokumentation erstellt
- README erneuert
- Onion-Test schlägt wegen Abhängigkeiten zwischen `application.config` und `application.service` fehl

---

### 04.02.2025
**Bereich**: Web / Tests
- Vier Exam-HTML-Seiten erstellt
- Passende Tests ergänzt
- Zusätzliche Methode in Antwort-Klassen
- ExamController erstellt (Korrekturbedarf vorhanden)

---

### 05.02.2025
**Bereich**: Security / Tests
- Bean-Dependency-Probleme in Tests behoben
- WebController angepasst und Zugriffsrechte ergänzt
- URLs in ExamController-Tests korrigiert
- Rolle `ROLE_USER` → `ROLE_STUDENT` geändert
- UUID-Extraktionsmethoden erstellt
- Onion-Test weiterhin fehlerhaft

---

### 06.02.2025
**Bereich**: Architektur / Tests
- Onion-Architektur stabilisiert
- Container- und Mapper-Tests angepasst
- Test für die Aggregate und Infrastructure geschrieben

---

### 07.02.2025
**Bereich**: Security / Tests
- OAuth2-Login, Rollenverteilung und Tests gefixt
- DTOs, Mapper und Interfaces vervollständigt
- Package-Dokumentationen (Javadoc) erstellt

---

### 09.02.2025
**Bereich**: Qualitätssicherung / Tests
- SpotBugs integriert
- Rollenverteilung angepasst (temporäre Lösung)
- Umgang mit `NichtVorhandenException` via `Optional<T>`
- DTOMapper- und Service-Tests ergänzt
- ExamControllerTests größtenteils behoben
- PowerMock-Dependencies ergänzt

---

### 18.02.2025
**Bereich**: Architektur
- Persistenz-Package in Infrastrukturschicht verschoben
- `domain.model.service` und `.impl` für zukünftige Logik erstellt
- Onion-Test angepasst

---

### 03.03.2025
**Bereich**:
- Tests korrigiert
- Unnötige Imports entfernt
- `AntwortServiceTest` gefixt

---

### 13.03.2025
**Bereich**: Umgebungsvariable
- `.env` Datei erstellt
- Fehleranalyse zu Issue #1

---

### 16.03.2025
**Bereich**: Build / Infrastruktur
- JaCoCo-Settings für Gradle 8.x aktualisiert
- Dotenv für Umgebungsvariablen integriert

---

### 29.03.2025
**Bereich**: Tests / Frontend
- Weitere Service-Tests erstellt
- Dateipfade in Exam-HTML-Seiten korrigiert
- `TestSystemPropertyInitializer` für Tests eingebunden

---

### 30.03.2025
**Bereich**: Infrastruktur / Tests
- Spring von 3.3.5 auf 3.4.3 aktualisiert
- DB-Umgebungsvariablen für Tests gesetzt
- `UserCreationTest` und `AppUserServiceTest` ergänzt

---

### 01.04.2025
**Bereich**: Domain / Tests
- Zulassungslogik inkl. Test umgesetzt
- Maximal 12 Exams für Zulassung
- Trennung von Domain- und Application-Schicht korrigiert

---

### 02.04.2025
**Bereich**: Bewertung / Tests
- Speicherung von MC/SC-Lösungen implementiert
- Automatische Bewertungslogik erstellt
- Tests angepasst
- ExamProfessoren erweitert (Fragen per Klick, noch fehlerhaft)

---

### 03.04.2025
**Bereich**: Frontend
- DOM hinzugefügt (fehlerhaft, technischer Schuldenpunkt)

---

### 05.04.2025
**Bereich**: Frontend
- Problem mit fehlerhaftem Indexing (Issue #2)

---

### 06.04.2025
**Bereich**: Frontend / UX
- Löschen mehrerer Fragen entfernt (zu komplex)
- Neue Logik: Nur letzte Frage kann gelöscht werden
- JavaScript-Code in `addQuestion.js`

---

### 17.04.2025
**Bereich**: Frontend / Tests
- Mehr Antwortoptionen für MC/SC (aktuell 6)
- Übergabe der Fragedaten als JSON
- Frageformular entfernt

---

### 18.04.2025
**Bereich**: Web / Tests
- JSON vom Model getrennt
- Zwei separate Forms erstellt
- `BindingResult` weiterhin fehlerhaft (White Error Page)

---

### 20.04.2025
**Bereich**: Web / Tests
- `BindingResult` gefixt (`@Valid` ohne `@ModelAttribute`)
- ExamManagementServiceTest erstellt (noch unvollständig)
- Feature: Anzeige der eigenen Fach-ID auf der Contact-Seite
- Weitere Details in Commits dokumentiert

---

### 11.08.2025
**Bereich**: Cleanup
- Löschfunktion (teilweise) implementiert
- TODO-Markierungen aktualisiert
- `update_progress.sh` gelöscht

---

### 12.08.2025
**Bereich**: Frontend / Export
- DOM-Event entfernt
- CSV-Export implementiert (noch ungetestet)
- Formular zur Exam-Erstellung angelegt

---

### 14.08.2025
**Bereich**: Domain / Tests
- Enums in allen Schichten definiert
- Tests für Fragetypen angepasst
- Speichern von Tests mit Fragen erfolgreich
- CSV-Export weiterhin fehlerhaft

---

### 08.09.2025
**Bereich**: Web / Tests
- Speicherung von Antwortmöglichkeiten ergänzt
- Anzeigen von Tests zur Bearbeitung funktionsfähig

---

### 09.09.2025
**Bereich**: Web
- Exams laden und Anzeige in `examsDurchfuehren.html`

---

### 11.09.2025
**Bereich**: Tests
- `SubmitExam` Test geschrieben
- Weitere TODOs ergänzt

---

### 15.09.2025
**Bereich**: Feature / Bewertung
- Extra Menü-Seite mit Testinformationen implementiert
- Automatischer Korrektor initialisiert
- Bewertungslogik noch fehlerhaft

---

### 17.09.2025
**Bereich**: Tests / Bugs
- Tests für automatische Bewertungen geschrieben
- Fehler dokumentiert (Issues)

---

### 22.09.2025
**Bereich**: Feature / CI
- Bearbeitung eines Tests implementiert
- SpotBugs deaktiviert
- Continuos Integration eingerichtet

---

### 23.09.2025
**Bereich**: Frontend / Dokumentation
- Tests an Änderungen vom 22.09. angepasst
- Aktiver Navigationsstatus für Menüpunkte
- Styleguide und TODO-Dokument aktualisiert

---

### 03.11.2025
**Bereich**: Dokumentation
- arc42 und README aktualisiert

---

### 04.01.2026
**Bereich**: Infrastruktur / Tests
- Docker auf Version 29.1.3 aktualisiert
- Testcontainers-Problem mit alter API-Version analysiert und behoben
- Fix: https://stackoverflow.com/questions/79817033/sudden-docker-error-about-client-api-version
- `TestSystemPropertyInitializer` entfernt
- Bewertungslogik: Nur letzter Versuch zählt
- Weitere Tests noch offen

---

### 05.01.2026
**Bereich**: Refactoring / UI
- Variablennamen in der Persistenzschicht angepasst
- Button im Test-Menü geändert

---

### 06.01.2026
**Bereich**: Web / UX / Dokumentation
- Statusmeldung auf Exam-Erstellungsseite korrekt eingebunden
- Logik ergänzt: Bearbeitung vor Startzeit nicht möglich
- Navigationsleiste zusätzlich unten eingefügt
- Exam-Übersicht optimiert (Statusanzeige + Tabelle)
- `Activity_Protocol` umgeschrieben

---

### 07.01.2026
**Bereich**: Frontend / Web / UX / Test
- Bewertungsübersicht vorerst implementiert
- Bewertungsstatus von manueller Korrekturen pro Exam eingebunden
- Angefangene Tests, weitere folgen