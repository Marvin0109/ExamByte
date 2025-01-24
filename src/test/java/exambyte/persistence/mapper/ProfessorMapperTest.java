package exambyte.persistence.mapper;

import exambyte.application.ExamByteApplication;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ExamByteApplication.class)
public class ProfessorMapperTest {

    @Autowired
    private ProfessorMapper mapper;

    @Test
    @DisplayName("Umwandlung von Entität zum Objekt (toDomain)")
    void testToDomain() {
        // Arrange
        ProfessorEntity professorEntity = new ProfessorEntity(1, "Superman");

        // Act
        Professor professor = mapper.toDomain(professorEntity);

        // Assert
        assertNotNull(professor);
        assertEquals(1, professor.getId());
        assertEquals("Superman", professor.getName());
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void testToEntity() {
        // Arrange
        Professor professor = Professor.of(1, "Superman");

        // Act
        ProfessorEntity professorEntity = mapper.toEntity(professor);

        // Assert
        assertNotNull(professorEntity);
        assertEquals(1, professorEntity.getId());
        assertEquals("Superman", professorEntity.getName());
    }
}
