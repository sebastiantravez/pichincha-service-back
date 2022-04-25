package com.pichincha.service.presentation.controller;

import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.service.PersonService;
import com.pichincha.service.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @InjectMocks
    private PersonController personController;
    @Mock
    private PersonService personService;

    private TestData testData;

    @BeforeEach
    public void setUp() {
        testData = new TestData();
    }

    @Test
    public void shouldSavePerson() {
        doNothing().when(personService).savePerson(any());
        personController.savePerson(testData.personPresenter(testData.person()));
        verify(personService).savePerson(any());
    }

    @Test
    public void shouldGetAllPerson() {
        List<PersonPresenter> personPresenters = List.of(testData.personPresenter(testData.person()));
        when(personService.getAllPersons()).thenReturn(personPresenters);
        List<PersonPresenter> personPresenterList = personController.getAllPerson();
        Assertions.assertThat(personPresenterList).isNotEmpty();
        verify(personService).getAllPersons();
    }

    @Test
    public void shouldDeletePerson() {
        doNothing().when(personService).deletePerson(any());
        personController.deletePerson(UUID.randomUUID());
        verify(personService).deletePerson(any());
    }
}