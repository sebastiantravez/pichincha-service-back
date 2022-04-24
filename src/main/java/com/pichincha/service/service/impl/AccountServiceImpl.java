package com.pichincha.service.service.impl;

import com.pichincha.service.entity.Account;
import com.pichincha.service.entity.Client;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.repository.AccountRepository;
import com.pichincha.service.repository.ClientRepository;
import com.pichincha.service.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveAccount(AccountPresenter accountPresenter) {
        try {
            Client client = clientRepository.findById(accountPresenter
                            .getPersonPresenter().getClientPresenter().getClientId())
                    .orElseThrow(() -> new ValidationException("Cliente no existe"));
            Account account = modelMapper.map(accountPresenter, Account.class);
            account.setClient(client);
            account.setStatus(Boolean.TRUE);
            accountRepository.save(account);
        } catch (Exception e) {
            throw new ValidationException("Error: Ocurrió un problema al registrar la cuenta del cliente. intente mas tarde");
        }
    }

    @Override
    public AccountPresenter updateAccount(AccountPresenter accountPresenter) {
        Account account = accountRepository.findById(accountPresenter.getAccountId())
                .orElseThrow(() -> new ValidationException("Cuenta de cliente no existe"));
        account.setAccountNumber(accountPresenter.getAccountNumber());
        account.setStatus(accountPresenter.getStatus());
        account.setAccountType(accountPresenter.getAccountType());
        accountRepository.save(account);
        return accountPresenter;
    }

    @Override
    public void deleteAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ValidationException("Cuenta de cliente no existe"));
        account.setStatus(Boolean.FALSE);
        accountRepository.save(account);
    }

    @Override
    public List<AccountPresenter> getAllAccounts() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .filter(account -> account.getStatus())
                .map(this::buildAccountPresenter)
                .collect(Collectors.toList());
    }

    private AccountPresenter buildAccountPresenter(Account account) {
        PersonPresenter personPresenter = modelMapper.map(account.getClient().getPerson(), PersonPresenter.class);
        ClientPresenter clientPresenter = modelMapper.map(account.getClient(), ClientPresenter.class);
        personPresenter.setClientPresenter(clientPresenter);
        AccountPresenter accountPresenter = modelMapper.map(account, AccountPresenter.class);
        accountPresenter.setPersonPresenter(personPresenter);
        return accountPresenter;
    }


}
