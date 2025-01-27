package exambyte.application;

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
            ProfessorEntityJPA entity = mapper.toEntity(professor);
            repository.save(entity);
            Optional<ProfessorEntityJPA> pEntity = repository.findById(0);
            pEntity.ifPresent(e -> System.out.println(mapper.toDomain(e)));
        };
    }
}
*/