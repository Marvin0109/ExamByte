package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.ProfessorEntityJPA;
import exambyte.persistence.mapper.JPA.FrageMapperJPA;
import exambyte.persistence.mapper.JPA.ProfessorMapperJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class FrageMapperJPATest {

    @Test
    @DisplayName("Umwandlung von der Entität zum Objekt (toDomain)")
    void test_01() {
        // Arrange
        ProfessorMapperJPA mockProfessorMapperJPA = mock(ProfessorMapperJPA.class);
        FrageMapperJPA frageMapperJPA = new FrageMapperJPA(mockProfessorMapperJPA);

        ProfessorEntityJPA professorEntityJPA = new ProfessorEntityJPA(1L, "Dr. Smith");
        FrageEntityJPA frageEntityJPA = new FrageEntityJPA(1L, "Was ist Java?", professorEntityJPA);

        Professor professor = Professor.of(1L, "Dr. Smith");
        when(mockProfessorMapperJPA.toDomain(professorEntityJPA)).thenReturn(professor);

        // Act
        Frage frage = frageMapperJPA.toDomain(frageEntityJPA);

        // Assert
        assertNotNull(frage);
        assertEquals(1L, frage.getId());
        assertEquals("Was ist Java?", frage.getFrageText());
        assertEquals(professor, frage.getProfessor());

        verify(mockProfessorMapperJPA).toDomain(professorEntityJPA);
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void test_02() {
        // Arrange
        ProfessorMapperJPA mockProfessorMapperJPA = mock(ProfessorMapperJPA.class);
        FrageMapperJPA frageMapperJPA = new FrageMapperJPA(mockProfessorMapperJPA);

        Professor professor = Professor.of(1L, "Dr. Smith");
        Frage frage = Frage.of(1L, "Was ist Java?", professor);

        ProfessorEntityJPA professorEntityJPA = new ProfessorEntityJPA(1L, "Dr. Smith");
        when(mockProfessorMapperJPA.toEntity(professor)).thenReturn(professorEntityJPA);

        // Act
        FrageEntityJPA frageEntityJPA = frageMapperJPA.toEntity(frage);

        // Assert
        assertNotNull(frageEntityJPA);
        assertEquals(1L, frageEntityJPA.getId());
        assertEquals("Was ist Java?", frage.getFrageText());
        assertEquals(professor, frage.getProfessor());

        verify(mockProfessorMapperJPA).toEntity(professor);
    }
}
