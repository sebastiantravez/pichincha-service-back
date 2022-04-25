package com.pichincha.service.presentation.controller;

import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.service.AccountService;
import com.pichincha.service.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountService accountService;
    private TestData testData;

    @BeforeEach
    public void setUp() {
        testData = new TestData();
    }

    @Test
    public void shouldSavePerson() {
        doNothing().when(accountService).saveAccount(any());
        accountController.savePerson(testData.accountPresenter(testData.account()));
    }

    @Test
    public void shouldUpdatePerson() {
        when(accountService.updateAccount(any())).thenReturn(new AccountPresenter());
        accountController.updateAccount(testData.accountPresenter(testData.account()));
    }

    @Test
    public void shouldGetAllAccounts() {
        when(accountService.getAllAccounts()).thenReturn(List.of(testData.accountPresenter(testData.account())));
        List<AccountPresenter> accountPresenters = accountController.getAllAccounts();
        Assertions.assertThat(accountPresenters).isNotEmpty();
    }

    @Test
    public void shouldDeleteAccount() {
        doNothing().when(accountService).deleteAccount(any());
        accountController.deleteAccount(UUID.randomUUID());
    }

}