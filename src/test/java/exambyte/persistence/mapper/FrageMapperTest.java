package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
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

        ProfessorEntity professorEntity = new ProfessorEntity(1L, "Dr. Smith");
        FrageEntity frageEntity = new FrageEntity(1L, "Was ist Java?", professorEntity);

        Professor professor = Professor.of(1L, "Dr. Smith");
        when(mockProfessorMapper.toDomain(professorEntity)).thenReturn(professor);

        // Act
        Frage frage = frageMapper.toDomain(frageEntity);

        // Assert
        assertNotNull(frage);
        assertEquals(1L, frage.getId());
        assertEquals("Was ist Java?", frage.getFrageText());
        assertEquals(professor, frage.getProfessor());

        verify(mockProfessorMapper).toDomain(professorEntity);
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void test_02() {
        // Arrange
        ProfessorMapper mockProfessorMapper = mock(ProfessorMapper.class);
        FrageMapper frageMapper = new FrageMapper(mockProfessorMapper);

        Professor professor = Professor.of(1L, "Dr. Smith");
        Frage frage = Frage.of(1L, "Was ist Java?", professor);

        ProfessorEntity professorEntity = new ProfessorEntity(1L, "Dr. Smith");
        when(mockProfessorMapper.toEntity(professor)).thenReturn(professorEntity);

        // Act
        FrageEntity frageEntity = frageMapper.toEntity(frage);

        // Assert
        assertNotNull(frageEntity);
        assertEquals(1L, frageEntity.getId());
        assertEquals("Was ist Java?", frage.getFrageText());
        assertEquals(professor, frage.getProfessor());

        verify(mockProfessorMapper).toEntity(professor);
    }
}
