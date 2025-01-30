package exambyte.domain.aggregate.user;

import java.util.UUID;

public interface Person {
    UUID uuid();
    Long getId();
    String getName();
}
