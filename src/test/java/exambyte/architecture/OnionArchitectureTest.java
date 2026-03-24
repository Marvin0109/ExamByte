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

@AnalyzeClasses(
        packages = "exambyte",
        importOptions = ImportOption.DoNotIncludeTests.class)
class OnionArchitectureTest {

    @ArchTest
    ArchRule onionArchitecture = Architectures.onionArchitecture()
        .domainModels("exambyte.domain..")
        .domainServices("exambyte.domain.service..")
        .applicationServices("exambyte.application..")
        .adapter("infrastructure", "exambyte.infrastructure..")
        .adapter("web", "exambyte.web..");

    @ArchTest
    ArchRule noRandomUUIDUsage = noClasses()
            .should()
            .callMethod(UUID.class, "randomUUID");

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
    ArchRule allRepositoriesShouldBeAnnotatedWithRepository = classes()
        .that()
        .resideInAPackage("..infrastructure.repository..")
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
    ArchRule allMapperShouldBeAnnotatedWithComponent = classes()
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
        .resideOutsideOfPackage("..enums..")
        .should()
        .bePrivate();

    @ArchTest
    ArchRule domainShouldNotDependOnOuterLayers = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..application..", "..infrastructure..", "..web..");

    @ArchTest
    ArchRule noApplicationDTOsInInfrastructureAllowed = noClasses()
            .that()
            .resideInAPackage("..infrastructure..")
            .and()
            .resideOutsideOfPackage("..infrastructure.mapper..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..application.dto..");

    @ArchTest
    ArchRule noEntitiesAllowedOutsideOfInfrastructure = noClasses()
            .that()
            .resideOutsideOfPackage("..infrastructure..")
            .and()
            .resideOutsideOfPackage("..domain.entitymapper..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..infrastructure.persistence.entities..");
}
