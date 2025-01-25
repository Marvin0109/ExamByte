# Projekt: ExamByte

## Aktueller Stand
![Fortschritt](https://img.shields.io/badge/Fortschritt-0%25-brightgreen)

Weitere Details zum Projekt und den Aufgaben findest du in der TO_DO.md.
Hinweis: Aktuell aktive Teilnehmer sind leider nur zwei, vom Rest kam keine Rückmeldung trotz mehrmaliger Nachfragen.

07.11.24
- index html wurde erstellt wie auch contact html 
- nötigen controller wie auch tests die bei gradle build nicht funktionieren aber beim manuellen durchführen doch?

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
- 5 min später: IndexTest ist wieder behoben :)

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