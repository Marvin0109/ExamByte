package exambyte.application.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("SEVERE: Object not found (check application.service)");
    }
    public NotFoundException(String message) {
        super(message);
    }
}
