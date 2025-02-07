package exambyte.infrastructure.service;

/**
 *
 * Cannot invoke "exambyte.persistence.repository.AntwortRepositoryImpl.findByFrageFachId(java.util.UUID)" because "this.antwortRepository" is null
 *
import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.repository.AntwortRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AntwortServiceTest {

    @MockBean
    private AntwortRepositoryImpl antwortRepository;

    @MockBean
    AntwortServiceImpl antwortService;

    @Test
    @DisplayName("find_by_frage_fach_id_returns_answer_when_exists_with_mockbean")
    public void test_01() {
        UUID frageFachId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID fachId = UUID.randomUUID();

        Antwort expectedAntwort = new Antwort.AntwortBuilder()
                .id(1L)
                .fachId(fachId)
                .frageFachId(frageFachId)
                .studentFachId(studentId)
                .antwortText("Test answer")
                .antwortZeitpunkt(LocalDateTime.now())
                .lastChangesZeitpunkt(LocalDateTime.now())
                .build();

        when(antwortRepository.findByFrageFachId(frageFachId)).thenReturn(expectedAntwort);

        Antwort result = antwortService.findByFrageFachId(frageFachId);

        assertEquals(expectedAntwort, result);
    }

    @Test
    @DisplayName("find_by_frage_fach_id_returns_null_when_not_exists")
    public void test_02() {
        UUID nonExistentFrageFachId = UUID.randomUUID();

        when(antwortRepository.findByFrageFachId(nonExistentFrageFachId)).thenReturn(null);

        Antwort result = antwortService.findByFrageFachId(nonExistentFrageFachId);

        assertNull(result);
    }
}*/