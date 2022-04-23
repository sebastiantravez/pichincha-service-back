package com.pichincha.service.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClientDniValidator.class)
@Documented
public @interface ClientValidDni {

    String message() default "El número de documento no es válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
