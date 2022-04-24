package com.pichincha.service.repository;

import com.pichincha.service.entity.Account;
import com.pichincha.service.entity.Client;
import com.pichincha.service.enums.AccountType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(@NotBlank String accountNumber);

    Optional<Account> findByAccountTypeAndClient(AccountType accountType, Client client);
}
