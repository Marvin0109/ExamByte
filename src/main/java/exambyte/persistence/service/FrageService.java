package exambyte.persistence.service;

import exambyte.persistence.JDBC.repository.FrageRepository;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FrageService {

    private final FrageRepository frageRepository;

    public FrageService(FrageRepository frageRepository) {
        this.frageRepository = frageRepository;
    }

    @Transactional
    public FrageEntityJDBC saveFrage(FrageEntityJDBC frageEntityJDBC) {
        return frageRepository.save(frageEntityJDBC);
    }

    public Optional<FrageEntityJDBC> findById(Long id) {
        return frageRepository.findById(id);
    }
}
