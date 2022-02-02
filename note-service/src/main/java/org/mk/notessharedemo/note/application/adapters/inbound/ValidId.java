package org.mk.notessharedemo.note.application.adapters.inbound;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = IdValidator.class)
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ValidId {
    String message() default "ID must have UUID format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
