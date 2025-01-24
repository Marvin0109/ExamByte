package exambyte.persistence.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestcontainerConfiguration.class);

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        LOGGER.warn("Verwende Testcontainer");
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"));
    }
}