package com.pichincha.service.service.impl;

import com.pichincha.service.exception.ValidationException;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.repository.PersonRepository;
import com.pichincha.service.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personService;
    @Mock
    private PersonRepository personRepository;
    @Spy
    private ModelMapper modelMapper = new ModelMapper();
    private TestData testData;


    @BeforeEach
    public void setUp() {
        testData = new TestData();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void shouldSavePerson() {
        PersonPresenter personPresenter = testData.personPresenter(testData.person());
        when(personRepository.findByDni(any())).thenReturn(Optional.empty());
        when(personRepository.save(any())).thenReturn(testData.person());
        personService.savePerson(personPresenter);
        verify(personRepository).save(any());
    }

    @Test
    public void shouldNotSaveWhenClientExist() {
        PersonPresenter personPresenter = testData.personPresenter(testData.person());
        when(personRepository.findByDni(any())).thenReturn(Optional.of(testData.person()));
        ValidationException exception = catchThrowableOfType(() ->
                personService.savePerson(
                        personPresenter
                ), ValidationException.class);
        Assertions.assertThat(exception).isNotNull();
        assertEquals("Cliente ya existe", exception.getMessage());
    }

    @Test
    public void shouldUpdatePerson() {
        PersonPresenter personPresenter = testData.personPresenter(testData.person());
        when(personRepository.findByDni(any())).thenReturn(Optional.of(testData.person()));
        when(personRepository.save(any())).thenReturn(testData.person());
        ClientPresenter clientPresenter = personService.updatePerson(personPresenter);
        Assertions.assertThat(clientPresenter.getStatus()).isTrue();
        verify(personRepository).findByDni(any());
    }

    @Test
    public void shouldNotFoundClient() {
        PersonPresenter personPresenter = testData.personPresenter(testData.person());
        when(personRepository.findByDni(any())).thenReturn(Optional.empty());
        ValidationException exception = catchThrowableOfType(() ->
                personService.updatePerson(
                        personPresenter
                ), ValidationException.class);
        Assertions.assertThat(exception).isNotNull();
        assertEquals("Cliente no existe, no se puede actualizar datos", exception.getMessage());
    }

    @Test
    public void shouldGetAllPersons() {
        when(personRepository.findAllPersons()).thenReturn(List.of(testData.person()));
        List<PersonPresenter> personPresenters = personService.getAllPersons();
        Assertions.assertThat(personPresenters).isNotEmpty();
        verify(personRepository).findAllPersons();
    }

    @Test
    public void shouldDeletePerson() {
        when(personRepository.findById(any())).thenReturn(Optional.of(testData.person()));
        when(personRepository.save(any())).thenReturn(testData.person());
        personService.deletePerson(UUID.randomUUID());
        verify(personRepository).findById(any());
    }

    @Test
    public void shouldNotFoundClientForDelete() {
        when(personRepository.findById(any())).thenReturn(Optional.empty());
        ValidationException exception = catchThrowableOfType(() ->
                personService.deletePerson(
                        UUID.randomUUID()
                ), ValidationException.class);
        Assertions.assertThat(exception).isNotNull();
        assertEquals("Cliente no existe, no se puede eliminar", exception.getMessage());
    }

    @Test
    public void shouldSearchPerson() {
        when(personRepository.findByNameIgnoreCase(anyString())).thenReturn(List.of(testData.person()));
        List<PersonPresenter> personPresenters = personService.searchPerson("sebstian");
        Assertions.assertThat(personPresenters).isNotEmpty();
        verify(personRepository).findByNameIgnoreCase(any());
    }
}