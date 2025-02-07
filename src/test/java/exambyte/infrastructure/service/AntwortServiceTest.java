package exambyte.domain.service;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.domain.repository.AntwortRepository;
import exambyte.infrastructure.service.AntwortServiceImpl;
import exambyte.persistence.mapper.AntwortMapperImpl;
import exambyte.persistence.repository.AntwortRepositoryImpl;
import exambyte.persistence.repository.SpringDataAntwortRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Import(SpringDataAntwortRepository.class)
public class AntwortServiceTest {

    private SpringDataAntwortRepository antRepository;
    private AntwortRepository antwortRepository;

    @BeforeEach
    void setUp() {
        AntwortMapper antMapper = new AntwortMapperImpl();
        antwortRepository = new AntwortRepositoryImpl(antRepository, antMapper);
    }

    private AntwortServiceImpl antwortServiceImpl;

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

        Antwort result = antwortServiceImpl.findByFrageFachId(frageFachId);

        assertEquals(expectedAntwort, result);
    }

    @Test
    @DisplayName("find_by_frage_fach_id_returns_null_when_not_exists")
    public void test_02() {
        UUID nonExistentFrageFachId = UUID.randomUUID();

        when(antwortRepository.findByFrageFachId(nonExistentFrageFachId)).thenReturn(null);

        Antwort result = antwortServiceImpl.findByFrageFachId(nonExistentFrageFachId);

        assertNull(result);
    }
}