package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;
import org.springframework.stereotype.Service;

@Service
public class FrageMapper {

    public Frage toDomain(FrageEntity frageEntity) {
        return Frage.of(frageEntity.getId(), frageEntity.getFrageText(), frageEntity.getProfessor());
    }

    public FrageEntity toEntity(Frage frage) {
        return new FrageEntity(frage.getId(), frage.getFrageText(), frage.getProfessor());
    }
}
