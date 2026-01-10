package exambyte.web.form;

import java.util.UUID;

public record SubmitInfo(String name, UUID fachId, boolean reviewStatus) {}
