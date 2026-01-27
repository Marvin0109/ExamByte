# Projekt-Styleguide

>**Autor**: Marvin0109,
>**erstellt am**: 08.01.2025,
>**aktualisiert am**: 27.01.2026

Dieser Styleguide enthält die Formatierung- und Konventionsregeln für unser Projekt. Ziel ist es,
die Zusammenarbeit zu erleichtern und die Codequalität zu sichern.
Er wurde ursprünglich für ein Team von 2 Entwickler:innen erstellt, wird aktuell jedoch von einer Person gepflegt und angepasst, die das Projekt privat weiterführt.

## Allgemeine Regeln

### Sprache
- Wir programmieren hauptsächlich in **Java** und verwenden **HTML** für die Weboberfläche.  
  Für die Datenhaltung kommt die **PostgreSQL-Datenbank** zum Einsatz.
- Alle Kommentare und Dokumentationen sind auf **Deutsch** zu schreiben.

### Dateinamen
- Verwende **UpperCamelCase** für Java Dateien (z.B. 'HelloWorld.java').
- Sonstige Dateien normaler **camelCase** (z.B. 'beepoRun.png').
- Tests enden auf 'Test' (z.B. 'UserRepositoryTest.java').

## Formatierung

### Einrückung
- Verwende einen Tab (Normale Einrückung in IntelliJ).

### Zeilenlänge
- Maximal **80 Zeichen pro Zeile** (außer bei langen URLs).
- Wenn längere Zeilen notwendig sind, wie bei SQL oder langen Strings, nutze sinnvolle Umbrüche.

### Leerzeilen
- 1 Leerzeile zwischen Methoden oder Klassen.
- **0** Leerzeilen zwischen Dokumentation und Klassen oder Methoden.

## Technologien und Versionen

### Java-Version
- Das Projekt verwendet **Java 21**. Es wird empfohlen, diese Version oder eine kompatible Version zu verwenden, 
  um sicherzustellen, dass der Code korrekt kompiliert und ausgeführt werden kann.

### Frameworks und Bibliotheken
- **Spring Boot:** Version **3.4.3**  
  (Zentrales Framework für die Erstellung der Webanwendung)
- **Spring Security:** (Für Sicherheitsaspekte, inkl. OAuth2-Client)
- **Thymeleaf:** (Template-Rendering mit Spring Boot)
- **JUnit:** (Unit-Tests)
- **ArchUnit:** Version **1.3.0**  
  (Architekturtests zur Einhaltung der Regeln)
- **Testcontainers:** (Ermöglicht Integrationstests mit realen Datenbanken in Containern)
- **Gradle:** Version **8.x**  
  (Build-Tool)
- **Dotenv:** (Lädt Umgebungsvariablen aus einer `.env` Datei)
- **Flyway:** Version **11.2.0**
  (Datenbank-Migrations-Tool)

### Entwicklungsumgebung
- **IDE:** Wir verwenden IntelliJ IDEA, am besten die Ultimate-Edition, für die andere Versionen siehe zusätzliche
  Informationen beim Reiter **Datenbanken**.

### Docker
- **Docker Desktop:** Wir verwenden **Docker Desktop** für das Erstellen, Testen und Ausführen von 
  Docker-Containern während der Entwicklung.
