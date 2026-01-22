package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.infrastructure.persistence.mapper.KorrektorMapperImpl;
import exambyte.infrastructure.persistence.repository.KorrektorDAO;
import exambyte.infrastructure.persistence.repository.KorrektorRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql("/data-test.sql")
class KorrektorDBTest {

    @Autowired
    private KorrektorDAO korrektorDAO;

    private KorrektorRepository repository;

    private static final UUID KORREKTORUUID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @BeforeEach
    void setUp() {
        KorrektorMapper mapper = new KorrektorMapperImpl();
        repository = new KorrektorRepositoryImpl(korrektorDAO, mapper);
    }

    @Test
    @DisplayName("Ein kann geladen werden")
    void test1() {
        // Act
        Optional<Korrektor> geladen = repository.findByFachId(KORREKTORUUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
