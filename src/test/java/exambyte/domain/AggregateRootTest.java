package exambyte.domain;
/**
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.axonframework.modelling.command.AggregateRoot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class AggregateRootTest {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("exambyte.domain.aggregate.exam");

    @Test
    @DisplayName("AggregatRoot Annotation Test")
    void testAggregateRoot() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Test")
                .should().beAnnotatedWith(AggregateRoot.class);

        rule.check(importedClasses);
    }
}
*/