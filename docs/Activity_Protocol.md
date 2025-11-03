# Aktueller Stand
![Fortschritt](https://img.shields.io/badge/Fortschritt-80%25-brightgreen)

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
- Integrationstest + Unit-Tests erstellt und laufen lassen
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
- ExamController erstellt (hat noch Bedarf zur Korrektur wie HTML kek)

05.02.2025
- Bean Dependency Fix in Tests
- WebController umgeändert und Zugriffsrechte hinzugefügt
- Richtige URL Angabe in ExamController Tests
- Student Rolle von "ROLE_USER" → "ROLE_STUDENT" geändert
- Methoden zur Extraktion der UUID anhand von Namen erstellt
- Onion Test geht nicht, nach vielen hin- und her Verschiebungen von Klassen und erstellen von DTO's half nichts

06.02.2025
- Onion Architektur aufrechterhalten
- Container Tests und Mapper Tests angepasst
- Test für die Aggregate, Infrastructure geschrieben

07.02.2025
- Oauth2 Login Fix
- Tests Fix
- Rollenverteilung Fix
- Restliche MapperDTO und Interfaces erstellt
- Package Dokumentationen erstellt (javadoc)

09.02.2025
- SpotBugs hinzugefügt
- Rollenverteilung geändert (Entwickler können 3 Rollen gleichzeitig haben, temporäre Lösung)
- NichtVorhandenException umgegangen durch Verwendung von Optional<T
- DTOMapper Tests
- infrastructure.AntwortServiceImplTest
- ExamControllerTests wurden fast vollständig behoben wie auch der Controller selbst (fehlt nur ein Test)
- PowerMock dependencies hinzugefügt für spezifische Fälle

18.02.2025
- Persistenz Package zu Infrastrukturschicht verschoben
- domain.model.service und .impl erstellt für zukünftige Geschäftslogik
- Onion Test angepasst

03.03.2025
- Tests korrigiert, unnötige Imports gelöscht
- AntwortServiceTest fix

13.03.2025
- .env erstellt
- s. #1 Error

16.03.2025
- Jacoco Settings in build.gradle aktualisiert für Gradle 8.X.X
- Dotenv für Umgebungsvariablen

29.03.2025
- Weitere Service Tests erstellt
- Dateipfade korrigiert in den HTML Exam Seiten
- TestSystemPropertyInitializer erstellt und eingebunden für Testdurchläufe

30.03.2025
- Spring Version von 3.3.5 auf 3.4.3 aktualisiert
- DB Umgebungsvariablen gesetzt für Tests
- UserCreationTest
- AppUserServiceTest

01.04.2025
- Zulassungslogik + Test
- Maximal 12 Tests (s. ExamController)
- Trennung der Domain Schicht und der Application + Tests Korrektur

02.04.2025
- Das Speichern von MC/SC Lösungen hinzugefügt (KorrekteAntworten)
- Automatisierte MC/SC Bewertungslogik erstellt
- Testanpassungen
- ExamProfessoren erweitert (Fragen können durch einen Klick hinzugefügt werden, funktioniert aber noch nicht so richtig)

03.04.2025
- DOM hinzugefügt (Fehlerhaft) **TECHNICAL DEBT!!!**

05.04.2025
- Indexing ist nicht richtig, s. Issue #2

06.04.2025
- Funktion entfernt, wo man zwischen z.B. 5 Fragen paar Fragen löschen kann (zu komplizierte Implementierung)
- Lösung: Immer die letzte Frage kann gelöscht werden
- Zukünftiges Feature: Delete All Button
- JavaScript Code in addQuestion.js

17.04.2025
- Für MC/SC mehr Antwortmöglichkeiten verfügbar (für jetzt 6 Optionen), auch für die Lösungen
- Übergabe der Fragedaten als JSON Objekt + Testanpassungen
- Löschen von FrageForm

18.04.2025
- JSON von Model getrennt, zwei Forms erstellt und Tests angepasst
- `BindingResult` geht immer noch nicht, WhiteErrorPage

20.04.2025
- `BindingResult` wurde gefixt (Lösung: `@Valid` in Kombination mit `@ModelAttribute`) -> `@ModelAttribute` entfernt,
war eh nicht zu gebrauchen
- ExamManagementServiceTest erstellt, noch nicht vollständig
- Feature: Auf der Contact Page ist die eigene FachID zu sehen.
- Alles Weitere den **Commits** zu entnehmen

11.08.2025
- Löschen von Daten eingebunden (noch nicht vollständig)
- TODO-Markierungen gesetzt
- TO_DO aktualisiert
- scripts/update_progress.sh gelöscht

12.08.2025
- DOM-Event gelöscht → hat mehr Probleme verursacht als geholfen
- CSV-Export implementiert, muss noch getestet werden
- Formular erstellt für Exam-Erstellung, Tests nötig

14.08.2025
- Enum in jede Schicht deklariert
- Tests angepasst für Fragetypen
- Speichern von Tests mit Fragen erfolgreich!
- CSV ist noch verbuggt

08.09.2025
- Das Speichern von Antwortmöglichkeiten hinzugefügt + Testanpassungen
- Das Anzeigen von Tests mit Fragen für die Bearbeitung von Exams funktioniert

09.09.2025
- Laden der Exams, Anzeigen des Exams auf examsDurchfuehren.html

11.09.2025
- SubmitExam Test geschrieben, weitere TODOs hinzugefügt (s. ExamManagementServiceTest)

15.09.2025
- Informationen zu den jeweiligen Tests und Optionen sind jetzt auf eine extra Menü Seite implementiert worden
- Automatischen Korrektor initialisiert
- Automatische Bewertungen muss noch behoben werden, s. TODO

17.09.2025
- Tests geschrieben für Automatisierte Bewertungen
- Bugs: s. Issues

22.09.2025
- Das Bearbeiten eines Tests erfolgt! (Prüfung auf Korrektheit folgt)
- Spotbugs erstmal ausgeschaltet
- Continuos Integration aufgestellt

23.09.2025
- Tests angepasst aufgrund der Änderung vom 22.09.2025
- Aktivstatus der Links für alle Navigationsitems hinzugefügt + getestet
- Die anderen Dokumente aktualisiert (s. [Styleguide](STYLEGUIDE.md) und [Todo](TO_DO.md))

03.11.2025
- arc42, README aktualisiert
