package ua.edu.ukma.objectanalysis.openvet.exception;

import java.util.List;

public class ValidationException extends BaseException {

    public ValidationException(List<String> messages) {
        super(400, "Validation error", messages);
    }

    public ValidationException(String message) {
        super(400, "Validation error", message);
    }
}
