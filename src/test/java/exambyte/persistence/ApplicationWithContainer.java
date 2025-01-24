package exambyte.persistence;

import exambyte.application.ExamByteApplication;
import exambyte.persistence.container.TestcontainerConfiguration;
import org.springframework.boot.SpringApplication;

public class ApplicationWithContainer {

    /*
  Kann zum lokalen Starten der Anwendung ohne einen laufenden DB-Container benutzt werden.
  Es wird dann ein Postgrescontainer via Testcontainers automatisch gestartet und verwendet.
  */

    public static void main(String[] args) {
        SpringApplication.from(ExamByteApplication::main)
                .with(TestcontainerConfiguration.class)
                .run(args);
    }
}
