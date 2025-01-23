package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;
import org.springframework.stereotype.Component;

@Component
public class AntwortMapper {

    public static Antwort toDomain(AntwortEntity antwortEntity) {
        return Antwort.of(antwortEntity.getId(), antwortEntity.getAntwortText(), antwortEntity.getIstKorrekt(),
                antwortEntity.getFrage(), antwortEntity.getStudent());
    }

    public static AntwortEntity toEntity(Antwort antwort) {
        return new AntwortEntity(antwort.getId(), antwort.getAntwortText(), antwort.getIstKorrekt(),
                antwort.getFrage(), antwort.getStudent());
    }
}
