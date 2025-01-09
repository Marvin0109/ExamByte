package exambyte.persistence.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Die Klasse DataSource ist dafür da, damit Spring eine Verbindung zur Datenbank herstellen kann.
 *
 * Wichtig ist, dass die Login-Daten für die Datenbank mit der in docker-compose.yml übereinstimmen müssen.
 */
@Configuration
public class DataConfig {

    @Bean
    public DataSource configureDB() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://exambyte_postgres:5432/exambyte_db")
                .username("exambyte_user")
                .password("exambyte_password")
                .build();
    }
}
