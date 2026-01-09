package exambyte.web.controllers;

import java.util.UUID;

public record SubmitInfo(String name, UUID fachId, boolean reviewStatus) {}
