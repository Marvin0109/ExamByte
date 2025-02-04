# Aktueller Stand
![Fortschritt](https://img.shields.io/badge/Fortschritt-70%25-brightgreen)

Weitere Details zum Projekt und den Aufgaben findest du in der TO_DO.md.
Hinweis: Aktuell aktive Teilnehmer sind leider nur zwei, vom Rest kam keine Rückmeldung trotz mehrmaliger Nachfragen.

---

### Aktivitätsprotokoll

07.11.24
- index html wurde erstellt wie auch contact html
- nötigen controller wie auch tests die bei gradle build nicht funktionieren aber beim manuellen Durchführen doch?

08.11.24
- Login Popup
- 405 Post Error Page
- Extras

13.11.24
- Navigation-Logik von HTML-Pages getrennt und scripts.js Einbindung

14.11.24
- OAuth2 Login

20.11.24
- Admin Rolle hinzugefügt
- Methode loadUser() wird aber nicht aufgerufen aus unbekannten Gründen
- 403 Error Page
- exams.html erstellt
- Extras

22.11.24
- Browser muss neu gestartet werden damit die Änderungen der Rollenverteilung
  in Kraft kommt.
- Benutzername in loadUser() selber manuell mit dem eigenen
  Benutzernamen ersetzen.
- Tests geschrieben für WebController/ Authentifizierung

24.11.24
- Logout Funktion wurde hinzugefügt, Login Daten werden aber bei Logout nicht richtig gelöscht

28.11.24
- Projekt nach Onion-Architektur umgestellt, IndexTest läuft nicht
- 5 Min später: IndexTest ist wieder behoben :)

17.12.2024
- Test zur Onion-Architektur geschrieben und daran gescheitert

07.01.2025
- Dokumentation

08.01.2025
- Styleguide hinzugefügt, bitte durchlesen.

09.01.2025
- docker-compose Datei erstellt
- (noch nicht funktionierender) Datenbank Test
- Gradle dependencies update + Dokumentation
- application.yml setting

14.01.2025
- SQL User, Antwort und Frage Tabellen erstellt
- Die dazu nötigen Aggregate für jetzt fertiggestellt :)

16.01.2025
- Onion Architektur nochmal überarbeitet
- Architektur Test angepasst (schlägt Erwartungsgemäß fehl)

21.01.2025
- Onion-Architektur in domain
- mapper-Klassen und entity-Klassen in persistence erstellt
- Bugfix in ExamByteApplication.java bei scanBasePackages
- Data-Ordner wird jetzt im Projekt gespeichert, auf keinen Fall commiten :)

23.01.2025
- Bug fix mit Server Connection
- Professor and Student Repositorys wurden hinzugefügt
- Dazugehörige Mapper wurden fertiggestellt
- Erster Test zum Datenaustausch zwischen ServerDB und Rechner wurde gemacht funktioniert aber bisher nicht

27.01.2025
- Separate Klassen für JPA und JDBC Verwendung + Packages
- JDBC Test erstellt und läuft (es fehlen noch die restlichen JDBC Tests)
- Service Klasse erstellt (implementiert Repository)
- SQL Datei in 4 Dateien zerkleinert
- (Hintergrundwissen zum Nachschauen: Woche 11 Spring Data)

29.01.2025
- Flyway Migration Error Fix (Bug wegen separate Flyway Skripte für 4 Tabellen lmao)
- DataBaseTest geht nicht, weil er aus irgendeinen Grund sich nicht zufriedenstellt für jegliche Art von Bean,
  die ich Spring zur Verfügung stelle (No Bean Type Found For SpringDataStudentRepository)
- Klassenverschiebungen ähnlich wie Code Woche 12 VL (half aber trotzdem nicht für DataBaseTest)

30.01.2025
- JPA entfernt + Dependencies
- H2 entfernt (verwenden nur Testcontainer für Tests)
- Bean Type Error Fix
- Klassenverschiebung + Umbenennung
- UUID eingeführt (noch nicht vollständig abgeschlossen und getestet)

31.01.2025
- Exam Entität hinzugefügt
- Laufende Tests für die Datenbankrelationen

01.02.2025
- Vollständige Datenbank-Logik
- Integrationstest + Unit Tests erstellt und laufen lassen
- README Inhalt zu Activity_Protocol verschoben

02.02.2025
- Onion Architektur + Test
- Bisschen toten Code wegschmeissen
- Neue packages
- Builder Pattern für Objekte

03.02.2025
- Service Klassen hinzugefügt
- arc42 Dokumentation
- README erneuert
- Onion schlägt fehl wegen Abhängigkeiten zwischen application.config und application.service

04.02.2025
- vier ExamHTMLS erstellt
- passende Tests zu den ExamHTMLS erstellt
- zusätzliche methode zu den AntwortKlassen hinzugefügt
- ExamController erstellt (hat noch Bedarf zur Korrektur wie HTMLS kek)