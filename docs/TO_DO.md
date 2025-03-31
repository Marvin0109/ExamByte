# Projekt: ExamByte

## 1. Aktueller Stand (Bestandsaufnahme)
- **Evaluationsfrist:** 11.02.2025 
- **Status:** ![Fortschritt](https://img.shields.io/badge/Fortschritt-70%25-brightgreen) <!-- 70 -->
- **Was funktioniert bereits:**
  - [x] HTML-Seiten Layout (Grundstruktur)
  - [x] Datenbank-Verbindung (Erfolgreich eingerichtet und getestet)
  - [x] Login (OAuth2-Integration funktioniert)
  - [x] Grundlegende Projektstruktur (Backend und Frontend integriert)

- **Begonnene, aber unvollständige Funktionen:**
  - [ ] Datenspeicherung von Test-Antworten (Backend-Logik teilweise implementiert) *BEDARF ZUR VERBESSERUNG*
  - [ ] Testfragen-Erstellungsseiten (HTML vorhanden, aber ohne Funktionalität) *BEDARF ZUR VERBESSERUNG*
  - [ ] Testauswertungsseiten (Frontend noch unvollständig)
  - [ ] Datenvalidierung
  
---

## 2. Offene Aufgaben (To-Do-Liste)

### Priorität: Hoch
- **Backend**
  - [ ] **Datenspeicherung**: Antworten aus Formularen extrahieren und in der Datenbank speichern (inkl. Validierung). *muss noch überprüft werden*
  - [x] **Datenbank-Tests**: Integrationstests für die Datenbankanbindung schreiben.
  - [x] **Mapper-Tests**: Unit-Tests für die Mapper Klassen schreiben.
  - [ ] **API-Routen**: Routen für den Zugriff auf gespeicherte Testergebnisse und Testfragen erstellen.

- **Frontend**
  - [x] **Testfragen-Seiten**: HTML und JavaScript für die dynamische Anzeige von Fragen.
  - [x] **Testantwort-Einsicht**: Ansicht für Admins/Lehrkräfte, um Testergebnisse einzusehen.
  - [ ] **Formularvalidierung**: Clientseitige Validierung (Pflichtfelder, Datentypen etc.).

- **Dokumentation**
  - [x] **Arc42 Dokumentation**: Architektur-Dokumentation (Grundstruktur vorhanden, muss ergänzt werden).
  - [x] **Code-Dokumentation**: Kommentare und JavaDoc für alle zentralen Klassen.

---

### Priorität: Mittel
- [x] **Java-Konventionen**: Sicherstellen, dass der Code durchgehend den Konventionen entspricht (Checkstyle verwenden). (s. arc42 Konventionen)
- [ ] **Unit-Tests für Logik**: Tests für zentrale Logik-Komponenten (z. B. Punktberechnung bei Tests).
- [ ] **UI-Design verbessern**: Ansprechenderes Design (z. B. Farbgebung, Usability).

---

### Priorität: Niedrig
- [ ] **Optimierung**: Performance der Datenbank-Queries verbessern (nur bei Bedarf).
- [x] **Fehlerseiten**: Benutzerfreundliche Fehlerseiten (z. B. 404, 500).
- [ ] **Deployment-Scripts**: Automatisierung von Build- und Deployment-Prozessen (CI/CD-Pipeline).

---

## 3. Nächste Schritte (Empfohlene Reihenfolge)
1. **Datenspeicherung fertigstellen** (Backend + Tests).
2. **Frontend für Testfragen und Antwort-Einsicht** implementieren.
3. **Dokumentation (Arc42)** auf vollständigen Stand bringen.
4. **Fehlende Tests** für Logik und Datenbank erstellen.
5. **Design-Verbesserungen** umsetzen.

---

## 4. Zusätzliche Notizen
- Das Projekt ist zu etwa 80% abgeschlossen, aber die wichtigsten Funktionen für die Tests (Datenspeicherung, Anzeige der Ergebnisse) sind noch unvollständig.
- Testabdeckung ist aktuell zu niedrig – mindestens 80% Codeabdeckung sollten angestrebt werden.
- Aktuell genutzte Tools:
  - **Backend**: Java, Spring Boot/Data
  - **Frontend**: HTML, CSS, JavaScript
  - **Datenbank**: PostgreSQL (bereits eingerichtet und verbunden)
  - **Build/Dependency Management**: Gradle
