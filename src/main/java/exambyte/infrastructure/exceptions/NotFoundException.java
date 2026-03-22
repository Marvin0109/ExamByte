package exambyte.infrastructure.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("SEVERE: Object not found (check package.infrastructure.service)");
    }
    public NotFoundException(String message) {
        super(message);
    }
}
