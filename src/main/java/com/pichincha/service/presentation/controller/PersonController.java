package com.pichincha.service.presentation.controller;

import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/savePerson")
    public void savePerson(@RequestBody PersonPresenter personPresenter) {
        personService.savePerson(personPresenter);
    }

    @PutMapping("/updatePerson")
    public void updatePerson(@RequestBody PersonPresenter personPresenter) {
        personService.updatePerson(personPresenter);
    }

    @GetMapping("/getAllPersons")
    public List<PersonPresenter> getAllPerson() {
        return personService.getAllPersons();
    }

    @DeleteMapping("/deletePerson")
    public void deletePerson(@RequestParam UUID personId) {
        personService.deletePerson(personId);
    }

}
