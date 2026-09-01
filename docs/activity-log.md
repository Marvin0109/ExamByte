# Current Status

> **Author:** Marvin0109
> **Updated on:** September 1, 2026

> [!NOTE]
> Since February 11, 2025, the application has been maintained exclusively by Marvin0109.
>
> Since March 24, 2026, further development of ExamByte has been discontinued. The last update included the
> standardization of the codebase language to English, a review of remaining code conventions according to the
> [Style Guide](style-guide.md), and fixes related to the Onion Architecture.

## Activity Log

### 07.11.24

**Area**: Frontend / Backend / Tests
- Created `index.html` and `contact.html`
- Implemented required controllers
- Wrote tests that fail during `gradle build`, but work when executed manually

---

### 08.11.24

**Area**: Frontend / Error Handling
- Implemented login popup
- Created 405 POST error page
- Added minor additional features

---

### 13.11.24

**Area**: Frontend / Structure
- Separated navigation logic from HTML pages
- Added `scripts.js`

---

### 14.11.24

**Area**: Security
- Implemented OAuth2 login

---

### 20.11.24

**Area**: Security / Frontend
- Added admin role
- Issue: `loadUser()` is not being called (root cause unknown)
- Created 403 error page
- Created `exams.html`
- Added additional minor features

---

### 22.11.24

**Area**: Security / Tests
- Browser restart required for role changes to take effect
- Temporarily hardcoded username in `loadUser()`
- Wrote tests for WebController and authentication

---

### 24.11.24

**Area**: Security
- Added logout functionality
- Issue: login data is not fully cleared on logout

---

### 28.11.24

**Area**: Architecture / Tests
- Migrated project to Onion Architecture
- `IndexTest` initially failing, later fixed

---

### 17.12.24

**Area**: Architecture / Tests
- Created Onion Architecture test, but it is failing

---

### 07.01.25

**Area**: Documentation
- Created general documentation

---

### 08.01.25

**Area**: Documentation
- Added style guide

---

### 09.01.25

**Area**: Infrastructure / Tests
- Created `docker-compose.yml`
- Added (non-functional) database test
- Updated and documented Gradle dependencies
- `application.yml` configuration updates

---

### 14.01.25

**Area**: Database / Domain
- Created SQL tables for users, answers, and questions
- Initial implementation of related aggregates completed

---

### 16.01.25

**Area**: Architecture / Tests
- Revised Onion Architecture
- Adjusted architecture test (expected to fail)

---

### 21.01.25

**Area**: Architecture / Persistence
- Moved Onion Architecture into `domain`
- Created mapper and entity classes in `persistence`
- Fixed bug in `ExamByteApplication.java` (`scanBasePackages`)
- Data directory is now stored locally (do not commit)

---

### 23.01.25

**Area**: Database / Tests
- Fixed server connection bug
- Added professor and student repositories
- Created related mappers
- First test for data exchange between server DB and local system (not yet successful)

---

### 27.01.25
**Area**: Persistence / Tests
- Separate classes and packages for JPA and JDBC
- Created and successfully executed JDBC test
- Added service class (implements repository)
- Split SQL file into four separate files
- Context: Spring Data, week 11

---

### 29.01.25
**Area**: Database / Tests
- Fixed Flyway migration issue (separate scripts for four tables)
- `DataBaseTest` still failing (`No Bean Type Found`)
- Package restructuring according to lecture week 12 (without success)

---

### 30.01.25
**Area**: Infrastructure / Refactoring
- Removed JPA and H2
- Switched to Testcontainers as the only test database
- Fixed bean type error
- Renamed and moved classes
- Introduced UUID (not fully tested yet)

---

### 31.01.25
**Area**: Domain / Tests
- Added Exam entity
- Started tests for database relationships

---

### 01.02.25
**Area**: Database / Tests
- Fully implemented database logic
- Created and executed integration and unit tests
- Moved README content to `Activity_log`

---

### 02.02.25
**Area**: Architecture / Refactoring
- Revised Onion Architecture and tests
- Removed dead code
- Created new packages
- Introduced Builder pattern

---

### 03.02.25
**Area**: Service / Documentation
- Added service classes
- Created arc42 architecture documentation
- Updated README
- Onion test failing due to dependencies between `application.config` and `application.service`

---

### 04.02.25
**Area**: Web / Tests
- Created four exam HTML pages
- Added corresponding tests
- Added additional method in answer classes
- Created ExamController (needs refactoring)

