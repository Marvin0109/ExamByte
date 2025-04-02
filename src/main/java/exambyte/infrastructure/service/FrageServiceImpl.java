package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.repository.FrageRepository;
import exambyte.domain.service.FrageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FrageServiceImpl implements FrageService {

    private final FrageRepository frageRepository;

    public FrageServiceImpl(FrageRepository frageRepository) {
        this.frageRepository = frageRepository;
    }

    @Override
    public List<Frage> getFragenForExam(UUID examId) {
        List<Frage> frageList = frageRepository.findByExamFachId(examId);
        if (frageList.isEmpty()) {
            throw new RuntimeException("Keine Fragen für das angegebene ExamId gefunden.");
        }
        return frageList;
    }

    @Override
    public UUID addFrage(Frage frage) {
        frageRepository.save(frage);
        return frage.getFachId();
    }
}
