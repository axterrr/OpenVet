package ua.edu.ukma.objectanalysis.openvet.exception;

public class ForbiddenException extends BaseException {

    public ForbiddenException() {
        super(403, "Forbidden", "You have no permission for this action");
    }
}