---

### 05.02.25
**Area**: Security / Tests
- Fixed bean dependency issues in tests
- Adjusted WebController and added access roles
- Fixed URLs in ExamController tests
- Renamed role `ROLE_USER` → `ROLE_STUDENT`
- Created UUID extraction methods
- Onion test still failing

---

### 06.02.25
**Area**: Architecture / Tests
- Stabilized Onion Architecture
- Adjusted container and mapper tests
- Added tests for aggregates and infrastructure

---

### 07.02.25
**Area**: Security / Tests
- Fixed OAuth2 login, role assignment, and tests
- Completed DTOs, mappers, and interfaces
- Added package documentation (Javadoc)

---

### 09.02.25
**Area**: Quality Assurance / Tests
- Integrated SpotBugs
- Adjusted role assignment (temporary solution)
- Handled `NotFoundException` using `Optional<T>`
- Added DTO mapper and service tests
- Mostly fixed ExamController tests
- Added PowerMock dependencies

---

### 18.02.25
**Area**: Architecture
- Moved persistence package into infrastructure layer
- Created `domain.model.service` and `.impl` for future logic
- Adjusted Onion Architecture test

---

### 03.03.25
**Area**: Tests / Infrastructure
- Fixed tests
- Removed unnecessary imports
- Fixed `AntwortServiceTest`

---

### 13.03.25
**Area**: Environment variables
- Created `.env` file
- Issue analysis for issue #1

---

### 16.03.25
**Area**: Build / Infrastructure
- Updated JaCoCo settings for Gradle 8.x
- Integrated Dotenv for environment variables

---

### 29.03.25
**Area**: Tests / Frontend
- Created additional service tests
- Fixed file paths in exam HTML pages
- Integrated `TestSystemPropertyInitializer` for tests

---

### 30.03.25
**Area**: Infrastructure / Tests
- Updated Spring from 3.3.5 to 3.4.3
- Set DB environment variables for tests
- Added `UserCreationTest` and `AppUserServiceTest`

---

### 01.04.25
**Area**: Domain / Tests
- Implemented admission logic with tests
- Maximum of 12 exams for admission
- Fixed separation between domain and application layer

---

### 02.04.25
**Area**: Grading / Tests
- Implemented storage of MC/SC solutions
- Created automatic grading logic
- Adjusted tests
- Extended ExamProfessor functionality (question selection via click, still buggy)

---

### 03.04.25
**Area**: Frontend
- Added DOM logic (buggy, technical debt)

---

