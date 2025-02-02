package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.repository.FrageRepositoryImpl;
import exambyte.persistence.repository.SpringDataExamRepository;
import exambyte.persistence.repository.SpringDataFrageRepository;
import exambyte.persistence.repository.SpringDataProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FrageMapperTest {

    @Test
    @DisplayName("FrageMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        SpringDataFrageRepository mockFrageDataRepo = mock(SpringDataFrageRepository.class);
        SpringDataProfessorRepository mockProfessorDataRepo = mock(SpringDataProfessorRepository.class);
        SpringDataExamRepository mockExamDataRepo = mock(SpringDataExamRepository.class);
        FrageRepositoryImpl frageRepository = new FrageRepositoryImpl(
                mockProfessorDataRepo,
                mockFrageDataRepo,
                mockExamDataRepo);
        FrageMapper frageMapper = new FrageMapper(frageRepository);

        UUID profFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(null)
                .frageText("Fragetext")
                .maxPunkte(5)
                .professorUUID(profFachId)
                .examUUID(examFachId)
                .build();

        // Act
        FrageEntity entity = frageMapper.toEntity(frage);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getFrageText()).isEqualTo("Fragetext");
        assertThat(entity.getMaxPunkte()).isEqualTo(5);
        assertThat(entity.getProfessorFachId()).isEqualTo(profFachId);
        assertThat(entity.getExamFachId()).isEqualTo(examFachId);
    }

    @Test
    @DisplayName("FrageMapper test 'toDomain'")
    public void test_02() {
        // Arrange
        FrageRepositoryImpl mockFrageRepository = mock(FrageRepositoryImpl.class);

        UUID fachId = UUID.randomUUID();
        UUID professorFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        FrageEntity frageEntity = new FrageEntity.FrageEntityBuilder()
                .id(null)
                .fachId(null)
                .frageText("Fragetext")
                .maxPunkte(5)
                .professorFachId(professorFachId)
                .examFachId(examFachId)
                .build();

        ProfessorEntity mockProfessor = new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(professorFachId)
                .name("Dr. Scalper")
                .build();

        when(mockFrageRepository.findByProfFachId(professorFachId)).thenReturn(mockProfessor);

        FrageMapper frageMapper = new FrageMapper(mockFrageRepository);

        // Act
        Frage frage = frageMapper.toDomain(frageEntity);

        assertThat(frage).isNotNull();
        assertThat(frage.getFrageText()).isEqualTo("Fragetext");
        assertThat(frage.getMaxPunkte()).isEqualTo(5);
        assertThat(frage.getProfessorUUID()).isEqualTo(professorFachId);

        verify(mockFrageRepository, times(1)).findByProfFachId(professorFachId);
    }
}
