package ua.edu.ukma.objectanalysis.openvet.exception;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(409, "Conflict", message);
    }
}