### 05.04.25
**Area**: Frontend
- Issue with broken indexing (Issue #2)

---

### 06.04.25
**Area**: Frontend / UX
- Removed multi-question deletion (too complex)
- New logic: only last question can be deleted
- JavaScript code in `addQuestion.js`

---

### 17.04.25
**Area**: Frontend / Tests
- Increased MC/SC answer options to 6
- Passing question data as JSON
- Removed question form

---

### 18.04.25
**Area**: Web / Tests
- Separated JSON from model
- Created two separate forms
- `BindingResult` still broken (White error page)

---

### 20.04.25
**Area**: Web / Tests
- Fixed `BindingResult` (`@Valid` without `@ModelAttribute`)
- Created `ExamManagementServiceTest` (still incomplete)
- Feature: display of faculty ID on contact page
- More details documented in commits

---

### 11.08.25
**Area**: Cleanup
- Partially implemented delete functionality
- Updated TODO markers
- Removed `update_progress.sh`

---

### 12.08.25
**Area**: Frontend / Export
- Removed DOM event
- Implemented CSV export (not yet tested)
- Added exam creation form

---

### 14.08.25
**Area**: Domain / Tests
- Defined enums across all layers
- Adjusted tests for question types
- Successfully saved exams with questions
- CSV export still failing

---

### 08.09.25
**Area**: Web / Tests
- Added storage of answer options
- Exam listing and editing now functional

---

### 09.09.25
**Area**: Web
- Loaded and displayed exams in `examsDurchfuehren.html`

---

### 11.09.25
**Area**: Tests
- Created `SubmitExam` test
- Added additional TODOs

---

### 15.09.25
**Area**: Feature / Grading
- Added extra menu page with exam information
- Initialized automatic grader
- Grading logic still buggy

---

### 17.09.25
**Area**: Tests / Bugs
- Created tests for automatic grading
- Documented errors (issues)

---

### 22.09.25
**Area**: Feature / CI
- Implemented editing of an exam
- Disabled SpotBugs
- Set up continuous integration

---

### 23.09.25
**Area**: Frontend / Documentation
- Adjusted tests after 22.09 changes
- Active navigation state for menu items
- Updated style guide and TODO documentation

---

### 03.11.25
**Area**: Documentation
- Updated arc42 and README

---

### 04.01.26
**Area**: Infrastructure / Tests
- Updated Docker to version 29.1.3
- Analyzed and fixed Testcontainers issue with outdated API version
- Fix: https://stackoverflow.com/questions/79817033/sudden-docker-error-about-client-api-version
- Removed `TestSystemPropertyInitializer`
- Grading logic: only the last attempt counts
- Additional tests still pending

---

### 05.01.26
**Area**: Refactoring / UI
- Adjusted variable names in persistence layer
- Modified button in test menu

---

### 06.01.26
**Area**: Web / UX / Documentation
- Properly integrated status message on exam creation page
- Added logic: editing not allowed before exam start time
- Added navigation bar at the bottom
- Improved exam overview (status display + table)
- Refactored `Activity_Protocol`

---

### 07.01.26
**Area**: Frontend / Web / UX / Tests
- Initial implementation of grading overview
- Added grading status of manual corrections per exam
- In-progress tests, further work pending

---

### 08.01.26 – 09.01.26
**Area**: Web / Tests / Refactoring
- Tested grading overview
- Cleaned up, adjusted, and added new test code
- Split logic from `ExamController` into `ExamControllerHelper.java`
- Refactored additional classes for code quality

---

### 10.01.26
**Area**: Web / Frontend / Architecture / Tests / Bugs
- Fixed bug where multiple students were shown in grading overview of an exam
- Updated tests due to architectural changes

---

### 21.01.26
**Area**: Documentation
- Updated arc42 documentation
- Updated TO_DO
- Updated Activity log

---

### 22.01.26
**Area**: Refactoring / Testing / Infrastructure
- Created new branch `refactor`
- Added `SonarQube` plugin to `build.gradle`
- Removed major code smells using `SonarQube`
- Added `data-test.sql` and adjusted database tests

---

### 23.01.26
**Area**: Refactoring / Testing / Documentation / Database / Environment Variables / JAR
- Extended SQL script `V1__init.sql` with additional integrity constraints
- Removed remaining code smells
- Added SonarQube usage notes to `example.env`
- Removed redundant attributes from DTOs, entities, and model objects
- Optimized `ExamManagementServiceImpl` and its test for maintainability
- JAR failed to find Thymeleaf templates due to `\` prefix in view names  
  (see https://github.com/spring-projects/spring-boot/issues/2057)
- Introduced Docker volume, removed external local data folder
- Updated README (added installation instructions)

---

### 24.01.26
**Area**: UX / Authentication / Tests / Architecture
- Added feature allowing users to choose their role
- Every logged-in user receives the `STUDENT` role by default
- Maintained Onion Architecture
- Fixed Index, Contact, and Settings tests

---

### 25.01.26
**Area**: Documentation
- Added Testcontainers troubleshooting to `README`

---

### 26.01.26
**Area**: Resources / Documentation
- Updated code statistics (`cloc`)
- Updated test coverage statistics

---

### 27.01.26
**Area**: Resources / Tests / Business Logic
- Added additional logic tests
- Updated JaCoCo test report
- Added validations in entities and service classes

---

### 28.01.26
**Area**: UI / UX
- Reviewed and fixed HTML documents for accessibility
- Added landing page with introduction and usage instructions
- Minor extras

---

### 29.01.26
**Area**: UI
- Final adjustments to HTML documents regarding usability

---

### 30.01.26 – 31.01.26
**Area**: UI / UX / Feature
- Implemented admission logic as a progress bar
- Added answer correction feature

---

### 01.02.26 – 03.02.26
**Area**: Maintainability / Feature / env / Documentation
- Refactored `ExamManagementServiceImpl` for better maintainability and cohesion
- Added `git pull` log to STYLEGUIDE under *Fun Facts*
- Manual correction results are now only visible after the result release time
- Renamed `example.env` to `.env.example` and updated `.gitignore`

---

### 04.02.26
**Area**: UI / UX / Architecture / Tests
- Implemented result view for students
- Split form objects into separate packages
- Added tests for result view feature

---

### 05.02.26 – 06.02.26
**Area**: UI / UX / Refactoring / Tests
- Moved helper methods from `ExamControllerServiceImpl` to `HelperServiceImpl`
- Implemented test run with historical answers
- Fixed comma conversion for database storage and UI display
- Tests

---

### 08.02.26
**Area**: Tests / Refactoring
- Added integration tests for the following use cases: `createExam`, `submitAnswers`, and `createReview`
- Fixed minor code smells

---

### 10.02.26 – 11.02.26
**Area**: UI / Refactoring
- Created HTML for results overview
- Split `ExamController` and updated tests and endpoints

---

### 13.02.26
**Area**: UI / UX / Tests / Dependencies
- Fully implemented and tested results overview
- Updated Spring Boot to **3.5.8** (Testcontainers failed on GitHub Actions without it)

---

### 15.02.26 – 16.02.26
**Area**: UI / UX / Feature / Tests
- Implemented and tested CSV download for exams and student results
- Improved delete exam logic

---

### 17.02.26
**Area**: UI / UX
- Proper rendering of long answer options in MC/SC questions
- Added usage guidelines for creating MC/SC questions

---

### 19.02.26
**Area**: UX / Feature
- Number of question types for exams is now configurable
- `showReview` now works even for non-submitted answers

---

### 03.03.26
**Area**: Race Condition / Database Optimization
- Replaced original primary key with subject ID (`UUID`)
- Optimized transactions in `submitExam` (previously: find + delete + insert, now: upsert)
- Implemented stress tests for `submitExam`
- Minor extras

---

### 04.03.26
**Area**: UI / UX
- Implemented exam preview for professors
- Fixed rendering of questions with separators
- Extras and related tests

---

### 12.03.26
**Area**: Documentation
- Used info annotations in Markdown files
- Updated documentation

---

### 15.03.26
**Area**: Documentation / UI
- Fixed rendering of multi-line questions
- Updated README and extras

---

### 16.03.26
**Area**: Database / Feature / Validation / UX
- Standardized application to use `double` for score calculation (half points allowed)
- Introduced trigger ensuring `maxPoints >= reviewPoints`
- Doubled points in database from `double` to `int`
- Extended input validation for `createExam` (`typeMismatch`, answer options, correct solutions, etc.)
- Replaced redirect on invalid input with returning to the current form (with validation messages displayed)
- Updated scoring logic in `AutomaticReviewService`
- Added required tests

---

### 17.03.26
**Area**: Documentation / Feature
- Created flowchart for software architecture and API calls
- Added login demo
- Started `feature/review-lock` branch

---

### 18.03.26
**Area**: Documentation / UI / Project closure
- Minor UI fixes (correct rendering of multi-line sentences)
- Implementation of `feature/review-lock` discarded (branch was not merged and has been deleted) → not required
  for the core functionality of ExamByte in its first complete version
- Updated documentation and added screenshots as visual demonstration of core features

---

### 19.03.26 – 20.03.26
**Area**: Language refactoring
- Major source code renaming:
    - `Korrektor` → `Reviewer`
    - `Antwort` → `Answer`
    - `KorrekteAntworten` → `CorrectAnswers`
- Updated corresponding mappers, entities, DTOs, etc.
- Verified application state using regression tests and manual testing

---

### 22.03.26 – 24.03.26
**Area**: Language / Onion Architecture / Error handling / UX
- Continued language migration in source code:
    - `Frage` → `Question`
    - Remaining method names and fields updated
- Introduced generic error page for frontend
- Added logic preventing students from submitting answers after the deadline
- Merged `infrastructure.service` layer into `application.service.query` layer
- Fixed Onion Architecture and added additional tests
- Minor extras and cleanup
- Updated documentation (added table of contents)

---

### 19.05.26
**Area**: Documentation
- Translating existing docs (de → en)

---

### 27.08.26
**Area**: Deployment / Time logic / Configurations
- Creating Dockerfile for deployment
- Creating `application-local.example.yaml` for local development
- Changing configs in `docker-compose.yml` for local development and deployment
- Using `Clock` instead of `LocalDateTime.now()` in some classes

---

### 30.08.26
**Area**: Refactoring / Review
- Removed unnecessary interfaces of mapper classes
- Display of reached points in a review is fixed (`maxPoints` -> `reachedPoints`)

---

### 31.08.26
**Area**: Deployment
- Adding Caddyfile
- Adding configurations for using Caddy

---

### 01.09.26
**Area**: Deployment / Local development
- Creating `dev.sh` for starting local development
- Changing some configurations in `application.yaml` for deployment