- **Dockerfiles und YAML:** Dockerfiles werden für das Erstellen von Docker-Images verwendet. In **docker-compose.yml**
  definieren wir die Container-Konfigurationen und Verknüpfungen, um eine konsistente Entwicklungsumgebung zu 
  gewährleisten.
  - Beispiel:
  ```yaml
  services:
    web:
      image: myapp:latest
      ports:
        - "8080:8080"
      environment:
        - SPRING_PROFILES_ACTIVE=dev
    db:
      image: postgres:13
      environment:
        POSTGRES_USER: user
        POSTGRES_PASSWORD: password
        POSTGRES_DB: mydb

### Datenbank
- **Datenbank:** Das Projekt nutzt **postgres:15-alpine**
- Nicht Ultimate-Versionen sollten Datenbank-Tools wie DBeaver-Community verwenden → https://dbeaver.io/download/

## Code-Stil

### Variablennamen
- Nutze **camelCase**.
  - Beispiel: 'chainBuilder'.

### Klassennamen
- Nutze **UpperCamelCase**.
  - Beispiel: 'AppUserService', 'SecurityConfig'

### Konstanten
- Schreibweise in **UPPER_CASE**.
  - Beispiel: 'MAX_ATTEMPTS', 'DEFAULT_TIMEOUT'.

### Kommentare und Dokumentation
- Dokumentation am **Anfang** der Klasse, was sie macht und wofür sie da ist.
- Nutze JavaDoc für die Dokumentation:
  ```java
    /**
     * Verarbeitet Benutzeranfragen.
     *
     * @param userId ID des Benutzers
     * @return Benutzer-Objekt
     * @see UserRepository#findById(Long)
     * @link https://docs.spring.io
     */

- Nach Bedarf auch *@link* und *@see* verwenden im Beschreibungstext für Verweise auf Variablen, Klassen oder sonstiges.
- Testklassen auch dokumentieren bei Bedarf
- Notwendig sind die JavaDoc-Annotationen nicht

## Versionierung

### Branch-Namen
- Aktuell: Nur 'main'
- Für mögliche Erweiterungen: Branches nach [feature], [bugfix], etc. benennen.
  - Beispiel: `feature/user-login`, `bugfix/login-error`.

### Commit-Messages
- Format: [Typ] Beschreibung
  - Beispiel: [Feature] Added user login API
- Übersicht aller Typen mit Beispielen:
  - [Feature]: Neue Funktionen oder Features werden hinzugefügt.
    - Bsp: [Feature] Add user authentication module
  - [Bug]: Bug Bericht erstellen mit **Issue Tracker**
    - s. Issue Tracker
  - [BugFix]: Fehlerbehebungen, um bestehende Probleme zu lösen.
    - Bsp: [BugFix] Fix login validation issue. Closes #x
  - [Docs]: Änderungen an der Dokumentation oder das Hinzufügen von Dokumentationsdateien.
    - Bsp: [Docs] Update STYLEGUIDE with new setup instructions
  - [Refactor]: Codeänderungen, die die Struktur verbessern, ohne das Verhalten zu ändern.
    - Bsp: [Refactor] Simplify authentication service
  - [Test]: Hinzufügen oder Ändern von Tests.
    - Bsp: [Test] Add unit tests for login service
  - [Chore]: Änderungen, die keine neue Funktionalität oder Fehlerbehebung betreffen, wie z. B. Konfigurationsänderungen
    oder das Hinzufügen von Tools.
    - Bsp: [Chore] Update dependencies

### Issue Tracker
1. **Issue erstellen auf GitHub**
   - Gehe zu GitHub → Repository → Issues → New Issues
   - Title: *Login button funktioniert nicht*
   - Description: *Login button funktioniert nicht, wenn man ihn auf der Startseite anklickt.*
   - Img: Verwende Bilder für bessere Nachvollziehbarkeit
   - GitHub wird ein Ticket erstellen, mit dem man bei Commits referenzieren kann z.B.: #1

2. **Committe zuerst den Bug**
   - Arbeite am Code und speichere deine Arbeit mit git add

3. **Referenziere dem Issue**
   - Erstelle einen Commit und referenziere es mit dem Issue #1
   ```
   git commit -m "[Bug]: Login Issue (#1)"
   
4. **Commit with fixed bug and closing issue**
   - Nach dem Bugfix dann committen und dem Issue schließen.
   ```
   git commit -m "[BugFix]: Fix login button issue. Closes #1"

### Testing
- Wir verwenden **JUnit**, **spring-security-test**, **ArchUnit**, **spring-boot-starter-test** und **Testcontainers**.

### Namenskonventionen für Tests
- Testmethoden werden nach den zu testenden Methoden benannt.
  - Beispiel: 
  ```java
  void multiplicationSuccess_03() { ... }
- Im Namen soll auch sehr kurz erwähnt werden, welche Logik getestet wird
  - Beispiel: 
  ```java
  void login_unauthorized_01() { ... }

