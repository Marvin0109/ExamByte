package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JPA.AntwortEntityJPA;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.StudentEntityJPA;
import exambyte.persistence.mapper.JPA.AntwortMapperJPA;
import exambyte.persistence.mapper.JPA.FrageMapperJPA;
import exambyte.persistence.mapper.JPA.StudentMapperJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AntwortMapperJPATest {

    @Test
    @DisplayName("Umwandlung von der Entität zum Objekt (toDomain)")
    void test_01() {
        // Arrange
        StudentMapperJPA mockStudentMapperJPA = mock(StudentMapperJPA.class);
        FrageMapperJPA mockFrageMapperJPA = mock(FrageMapperJPA.class);
        AntwortMapperJPA antwortMapperJPA = new AntwortMapperJPA(mockStudentMapperJPA, mockFrageMapperJPA);

        FrageEntityJPA mockFrageEntityJPA = mock(FrageEntityJPA.class);
        Frage mockFrage = mock(Frage.class);
        when(mockFrageMapperJPA.toDomain(mockFrageEntityJPA)).thenReturn(mockFrage);

        StudentEntityJPA mockStudentEntityJPA = mock(StudentEntityJPA.class);
        Student mockStudent = mock(Student.class);
        when(mockStudentMapperJPA.toDomain(mockStudentEntityJPA)).thenReturn(mockStudent);

        AntwortEntityJPA entity = new AntwortEntityJPA(1L, "Antwort Text ist falsch", false, mockFrageEntityJPA, mockStudentEntityJPA);

        // Act
        Antwort antwort = antwortMapperJPA.toDomain(entity);

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
        StudentMapperJPA mockStudentMapperJPA = mock(StudentMapperJPA.class);
        FrageMapperJPA mockFrageMapperJPA = mock(FrageMapperJPA.class);
        AntwortMapperJPA antwortMapperJPA = new AntwortMapperJPA(mockStudentMapperJPA, mockFrageMapperJPA);

        Frage mockFrage = mock(Frage.class);
        FrageEntityJPA mockFrageEntityJPA = mock(FrageEntityJPA.class);
        when(mockFrageMapperJPA.toEntity(mockFrage)).thenReturn(mockFrageEntityJPA);

        Student mockStudent = mock(Student.class);
        StudentEntityJPA mockStudentEntityJPA = mock(StudentEntityJPA.class);
        when(mockStudentMapperJPA.toEntity(mockStudent)).thenReturn(mockStudentEntityJPA);

        Antwort antwort = Antwort.of(1L, "Antwort Text ist falsch", false, mockFrage, mockStudent);

        // Act
        AntwortEntityJPA entity = antwortMapperJPA.toEntity(antwort);

        // Assert
        assertNotNull(entity);
        assertEquals("Antwort Text ist falsch", entity.getAntwortText());
        assertFalse(entity.getIstKorrekt());
    }
}
