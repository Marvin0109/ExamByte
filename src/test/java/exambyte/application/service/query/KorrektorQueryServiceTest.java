package exambyte.application.service.query;

import exambyte.domain.mapper.KorrektorDTOMapper;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.service.KorrektorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class KorrektorQueryServiceTest {

    private KorrektorQueryService korrektorQueryService;

    @Mock
    private KorrektorService korrektorService;

    @Mock
    private KorrektorDTOMapper korrektorDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        korrektorQueryService = new KorrektorQueryServiceImpl(korrektorService, korrektorDTOMapper);
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerFound() {
        Korrektor korrektor = new Korrektor.KorrektorBuilder().name("Automatischer Korrektor").build();
        when(korrektorService.getKorrektorByName("Automatischer Korrektor")).thenReturn(Optional.of(korrektor));
        korrektorQueryService.saveAutomaticReviewer();
        verify(korrektorService, never()).saveKorrektor("Automatischer Korrektor");
    }

    @Test
    void saveAutomaticReviewer_automaticReviewerNotFound() {
        when(korrektorService.getKorrektorByName("Automatischer Korrektor")).thenReturn(Optional.empty());
        korrektorQueryService.saveAutomaticReviewer();
        verify(korrektorService).saveKorrektor("Automatischer Korrektor");
    }
}
