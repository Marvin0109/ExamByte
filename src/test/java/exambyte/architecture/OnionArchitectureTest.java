package exambyte.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
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

    // Architekturregel für die Onion-Architektur
    /*@ArchTest
    static final ArchRule onionArchitectureRule = onionArchitecture()
            .domainModels("exambyte.domain..") // Domain-Modelle
            .domainServices("exambyte.domain..") // Domain-Services
            .applicationServices("exambyte.application..") // Application-Services
            .adapter("web", "exambyte.presentation.controllers..") // Web-Adapter (Controller)
            .adapter("config", "exambyte.domain.config.."); // Konfigurationsadapter*/

    /**
     * Diese Regel stellt sicher, dass keine Produktionskonfigurationsklassen wie {@link exambyte.domain.config.SecurityConfig}
     * in Testklassen verwendet werden.
     * Hinweis: Der Test funktioniert momentan noch nicht korrekt.
     */
    @ArchTest
    static final ArchRule noProductionCodeInTests = classes()
            .that().resideInAPackage("exambyte.presentation.controllers..")
            .should().onlyHaveDependentClassesThat()
            .resideOutsideOfPackages("exambyte.domain.config..")
            .because("Testklassen sollten keine Produktionskonfigurationsklassen wie @SecurityConfig verwenden");

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
