package exambyte.domain.aggregate.user;

import jakarta.persistence.Id;

public interface Person {
    Long getId();
    String getName();
}
