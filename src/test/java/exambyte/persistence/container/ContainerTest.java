package exambyte.persistence.container;

import static org.assertj.core.api.Assertions.assertThat;
/**
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ProfessorMapper.class, ProfessorRepository.class, TestcontainerConfiguration.class})
public class ContainerTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    @Test
    @DisplayName("Ein Aggregat kann gespeichert")
    void test_01() throws Exception {
        // Arrange
        Professor professor = Professor.of(1L, "Professor");
        ProfessorEntityJPA professorEntityJDBC = professorMapper.toEntity(professor);

        // Act
        professorRepository.save(professorEntityJDBC);

        // Assert
        assertThat(professorRepository.count()).isEqualTo(1);
    }

    // ...
}
*/