package com.pichincha.service.service.impl;

import com.pichincha.service.entity.Client;
import com.pichincha.service.entity.Person;
import com.pichincha.service.exception.ValidationException;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.repository.PersonRepository;
import com.pichincha.service.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void savePerson(PersonPresenter personPresenter) {
        try {
            Optional<Person> personQuery = personRepository.findByDni(personPresenter.getDni());
            if (personQuery.isPresent()) {
                throw new ValidationException("Cliente ya existe");
            }
            Person person = new Person();
            person.setFullName(personPresenter.getFullName());
            person.setGenderPerson(personPresenter.getGenderPerson());
            person.setAge(personPresenter.getAge());
            person.setDni(personPresenter.getDni());
            person.setIdentificationPattern(personPresenter.getIdentificationPattern());
            person.setAddress(personPresenter.getAddress());
            person.setPhone(personPresenter.getPhone());
            Client client = Client.builder()
                    .person(person)
                    .password(personPresenter.getClientPresenter().getPassword())
                    .status(personPresenter.getClientPresenter().getStatus())
                    .build();
            person.setCreateDate(new Date());
            person.setClient(client);
            personRepository.save(person);
        } catch (Exception e) {
            throw new ValidationException("Error: Ocurrió un problema al registrar el cliente, intente mas tarde");
        }
    }

    @Override
    public ClientPresenter updatePerson(PersonPresenter personPresenter) {
        try {
            Person person = personRepository.findByDni(personPresenter.getDni())
                    .orElseThrow(() -> new ValidationException("Cliente no existe, no se puede actualizar datos"));
            person.setFullName(personPresenter.getFullName());
            person.setGenderPerson(personPresenter.getGenderPerson());
            person.setAge(personPresenter.getAge());
            person.setDni(personPresenter.getDni());
            person.setIdentificationPattern(personPresenter.getIdentificationPattern());
            person.setAddress(personPresenter.getAddress());
            person.setPhone(personPresenter.getPhone());
            person.getClient().setStatus(personPresenter.getClientPresenter().getStatus());
            person.getClient().setPassword(personPresenter.getClientPresenter().getPassword());
            personRepository.save(person);
            return personPresenter.getClientPresenter();
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public List<PersonPresenter> getAllPersons() {
        List<PersonPresenter> personPresenters = StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .filter(person -> person.getClient().getStatus())
                .map(this::buildClientePresenter)
                .collect(Collectors.toList());
        return personPresenters;
    }

    @Override
    public void deletePerson(UUID personId) {
        Person person = personRepository.findById(personId).orElseThrow(() ->
                new ValidationException("Cliente no existe, no se puede eliminar"));
        person.getClient().setStatus(Boolean.FALSE);
        personRepository.save(person);
    }

    private PersonPresenter buildClientePresenter(Person person) {
        PersonPresenter personPresenter = modelMapper.map(person, PersonPresenter.class);
        ClientPresenter clientPresenter = modelMapper.map(person.getClient(), ClientPresenter.class);
        personPresenter.setClientPresenter(clientPresenter);
        return personPresenter;
    }
}
