package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface SpringDataFrageRepository{

    Optional<FrageEntityJDBC> findById(Long id);

    FrageEntityJDBC save(FrageEntityJDBC frageEntityJDBC);
}
