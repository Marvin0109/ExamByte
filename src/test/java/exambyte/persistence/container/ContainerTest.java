package exambyte.persistence.container;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(TestcontainerConfiguration.class)
public class ContainerTest {

    @Autowired
    // Repository repository;

    @Test
    @DisplayName("Ein ... Aggregat kann gespeichert werden")
    void test_01() throws Exception {
        /**
         * Foo foo = new Foo("Bar");
         * repository.save(foo);
         * assertThat(repository.count()).isEqualTo(1);
         */
    }

    // ...
}
