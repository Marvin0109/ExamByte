package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JPA.AntwortEntityJPA;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.StudentEntityJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AntwortMapperTest {

    @Test
    @DisplayName("Umwandlung von der Entität zum Objekt (toDomain)")
    void test_01() {
        // Arrange
        StudentMapper mockStudentMapper = mock(StudentMapper.class);
        FrageMapper mockFrageMapper = mock(FrageMapper.class);
        AntwortMapper antwortMapper = new AntwortMapper(mockStudentMapper, mockFrageMapper);

        FrageEntityJPA mockFrageEntityJPA = mock(FrageEntityJPA.class);
        Frage mockFrage = mock(Frage.class);
        when(mockFrageMapper.toDomain(mockFrageEntityJPA)).thenReturn(mockFrage);

        StudentEntityJPA mockStudentEntityJPA = mock(StudentEntityJPA.class);
        Student mockStudent = mock(Student.class);
        when(mockStudentMapper.toDomain(mockStudentEntityJPA)).thenReturn(mockStudent);

        AntwortEntityJPA entity = new AntwortEntityJPA(1L, "Antwort Text ist falsch", false, mockFrageEntityJPA, mockStudentEntityJPA);

        // Act
        Antwort antwort = antwortMapper.toDomain(entity);

        // Assert
        assertNotNull(antwort);
        assertEquals(1L, antwort.getId());
        assertFalse(antwort.getIstKorrekt());
        assertEquals("Antwort Text ist falsch", antwort.getAntwortText());
        assertFalse(antwort.getIstKorrekt());
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void test_02() {
        // Arrange
        StudentMapper mockStudentMapper = mock(StudentMapper.class);
        FrageMapper mockFrageMapper = mock(FrageMapper.class);
        AntwortMapper antwortMapper = new AntwortMapper(mockStudentMapper, mockFrageMapper);

        Frage mockFrage = mock(Frage.class);
        FrageEntityJPA mockFrageEntityJPA = mock(FrageEntityJPA.class);
        when(mockFrageMapper.toEntity(mockFrage)).thenReturn(mockFrageEntityJPA);

        Student mockStudent = mock(Student.class);
        StudentEntityJPA mockStudentEntityJPA = mock(StudentEntityJPA.class);
        when(mockStudentMapper.toEntity(mockStudent)).thenReturn(mockStudentEntityJPA);

        Antwort antwort = Antwort.of(1L, "Antwort Text ist falsch", false, mockFrage, mockStudent);

        // Act
        AntwortEntityJPA entity = antwortMapper.toEntity(antwort);

        // Assert
        assertNotNull(entity);
        assertEquals("Antwort Text ist falsch", entity.getAntwortText());
        assertFalse(entity.getIstKorrekt());
    }
}
