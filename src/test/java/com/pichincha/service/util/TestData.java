package com.pichincha.service.util;

import com.pichincha.service.entity.Person;
import com.pichincha.service.enums.GenderPerson;
import com.pichincha.service.enums.IdentificationPattern;
import com.pichincha.service.presentation.presenter.PersonPresenter;

public class TestData {

    public Person person() {
        return Person.builder()
                .fullName("TEST TEST")
                .genderPerson(GenderPerson.MASCULINO)
                .age(20)
                .dni("1724022437")
                .identificationPattern(IdentificationPattern.CEDULA)
                .build();
    }

    public PersonPresenter personPresenter(Person person) {
        return PersonPresenter.builder()
                .fullName(person.getFullName())
                .genderPerson(person.getGenderPerson())
                .age(person.getAge())
                .dni(person.getDni())
                .identificationPattern(person.getIdentificationPattern())
                .build();
    }
}