### Dokumentation der Tests
- Wir verwenden **@DisplayName** für eine **kurze** Beschreibung der jeweiligen Testfälle, falls der Methodenname nicht mehr ausreichen tut
  - Beispiel:
  ```java
  @Test
  @DisplayName("Testbeschreibung")
  void test_01() { ... }

### Codeabdeckung
- Ziel: **80 % der Funktionen** sollen durch Tests abgedeckt werden als auch die Logik sollte bei **80 %** Test-Abdeckung sein.

## Tools

### Versionskontrolle
- Git-Workflow
- Authorized OAuth App ExamByte owned by 'Marvin0109' (Nach Bedarf kann man selber eine erstellen und sie 
  anbinden ans Projekt, wichtig hier sind die Umgebungsvariablen zu setzen in einer eigenen `.env`)

### Build-Tool
- Gradle

## HTML-Regeln

### Grundlegende Struktur
- Jeder HTML-Code sollte mit der '<!DOCTYPE html>'-Deklaration beginnen.
- Das '<html>'-Tag muss das 'lang'-Attribut enthalten, um die Sprache der Seite zu definieren (z.B. '<html lang="de">').
- Der Code sollte klar in '<head>' und '<body>' unterteilt sein.
- Unsere Anwendung hat eine Navigationsleiste, welche man hinzufügen sollte auf HTML-Seiten, die die Navigationsleiste 
  anzeigen sollen.
  - Einbindung der Navigationsleiste auf einer beliebigen HTML-Seite:
    ```html
    <header th:replace="~{navigation/navbar.html}"> </header>

### Einrückung und Formatierung
- Verwende **2 Leerzeichen** für die Einrückung.
- Jede Öffnung eines neuen Elements sollte eingerückt sein, um die Struktur lesbar zu machen.
  - Beispiel:
  ```html
  <div>
    <p>Beispieltext</p>
  </div>

### Tags
- **Geschlossene Tags:** Alle HTML-Tags, die ein End-Tag benötigen, müssen korrekt geschlossen werden.

### Attribute
- Alle Attribute in **Kleinbuchstaben** schreiben.
- Doppelte Anführungszeichen (") für Attributwerte verwenden.
  - Beispiel:
  ```html
  <input type="text" name="username" />

- Reihenfolge der Attribute: Zuerst Pflichtattribute, dann optionale.
  - Beispiel:
  ```html
  <img src="bild.jpg" alt="Beschreibung" width="300" height="200" />

### Klassen- und ID-Namen
- Verwende **kebab-case** für Klassen- und ID-Namen.
  - Beispiel:
  ```html
  <div id="main-header" class="user-profile"></div>

- Verwende Kommentare, um Abschnitte im HTML-Code zu kennzeichnen.
  - Beispiel:
  ```html
  <!-- Navigation -->
  <nav>
    ...
  </nav>
  
### Accessibility
- Nutze `alt`-Attribute bei Bildern für Screenreader.
- Vermeide rein dekorative Inhalte ohne Kontext.
- Formulare sollten immer ein `<label>` enthalten, das mit einem `for`-Attribut auf das Eingabefeld verweist.

### Externe Ressourcen
- Lade CSS- und JavaScript-Dateien am richtigen Ort:
  - CSS: Im `<head>`-Tag.
  - JavaScript: Am Ende des `<body>`-Tag

## Thymeleaf-Regeln

### Verwendung von Platzhaltern
- Nutze die **Thymeleaf-Ausdruckssyntax** `${}` für Variablen.
- Vermeide es, reinen HTML-Code mit serverseitigen Werten zu mischen, wenn es durch Thymeleaf-Ausdrücke gelöst werden 
  kann.
  - Beispiel:
  ```html
  <p th:text="${user.name}">Benutzername</p>

