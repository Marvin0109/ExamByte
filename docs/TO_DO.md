# TODO für ExamByte

> **Autor:** Marvin0109
> **Datum:** 21.01.2026

## 1. Offene Aufgaben (To-Do-Liste)

### Priorität: Hoch
- **Testing**
  - [ ] **Logik-Tests**: Restliche Logik Tests müssen implementiert werden, s. Jacoco Test Report

- **Frontend**
  - [ ] **CSV-Export**: Formular exportieren
  - [ ] **Testantwort-Einsicht**: Ansicht für Admins/Lehrkräfte, um Testergebnisse einzusehen.
  - [ ] **Korrektor korrigiert**: Das korrigieren der Freitextantworten entwickeln
  - [ ] **Neuer Testdurchlauf**: Implementierung von Testdurchläufen mit alten Antworten 
  - [ ] **Zulassungslogik**: Der Zulassungsstatus muss ins Frontend integriert werden

- **Dokumentation**
  - [X] **Arc42 Dokumentation**: Architektur-Dokumentation aktualisieren
 
- **Authentifizierung**
  - [ ] **Rollenverteilung**: Feature einführen, wo man seine Rollen aktivieren kann nach Bedarf

---

### Priorität: Mittel
- [ ] **Java-Konventionen**: Sicherstellen, dass der Code durchgehend den Konventionen entspricht (s. Styleguide)
- [ ] **Accessiblity**: Überprüfung auf Accessibility nötig
- [ ] **Codequalität**: Refaktorisierung nötig

---

### Priorität: Niedrig
- [ ] **Optimierung**: Performance der Datenbank-Queries verbessern
- [ ] **CD**: Code Deployment einrichten
- [ ] **Threading**: Anwendung erweitern auf parallele Ausführungen von Usern

---

## 2. Nächste Schritte (Empfohlene Reihenfolge)
1. [X] acr42-Doc aktualisieren
2. [ ] Logik-Test vervollständigen für vorhandenen Quellcode
3. [ ] Korrektur der Freitextantworten einführen + Test
4. [ ] Zulassungslogik + Test
5. [ ] Neuer Testdurchlauf mit alten Anworten umsetzen + Test
6. [ ] Einsicht auf Testergebnisse + CSV Export einführen + Test
7. [ ] Rollenverteilung-Feature
