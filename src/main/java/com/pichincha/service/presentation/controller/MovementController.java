package com.pichincha.service.presentation.controller;

import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class MovementController {

    @Autowired
    private MovementService movementService;

    @PostMapping("/saveMovement")
    public void saveMovement(@RequestBody MovementPresenter movementPresenter) {
        movementService.saveMovement(movementPresenter);
    }

    @PutMapping("/updateMovement")
    public MovementPresenter updateMovement(@RequestBody MovementPresenter movementPresenter) {
        return movementService.updateMovement(movementPresenter);
    }

    @GetMapping("/getAllMovements")
    public List<MovementPresenter> getAllMovements() {
        return movementService.getAllMovements();
    }

    @DeleteMapping("/deleteMovement")
    public void deleteMovement(@RequestParam UUID movementId) {
        movementService.deleteMovement(movementId);
    }

    @GetMapping("/searchMovement/{value}")
    public List<MovementPresenter> searchMovement(@PathVariable("value") String value) {
        return movementService.searchMovement(value);
    }

}
