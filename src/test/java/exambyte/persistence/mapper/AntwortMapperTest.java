package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.AntwortEntity;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.StudentEntity;
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

        FrageEntity mockFrageEntity = mock(FrageEntity.class);
        Frage mockFrage = mock(Frage.class);
        when(mockFrageMapper.toDomain(mockFrageEntity)).thenReturn(mockFrage);

        StudentEntity mockStudentEntity = mock(StudentEntity.class);
        Student mockStudent = mock(Student.class);
        when(mockStudentMapper.toDomain(mockStudentEntity)).thenReturn(mockStudent);

        AntwortEntity entity = new AntwortEntity(1L, "Antwort Text ist falsch", false, mockFrageEntity, mockStudentEntity);

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
        FrageEntity mockFrageEntity = mock(FrageEntity.class);
        when(mockFrageMapper.toEntity(mockFrage)).thenReturn(mockFrageEntity);

        Student mockStudent = mock(Student.class);
        StudentEntity mockStudentEntity = mock(StudentEntity.class);
        when(mockStudentMapper.toEntity(mockStudent)).thenReturn(mockStudentEntity);

        Antwort antwort = Antwort.of(1L, "Antwort Text ist falsch", false, mockFrage, mockStudent);

        // Act
        AntwortEntity entity = antwortMapper.toEntity(antwort);

        // Assert
        assertNotNull(entity);
        assertEquals("Antwort Text ist falsch", entity.getAntwortText());
        assertFalse(entity.getIstKorrekt());
    }
}
