# Projekt-Styleguide

**Autor**: Marvin0109
**erstellt am**: 08.01.2025
**letzte Änderung**: 23.09.2025

Dieser Styleguide enthält die Formatierung- und Konventionsregeln für unser Projekt. Ziel ist es,
die Zusammenarbeit zu erleichtern und die Codequalität zu sichern.
Er ist für unser aktuelles Team von 2 aktiven Entwicklern geschrieben und berücksichtigt die Bedürfnisse unseres Projekts.

## Allgemeine Regeln

### Sprache
- Wir programmieren hauptsächlich in **Java** und **HTML**, **SQL**, **CSS** als auch **JavaScript** werden verwendet.
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
- Wir verwenden **Java 21** für das gesamte Projekt. Alle Teammitglieder sollten sicherstellen, 
  dass sie diese Version oder eine kompatible Version von Java verwenden.

### Frameworks und Bibliotheken
- **Spring Boot:** Version **3.3.5** 
  (Spring Boot wird als zentrales Framework für die Erstellung der Webanwendung verwendet)
- **Spring Security:** Version **5.x.x**
  (Wird für die Sicherheitsaspekte der Anwendung verwendet, inklusive OAuth2-Client)
- **Thymeleaf:** Version **3.x.x**
  (Wird für das Template-Rendering in Verbindung mit Spring Boot verwendet)
- **JUnit:** Version **5.x.x**
  (Wird für Unit-Tests verwendet)
- **ArchUnit:** Version **1.3.0**
  (Für Architekturtests, um die Einhaltung der Architekturregeln zu überprüfen)
- **Spring Boot DevTools:** Wird für das automatische Neustarten und Debugging während der Entwicklung verwendet.
- **Spring Boot Configuration Processor:** Wird zur Verarbeitung von Konfigurationswerten genutzt.
- **Maven/Gradle:** Gradle wird als Build-Tool verwendet.
- **Dotenv:** 3rd Party Tool für das Laden der Umgebungsvariablen aus einer .env Datei

### Entwicklungsumgebung
- **IDE:** Wir verwenden IntelliJ IDEA, am besten die Ultimate-Edition, für die andere Versionen siehe zusätzliche
  Informationen beim Reiter **Datenbanken**.

### Docker
- **Docker Desktop:** Wir verwenden **Docker Desktop** für das Erstellen, Testen und Ausführen von 
  Docker-Containern während der Entwicklung.
- **Docker Compose:** Wir setzen **Docker Compose** ein, um mehrere Docker-Container in einer definierten Umgebung zu 
  orchestrieren (z.B. für die Datenbank und die Webanwendung).
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

## Versionierung

### Branch-Namen
- Aktuell: Nur 'main'
- Für zukünftige Erweiterungen: Branches nach [Feature], [BugFix], etc. benennen.
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

2. **Committe zuert den Bug**
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
- Wir verwenden **jUnit**, **spring-security-test**, **archunit**, **spring-boot-starter-test** und **testcontainers**.

### Namenskonventionen für Tests
- Testmethoden beginnen mit test_.
  - Beispiel: 
  ```java
  void test_03() { ... }

### Dokumentation der Tests
- Wir verwenden **@DisplayName** für eine **sehr kurze** Beschreibung der jeweiligen Testfälle
  - Beispiel:
  ```java
  @Test
  @DisplayName("Testbeschreibung")
  void test_01() { ... }

### Codeabdeckung
- Ziel: **80 % der Funktionen** sollen durch Tests abgedeckt werden.

## Tools

### Versionskontrolle
- Git-Workflow
- Authorized OAuth App ExamByte owned by 'Marvin0109' (Nach Bedarf kann man selber eine erstellen und sie 
  anbinden ans Projekt, wichtig hier sind die Umgebungsvariablen zu setzen)

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
- Geschlossene Tags: Schließe Tags korrekt nach **Bedarf**.

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
- Nutze 'alt'-Attribute bei Bildern für Screenreader.
- Vermeide rein dekorative Inhalte ohne Kontext.
- Formulare sollten immer ein '<label>' enthalten, das mit einem 'for'-Attribut auf das Eingabefeld verweist.

### Externe Ressourcen
- Lade CSS- und JavaScript-Dateien am richtigen Ort:
  - CSS: Im '<head>'-Tag.
  - JavaScript: Am Ende des '<body>'-Tag

## Thymeleaf-Regeln

