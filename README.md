07.11.24
- index html wurde erstellt wie auch contact html 
- nötigen controller wie auch tests die bei gradle build nicht funktionieren aber beim manuellen durchführen doch?

08.11.24
- Login Popup
- 405 Post Error Page
- Extras

13.11.24
- Navigations-Logik von HTML-Pages getrennt und scripts.js Einbindung

14.11.24
- oauth2 Login

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
- Logout Funktion wurde hinzugefügt, Einlog daten werden aber bei Logout nicht richtig gelöscht

28.11.24
- Projekt nach Onion-Architektur umgestellt, IndexTest läuft nicht
- 5 min später: IndexTest ist wieder behoben :)

17.12.2024
- Test zur Onion-Architektur geschrieben und daran gescheitert