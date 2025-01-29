package exambyte.persistence.service;

import exambyte.persistence.JDBC.repository.ProfessorRepository;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Transactional
    public ProfessorEntityJDBC saveProfessor(ProfessorEntityJDBC professor) {
        return professorRepository.save(professor);
    }

    public Optional<ProfessorEntityJDBC> findProfessorById(Long id) {
        return professorRepository.findById(id);
    }
}
