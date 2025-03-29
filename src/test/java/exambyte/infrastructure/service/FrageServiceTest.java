package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.repository.FrageRepository;
import exambyte.domain.service.FrageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FrageServiceTest {

    private final FrageRepository frageRepository = mock(FrageRepository.class);
    private final FrageService frageService = new FrageServiceImpl(frageRepository);

    @Test
    @DisplayName("Fragen werden geladen mit einer bestimmten Exam FachID")
    void test_01() {
        // Arrange
        UUID examFachId = UUID.randomUUID();
        Exam exam = new Exam.ExamBuilder().fachId(examFachId).build();
        Frage frage1 = new Frage.FrageBuilder().examUUID(examFachId).build();
        Frage frage2 = new Frage.FrageBuilder().examUUID(examFachId).build();
        List<Frage> fragen = List.of(frage1, frage2);

        when(frageRepository.findByExamFachId(examFachId)).thenReturn(fragen);

        // Act
        List<Frage> result = frageService.getFragenForExam(examFachId);

        // Assert
        assertThat(result).hasSize(2);
        verify(frageRepository).findByExamFachId(examFachId);
    }

    @Test
    @DisplayName("Keine Fragen gefunden für ein Exam")
    void test_02() {
        UUID examFachId = UUID.randomUUID();
        when(frageRepository.findByExamFachId(examFachId)).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> frageService.getFragenForExam(examFachId));
        verify(frageRepository).findByExamFachId(examFachId);
    }

    @Test
    @DisplayName("Eine Frage kann erfolgreich hinzugefügt werden")
    void test_03() {
        // Arrange
        Frage frage1 = new Frage.FrageBuilder().build();

        // Act
        frageService.addFrage(frage1);

        // Assert
        verify(frageRepository).save(frage1);
    }
}
