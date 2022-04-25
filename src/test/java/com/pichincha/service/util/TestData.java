package com.pichincha.service.util;

import com.pichincha.service.entity.Client;
import com.pichincha.service.entity.Person;
import com.pichincha.service.enums.GenderPerson;
import com.pichincha.service.enums.IdentificationPattern;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;

import java.util.UUID;

public class TestData {

    public Person person() {
        return Person.builder()
                .personId(UUID.randomUUID())
                .fullName("TEST TEST")
                .genderPerson(GenderPerson.MASCULINO)
                .age(20)
                .dni("1724022437")
                .identificationPattern(IdentificationPattern.CEDULA)
                .client(Client.builder()
                        .clientId(UUID.randomUUID())
                        .password("4324324")
                        .status(Boolean.TRUE)
                        .build())
                .build();
    }

    public PersonPresenter personPresenter(Person person) {
        return PersonPresenter.builder()
                .personId(person.getPersonId())
                .fullName(person.getFullName())
                .genderPerson(person.getGenderPerson())
                .age(person.getAge())
                .dni(person.getDni())
                .identificationPattern(person.getIdentificationPattern())
                .clientPresenter(ClientPresenter.builder()
                        .clientId(person.getClient().getClientId())
                        .clientId(UUID.randomUUID())
                        .password(person.getClient().getPassword())
                        .status(person.getClient().getStatus())
                        .build())
                .build();
    }
}
