package com.pichincha.service.service;

import com.pichincha.service.presentation.presenter.AccountPresenter;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    void saveAccount(AccountPresenter accountPresenter);

    AccountPresenter updateAccount(AccountPresenter accountPresenter);

    void deleteAccount(UUID accountId);

    List<AccountPresenter> getAllAccounts();
}
