package ua.edu.ukma.objectanalysis.openvet.validator.data;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseValidator<ENTITY, REQUEST> {

    @Autowired
    private Validator validator;

    public void validateForCreate(REQUEST request) {
        Set<ConstraintViolation<REQUEST>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throwException(violations);
        }
    }

    public void validateForUpdate(REQUEST request, ENTITY entity) {
        Set<ConstraintViolation<REQUEST>> violations = validator.validate(request).stream()
            .filter(violation -> violation.getInvalidValue() != null)
            .collect(Collectors.toSet());
        if (!violations.isEmpty()) {
            throwException(violations);
        }
    }

    public void validateForDelete(ENTITY entity) {
    }

    private void throwException(Set<ConstraintViolation<REQUEST>> violations) {
        throw new ValidationException(violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
        );
    }
}

