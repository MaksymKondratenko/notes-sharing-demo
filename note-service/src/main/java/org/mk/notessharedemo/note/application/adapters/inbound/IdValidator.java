package org.mk.notessharedemo.note.application.adapters.inbound;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.UUID;

public class IdValidator implements ConstraintValidator<ValidId, String> {

    @Override
    public boolean isValid(String idToValidate, ConstraintValidatorContext context) {

        try {
            return checkIfHasUuidFormat(idToValidate);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private boolean checkIfHasUuidFormat(String idToValidate) {
        return Optional.ofNullable(idToValidate)
                .map(UUID::fromString)
                .isPresent();
    }
}