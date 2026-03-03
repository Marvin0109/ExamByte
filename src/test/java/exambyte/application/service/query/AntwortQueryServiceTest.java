package exambyte.application.service.query;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.service.AntwortService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AntwortQueryServiceTest {

    private AntwortQueryService antwortQueryService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private AntwortService antwortService;

    @Mock
    private AntwortDTOMapper antwortDTOMapper;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID FRAGE1_ID = UUID.randomUUID();
    private static final UUID FRAGE2_ID = UUID.randomUUID();
    private static final LocalDateTime TIME =  LocalDateTime.of(2000, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        antwortQueryService = new AntwortQueryServiceImpl(frageQueryService, antwortService, antwortDTOMapper);
    }

    @Test
    void saveAnswers_success() {
        // Arrange

        Map<String, List<String>> antworten = Map.of(
                FRAGE1_ID.toString(), List.of("Antwort 1", "Antwort 2"),
                FRAGE2_ID.toString(), List.of("Antwort A")
        );

        AntwortDTO dto1 = new AntwortDTO(null, "Antwort 1\nAntwort 2", FRAGE1_ID, STUDENT_ID, TIME);
        AntwortDTO dto2 = new AntwortDTO(null, "Antwort A", FRAGE2_ID, STUDENT_ID, TIME);

        when(antwortService.findByStudentAndFrage(FRAGE1_ID, STUDENT_ID)).thenReturn(null);
        when(antwortService.findByStudentAndFrage(FRAGE2_ID, STUDENT_ID)).thenReturn(null);
        when(antwortDTOMapper.toDomain(dto1)).thenReturn(mock());
        when(antwortDTOMapper.toDomain(dto2)).thenReturn(mock());

        // Act
        boolean result = antwortQueryService.saveAnswers(STUDENT_ID, antworten);

        // Assert
        assertTrue(result);
        verify(antwortService, times(2)).addAntwort(any());
    }

    @Test
    void saveAnswers_exception() {
        // Arrange
        Map<String, List<String>> antworten = Map.of(
                FRAGE1_ID.toString(), List.of("Antwort 1")
        );

        when(antwortDTOMapper.toDomain(any()))
                .thenThrow(new RuntimeException("Error Message"));

        // Act
        boolean result = antwortQueryService.saveAnswers(STUDENT_ID, antworten);

        // Assert
        assertFalse(result);
        verify(antwortService, never()).addAntwort(any());
    }

    @Test
    void getAntworten_success() {
        // Arrange
        Antwort domainAntwort = mock(Antwort.class);
        AntwortDTO dto = mock(AntwortDTO.class);

        when(antwortService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID))
                .thenReturn(domainAntwort);
        when(antwortService.findByStudentAndFrage(STUDENT_ID, FRAGE2_ID))
                .thenReturn(null);

        when(antwortDTOMapper.toDTO(domainAntwort)).thenReturn(dto);

        // Act
        List<AntwortDTO> result = antwortQueryService.getAntworten(
                STUDENT_ID,
                Set.of(FRAGE1_ID, FRAGE2_ID)
        );

        // Assert
        assertThat(result).hasSize(1).contains(dto);

        verify(antwortService, times(2))
                .findByStudentAndFrage(eq(STUDENT_ID), any());

        verify(antwortDTOMapper).toDTO(domainAntwort);
        verifyNoMoreInteractions(antwortDTOMapper);
    }

    @Test
    void getAntworten_empty() {
        // Arrange
        when(antwortService.findByStudentAndFrage(any(), any()))
                .thenReturn(null);

        // Act
        List<AntwortDTO> result = antwortQueryService.getAntworten(
                STUDENT_ID,
                Set.of(FRAGE1_ID)
        );

        // Assert
        assertThat(result).isEmpty();
        verify(antwortDTOMapper, never()).toDTO(any());
    }

    @Test
    void getFreitextAntwortenForExam_success() {
        // Arrange
        UUID examId = UUID.randomUUID();

        FrageDTO frageDTO = mock(FrageDTO.class);
        Antwort domainAntwort = mock(Antwort.class);
        AntwortDTO dto = mock(AntwortDTO.class);

        when(frageDTO.id()).thenReturn(FRAGE1_ID);
        when(frageQueryService.getFreitextFragen(examId))
                .thenReturn(List.of(frageDTO));

        when(antwortService.findByFrageId(FRAGE1_ID))
                .thenReturn(domainAntwort);

        when(antwortDTOMapper.toDTO(domainAntwort))
                .thenReturn(dto);

        // Act
        List<AntwortDTO> result = antwortQueryService.getFreitextAntwortenForExam(examId);

        // Assert
        assertThat(result).containsExactly(dto);
    }

    @Test
    void getFreitextAntwortenForExam_nullFiltered() {
        // Arrange
        UUID examId = UUID.randomUUID();

        FrageDTO frageDTO = mock(FrageDTO.class);
        when(frageDTO.id()).thenReturn(FRAGE1_ID);

        when(frageQueryService.getFreitextFragen(examId))
                .thenReturn(List.of(frageDTO));

        when(antwortService.findByFrageId(FRAGE1_ID))
                .thenReturn(null);

        // Act
        List<AntwortDTO> result = antwortQueryService.getFreitextAntwortenForExam(examId);

        // Assert
        assertThat(result).isEmpty();
        verify(antwortDTOMapper, never()).toDTO(any());
    }

    @Test
    void findByStudentAndFrage_success() {
        // Arrange
        Antwort domainAntwort = mock(Antwort.class);
        AntwortDTO dto = mock(AntwortDTO.class);

        when(antwortService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID))
                .thenReturn(domainAntwort);
        when(antwortDTOMapper.toDTO(domainAntwort))
                .thenReturn(dto);

        // Act
        AntwortDTO result =
                antwortQueryService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);

        // Assert
        assertThat(result).isEqualTo(dto);

        verify(antwortService, times(2))
                .findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);
        verify(antwortDTOMapper)
                .toDTO(domainAntwort);
    }

    @Test
    void findByStudentAndFrage_notFound() {
        when(antwortService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID))
                .thenReturn(null);

        AntwortDTO result = antwortQueryService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);

        assertThat(result).isNull();

        verify(antwortService).findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);
    }
}
