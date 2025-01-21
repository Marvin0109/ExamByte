package exambyte.persistence.mapper;

import exambyte.domain.aggregate.test.Antwort;
import exambyte.persistence.entities.AntwortEntity;

public class AntwortMapper {

    public static Antwort toDomain(AntwortEntity antwortEntity) {
        return Antwort.of(antwortEntity.getId(), antwortEntity.getAntwortText(), antwortEntity.getIstKorrekt(),
                antwortEntity.getFrage(), antwortEntity.getStudent());
    }
}
