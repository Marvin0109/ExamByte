package exambyte.persistence.mapper;

import exambyte.domain.aggregate.test.Frage;
import exambyte.persistence.entities.FrageEntity;

public class FrageMapper {

    public static Frage toDomain(FrageEntity frageEntity) {
        return Frage.of(frageEntity.getId(), frageEntity.getFrageText(), frageEntity.getProfessor());
    }
}
