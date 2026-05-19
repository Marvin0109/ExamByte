# Project Style Guide

> **Author**: Marvin0109  
> **Created on**: January 08, 2025  
> **Updated on**: March 24, 2026

> [!NOTE]
> This style guide defines formatting and convention rules for the project. The goal is to
facilitate collaboration and ensure code quality.
> It was originally created for a team of 5 developers but is currently maintained and adapted by a single person
who continues the project privately.

## Overview

- [General Rules](#general-rules)
  - [Language](#language)
  - [File Names](#file-names)
- [Formatting](#formatting)
  - [Indentation](#indentation)
  - [Line Length](#line-length)
  - [Blank Lines](#blank-lines)
- [Technology and Versions](#technologies-and-versions)
  - [Java Version](#java-version)
  - [Frameworks and Libraries](#frameworks-and-libraries)
  - [GitHub OAuth App](#github-oauth-app)
  - [Development Environment](#development-environment)
  - [Docker](#docker)
  - [Database](#database)
- [Code Style](#code-style)
  - [Variable Names](#variable-names)
  - [Class Names](#class-names)
  - [Constants](#constants)
  - [Comments and Documentation](#comments-and-documentation)
- [Version Control (Git)](#version-control-git)
  - [Branch Names](#branch-names)
  - [Commit Messages](#commit-messages)
  - [Issue Tracker](#issue-tracker)
- [Testing and Quality Assurance](#testing-and-quality-assurance)
  - [Naming Conventions for Tests](#naming-conventions-for-tests)
  - [Test documentation](#test-documentation)
  - [Code Coverage](#code-coverage)
  - [SonarQube](#sonarqube)
- [HTML-Regeln](#html-guidelines)
  - [Sample HTML Page](#sample-html-page)
  - [Indentation and Formatting](#indentation-and-formatting)
  - [Tags](#tags)
  - [Attributes](#attributes)
  - [Class and ID Names](#class-and-id-names)
  - [Accessibility](#accessibility)
  - [External Resources](#external-resources)
- [Thymeleaf Rules](#thymeleaf-rules)
  - [Usage of Placeholders](#usage-of-placeholders)
  - [Attributes](#attributes-1)
  - [Loops and Conditions](#loops-and-conditions)
  - [Default Values](#default-values)
  - [Comments](#comments)
  - [Structured Templates](#structured-templates)
- [Security](#security)
  - [CSRF](#csrf-cross-site-request-forgery)
  - [XSS](#xss-cross-site-scripting)
- [Quality Assurance (Demo)](#quality-assurance-demo)
  - [JacocoTest Report](#jacocotestreport)
  - [SonarQube Overview](#sonarqube-overview)
- [Resources](#resources)
  - [Images, GIFS, Videos, etc.](#images-gifs-videos-etc)
  - [Text](#text)
- [Fun Facts](#fun-facts)
  - [Codelines Statistics](#codelines-statistics)
  - [God-Class Refactoring](#god-class-refactoring-exammanagementserviceimpl)
- [Closing Remarks](#closing-remarks)

## General Rules

### Language
- We mainly program in **Java** and use **HTML** for the web interface.
- The **PostgreSQL database** is used for data storage.
- All comments and documentation should be written in **German**.

> [!NOTE]
> Code language should be English whenever possible.

### File Names
- Use **UpperCamelCase** for Java files (e.g. `HelloWorld.java`).
- Other files use **camelCase** (e.g. `beepoRun.png`).
- Tests must end with `Test` (e.g. `UserRepositoryTest.java`).

## Formatting

### Indentation
- Use a tab (default indentation in IntelliJ).

### Line Length
- Maximum **80 characters per line** (except for long URLs).
- If longer lines are necessary (e.g. SQL or long strings), use meaningful line breaks.

### Blank Lines
- 1 blank line between methods or classes.
- **0** blank lines between documentation and classes or methods.

## Technologies and Versions

### Java Version
- The project uses **Java 21**. It is recommended to use this version or a compatible one to ensure that the code compiles and runs correctly.

### Frameworks and Libraries
- **Spring Boot:** Version **3.5.8**  
  (Core framework for building the web application)
- **Spring Security:** (For security aspects, including OAuth2 client)
- **Thymeleaf:** (Template rendering with Spring Boot)
- **JUnit:** (Unit testing)
- **ArchUnit:** Version **1.3.0**  
  (Architecture tests to enforce architectural rules)
- **Testcontainers:** (Enables integration testing with real databases in containers)
- **Gradle:** Version **8.x**  
  (Build tool)
- **Dotenv:** (Loads environment variables from a `.env` file)
- **Flyway:** Version **11.2.0**  
  (Database migration tool)
- **SonarQube:** Version **7.2.2.6593**  
  (Code analysis tool for detecting code smells)

### GitHub OAuth App

Uses an *Authorized OAuth App ExamByte* from *Marvin0109*.

(If needed, a custom OAuth app can be created and connected to the project. In this case, the required environment 
variables must be set in a local `.env` file.)

### Development Environment
- **IDE:** We use IntelliJ IDEA, preferably the Ultimate edition, as other versions may have limitations regarding databases.  
  See [here](#database) for more details.

### Docker
- **Docker Desktop:** We use **Docker Desktop** for building, testing, and running
  Docker containers during development.
- **Dockerfiles and YAML:** Dockerfiles are used to build Docker images. In **docker-compose.yml**,
  container configurations and connections are defined to ensure a consistent development environment.
  - Example:
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

### Database
- **Database:** The project uses `postgres:15-alpine`
- Non-Ultimate versions of IntelliJ should use database tools such as [DBeaver Community](https://dbeaver.io/download/)
- Alternatively, any database tool the user is familiar with can be used

## Code Style

### Variable Names
- Use **camelCase**
  - Example: `chainBuilder`

### Class Names
- Use **UpperCamelCase**
  - Example: `AppUserService`, `SecurityConfig`

### Constants
- Use **UPPER_CASE**
  - Example: `MAX_ATTEMPTS`, `DEFAULT_TIMEOUT`

### Comments and Documentation
- Class-level documentation should be placed at the **beginning of the class**, describing its purpose when necessary.
- Use Javadoc for documentation:

  ```java
  /**
   * Processes user requests.
   *
   * @param userId ID of the user
   * @return user object
   * @see UserRepository#findById(Long)
   * @link https://docs.spring.io
   */

- Use @link and @see where appropriate to reference variables, classes, or other elements.
- Test classes should also be documented when necessary, check [here.](#test-documentation)

## Version Control (Git)

### Branch Names
- Current main branch: `main`
- For possible extensions: use branches like `feature`, `bugfix`, etc.
  - Example: `feature/user-login`, `bugfix/login-error`

### Commit Messages
- Format: `[Type] Description`
  - Example: `[Feature] Added user login API`

- Overview of types with examples:
  - `[Feature]`: New functionality or features are added.
    - Example: `[Feature] Add user authentication module`
  - `[Bug]`: Bug reports are created using the issue tracker
    - see [here](#issue-tracker)
  - `[BugFix]`: Fixes for existing issues
    - Example: `[BugFix] Fix login validation issue. Closes #x`
  - `[Docs]`: Changes or additions to documentation
    - Example: `[Docs] Update STYLEGUIDE with new setup instructions`
  - `[Refactor]`: Code changes that improve structure without changing behavior
    - Example: `[Refactor] Simplify authentication service`
  - `[Test]`: Adding or modifying tests
    - Example: `[Test] Add unit tests for login service`
  - `[Chore]`: Changes that do not affect functionality or bug fixes (e.g. configuration, tooling)
    - Example: `[Chore] Update dependencies`

> [!TIP]
> For commit messages in other branches, using the `[Type] <commit message>` format is not mandatory.

### Issue Tracker
**Create an Issue on GitHub**
  - Go to GitHub → Repository → Issues → New Issue
  - **Title:** e.g., *Login button not working*
  - **Description:** e.g., *The login button does not work when clicked on the homepage.*
  - **Images:** Use screenshots for better clarity
  - GitHub will create a ticket that can be referenced in commits, e.g., `#1`

**Work on the Bug**
  - Make changes in the code and stage your work locally:
    ```
    git add .

**Commit referencing the Issue**
  - Create a commit and reference the issue:
    ```
    git commit -m "[Bug]: Login Issue (#1)"

**Commit the Fix and Close the Issue**
  - After fixing the bug:
    ```
    git commit -m "[BugFix]: Fix login button issue. Closes #1"

## Testing and Quality Assurance

### Naming Conventions for Tests
- Test methods are named after the method or functionality being tested.
  - Example:
  ```java
  void multiplicationSuccess_03() { ... }
- The name should also briefly indicate which logic is being tested.
  - Example: 
  ```java
  void login_unauthorized_01() { ... }

### Test documentation
- We use `@DisplayName` for a short description of each test case, in case the method name alone is not sufficient.
  - Example:
  ```java
  @Test
  @DisplayName("Test description")
  void test_01() { ... }

### Code Coverage
- **> 90%** coverage for *Instructions* (code executed at least once in a test)
- **> 80%** coverage for *Branches* (edge cases, logic, exceptions, ...)
- This is how a good [JacocoTestReport](#jacocotestreport) should look like

> [!TIP]
> Run all tests beforehand to ensure the code coverage is up to date.
> The coverage report can be found at `build/jacocoHtml/index.html`.

# SonarQube
- Use SonarQube for automated code review regarding code smells, bugs, vulnerabilities, etc.
- To achieve a passing overall status, all Sonar issues should be addressed and code coverage should be kept at a good level.
- See the current [Sonar overview](#sonarqube-overview)

## HTML Guidelines

### Sample HTML Page

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
> Template available [here.](../src/main/resources/templates/example/example.html)

### Indentation and Formatting
- Use **2 tabs** for indentation.
- Each opening of a new element should be indented to make the structure readable.
  - Example:
  ```html
  <div>
      <p>Example</p>
  </div>

### Tags
- **Closed tags:** All HTML tags that require an end tag must be properly closed.

### Attributes
- Write all attributes in **lowercase**.
- Use double quotes (`"`) for attribute values.
  - Example:
  ```html
  <input type="text" name="username" />

- **Attribute order:** List required attributes first, then optional ones.
  - Example:
  ```html
  <img src="fig.jpg" alt="Description" width="300" height="200" />

### Class and ID Names
- Use **kebab-case** for class and ID names.
  - Example:
  ```html
  <div id="main-header" class="user-profile"></div>

- Use comments to mark sections in the HTML code.
  - Example:
  ```html
  <!-- Navigation -->
  <nav>
    ...
  </nav>

### Accessibility
- Use `alt` attributes for images to support screen readers.
- Avoid purely decorative content without context.
- Forms should always include a `<label>` element linked to the input field via the `for` attribute.

### External Resources
- Load CSS and JavaScript files in the correct location:
  - CSS: In the `<head>` tag.
  - JavaScript: At the end of the `<body>` tag.

## Thymeleaf Rules

### Usage of Placeholders
- Use the **Thymeleaf expression syntax** `${}` for variables.
- Avoid mixing plain HTML with server-side values if it can be handled using Thymeleaf expressions.
  - Example:
  ```html
  <p th:text="${user.name}">Username</p>

### Attributes
- Use Thymeleaf-specific attributes like `th:text`, `th:href`, `th:if`, `th:each` for dynamic content.
- The `th:*` attributes should be used **instead of static HTML attributes**.
  - Example:
  ```html
  <a th:href="@{/profile}" href="#">Profile</a>

### Loops and Conditions
- Use `th:each` for loops and `th:if` or `th:unless` for conditions.
  - Example:
  ```html
  <ul>
      <li th:each="item : ${items}" th:text="${item}">Item</li>
  </ul>
  
- Examples for conditions:
  ```html
  <p th:if="${user.loggedIn}">Welcome back!</p>
  <p th:unless="${user.loggedIn}">Login please.</p>

### Default Values
- Use `th:text` with a default value in case the placeholder is `null`.
  - Example:
  ```html
  <p th:text="${user.name ?: 'Guest'}">Guest</p>

- **Never** use `th:utext`! For more details check [here.](#xss-cross-site-scripting)

### Comments
- Thymeleaf provides its own comment syntax, which is removed during rendering:
  - Example:
  ```html
  <!--/* Deleting comment */-->

### Structured Templates
- Error pages in `templates/error`
- Web pages in `templates`

## Security

### CSRF (Cross-Site Request Forgery)

To protect our application from [CSRF attacks](https://en.wikipedia.org/wiki/Cross-site_request_forgery),
we use `Spring Security` and Thymeleaf for all requests (`POST`).

**Example:**

```html
<form th:action="@{/post-x}" method="post"> <!-- Using th:action -->
    <input type="text" name="name"/>
</form>
```

> [!NOTE]
> Spring Security and Thymeleaf automatically add a *hidden* CSRF input when CSRF protection is enabled.
> You do **not** need to do something like this:
> ```html
> <form th:action="@{/post-x}" method="post">
>    <!-- not needed, automatically generated -->
>    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
> 
>    <input type="text" name="name"/>
> </form>
> ```

### XSS (Cross-Site Scripting)

[XSS attacks](https://developer.mozilla.org/en-US/docs/Web/Security/Attacks/XSS) are another way to harm our
application. We protect our application by using Thymeleaf.

**Example:**

A user submits the following input:

```javascript
<script>alert('Hacked!')</script>
```

With `th:text`:
```html
<p th:text="${user.input}"></p>
```

It will be rendered as:
```html
<p>&lt;script&gt;alert('Hacked!')&lt;/script&gt;</p>
```

The injected script will **not** be executed.

> [!CAUTION]
> Never use `th:utext`!
> Why? Let's take the previous example but with `th:utext`:
> ```html
> <p th:utext="${user.input}"></p>
> ```
>
> It will be rendered as:
> ```html
> <p><script>alert('Hacked!')</script></p>
> ```
>
> The script will be executed, and an XSS attack becomes possible!
>
> `th:utext` treats everything as raw content, unlike `th:text`.

## Quality Assurance (Demo)

### JacocoTestReport

![JacocoTestReport](/src/main/resources/static/public/pictures/quality_assurance/JacocoTest_Report-24-03-2026.png)

### SonarQube Overview

![SonarQube Overview](/src/main/resources/static/public/pictures/quality_assurance/sonarOverview.png)

## Resources

### Images, GIFS, Videos, etc.
- Memes **allowed**.
- Emotes: https://old.7tv.app/emotes
- Save all files in `resources.static.public`.

### Text
- Generating dummy text: https://www.loremipsum.de/

## Fun Facts

### Codelines Statistics

Using `cloc`, you can see how many lines of code exist and in which programming languages.  
Here is the code statistics as of 24.03.2026 
(including `JSON`, `YAML`, `CSS`, etc., due to *FontAwesome* and *Bootstrap*):
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

## Closing Remarks
- The project initially started with 5 participants. After the completion of the exam, 
I continued as the sole developer.
- Many thanks to all original team members for their support and contributions.
- The further development of the project was carried out solely by me.  
  With patience and focused work, the project was successfully completed (for more details, check [README](../README.md)).

[Back to overview](#overview)
