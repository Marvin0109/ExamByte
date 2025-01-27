package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.ProfessorEntityJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class FrageMapperTest {

    @Test
    @DisplayName("Umwandlung von der Entität zum Objekt (toDomain)")
    void test_01() {
        // Arrange
        ProfessorMapper mockProfessorMapper = mock(ProfessorMapper.class);
        FrageMapper frageMapper = new FrageMapper(mockProfessorMapper);

        ProfessorEntityJPA professorEntityJPA = new ProfessorEntityJPA(1L, "Dr. Smith");
        FrageEntityJPA frageEntityJPA = new FrageEntityJPA(1L, "Was ist Java?", professorEntityJPA);

        Professor professor = Professor.of(1L, "Dr. Smith");
        when(mockProfessorMapper.toDomain(professorEntityJPA)).thenReturn(professor);

        // Act
        Frage frage = frageMapper.toDomain(frageEntityJPA);

        // Assert
        assertNotNull(frage);
        assertEquals(1L, frage.getId());
        assertEquals("Was ist Java?", frage.getFrageText());
        assertEquals(professor, frage.getProfessor());

        verify(mockProfessorMapper).toDomain(professorEntityJPA);
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void test_02() {
        // Arrange
        ProfessorMapper mockProfessorMapper = mock(ProfessorMapper.class);
        FrageMapper frageMapper = new FrageMapper(mockProfessorMapper);

        Professor professor = Professor.of(1L, "Dr. Smith");
        Frage frage = Frage.of(1L, "Was ist Java?", professor);

        ProfessorEntityJPA professorEntityJPA = new ProfessorEntityJPA(1L, "Dr. Smith");
        when(mockProfessorMapper.toEntity(professor)).thenReturn(professorEntityJPA);

        // Act
        FrageEntityJPA frageEntityJPA = frageMapper.toEntity(frage);

        // Assert
        assertNotNull(frageEntityJPA);
        assertEquals(1L, frageEntityJPA.getId());
        assertEquals("Was ist Java?", frage.getFrageText());
        assertEquals(professor, frage.getProfessor());

        verify(mockProfessorMapper).toEntity(professor);
    }
}
