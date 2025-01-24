package exambyte.application;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.mapper.ProfessorMapper;
import exambyte.persistence.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;
/**
@Component
public class ExamByteApplicationRunner {

    private final ProfessorRepository repository;
    private final ProfessorMapper mapper;

    public ExamByteApplicationRunner(ProfessorRepository repository, ProfessorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Bean
    CommandLineRunner main() {
        return args -> {
            Professor professor = Professor.of(null, "Supermen");
            ProfessorEntity entity = mapper.toEntity(professor);
            repository.save(entity);
            Optional<ProfessorEntity> pEntity = repository.findById(0);
            pEntity.ifPresent(e -> System.out.println(mapper.toDomain(e)));
        };
    }
}
*/