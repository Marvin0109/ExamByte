package exambyte.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

//Uebung_7
@AnalyzeClasses(packages = "exambyte")
public class OnionArchitectureTest {

    @ArchTest
    static final ArchRule onionArchitectureRule = onionArchitecture()
            .domainModels("exambyte.domain..") // Domain-Modelle
            .domainServices("exambyte.domain..") // Domain-Services
            .applicationServices("exambyte.application..") // Application-Services
            .adapter("web", "exambyte.presentation.controllers..") // Web-Adapter (Controller)
            .adapter("config", "exambyte.domain.config.."); // Konfigurationsadapter

    @ArchTest
    static final ArchRule noFieldInjectionRule = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
}
