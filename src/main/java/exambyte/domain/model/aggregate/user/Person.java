package exambyte.domain.model.aggregate.user;

import java.util.UUID;

public interface Person {
    UUID id();
    String getName();
}
