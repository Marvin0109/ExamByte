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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class KorrektorDBTest {

    @Autowired
    private KorrektorDAO korrektorRepository;
    private KorrektorRepository repository;

    @BeforeEach
    void setUp() {
        KorrektorMapper mapper = new KorrektorMapperImpl();
        repository = new KorrektorRepositoryImpl(korrektorRepository, mapper);
    }

    @Test
    @DisplayName("Ein Korrektor kann gespeichert und wieder geladen werden")
    void test1() {
        // Arrange
        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(null)
                .name("Korrektor 1")
                .build();

        // Act
        repository.save(korrektor);
        Optional<Korrektor> geladen = repository.findByFachId(korrektor.uuid());

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getName()).isEqualTo("Korrektor 1");
        assertThat(geladen.get().uuid()).isEqualTo(korrektor.uuid());
    }
}
