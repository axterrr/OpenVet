package ua.edu.ukma.objectanalysis.openvet.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(404, "Resource not found", message);
    }
}
