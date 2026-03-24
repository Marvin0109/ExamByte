package exambyte.domain.model.user;

import java.util.UUID;

public final class AutoReviewer {
    private static final String NAME = "Auto reviewer";
    private static final UUID AUTOMATIC_REVIEWER = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private AutoReviewer() {}

    public static String getName() {
        return NAME;
    }

    public static UUID getAutoReviewer() {
        return AUTOMATIC_REVIEWER;
    }
}
