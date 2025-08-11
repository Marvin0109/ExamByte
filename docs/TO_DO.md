# TODO für ExamByte

## Metadaten
**Letzte Änderungen:** 11.08.2025
**Autor:** Marvin0109
  
---

## 1. Offene Aufgaben (To-Do-Liste)

### Priorität: Hoch
- **Backend**
  - [ ] **Datenspeicherung**: Antworten aus Formularen extrahieren und in der Datenbank speichern (inkl. Validierung).
  - [ ] **Datenbank-Tests**: Löschung von Daten testen

- **Frontend**
  - [ ] **Testfragen-Seiten**: HTML und JavaScript für die dynamische Anzeige von Fragen.
  - [ ] **CSV-Export**: Formular exportieren
  - [ ] **Testantwort-Einsicht**: Ansicht für Admins/Lehrkräfte, um Testergebnisse einzusehen.

- **Dokumentation**
  - [ ] **Arc42 Dokumentation**: Architektur-Dokumentation aktualisieren

---

### Priorität: Mittel
- [ ] **Java-Konventionen**: Sicherstellen, dass der Code durchgehend den Konventionen entspricht (s. Styleguide)
- [ ] **Unit-Tests für Logik**: Tests für zentrale Logik-Komponenten (z. B. Punktberechnung bei Tests).
- [ ] **UI-Design verbessern**: Ansprechenderes Design (z. B. Farbgebung, Usability).
- [ ] **Threading**: Anwendung erweitern auf parallele Ausführungen von Usern

---

### Priorität: Niedrig
- [ ] **Optimierung**: Performance der Datenbank-Queries verbessern (nur bei Bedarf).
- [ ] **Deployment-Scripts**: Automatisierung von Build- und Deployment-Prozessen (CI/CD-Pipeline).

---

## 2. Nächste Schritte (Empfohlene Reihenfolge)
1. [ ] **Datenspeicherung fertigstellen** (Backend + Tests).
2. [ ] **Frontend für Testfragen und Antwort-Einsicht** implementieren.
3. [ ] **Geschäftslogik** implementieren/zu Ende bringen
4. [ ] **Dokumentation (Arc42)** aktualisieren
5. [ ] **Konsistenz und Threads** für die gesamte Anwendung
6. [ ] **Design-Verbesserungen** umsetzen.

---

## 3. Zusätzliche Notizen
(Stand: 22.04.2025)
- Testabdeckung ist aktuell zu niedrig (aktuell: 74%) – mindestens 80% Codeabdeckung sollten angestrebt werden.
- Aktuell genutzte Tools:
  - **Backend**: Java, Spring Boot, Spring Data JDBC
  - **Frontend**: HTML, CSS, JavaScript
  - **Datenbank**: PostgreSQL (bereits eingerichtet und verbunden)
  - **Build/Dependency Management**: Gradle
  - **Laden aller Umgebungsvariablen**: Dotenv
  - Mehr Informationen in der [arc42](arc42.md)
