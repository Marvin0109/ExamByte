package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JPA.ProfessorEntityJPA;
import exambyte.persistence.mapper.JPA.ProfessorMapperJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProfessorMapperJPATest {

    @Test
    @DisplayName("Umwandlung von der Entität zum Objekt (toDomain)")
    void testToDomain() {
        // Arrange
        ProfessorMapperJPA professorMapperJPA = new ProfessorMapperJPA();
        ProfessorEntityJPA professorEntityJPA = new ProfessorEntityJPA(1L, "Dr. Smith");

        // Act
        Professor professor = professorMapperJPA.toDomain(professorEntityJPA);

        // Assert
        assertNotNull(professor);
        assertEquals(1L, professor.getId());
        assertEquals("Dr. Smith", professor.getName());
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void testToEntity() {
        // Arrange
        ProfessorMapperJPA professorMapperJPA = new ProfessorMapperJPA();
        Professor professor = Professor.of(1L, "Superman");

        // Act
        ProfessorEntityJPA professorEntityJPA = professorMapperJPA.toEntity(professor);

        // Assert
        assertNotNull(professorEntityJPA);
        assertEquals(1L, professorEntityJPA.getId());
        assertEquals("Superman", professorEntityJPA.getName());
    }
}
