package exambyte.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

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

    @ArchTest
    static final ArchRule noProductionCodeInTests = classes()
            .that().resideInAPackage("exambyte.presentation.controllers..")
            .should().onlyHaveDependentClassesThat()
            .resideOutsideOfPackages("exambyte.domain.config..")
            .because("Testklassen sollten keine Produktionskonfigurationsklassen wie @SecurityConfig verwenden");


    /* Regel zur Vermeidung von Feldinjektion in Produktionscode (außer Testklassen) **WORK IN PROGRESS**
    @ArchTest
    static final ArchRule noFieldInjectionInProductionCode = classes()
            .that().resideInAPackage("exambyte..")
            .and(classes -> !classes.getSimpleName().contains("Test"))
            .should()
            .notBeAnnotatedWith(Autowired.class);*/
}
