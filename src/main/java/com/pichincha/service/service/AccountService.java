package com.pichincha.service.service;

import com.pichincha.service.enums.AccountType;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    void saveAccount(AccountPresenter accountPresenter);

    AccountPresenter updateAccount(AccountPresenter accountPresenter);

    void deleteAccount(UUID accountId);

    List<AccountPresenter> getAllAccounts();

    List<AccountPresenter> searchAccount(String searchValue);

    List<AccountPresenter> getAccountsByClient(UUID clientId);

    AccountPresenter getAccountStatus(UUID clientId, AccountType accountType, Date initDate, Date endDate) throws JRException, IOException;
}
