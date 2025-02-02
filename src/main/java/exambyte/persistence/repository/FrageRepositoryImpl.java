package exambyte.persistence.repository;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.entities.ExamEntity;
import exambyte.persistence.mapper.FrageMapper;
import exambyte.domain.repository.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

    private final FrageMapper frageMapper = new FrageMapper();
    private final SpringDataFrageRepository springDataFrageRepository;
    private final SpringDataProfessorRepository springDataProfessorRepository;
    private final SpringDataExamRepository springDataExamRepository;

    @Autowired
    public FrageRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository,
                               SpringDataFrageRepository springDataFrageRepository,
                               SpringDataExamRepository springDataExamRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
        this.springDataFrageRepository = springDataFrageRepository;
        this.springDataExamRepository = springDataExamRepository;
    }

    @Override
    public Optional<Frage> findByFachId(UUID fachId) {
        Optional<FrageEntity> entity = springDataFrageRepository.findByFachId(fachId);
        return entity.map(FrageMapper::toDomain);
    }

    @Override
    public void save(Frage frageEntity) {
        FrageEntity entity = frageMapper.toEntity(frageEntity);
        springDataFrageRepository.save(entity);
    }

    public ProfessorEntity findByProfFachId(UUID profFachId) {
        Optional<ProfessorEntity> existingProfessor = springDataProfessorRepository.findByFachId(profFachId);

        if (existingProfessor.isPresent()) {
            return existingProfessor.get();
        }

        ProfessorEntity newProfessor = new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(profFachId)
                .name("")
                .build();
        springDataProfessorRepository.save(newProfessor);
        return newProfessor;
    }

    public ExamEntity findByTestFachId(UUID testFachId) {
        Optional<ExamEntity> existingTest = springDataExamRepository.findByFachId(testFachId);

        if (existingTest.isPresent()) {
            return existingTest.get();
        }

        // Hier wird es zur Exception geworfen (s. ExamEntityBuilder.build())
        ExamEntity newTest = new ExamEntity.ExamEntityBuilder()
                .id(null)
                .fachId(testFachId)
                .title("")
                .build();
        springDataExamRepository.save(newTest);
        return newTest;
    }
}
