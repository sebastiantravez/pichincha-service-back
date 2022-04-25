package com.pichincha.service.presentation.controller;

import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.service.MovementService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovementControllerTest {

    @InjectMocks
    private MovementController movementController;
    @Mock
    private MovementService movementService;
    private TestData testData;

    @BeforeEach
    public void setUp() {
        testData = new TestData();
    }

    @Test
    public void shouldSaveMovement() {
        doNothing().when(movementService).saveMovement(any());
        movementController.saveMovement(testData.movementPresenter(testData.movements()));
    }

    @Test
    public void shouldUpdateMovement() {
        when(movementService.updateMovement(any())).thenReturn(new MovementPresenter());
        movementController.updateMovement(testData.movementPresenter(testData.movements()));
    }

    @Test
    public void shouldGetAllMovements() {
        when(movementService.getAllMovements()).thenReturn(List.of(testData.movementPresenter(testData.movements())));
        List<MovementPresenter> movementPresenters = movementController.getAllMovements();
        Assertions.assertThat(movementPresenters).isNotEmpty();
    }

    @Test
    public void shouldDeleteMovement() {
        doNothing().when(movementService).deleteMovement(any());
        movementController.deleteMovement(UUID.randomUUID());
    }

}