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
  - [ ] Datenspeicherung von Test-Antworten (Backend-Logik teilweise implementiert)
  - [ ] Testfragen-Erstellungsseiten (HTML vorhanden, aber ohne Funktionalität)
  - [ ] Testauswertungsseiten (Frontend noch unvollständig, Backend fehlt)
  - [ ] Datenvalidierung (Nur für wenige Eingaben implementiert)

---

## 2. Offene Aufgaben (To-Do-Liste)

### Priorität: Hoch
- **Backend**
  - [ ] **Datenspeicherung**: Antworten aus Formularen extrahieren und in der Datenbank speichern (inkl. Validierung).
  - [ ] **Datenbank-Tests**: Unit- und Integrationstests für die Datenbankanbindung schreiben.
  - [ ] **API-Routen**: Routen für den Zugriff auf gespeicherte Testergebnisse und Testfragen erstellen.

- **Frontend**
  - [ ] **Testfragen-Seiten**: HTML und JavaScript für die dynamische Anzeige von Fragen.
  - [ ] **Testantwort-Einsicht**: Ansicht für Admins/Lehrkräfte, um Testergebnisse einzusehen.
  - [ ] **Formularvalidierung**: Clientseitige Validierung (Pflichtfelder, Datentypen etc.).

- **Dokumentation**
  - [ ] **Arc42 Dokumentation**: Architektur-Dokumentation (Grundstruktur vorhanden, muss ergänzt werden).
  - [ ] **Code-Dokumentation**: Kommentare und JavaDoc für alle zentralen Klassen.

---

### Priorität: Mittel
- [ ] **Java-Konventionen**: Sicherstellen, dass der Code durchgehend den Konventionen entspricht (Checkstyle verwenden).
- [ ] **Unit-Tests für Logik**: Tests für zentrale Logik-Komponenten (z. B. Punktberechnung bei Tests).
- [ ] **UI-Design verbessern**: Ansprechenderes Design (z. B. Farbgebung, Usability).

---

### Priorität: Niedrig
- [ ] **Optimierung**: Performance der Datenbank-Queries verbessern (nur bei Bedarf).
- [ ] **Fehlerseiten**: Benutzerfreundliche Fehlerseiten (z. B. 404, 500).
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
- Das Projekt ist zu etwa 70% abgeschlossen, aber die wichtigsten Funktionen für die Tests (Datenspeicherung, Anzeige der Ergebnisse) sind noch unvollständig.
- Testabdeckung ist aktuell zu niedrig – mindestens 80% Codeabdeckung sollten angestrebt werden.
- Aktuell genutzte Tools:
  - **Backend**: Java, Spring Boot
  - **Frontend**: HTML, CSS, JavaScript
  - **Datenbank**: MySQL (bereits eingerichtet und verbunden)
  - **Build/Dependency Management**: Gradle
