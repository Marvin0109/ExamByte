package exambyte.infrastructure.service;

import exambyte.domain.repository.FrageRepository;
import exambyte.domain.service.FrageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FrageServiceTest {

    private final FrageRepository frageRepository = mock(FrageRepository.class);
    private final FrageService frageService = new FrageServiceImpl(frageRepository);

    @Test
    @DisplayName("Keine Fragen gefunden für ein Exam")
    void test_01() {
        UUID examFachId = UUID.randomUUID();
        when(frageRepository.findByExamFachId(examFachId)).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> frageService.getFragenForExam(examFachId));
        verify(frageRepository).findByExamFachId(examFachId);
    }
}
