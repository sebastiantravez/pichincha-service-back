package com.pichincha.service.service;

import com.pichincha.service.presentation.presenter.MovementPresenter;

import java.util.List;
import java.util.UUID;

public interface MovementService {
    void saveMovement(MovementPresenter movementPresenter);

    MovementPresenter updateMovement(MovementPresenter movementPresenter);

    List<MovementPresenter> getAllMovements();

    void deleteMovement(UUID movementId);
}
