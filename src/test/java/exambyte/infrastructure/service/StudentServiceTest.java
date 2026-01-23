package exambyte.infrastructure.service;

import exambyte.domain.repository.StudentRepository;
import exambyte.domain.service.StudentService;
import exambyte.infrastructure.exceptions.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final StudentService studentService = new StudentServiceImpl(studentRepository);

    @Test
    @DisplayName("Ein Student kann nicht gefunden werden mit einer FachID")
    void test_01() {
        UUID studentFachId = UUID.randomUUID();
        when(studentRepository.findByFachId(studentFachId)).thenReturn(Optional.empty());

        assertThrows(NichtVorhandenException.class, () -> studentService.getStudent(studentFachId));
        verify(studentRepository).findByFachId(studentFachId);
    }
}
