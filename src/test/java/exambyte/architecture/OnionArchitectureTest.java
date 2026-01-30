package exambyte.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

/**
 * Diese Klasse enthält ArchUnit-Tests, die Architekturregeln für die Exambyte-Anwendung überprüfen.
 */
@AnalyzeClasses(packages = "exambyte")
class OnionArchitectureTest {

    /**
     * Enthält die importierten Java-Klassen aus dem angegebenen Paket "exambyte".
     * Die importierten Klassen werden für ArchUnit-Tests verwendet, um Architekturregeln innerhalb
     * der Exambyte-Anwendung zu prüfen.
     * Diese Variable wird hauptsächlich zur Definition und Überprüfung verschiedener Architekturregelexemplare
     * verwendet, um sicherzustellen, dass die vorgegebene Schichtenarchitektur und andere Richtlinien
     * eingehalten werden.
     */
    private final JavaClasses klassen = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("exambyte");

    @Test
    @DisplayName("Die ExamByte Anwendung hat eine Onion Architektur")
    void onionArchitecture() {
        ArchRule rule = Architectures.onionArchitecture()
            .domainModels("exambyte.domain..")
            .domainServices("exambyte.domain.service..")
            .applicationServices("exambyte.application.service..")
            .adapter("persistence", "exambyte.infrastructure.persistence.repository..")
            .adapter("service", "exambyte.infrastructure.service..",
                    "exambyte.infrastructure.config..", "exambyte.web.service..")
            .adapter("repository", "exambyte.infrastructure.persistence.repository..")
            .adapter("controller", "exambyte.web.controllers..")
            .adapter("mapper", "exambyte.infrastructure.mapper..",
                    "exambyte.infrastructure.persistence.mapper..");
        rule.check(klassen);
    }

    @ArchTest
    ArchRule allClassesInInfrastructureServiceShouldBeAnnotatedWithService = classes()
        .that()
        .resideInAPackage("..infrastructure.service..")
        .and().haveSimpleNameEndingWith("Impl")
        .should()
        .beAnnotatedWith(Service.class);

    @ArchTest
    ArchRule allClassesInApplicationServiceShouldBeAnnotatedWithService = classes()
        .that()
        .resideInAPackage("..application.service..")
        .and().haveSimpleNameEndingWith("Impl")
        .should()
        .beAnnotatedWith(Service.class);

    @ArchTest
    ArchRule allClassesInWebServiceShouldBeAnnotatedWithService = classes()
        .that()
        .resideInAPackage("..web.service..")
        .and().haveSimpleNameEndingWith("Impl")
        .should()
        .beAnnotatedWith(Service.class);

    @ArchTest
    ArchRule allClassesInPersistenceRepositoryShouldBeAnnotatedWithRepository = classes()
        .that()
        .resideInAPackage("..persistence.repository..")
        .and().haveSimpleNameEndingWith("Impl")
        .should()
        .beAnnotatedWith(Repository.class);

    @ArchTest
    ArchRule allControllerInWebControllersShouldBeAnnotatedWithController = classes()
        .that()
        .resideInAPackage("..web.controllers..")
        .and().haveSimpleNameEndingWith("Controller")
        .should()
        .beAnnotatedWith(Controller.class);

    @ArchTest
    ArchRule domainClassesHasPrivateFields = fields()
        .that()
        .areDeclaredInClassesThat()
        .resideInAPackage("..domain..")
        .and()
        .areDeclaredInClassesThat()
        .resideOutsideOfPackage("..common..")
        .should()
        .bePrivate();
}
