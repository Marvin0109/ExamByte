package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;

public class FrageMapper {

    public static Frage toDomain(FrageEntity frageEntity) {
        return Frage.of(frageEntity.getId(), frageEntity.getFrageText(), frageEntity.getProfessor());
    }

    public static FrageEntity toEntity(Frage frage) {
        return new FrageEntity(frage.getId(), frage.getFrageText(), frage.getProfessor());
    }
}