### Verwendung von Platzhaltern
- Nutze die **Thymeleaf-Ausdruckssyntax** '${}' für Variablen.
- Vermeide es, reinen HTML-Code mit serverseitigen Werten zu mischen, wenn es durch Thymeleaf-Ausdrücke gelöst werden 
  kann.
  - Beispiel:
  ```html
  <p th:text="${user.name}">Benutzername</p>

### Attribute
- Verwende Thymeleaf-spezifische Attribute wie 'th:text', 'th:href', 'th:if', 'th:each' für dynamische Inhalte.
- Die 'th:*'-Attribute sollten anstelle der statischen HTML-Attribute stehen.
  - Beispiel:
  ```html
  <a th:href="@{/profile}" href="#">Profil</a>

### Schleifen und Bedingungen
- Nutze 'th:each' für Schleifen und 'th:if' oder 'th:unless' für Bedingungen.
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
- Nutze 'th:utext' oder 'th:text' mit einem Default-Wert, falls der Platzhalter 'null' ist.
  - Beispiel:
  ```html
  <p th:text="${user.name ?: 'Gast'}">Gast</p>

### HTML-Kommentare
- Thymeleaf biete eigene Kommentar-Syntax, die beim Rendern entfernt wird:
  - Beispiel:
  ```html
  <!--/* Kommentar wird entfernt */-->

### Barrierefreiheit
- Stelle sicher, dass dynamische Inhalte (z.B. Bilder oder Links) immer mit sinnvollen Attributen (wie 'alt' bei Bildern)
  ergänzt werden.

### Strukturierte Templates
- Error-Pages in 'templates.error'
- Webseiten in 'templates' **zurzeit**

## Ressourcen

### Bilder, GIFS, Videos, usw.
- Memes **erlaubt**.
- Emotes: https://old.7tv.app/emotes
- *Hinweis*: Übertreibung nicht notwendig.
- Alle Ressourcen unter 'resources.static.public' speichern.

### Text
- Generierte Text: https://www.loremipsum.de/

## Änderungen

### Test
- Mit dem Start der Geschäftslogik und Funktionalitäten sollten Tests **rechtzeitig** geschrieben werden!

## Fun Facts
Mit `cloc` kann man anzeigen lassen, wie viel Codezeilen man hat und auch in welcher Sprache.
Hier die Statistik vom 20.04.2025:
```
$ cloc .

github.com/AlDanial/cloc v 2.06  T=0.45 s (1564.1 files/s, 135535.3 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
HTML                           406           1592             88          16263
CSS                             13           1310             53          10706
Java                           189           2072            727           8268
JavaScript                       8           1934           2167           7998
XML                             68             21              0           6258
Markdown                         6            205              2            733
Text                             4             20              0            223
Bourne Shell                     1             28            118            105
YAML                             4             13              0             98
Maven                            1              9              9             88
SQL                              1              7              0             74
DOS Batch                        1             21              2             71
Gradle                           2             20             24             70
PlantUML                         2              3              1             25
Properties                       3              0              1             11
-------------------------------------------------------------------------------
SUM:                           709           7255           3192          50991
-------------------------------------------------------------------------------

$ cloc src/main/java/

  133 text files.
     133 unique files.                                          
       0 files ignored.

github.com/AlDanial/cloc v 2.06  T=0.06 s (2286.6 files/s, 97173.7 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                           133           1158            382           4112
-------------------------------------------------------------------------------
SUM:                           133           1158            382           4112
-------------------------------------------------------------------------------

$ cloc src/test/java/

57 text files.
      56 unique files.                              
       1 file ignored.

github.com/AlDanial/cloc v 2.06  T=0.05 s (1093.5 files/s, 105734.8 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            56            914            345           4156
-------------------------------------------------------------------------------
SUM:                            56            914            345           4156
-------------------------------------------------------------------------------
```

## JacocoTestReport

![JacocoTestReport](/src/main/resources/static/public/pictures/JacocoTest_Report-23-09-2025.jpg)

## Schlussbemerkung
- Randbemerkung: Die Gruppe startete mit 5 Teilnehmern, von denen **3** ausgestiegen sind.

![Caught Gif](https://cdn.7tv.app/emote/01H0SQNM9R0005HNCSM10SYJEQ/2x.webp)

- Danke an muz70wuc für die Mitarbeit, der Typ ist echt **GOATED**.

![Based Tectone Gif](https://cdn.7tv.app/emote/01G39QQWC80004KFXXGTW7Q4XE/4x.webp)

- Nach der ersten Klausur bliebt nur noch ich als letzter Entwickler übrig. Ist nicht einfach alles alleine zu entwickeln,
aber mit Geduld und Konzentration geht's

![Beepo Hopium](https://cdn.7tv.app/emote/01F7Z8X2K80005DMJNB0FWZDZH/4x.avif)
