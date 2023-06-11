package wdefassio.io.webflux.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {TrimStringValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrimString {

    String message() default "field cannot start end finish with blank spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
