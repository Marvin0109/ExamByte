package exambyte.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Diese Klasse enthält ArchUnit-Tests, die Architekturregeln für die Exambyte-Anwendung überprüfen.
 */
@AnalyzeClasses(
        packages = "exambyte",
        importOptions = ImportOption.DoNotIncludeTests.class)
class OnionArchitectureTest {

    /**
     * Enthält die importierten Java-Klassen aus dem angegebenen Paket "exambyte".
     * Die importierten Klassen werden für ArchUnit-Tests verwendet, um Architekturregeln innerhalb
     * der Exambyte-Anwendung zu prüfen.
     * Diese Variable wird hauptsächlich zur Definition und Überprüfung verschiedener Architekturregelexemplare
     * verwendet, um sicherzustellen, dass die vorgegebene Schichtenarchitektur und andere Richtlinien
     * eingehalten werden.
     */

    @ArchTest
    ArchRule onionArchitecture = Architectures.onionArchitecture()
        .domainModels("exambyte.domain..")
        .domainServices("exambyte.domain.service..")
        .applicationServices("exambyte.application.service..")
        .adapter("persistence", "exambyte.infrastructure.persistence.repository..")
        .adapter("service", "exambyte.infrastructure.service..",
                "exambyte.infrastructure.config..", "exambyte.web.service..")
        .adapter("repository", "exambyte.infrastructure.persistence.repository..")
        .adapter("controller", "exambyte.web.controllers..")
        .adapter("mapper", "exambyte.infrastructure.mapper..",
                "exambyte.infrastructure.persistence.mapper..", "exambyte.application.mapper..");


    @ArchTest
    ArchRule noRandomUUIDUsage = noClasses()
            .should()
            .callMethod(UUID.class, "randomUUID");

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
    ArchRule allMapperShouldBeAnnotatedWithController = classes()
        .that()
        .haveSimpleNameEndingWith("MapperImpl")
        .should()
        .beAnnotatedWith(Component.class);

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
