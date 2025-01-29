package exambyte.persistence.container;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainerConfiguration {

    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
            .withUsername("exambyte_user")
            .withPassword("exambyte_password")
            .withDatabaseName("exambyte_db");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @AfterAll
    static void stopContainer() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }
}