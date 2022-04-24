package com.pichincha.service.constraint;

import com.pichincha.service.entity.Person;
import com.pichincha.service.enums.IdentificationPattern;
import com.pichincha.service.util.IdentityValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClientDniValidator implements ConstraintValidator<ClientValidDni, Person> {

    @Override
    public boolean isValid(Person value, ConstraintValidatorContext context) {
        if (value.getIdentificationPattern().equals(IdentificationPattern.CEDULA)) {
            return IdentityValidation.dniIdentificationCardValidation(value.getDni());
        } else if (value.getIdentificationPattern().equals(IdentificationPattern.RUC)) {
            return IdentityValidation.rucCardValidation(value.getDni());
        }
        return true;
    }
}
