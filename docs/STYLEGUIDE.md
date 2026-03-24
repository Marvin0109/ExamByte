# Projekt-Styleguide

>**Autor**: Marvin0109,
>**erstellt am**: 08.01.2025,
>**aktualisiert am**: 24.03.2026

> [!NOTE]
> Dieser Styleguide enthält die Formatierung- und Konventionsregeln für unser Projekt. Ziel ist es,
die Zusammenarbeit zu erleichtern und die Codequalität zu sichern.
Er wurde ursprünglich für ein Team von 5 Entwickler:innen erstellt, wird aktuell jedoch von einer Person gepflegt 
und angepasst, die das Projekt privat weiterführt.

## Übersicht

- [Formatierung](#formatierung)
- [Technologien und Versionen](#technologien-und-versionen)
- [Code-Stil](#code-stil)
- [Versionierung](#versionierung-git)
- [Testing und Qualitätssicherung](#testing-und-qualitätssicherung)
- [HTML-Regeln](#html-regeln)
- [Thymeleaf-Regeln](#thymeleaf-regeln)
- [Sicherheit](#sicherheit)
- [QA (Demo)](#quality-assurance-demo)
- [Ressourcen](#ressourcen)
- [Fun Facts](#fun-facts)
- [Schlussbemerkung](#schlussbemerkung)

## Allgemeine Regeln

### Sprache
- Wir programmieren hauptsächlich in **Java** und verwenden **HTML** für die Weboberfläche.  
- Für die Datenhaltung kommt die **PostgreSQL-Datenbank** zum Einsatz.
- Alle Kommentare und Dokumentationen sind auf **Deutsch** zu schreiben.

> [!NOTE]
> Codesprache auf Englisch sofern möglich.

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
- **Spring Boot:** Version **3.5.8**  
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
- **SonarQube:** Version **7.2.2.6593**
  (Scanning tool für Erkennen von Code Smells)

### Github OAuth-App

Verwendung einer *Authorized OAuth App ExamByte* von `Marvin0109`.

(Nach Bedarf kann man selber eine erstellen und sie anbinden ans Projekt, wichtig hier sind die Umgebungsvariablen zu setzen in einer eigenen `.env`)

### Entwicklungsumgebung
- **IDE:** Wir verwenden IntelliJ IDEA, am besten die Ultimate-Edition, für die andere Versionen bezüglich Datenbanken
siehe [hier.](#datenbank)

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
- **Datenbank:** Das Projekt nutzt `postgres:15-alpine`
- Nicht Ultimate-Versionen sollten Datenbank-Tools wie [DBeaver-Community.](https://dbeaver.io/download/)
- Oder halt andere DB-Tools mit denen Sie vertraut sind.

## Code-Stil

### Variablennamen
- Nutze **camelCase**.
  - Beispiel: `chainBuilder`

### Klassennamen
- Nutze **UpperCamelCase**.
  - Beispiel: `AppUserService`, `SecurityConfig`

### Konstanten
- Schreibweise in **UPPER_CASE**.
  - Beispiel: `MAX_ATTEMPTS`, `DEFAULT_TIMEOUT`.

### Kommentare und Dokumentation
- Dokumentation am **Anfang** der Klasse, was sie macht und wofür sie da ist, falls nötig.
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
- Testklassen auch dokumentieren bei Bedarf, siehe [hier.](#dokumentation-der-tests)

## Versionierung (Git)

### Branch-Namen
- Aktueller Hauptbranch: `main`
- Für mögliche Erweiterungen: Branches nach [feature], [bugfix], etc. benennen.
  - Beispiel: `feature/user-login`, `bugfix/login-error`.

### Commit-Messages
- Format: `[Typ] Beschreibung`
  - Beispiel: `[Feature] Added user login API`
- Übersicht aller Typen mit Beispielen:
  - [Feature]: Neue Funktionen oder Features werden hinzugefügt.
    - Bsp: `[Feature] Add user authentication module`
  - [Bug]: Bug Bericht erstellen mit **Issue Tracker**
    - siehe [hier](#issue-tracker)
  - [BugFix]: Fehlerbehebungen, um bestehende Probleme zu lösen.
    - Bsp: `[BugFix] Fix login validation issue. Closes #x`
  - [Docs]: Änderungen an der Dokumentation oder das Hinzufügen von Dokumentationsdateien.
    - Bsp: `[Docs] Update STYLEGUIDE with new setup instructions`
  - [Refactor]: Codeänderungen, die die Struktur verbessern, ohne das Verhalten zu ändern.
    - Bsp: `[Refactor] Simplify authentication service`
  - [Test]: Hinzufügen oder Ändern von Tests.
    - Bsp: `[Test] Add unit tests for login service`
  - [Chore]: Änderungen, die keine neue Funktionalität oder Fehlerbehebung betreffen, wie z. B. Konfigurationsänderungen
    oder das Hinzufügen von Tools.
    - Bsp: `[Chore] Update dependencies`

> [!NOTE]
> Für Commit-Messages in anderen Branches ist die Verwendung von `[...] <commit message>` nicht zwingend nötig.

### Issue Tracker
1. **Issue erstellen auf GitHub**
   - Gehe zu GitHub → Repository → Issues → New Issues
   - Title: *Login button funktioniert nicht*
   - Description: *Login button funktioniert nicht, wenn man ihn auf der Startseite anklickt.*
   - Images: Verwende Bilder für bessere Nachvollziehbarkeit
   - GitHub wird ein Ticket erstellen, mit dem man bei Commits referenzieren kann z.B.: `#1`

2. **Committe zuerst den Bug**
   - Arbeite am Code und speichere deine Arbeit mit git add

3. **Referenziere dem Issue**
   - Erstelle einen Commit und referenziere es mit dem Issue `#1`
   ```
   git commit -m "[Bug]: Login Issue (#1)"
   
4. **Commit with fixed bug and closing issue**
   - Nach dem Bugfix dann committen und dem Issue schließen.
   ```
   git commit -m "[BugFix]: Fix login button issue. Closes #1"

## Testing und Qualitätssicherung
- Wir verwenden **JUnit**, **Mockito**, **ArchUnit** und **Testcontainers**.

### Namenskonventionen für Tests
- Testmethoden werden nach den zu testenden Methoden benannt.
  - Beispiel: 
  ```java
  void multiplicationSuccess_03() { ... }
- Im Namen soll auch sehr kurz erwähnt werden, welche Logik getestet wird.
  - Beispiel: 
  ```java
  void login_unauthorized_01() { ... }

### Dokumentation der Tests
- Wir verwenden **@DisplayName** für eine **kurze** Beschreibung der jeweiligen Testfälle, falls der Methodenname nicht 
mehr ausreicht.
  - Beispiel:
  ```java
  @Test
  @DisplayName("Testbeschreibung")
  void test_01() { ... }

### Codeabdeckung
- **> 90 %** Abdeckung bei *Instructions* (Code wurde mind. einmal in einem Test durchlaufen)
- **> 80 %** Abdeckung bei *Branches* (Randfälle, Logik, Exceptions, ...)
- So soll ein guter [JacocoTestReport](#jacocotestreport) aussehen

> [!TIP]
> Führen sie alle Tests vorher aus, damit die Testabdeckung auf dem neuesten Stand ist. 
> Unter `build/jacocoHtml/index.html` ist die Testabdeckung zu finden.

### SonarQube
- Verwende SonarQube für automatischen Code-Review in Hinblick auf Code Smells, Bugs, Vulnerabilities usw.
- Um einen bestandenen Gesamtstatus zu erreichen, sind sämtliche Hinweise 
von Sonar zu berücksichtigen und die Testabdeckung in einem guten Bereich zu halten.
- Siehe aktuelle [Sonar Übersicht](#sonarqube-übersicht)

## HTML-Regeln

### Muster-HTML Seite

```html
<!DOCTYPE html>
<html lang="de" xmlns:th="http://www.thymeleaf.org" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Description here">    <!-- Replace -->
    <meta name="author" content="Author name">              <!-- Replace -->
    <link rel="icon" href="/public/pictures/exambyteIcon.ico">

    <title>Title</title>

    <link rel="stylesheet" href="/public/fontawesome-free-7.1.0-web/css/all.min.css">

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="/public/bootstrap-5.3.8-dist/css/bootstrap.min.css">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="/public/custom/index.css">
</head>

<body>
<!-- Navigation top -->
<header th:replace="~{navigation/navbar.html}"> </header>

<main role="main" class="container mt-5 pt-5">

  <h1> Content here </h1>
  
</main>

<!-- Navigation bottom -->
<header th:replace="~{navigation/navbarDown.html}"> </header>

<!-- js Imports -->
<script src="/public/bootstrap-5.3.8-dist/js/bootstrap.min.js"></script>

</body>
</html>
```

> [!TIP]
> Verfügbar [hier.](../src/main/resources/templates/example/example.html)

### Einrückung und Formatierung
- Verwende **2 Tab** für die Einrückung.
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
- Doppelte Anführungszeichen (`"`) für Attributwerte verwenden.
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

- **Niemals** `th:utext` verwenden! Weitere Informationen [hier.](#xss-cross-site-scripting)

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

## Sicherheit

### CSRF (Cross-Site Request Forgery)

Um unsere Anwendung durch [CSRF-Angriffe](https://de.wikipedia.org/wiki/Cross-Site-Request-Forgery) zu schützen,
verwenden wir `Spring Security` und Thymeleaf auf alle Anfragen (`POST`).

**Beispiel:**

```html
<form th:action="@{/post-x}" method="post"> <!-- Mit th:action arbeiten -->
    <input type="text" name="name"/>
</form>
```

> [!NOTE]
> Spring Security und Thymeleaf fügen **automatisch** ein *hidden* CSRF-Input ein, wenn CSRF aktiv ist.
> Nicht nötig wäre dann sowas:
> ```html
> <form th:action="@{/post-x}" method="post">
>    <!-- nicht nötig, wird automatisch erzeugt -->
>    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
> 
>    <input type="text" name="name"/>
> </form>
> ```

### XSS (Cross-Site Scripting)

[XSS-Angriff](https://developer.mozilla.org/de/docs/Web/Security/Attacks/XSS) ist eine weitere Möglichkeit, unsere
Anwendung zu schaden. Wird schützen unsere Anwendung durch die Verwendung von Thymeleaf.

**Beispiel:**

Ein Nutzer gibt Folgendes ein:

```javascript
<script>alert('Hacked!')</script>
```

Mit `th:text`:
```html
<p th:text="${user.input}"></p>
```

Wird gerendert als:
```html
<p>&lt;script&gt;alert('Hacked!')&lt;/script&gt;</p>
```

Das eingefügte Script wird nicht ausgeführt.

> [!CAUTION]
> Verwende nie `th:utext` !
> Warum? Mal das vorherige Beispiel, aber mit `th:utext`:
> ```html
> <p th:utext="${user.input}"></p>
> ```
> 
> Wird gerendert als:
> ```html
> <p><script>alert('Hacked!')</script></p>
> ```
> 
> Script wird ausgeführt, XSS-Angriff ist dann hiermit erfolgreich!
> 
> `th:utext` behandelt alles als `raw` statt wie `th:text`.

## Quality Assurance (Demo)

### JacocoTestReport

![JacocoTestReport](/src/main/resources/static/public/pictures/quality_assurance/JacocoTest_Report-24-03-2026.png)

### SonarQube Übersicht

![SonarQube Overview](/src/main/resources/static/public/pictures/quality_assurance/sonarOverview.png)

## Ressourcen

### Bilder, GIFS, Videos, usw.
- Memes **erlaubt**.
- Emotes: https://old.7tv.app/emotes
- Alle Ressourcen im Ordner `resources.static.public` ablegen.

### Text
- Textgenerator: https://www.loremipsum.de/

## Fun Facts

### Codelines Statistik

Mit `cloc` kann man anzeigen lassen, wie viel Codezeilen man hat und auch in welcher Sprache.
Hier die Statistik vom 24.03.2026 (`JSON`, `YAML` `CSS` usw. wegen *FontAwesome* und *Bootstrap*):
```
$ cloc .

github.com/AlDanial/cloc v 1.98  T=4.11 s (1239.8 files/s, 125872.7 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
JSON                             3              0              0         225011
YAML                             9             13              0         113764
CSS                             41           9293            266          60357
JavaScript                      23           1620           2779          30956
HTML                           433           1650            126          20322
SVG                           4180              0             30          16726
Java                           266           3393            551          12662
SCSS                            20             98             85           7718
XML                             98             28              0           7158
Markdown                         6            402              7           1407
Text                             4             61              0            158
SQL                              2             23              0            132
Bourne Shell                     1             28            118            105
Maven                            1              9              9             88
Gradle                           2             20              9             83
DOS Batch                        1             21              2             71
PlantUML                         2              5              1             20
Properties                       4              0              1             12
-------------------------------------------------------------------------------
SUM:                          5096          16664           3984         496750
-------------------------------------------------------------------------------

$ cloc src/main/java/

github.com/AlDanial/cloc v 1.98  T=0.08 s (2377.1 files/s, 100436.2 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                           183           1677            223           5832
-------------------------------------------------------------------------------
SUM:                           183           1677            223           5832
-------------------------------------------------------------------------------

$ cloc src/test/java/

github.com/AlDanial/cloc v 1.98  T=0.06 s (1462.7 files/s, 156388.9 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            83           1716            328           6830
-------------------------------------------------------------------------------
SUM:                            83           1716            328           6830
-------------------------------------------------------------------------------
```

### God-Class Refactoring (ExamManagementServiceImpl)

```
$ git pull
remote: Enumerating objects: 1, done.
remote: Counting objects: 100% (1/1), done.
remote: Total 1 (delta 0), reused 0 (delta 0), pack-reused 0 (from 0)
Entpacke Objekte: 100% (1/1), 920 Bytes | 920.00 KiB/s, fertig.
Von github.com:Marvin0109/ExamByte
   f78801e..2f52394  main       -> origin/main
Aktualisiere f78801e..2f52394
Fast-forward
 src/main/java/exambyte/application/service/{ExamManagementService.java => ExamFacadeService.java} |    8 +-
 src/main/java/exambyte/application/service/ExamFacadeServiceImpl.java                             |  175 ++++++++++++++++++++++++++++++
 src/main/java/exambyte/application/service/ExamManagementServiceImpl.java                         |  480 --------------------------------------------------------------------------------
 src/main/java/exambyte/application/service/query/AntwortQueryService.java                         |   21 ++++
 src/main/java/exambyte/application/service/query/AntwortQueryServiceImpl.java                     |   72 ++++++++++++
 src/main/java/exambyte/application/service/query/ExamQueryService.java                            |   23 ++++
 src/main/java/exambyte/application/service/query/ExamQueryServiceImpl.java                        |   99 +++++++++++++++++
 src/main/java/exambyte/application/service/query/FrageQueryService.java                           |   22 ++++
 src/main/java/exambyte/application/service/query/FrageQueryServiceImpl.java                       |   74 +++++++++++++
 src/main/java/exambyte/application/service/query/KorrektorQueryService.java                       |   10 ++
 src/main/java/exambyte/application/service/query/KorrektorQueryServiceImpl.java                   |   35 ++++++
 src/main/java/exambyte/application/service/query/ProfessorQueryService.java                       |   13 +++
 src/main/java/exambyte/application/service/query/ProfessorQueryServiceImpl.java                   |   31 ++++++
 src/main/java/exambyte/application/service/query/ReviewQueryService.java                          |   18 +++
 src/main/java/exambyte/application/service/query/ReviewQueryServiceImpl.java                      |   57 ++++++++++
 src/main/java/exambyte/application/service/query/StudentQueryService.java                         |   13 +++
 src/main/java/exambyte/application/service/query/StudentQueryServiceImpl.java                     |   48 ++++++++
 src/main/java/exambyte/application/service/{ => review}/AutomaticReviewService.java               |    2 +-
 src/main/java/exambyte/application/service/{ => review}/AutomaticReviewServiceImpl.java           |    2 +-
 src/main/java/exambyte/application/service/review/ReviewGenerationService.java                    |   13 +++
 src/main/java/exambyte/application/service/review/ReviewGenerationServiceImpl.java                |   54 +++++++++
 src/main/java/exambyte/application/service/usecase/ExamManagementService.java                     |   31 ++++++
 src/main/java/exambyte/application/service/usecase/ExamManagementServiceImpl.java                 |  229 +++++++++++++++++++++++++++++++++++++++
 src/main/java/exambyte/application/service/usecase/ReviewManagementService.java                   |   10 ++
 src/main/java/exambyte/application/service/usecase/ReviewManagementServiceImpl.java               |   62 +++++++++++
 src/main/java/exambyte/application/service/usecase/ScoringService.java                            |   14 +++
 src/main/java/exambyte/application/service/usecase/ScoringServiceImpl.java                        |   59 ++++++++++
 src/main/java/exambyte/application/service/usecase/SubmitExamResult.java                          |    8 ++
 src/main/java/exambyte/application/service/usecase/TimeConfig.java                                |   15 +++
 src/main/java/exambyte/web/service/ExamControllerServiceImpl.java                                 |   10 +-
 src/test/java/exambyte/application/AutomaticReviewServiceTest.java                                |  412 ---------------------------------------------------------------------
 src/test/java/exambyte/application/ExamManagementServiceTest.java                                 | 1202 ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 src/test/java/exambyte/application/service/ExamFacadeServiceTest.java                             |   82 ++++++++++++++
 src/test/java/exambyte/application/service/query/AntwortQueryServiceTest.java                     |  207 +++++++++++++++++++++++++++++++++++
 src/test/java/exambyte/application/service/query/ExamQueryServiceTest.java                        |  190 ++++++++++++++++++++++++++++++++
 src/test/java/exambyte/application/service/query/FrageQueryServiceTest.java                       |  124 +++++++++++++++++++++
 src/test/java/exambyte/application/service/query/KorrektorQueryServiceTest.java                   |   45 ++++++++
 src/test/java/exambyte/application/service/query/ReviewQueryServiceTest.java                      |   90 +++++++++++++++
 src/test/java/exambyte/application/service/query/StudentQueryServiceTest.java                     |   91 ++++++++++++++++
 src/test/java/exambyte/application/service/review/AutomaticReviewServiceTest.java                 |  316 +++++++++++++++++++++++++++++++++++++++++++++++++++++
 src/test/java/exambyte/application/service/review/ReviewGenerationServiceTest.java                |  133 +++++++++++++++++++++++
 src/test/java/exambyte/application/service/usecase/ExamManagementServiceTest.java                 |  410 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 src/test/java/exambyte/application/service/usecase/ReviewManagementServiceTest.java               |  113 +++++++++++++++++++
 src/test/java/exambyte/application/service/usecase/ScoringServiceTest.java                        |  145 +++++++++++++++++++++++++
 src/test/java/exambyte/web/service/ExamControllerServiceTest.java                                 |   51 +++++----
 45 files changed, 3188 insertions(+), 2131 deletions(-)
 rename src/main/java/exambyte/application/service/{ExamManagementService.java => ExamFacadeService.java} (88%)
 create mode 100644 src/main/java/exambyte/application/service/ExamFacadeServiceImpl.java
 delete mode 100644 src/main/java/exambyte/application/service/ExamManagementServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/AntwortQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/AntwortQueryServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/ExamQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/ExamQueryServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/FrageQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/FrageQueryServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/KorrektorQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/KorrektorQueryServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/ProfessorQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/ProfessorQueryServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/ReviewQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/ReviewQueryServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/query/StudentQueryService.java
 create mode 100644 src/main/java/exambyte/application/service/query/StudentQueryServiceImpl.java
 rename src/main/java/exambyte/application/service/{ => review}/AutomaticReviewService.java (94%)
 rename src/main/java/exambyte/application/service/{ => review}/AutomaticReviewServiceImpl.java (99%)
 create mode 100644 src/main/java/exambyte/application/service/review/ReviewGenerationService.java
 create mode 100644 src/main/java/exambyte/application/service/review/ReviewGenerationServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/ExamManagementService.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/ExamManagementServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/ReviewManagementService.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/ReviewManagementServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/ScoringService.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/ScoringServiceImpl.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/SubmitExamResult.java
 create mode 100644 src/main/java/exambyte/application/service/usecase/TimeConfig.java
 delete mode 100644 src/test/java/exambyte/application/AutomaticReviewServiceTest.java
 delete mode 100644 src/test/java/exambyte/application/ExamManagementServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/ExamFacadeServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/query/AntwortQueryServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/query/ExamQueryServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/query/FrageQueryServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/query/KorrektorQueryServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/query/ReviewQueryServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/query/StudentQueryServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/review/AutomaticReviewServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/review/ReviewGenerationServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/usecase/ExamManagementServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/usecase/ReviewManagementServiceTest.java
 create mode 100644 src/test/java/exambyte/application/service/usecase/ScoringServiceTest.java
```

## Schlussbemerkung
- Das Projekt startete ursprünglich mit 5 Teilnehmer:innen. Nach Abschluss der Klausur arbeitete 
schließlich nur noch ich als alleiniger Entwickler weiter.
- Vielen Dank an alle ursprünglichen Teammitglieder für ihre Unterstützung und Beiträge.
- Die Weiterentwicklung des Projekts wurde von mir allein durchgeführt. 
Mit Geduld und konzentrierter Arbeit ist es gelungen, das Projekt erfolgreich abzuschließen 
(weitere Details in [Readme](../README.md)).

[Zurück zur Übersicht](#übersicht)
