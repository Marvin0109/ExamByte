package exambyte.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

/**
 * Diese Klasse enthält ArchUnit-Tests, die Architekturregeln für die Exambyte-Anwendung überprüfen.
 * Einige Tests sind noch in Arbeit oder funktionieren nicht vollständig.
 *
 * Hinweis: Der Test 'noFieldInjectionInProductionCode' ist momentan in "Work in progress" und wurde noch nicht
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
                .adapter("web", "exambyte.web..")
                .adapter("persistence", "exambyte.persistence..")
                .adapter("configuration", "exambyte.domain.config..", "exambyte.persistence.config..");
        rule.check(klassen);
    }

    /**
     * Diese Regel stellt sicher, dass keine Produktionskonfigurationsklassen wie {@link exambyte.domain.config.SecurityConfig}
     * in Testklassen verwendet werden.
     * Hinweis: Der Test funktioniert nicht, da wir noch die Config Klassen als Testhilfsmittel verwenden.
     */
    //@ArchTest
    //static final ArchRule noProductionCodeInTests = classes()
    //      .should().onlyHaveDependentClassesThat()
    //        .resideOutsideOfPackages("exambyte.domain.config..")
    //        .because("Testklassen sollten keine Produktionskonfigurationsklassen wie @SecurityConfig verwenden");

    /**
     * Regel zur Vermeidung von Feldinjektionen in Produktionscode (außer Testklassen).
     * Der Test ist momentan noch in Arbeit und wurde noch nicht vollständig implementiert.
     * {@link ArchTest} ist deaktiviert, bis der Test vollständig funktionstüchtig ist.
     */
    /*  **WORK IN PROGRESS**
    @ArchTest
    static final ArchRule noFieldInjectionInProductionCode = classes()
            .that().resideInAPackage("exambyte..")
            .and(classes -> !classes.getSimpleName().contains("Test"))
            .should()
            .notBeAnnotatedWith(Autowired.class);*/
}