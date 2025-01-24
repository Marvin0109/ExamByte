package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;
import org.springframework.stereotype.Service;

@Service
public class AntwortMapper {

    public Antwort toDomain(AntwortEntity antwortEntity) {
        return Antwort.of(antwortEntity.getId(), antwortEntity.getAntwortText(), antwortEntity.getIstKorrekt(),
                antwortEntity.getFrage(), antwortEntity.getStudent());
    }

    public AntwortEntity toEntity(Antwort antwort) {
        return new AntwortEntity(antwort.getId(), antwort.getAntwortText(), antwort.getIstKorrekt(),
                antwort.getFrage(), antwort.getStudent());
    }
}
