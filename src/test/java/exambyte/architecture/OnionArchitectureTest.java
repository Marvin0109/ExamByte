package exambyte.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import exambyte.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse enthält ArchUnit-Tests, die Architekturregeln für die Exambyte-Anwendung überprüfen.
 * Einige Tests sind noch in Arbeit oder funktionieren nicht vollständig.
 *
 * Hinweis: Der Exam 'noFieldInjectionInProductionCode' ist momentan in "Work in progress" und wurde noch nicht
 * vollständig implementiert. Er wird dazu verwendet, um sicherzustellen, dass keine Feldinjektionen in Produktionscode
 * (außerhalb von Testklassen) verwendet wird.
 */
@AnalyzeClasses(packages = "exambyte")
public class OnionArchitectureTest {

    /**
     * Enthält die importierten Java-Klassen aus dem angegebenen Paket "exambyte".
     * Die importierten Klassen werden für ArchUnit-Tests verwendet, um Architekturregeln innerhalb
     * der Exambyte-Anwendung zu prüfen.
     *
     * Diese Variable wird hauptsächlich zur Definition und Überprüfung verschiedener Architekturregelexemplare
     * verwendet, um sicherzustellen, dass die vorgegebene Schichtenarchitektur und andere Richtlinien
     * eingehalten werden.
     */
    private final JavaClasses klassen = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("exambyte");

    @Test
    @DisplayName("Die ExamByte Anwendung hat eine Onion Architektur")
    public void onionArchitecture() throws Exception {
        ArchRule rule = Architectures.onionArchitecture()
                .domainModels("exambyte.domain..")
                .domainServices("exambyte.domain.service..")
                .applicationServices("exambyte.application.service..")
                .adapter("web", "exambyte.web.controllers..")
                .adapter("persistence", "exambyte.persistence..")
                .adapter("service", "exambyte.service..", "exambyte.application.service..",
                        "exambyte.application.interfaces..")
                .adapter("configuration", "exambyte.application.config..", "exambyte.application.service..")
                .adapter("repository", "exambyte.persistence.repository..")
                .adapter("controller", "exambyte.web.controllers..")
                .adapter("dto", "exambyte.application.dto..");
        rule.check(klassen);
    }

    /**
     * Diese Regel stellt sicher, dass keine Produktionskonfigurationsklassen wie {@link SecurityConfig}
     * in Testklassen verwendet werden.
     * Hinweis: Der Exam funktioniert nicht, da wir noch die Config Klassen als Testhilfsmittel verwenden.
     */
    //@ArchTest
    //static final ArchRule noProductionCodeInTests = classes()
    //      .should().onlyHaveDependentClassesThat()
    //        .resideOutsideOfPackages("exambyte.domain.config..")
    //        .because("Testklassen sollten keine Produktionskonfigurationsklassen wie @SecurityConfig verwenden");

    /**
     * Regel zur Vermeidung von Feldinjektionen in Produktionscode (außer Testklassen).
     * Der Exam ist momentan noch in Arbeit und wurde noch nicht vollständig implementiert.
     * {@link ArchTest} ist deaktiviert, bis der Exam vollständig funktionstüchtig ist.
     */
    /*  **WORK IN PROGRESS**
    @ArchTest
    static final ArchRule noFieldInjectionInProductionCode = classes()
            .that().resideInAPackage("exambyte..")
            .and(classes -> !classes.getSimpleName().contains("Exam"))
            .should()
            .notBeAnnotatedWith(Autowired.class);*/
}