package com.pichincha.service.util;

import com.pichincha.service.entity.Account;
import com.pichincha.service.entity.Client;
import com.pichincha.service.entity.Movements;
import com.pichincha.service.entity.Person;
import com.pichincha.service.enums.*;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class TestData {

    public Person person() {
        return Person.builder()
                .personId(UUID.randomUUID())
                .fullName("TEST TEST")
                .genderPerson(GenderPerson.MASCULINO)
                .age(20)
                .dni("1724022437")
                .identificationPattern(IdentificationPattern.CEDULA)
                .client(Client.builder()
                        .clientId(UUID.randomUUID())
                        .password("4324324")
                        .status(Boolean.TRUE)
                        .person(Person.builder()
                                .personId(UUID.randomUUID())
                                .build())
                        .build())
                .build();
    }

    public PersonPresenter personPresenter(Person person) {
        return PersonPresenter.builder()
                .personId(person.getPersonId())
                .fullName(person.getFullName())
                .genderPerson(person.getGenderPerson())
                .age(person.getAge())
                .dni(person.getDni())
                .identificationPattern(person.getIdentificationPattern())
                .clientPresenter(ClientPresenter.builder()
                        .clientId(person.getClient().getClientId())
                        .clientId(UUID.randomUUID())
                        .password(person.getClient().getPassword())
                        .status(person.getClient().getStatus())
                        .build())
                .build();
    }

    public Account account() {
        return Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("48632487")
                .accountType(AccountType.AHORROS)
                .createDate(new Date())
                .initialAmount(BigDecimal.TEN)
                .client(person().getClient())
                .status(true)
                .build();
    }

    public AccountPresenter accountPresenter(Account account) {
        return AccountPresenter.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialAmount(account.getInitialAmount())
                .status(account.getStatus())
                .personPresenter(personPresenter(person()))
                .build();
    }

    public Movements movements() {
        return Movements.builder()
                .account(account())
                .movementId(UUID.randomUUID())
                .movementAmount(BigDecimal.valueOf(4))
                .movementType(MovementType.CREDITO)
                .movementDate(new Date())
                .balanceAvailable(BigDecimal.TEN)
                .transactionType(TransactionType.APROBADA)
                .build();
    }

    public MovementPresenter movementPresenter(Movements movements) {
        return MovementPresenter.builder()
                .accountPresenter(accountPresenter(account()))
                .movementId(movements.getMovementId())
                .movementAmount(movements.getMovementAmount())
                .movementType(movements.getMovementType())
                .movementDate(new Date())
                .balanceAvailable(movements.getBalanceAvailable())
                .transactionType(movements.getTransactionType())
                .build();
    }
}
