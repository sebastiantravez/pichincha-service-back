package com.pichincha.service.service.impl;

import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.repository.AccountRepository;
import com.pichincha.service.repository.MovementsRepository;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovementServiceImplTest {

    @InjectMocks
    private MovementServiceImpl movementService;
    @Mock
    private MovementsRepository movementsRepository;
    @Mock
    private AccountRepository accountRepository;
    @Spy
    private ModelMapper modelMapper;

    private TestData testData;

    @BeforeEach
    public void setUp() {
        testData = new TestData();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void shouldSaveMovement() {
        MovementPresenter movementPresenter = testData.movementPresenter(testData.movements());
        when(accountRepository.findById(any())).thenReturn(Optional.of(testData.account()));
        when(movementsRepository.save(any())).thenReturn(testData.movements());
        movementService.saveMovement(movementPresenter);
        verify(accountRepository).findById(any());
    }

    @Test
    public void shouldUpdateMovement() {
        MovementPresenter movementPresenter = testData.movementPresenter(testData.movements());
        when(movementsRepository.findById(any())).thenReturn(Optional.of(testData.movements()));
        when(movementsRepository.save(any())).thenReturn(testData.movements());
        MovementPresenter movementPresenter1 = movementService.updateMovement(movementPresenter);
        Assertions.assertThat(movementPresenter1.getMovementAmount()).isNotZero();
        verify(movementsRepository).findById(any());
    }

    @Test
    public void shouldGetAllMovements() {
        when(movementsRepository.finAllMovements()).thenReturn(List.of(testData.movements()));
        List<MovementPresenter> movementPresenters = movementService.getAllMovements();
        Assertions.assertThat(movementPresenters).isNotEmpty();
        verify(movementsRepository).finAllMovements();
    }

    @Test
    public void shouldDeleteMovement() {
        when(movementsRepository.findById(any())).thenReturn(Optional.of(testData.movements()));
        when(movementsRepository.findLastMove(any(), any())).thenReturn(Optional.of(testData.movements()));
        movementService.deleteMovement(UUID.randomUUID());
        verify(movementsRepository, times(2)).save(any());
    }

    @Test
    public void shouldSearchMovement() {
        when(movementsRepository.findByNameIgnoreCase(anyString())).thenReturn(List.of(testData.movements()));
        List<MovementPresenter> movementPresenters = movementService.searchMovement("test");
        Assertions.assertThat(movementPresenters).isNotEmpty();
        verify(movementsRepository).findByNameIgnoreCase(anyString());
    }

}