package com.pichincha.service.service.impl;

import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.repository.MovementsRepository;
import com.pichincha.service.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovementServiceImpl implements MovementService {

    @Autowired
    private MovementsRepository movementsRepository;

    @Override
    public void saveMovement(MovementPresenter movementPresenter) {
        
    }
}
