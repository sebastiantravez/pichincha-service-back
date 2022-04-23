package com.pichincha.service.service;

import com.pichincha.service.presentation.presenter.PersonPresenter;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    void savePerson(PersonPresenter personPresenter);

    List<PersonPresenter> getAllPersons();

    void deletePerson(UUID personId);
}
