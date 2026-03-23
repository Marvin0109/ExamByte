package exambyte.infrastructure.service;

import exambyte.domain.repository.StudentRepository;
import exambyte.domain.service.StudentService;
import exambyte.application.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final StudentService studentService = new StudentServiceImpl(studentRepository);

    @Test
    void findById_notFound() {
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.getStudent(studentId));
        verify(studentRepository).findById(studentId);
    }
}
