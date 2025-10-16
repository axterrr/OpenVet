package ua.edu.ukma.objectanalysis.openvet.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.security.AuthManager;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseValidator<ENTITY extends Identifiable<ID>, VIEW, ID> {

    @Autowired
    private Validator validator;

    @Autowired
    protected AuthManager authManager;

    public void validateForView(ENTITY entity) {
        validatePermissionForView(entity);
    }

    public void validateForCreate(VIEW view) {
        validatePermissionForCreate(view);

        Set<ConstraintViolation<VIEW>> violations = validator.validate(view);
        if (!violations.isEmpty()) {
            throwException(violations);
        }
    }

    public void validateForUpdate(VIEW view, ENTITY entity) {
        validatePermissionForUpdate(entity);

        Set<ConstraintViolation<VIEW>> violations = validator.validate(view).stream()
            .filter(violation -> violation.getInvalidValue() != null)
            .collect(Collectors.toSet());
        if (!violations.isEmpty()) {
            throwException(violations);
        }

        if (entity.getId() == null) {
            throw new ValidationException("Cannot update entity without id");
        }
    }

    public void validateForDelete(ENTITY entity) {
        validatePermissionForDelete(entity);
    }

    private void throwException(Set<ConstraintViolation<VIEW>> violations) {
        throw new ValidationException(violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
        );
    }

    public void validateForViewAll() {
        throw new ForbiddenException();
    }

    protected abstract void validatePermissionForView(ENTITY entity);

    protected abstract void validatePermissionForCreate(VIEW view);

    protected abstract void validatePermissionForUpdate(ENTITY entity);

    protected abstract void validatePermissionForDelete(ENTITY entity);
}

