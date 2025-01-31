package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.entities.JDBC.ExamEntityJDBC;
import exambyte.service.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

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
    public Optional<FrageEntityJDBC> findByFachId(UUID fachId) {
        return springDataFrageRepository.findByFachId(fachId);
    }

    @Override
    public void save(FrageEntityJDBC frageEntityJDBC) {
        springDataFrageRepository.save(frageEntityJDBC);
    }

    public ProfessorEntityJDBC findByProfFachId(UUID profFachId) {
        Optional<ProfessorEntityJDBC> existingProfessor = springDataProfessorRepository.findByFachId(profFachId);

        if (existingProfessor.isPresent()) {
            return existingProfessor.get();
        }

        ProfessorEntityJDBC newProfessor = new ProfessorEntityJDBC(null, profFachId, "N/A");
        springDataProfessorRepository.save(newProfessor);
        return newProfessor;
    }

    public ExamEntityJDBC findByTestFachId(UUID testFachId) {
        Optional<ExamEntityJDBC> existingTest = springDataExamRepository.findByFachId(testFachId);

        if (existingTest.isPresent()) {
            return existingTest.get();
        }

        // Erstmal so, ist auch an sich nicht korrekt
        ExamEntityJDBC newTest = new ExamEntityJDBC(null, testFachId, "", null);
        springDataExamRepository.save(newTest);
        return newTest;
    }
}