### Attribute
- Verwende Thymeleaf-spezifische Attribute wie `th:text`, `th:href`, `th:if`, `th:each` für dynamische Inhalte.
- Die `th:*`-Attribute sollten anstelle der statischen HTML-Attribute stehen.
  - Beispiel:
  ```html
  <a th:href="@{/profile}" href="#">Profil</a>

### Schleifen und Bedingungen
- Nutze `th:each` für Schleifen und `th:if` oder `th:unless` für Bedingungen.
  - Beispiel:
  ```html
  <ul>
    <li th:each="item : ${items}" th:text="${item}">Eintrag</li>
  </ul>
  
- Beispiel für Bedingungen:
  ```html
  <p th:if="${user.loggedIn}">Willkommen zurück!</p>
  <p th:unless="${user.loggedIn}">Bitte einloggen.</p>
  
### Standardwerte
- Nutze `th:text` mit einem Default-Wert, falls der Platzhalter `null` ist.
  - Beispiel:
  ```html
  <p th:text="${user.name ?: 'Gast'}">Gast</p>

- **Niemals** `th:utext` verwenden! Es rendert den Inhalt **ungefiltert**, was ein Sicherheitsrisiko ist.

### HTML-Kommentare
- Thymeleaf biete eigene Kommentar-Syntax, die beim Rendern entfernt wird:
  - Beispiel:
  ```html
  <!--/* Kommentar wird entfernt */-->

### Barrierefreiheit
- Stelle sicher, dass dynamische Inhalte (z.B. Bilder oder Links) immer mit sinnvollen Attributen (wie 'alt' bei Bildern)
  ergänzt werden.

### Strukturierte Templates
- Error-Pages in `templates.error`
- Webseiten in `templates`

## Ressourcen

### Bilder, GIFS, Videos, usw.
- Memes **erlaubt**.
- Emotes: https://old.7tv.app/emotes
- Alle Ressourcen im Ordner `resources.static.public` ablegen.

### Text
- Textgenerator: https://www.loremipsum.de/

## Fun Facts
Mit `cloc` kann man anzeigen lassen, wie viel Codezeilen man hat und auch in welcher Sprache.
Hier die Statistik vom 27.01.2026:
```
$ cloc .

github.com/AlDanial/cloc v 1.98  T=0.49 s (1300.4 files/s, 124915.1 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
HTML                           317           1237             83          15322
CSS                             12           1310             53          10676
Java                           201           2342            690           8873
JavaScript                       7           1933           2167           7988
XML                             78             21              0           6598
Markdown                         6            293              2            920
SQL                              2             14              0            112
Bourne Shell                     1             28            118            105
Maven                            1              9              9             88
Gradle                           2             21             24             78
YAML                             3              8              0             73
DOS Batch                        1             21              2             71
Text                             3             20              0             34
PlantUML                         2              5              1             20
Properties                       3              0              1             11
-------------------------------------------------------------------------------
SUM:                           639           7262           3150          50969
-------------------------------------------------------------------------------

$ cloc src/main/java/

github.com/AlDanial/cloc v 1.98  T=0.06 s (2252.9 files/s, 92253.0 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                           137           1200            320           4090
-------------------------------------------------------------------------------
SUM:                           137           1200            320           4090
-------------------------------------------------------------------------------

$ cloc src/test/java/

github.com/AlDanial/cloc v 1.98  T=0.04 s (1473.2 files/s, 144899.5 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            64           1142            370           4783
-------------------------------------------------------------------------------
SUM:                            64           1142            370           4783
-------------------------------------------------------------------------------
```

## JacocoTestReport

![JacocoTestReport](/src/main/resources/static/public/pictures/JacocoTest_Report-27-01-2026.jpg)

## Schlussbemerkung
- Das Projekt startete ursprünglich mit 5 Teilnehmer:innen. Nach Abschluss der Klausur arbeiteten schließlich nur noch ich als alleiniger Entwickler weiter.
- Vielen Dank an alle ursprünglichen Teammitglieder für ihre Unterstützung und Beiträge.
- Die Weiterentwicklung des Projekts wird derzeit von mir allein durchgeführt. Mit Geduld und konzentrierter Arbeit ist es gelungen, das Projekt erfolgreich fortzuführen.
