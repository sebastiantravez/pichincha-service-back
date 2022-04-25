package com.pichincha.service.service.impl;

import com.pichincha.service.entity.Account;
import com.pichincha.service.entity.Client;
import com.pichincha.service.enums.AccountType;
import com.pichincha.service.exception.ValidationException;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.presentation.presenter.ClientPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.repository.AccountRepository;
import com.pichincha.service.repository.ClientRepository;
import com.pichincha.service.service.AccountService;
import com.pichincha.service.service.JasperReportService;
import net.sf.jasperreports.engine.JRException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JasperReportService jasperReportService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveAccount(AccountPresenter accountPresenter) {
        Client client = clientRepository.findById(accountPresenter
                        .getPersonPresenter().getClientPresenter().getClientId())
                .orElseThrow(() -> new ValidationException("Cliente no existe, no se puede crear cuenta, debe crear el cliente"));

        Optional<Account> accountQuery = accountRepository.findByAccountTypeAndClient(accountPresenter.getAccountType(),
                client);

        if (accountQuery.isPresent()) {
            throw new ValidationException("Error: Cliente ya tiene un registro de cuentas con el banco");
        }

        Optional<Account> accountQuery2 = accountRepository.findByAccountNumber(accountPresenter.getAccountNumber());
        if (accountQuery2.isPresent()) {
            throw new ValidationException("Error: Numero de cuenta ya existe");
        }

        Account account = modelMapper.map(accountPresenter, Account.class);
        account.setClient(client);
        account.setStatus(accountPresenter.getStatus());
        account.setCreateDate(new Date());
        accountRepository.save(account);
    }

    @Override
    public AccountPresenter updateAccount(AccountPresenter accountPresenter) {
        try {
            Account account = accountRepository.findById(accountPresenter.getAccountId())
                    .orElseThrow(() -> new ValidationException("Cuenta de cliente no existe, no se puede actualizar"));
            BigDecimal initialAmount = account.getInitialAmount().setScale(2, RoundingMode.HALF_UP);
            BigDecimal modifyInitialAmount = accountPresenter.getInitialAmount().setScale(2, RoundingMode.HALF_UP);
            if (!initialAmount.equals(modifyInitialAmount) && account.getMovements().size() > 0) {
                throw new ValidationException("Error: No se puede modificar el saldo inicial, el cliente tiene movimientos");
            }
            account.setAccountNumber(accountPresenter.getAccountNumber());
            account.setStatus(accountPresenter.getStatus());
            account.setAccountType(accountPresenter.getAccountType());
            account.setCreateDate(new Date());
            account.setInitialAmount(accountPresenter.getInitialAmount());
            accountRepository.save(account);
            return accountPresenter;
        } catch (Exception e) {
            throw new ValidationException(e.getCause().getCause().getCause().getMessage());
        }
    }

    @Override
    public void deleteAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ValidationException("Cuenta de cliente no existe, no se puede eliminar"));
        account.setStatus(Boolean.FALSE);
        accountRepository.save(account);
    }

    @Override
    public List<AccountPresenter> getAllAccounts() {
        return accountRepository.findAllAccounts().stream()
                .filter(account -> account.getStatus())
                .map(this::buildAccountPresenter)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountPresenter> searchAccount(String searchValue) {
        searchValue = searchValue.replace(' ', '%');
        return accountRepository.findByNameIgnoreCase(searchValue).stream()
                .filter(account -> account.getStatus())
                .map(this::buildAccountPresenter)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountPresenter> getAccountsByClient(UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ValidationException("Error: Cliente no existe"));
        return accountRepository.findByClient(client)
                .stream().filter(account -> account.getStatus())
                .map(this::buildAccountPresenter)
                .collect(Collectors.toList());
    }

    @Override
    public AccountPresenter getAccountStatus(UUID clientId, AccountType accountType, Date initDate, Date endDate) throws JRException, IOException {
        Account account = accountRepository.findAccountsMovements(clientId, accountType, initDate, endDate)
                .orElseThrow(() -> new ValidationException("Cliente sin registros"));
        HashMap<String, Object> parameters = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String initDateFormat = dateFormat.format(initDate);
        String endDateFormat = dateFormat.format(endDate);
        parameters.put("fullName", account.getClient().getPerson().getFullName());
        parameters.put("dni", account.getClient().getPerson().getDni());
        parameters.put("accountType", account.getAccountType().name());
        parameters.put("accountNumber", account.getAccountNumber());
        parameters.put("initialAmount", account.getInitialAmount().setScale(2, RoundingMode.HALF_UP));
        parameters.put("initDate", initDateFormat);
        parameters.put("endDate", endDateFormat);
        parameters.put("status", account.getStatus() ? "ACTIVO" : "INACTIVO");
        final int[] count = {1};
        List<HashMap<Object, Object>> dataSource = account.getMovements().stream()
                .map(item -> {
                    HashMap<Object, Object> line = new HashMap<>();
                    line.put("count", count[0]++);
                    line.put("transactionType", item.getTransactionType().name());
                    line.put("movementDate", dateFormat.format(item.getMovementDate()));
                    line.put("movementType", item.getMovementType().name());
                    line.put("movementAmount", item.getMovementAmount().setScale(2, RoundingMode.HALF_UP));
                    line.put("balanceAvailable", item.getBalanceAvailable().setScale(2, RoundingMode.HALF_UP));
                    line.put("observation", item.getObservation());
                    return line;
                }).collect(Collectors.toList());
        byte[] pdf = jasperReportService.getReportWithCollectionDataSource("accountStatus", parameters, dataSource);
        return AccountPresenter.builder()
                .pdf(pdf)
                .build();
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
