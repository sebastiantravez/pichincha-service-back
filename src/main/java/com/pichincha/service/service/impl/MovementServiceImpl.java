package com.pichincha.service.service.impl;

import com.pichincha.service.entity.Account;
import com.pichincha.service.entity.Movements;
import com.pichincha.service.enums.MovementType;
import com.pichincha.service.enums.TransactionType;
import com.pichincha.service.exception.ValidationException;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.repository.AccountRepository;
import com.pichincha.service.repository.MovementsRepository;
import com.pichincha.service.service.MovementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovementServiceImpl implements MovementService {

    @Autowired
    private MovementsRepository movementsRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveMovement(MovementPresenter movementPresenter) {
        try {
            if (movementPresenter.getMovementAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Error: El valor del movimiento debe ser superior a 0");
            }

            Account account = accountRepository.findById(movementPresenter.getAccountPresenter().getAccountId())
                    .orElseThrow(() -> new ValidationException("Error: Cuenta de cliente no existe"));

            Movements movements = new Movements();

            if (account.getMovements().isEmpty()) {
                switch (movementPresenter.getMovementType()) {
                    case DEBITO:
                        if (movementPresenter.getMovementAmount().compareTo(account.getInitialAmount()) > 0) {
                            throw new ValidationException("Error: El valor del retiro debe ser menor o igual al saldo disponible del cliente");
                        }
                        BigDecimal amountAvailableDebit = account.getInitialAmount().subtract(movementPresenter.getMovementAmount())
                                .setScale(2, RoundingMode.HALF_UP);
                        if (amountAvailableDebit.compareTo(BigDecimal.ZERO) <= 0) {
                            throw new ValidationException("Error: Saldo no disponible");
                        }
                        movements.setMovementAmount(movementPresenter.getMovementAmount());
                        movements.setBalanceAvailable(amountAvailableDebit);
                        break;
                    case CREDITO:
                        BigDecimal amountAvailableCredit = account.getInitialAmount().add(movementPresenter.getMovementAmount())
                                .setScale(2, RoundingMode.HALF_UP);
                        movements.setMovementAmount(movementPresenter.getMovementAmount());
                        movements.setBalanceAvailable(amountAvailableCredit);
                        break;
                }
            } else {

                Movements movementsQuery = movementsRepository.findLastMove(movementPresenter.getAccountPresenter().getAccountId(),
                                TransactionType.APROBADA.name())
                        .orElseThrow(() -> new ValidationException("Error: Cuenta sin movimientos"));

                switch (movementPresenter.getMovementType()) {
                    case DEBITO:
                        if (movementsQuery.getBalanceAvailable().compareTo(BigDecimal.ZERO) <= 0) {
                            throw new ValidationException("Error: Saldo no disponible");
                        }
                        if (movementPresenter.getMovementAmount().compareTo(movementsQuery.getBalanceAvailable()) > 0) {
                            throw new ValidationException("Error: El valor del retiro debe ser menor o igual al saldo disponible del cliente");
                        }
                        BigDecimal amountAvailableDebit = movementsQuery.getBalanceAvailable().subtract(movementPresenter.getMovementAmount())
                                .setScale(2, RoundingMode.HALF_UP);
                        movements.setMovementAmount(movementPresenter.getMovementAmount());
                        movements.setBalanceAvailable(amountAvailableDebit);
                        break;
                    case CREDITO:
                        BigDecimal amountAvailableCredit = movementsQuery.getBalanceAvailable().add(movementPresenter.getMovementAmount())
                                .setScale(2, RoundingMode.HALF_UP);
                        movements.setMovementAmount(movementPresenter.getMovementAmount());
                        movements.setBalanceAvailable(amountAvailableCredit);
                        break;
                }
            }
            movements.setAccount(account);
            movements.setMovementType(movementPresenter.getMovementType());
            movements.setMovementDate(new Date());
            movements.setObservation(movementPresenter.getObservation());
            movements.setTransactionType(TransactionType.APROBADA);
            movementsRepository.save(movements);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public MovementPresenter updateMovement(MovementPresenter movementPresenter) {
        Movements movements = movementsRepository.findById(movementPresenter.getMovementId())
                .orElseThrow(() -> new ValidationException("Error: Movimiento no existe"));
        switch (movementPresenter.getMovementType()) {
            case DEBITO:
                BigDecimal amountAvailableDebit = movements.getBalanceAvailable().subtract(movementPresenter.getMovementAmount())
                        .setScale(2, RoundingMode.HALF_UP);
                movements.setMovementAmount(movementPresenter.getMovementAmount());
                movements.setBalanceAvailable(amountAvailableDebit);
                break;
            case CREDITO:
                BigDecimal amountAvailableCredit = movements.getBalanceAvailable().add(movementPresenter.getMovementAmount())
                        .setScale(2, RoundingMode.HALF_UP);
                movements.setMovementAmount(movementPresenter.getMovementAmount());
                movements.setBalanceAvailable(amountAvailableCredit);
                break;
        }
        movements.setMovementType(movementPresenter.getMovementType());
        movements.setMovementDate(new Date());
        movements.setObservation(movementPresenter.getObservation());
        movementsRepository.save(movements);
        return movementPresenter;
    }

    @Override
    public List<MovementPresenter> getAllMovements() {
        return movementsRepository.finAllMovements().stream()
                .map(this::buildMovementPresenter)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMovement(UUID movementId) {
        Movements movementCanceled = movementsRepository.findById(movementId)
                .orElseThrow(() -> new ValidationException("No existe movimiento"));

        Movements movementsApproved = movementsRepository.findLastMove(movementCanceled.getAccount().getAccountId(), TransactionType.APROBADA.name())
                .orElseThrow(() -> new ValidationException("Error: Cuenta sin movimientos"));

        Movements newMovement = new Movements();

        switch (movementCanceled.getMovementType()) {
            case DEBITO:
                BigDecimal amountCredit = movementsApproved.getBalanceAvailable().add(movementCanceled.getMovementAmount())
                        .setScale(2, RoundingMode.HALF_UP);
                newMovement.setMovementAmount(movementCanceled.getMovementAmount());
                newMovement.setBalanceAvailable(amountCredit);
                newMovement.setTransactionType(TransactionType.APROBADA);
                newMovement.setMovementType(MovementType.CREDITO);
                newMovement.setObservation("SE ACREDITA VALOR POR CANCELACION DE TRANSACCION");
                newMovement.setAccount(movementsApproved.getAccount());
                newMovement.setMovementDate(new Date());
                break;
            case CREDITO:
                BigDecimal amountDebit = movementsApproved.getBalanceAvailable().subtract(movementCanceled.getMovementAmount())
                        .setScale(2, RoundingMode.HALF_UP);
                newMovement.setMovementAmount(movementCanceled.getMovementAmount());
                newMovement.setBalanceAvailable(amountDebit);
                newMovement.setTransactionType(TransactionType.APROBADA);
                newMovement.setMovementType(MovementType.DEBITO);
                newMovement.setObservation("SE DEBITA VALOR POR CANCELACION DE TRANSACCION");
                newMovement.setMovementDate(new Date());
                newMovement.setAccount(movementsApproved.getAccount());
                break;
        }

        movementsRepository.save(newMovement);

        movementCanceled.setTransactionType(TransactionType.CANCELADA);
        movementCanceled.setMovementDate(new Date());
        movementsRepository.save(movementCanceled);
    }

    public MovementPresenter buildMovementPresenter(Movements movements) {
        MovementPresenter movementPresenter = modelMapper.map(movements, MovementPresenter.class);
        AccountPresenter accountPresenter = modelMapper.map(movements.getAccount(), AccountPresenter.class);
        ClientPresenter clientPresenter = modelMapper.map(movements.getAccount().getClient(), ClientPresenter.class);
        PersonPresenter personPresenter = modelMapper.map(movements.getAccount().getClient().getPerson(), PersonPresenter.class);
        personPresenter.setClientPresenter(clientPresenter);
        accountPresenter.setPersonPresenter(personPresenter);
        movementPresenter.setAccountPresenter(accountPresenter);
        return movementPresenter;
    }
}
