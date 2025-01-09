package exambyte.persistence.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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
