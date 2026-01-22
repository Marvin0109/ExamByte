package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.repository.FrageRepository;
import exambyte.domain.service.FrageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FrageServiceImpl implements FrageService {

    private final FrageRepository repository;

    public FrageServiceImpl(FrageRepository frageRepository) {
        this.repository = frageRepository;
    }

    @Override
    public List<Frage> getFragenForExam(UUID examId) {
        return repository.findByExamFachId(examId);
    }

    @Override
    public UUID addFrage(Frage frage) {
        repository.save(frage);
        return frage.getFachId();
    }

    @Override
    public Optional<Frage> getFrage(UUID frageId) {
        return repository.findByFachId(frageId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
