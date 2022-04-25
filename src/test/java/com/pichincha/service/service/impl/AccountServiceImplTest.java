package com.pichincha.service.service.impl;

import com.pichincha.service.enums.AccountType;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.repository.AccountRepository;
import com.pichincha.service.repository.ClientRepository;
import com.pichincha.service.service.JasperReportService;
import com.pichincha.service.util.TestData;
import net.sf.jasperreports.engine.JRException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private JasperReportService jasperReportService;
    @Spy
    private ModelMapper modelMapper;

    private TestData testData;

    @BeforeEach
    public void setUp() {
        testData = new TestData();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void shouldSaveAccount() {
        AccountPresenter accountPresenter = testData.accountPresenter(testData.account());
        when(clientRepository.findById(any())).thenReturn(Optional.of(testData.person().getClient()));
        when(accountRepository.findByAccountTypeAndClient(any(), any())).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenReturn(testData.account());
        accountService.saveAccount(accountPresenter);
        verify(clientRepository).findById(any());
    }

    @Test
    public void shouldUpdateAccount() {
        AccountPresenter accountPresenter = testData.accountPresenter(testData.account());
        when(accountRepository.findById(any())).thenReturn(Optional.of(testData.account()));
        when(accountRepository.save(any())).thenReturn(testData.account());
        AccountPresenter accountPresenter1 = accountService.updateAccount(accountPresenter);
        Assertions.assertThat(accountPresenter1.getAccountNumber()).isNotBlank();
        verify(accountRepository).findById(any());
    }

    @Test
    public void shouldDeleteAccount() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(testData.account()));
        accountService.deleteAccount(UUID.randomUUID());
        verify(accountRepository).findById(any());
    }

    @Test
    public void shouldGetAllAccounts() {
        when(accountRepository.findAllAccounts()).thenReturn(List.of(testData.account()));
        List<AccountPresenter> accountPresenters = accountService.getAllAccounts();
        Assertions.assertThat(accountPresenters).isNotEmpty();
    }

    @Test
    public void shouldSearchAccount() {
        when(accountRepository.findByNameIgnoreCase(anyString())).thenReturn(List.of(testData.account()));
        List<AccountPresenter> accountPresenters = accountService.searchAccount("test");
        Assertions.assertThat(accountPresenters).isNotEmpty();
        verify(accountRepository).findByNameIgnoreCase(any());
    }

    @Test
    public void shouldGetAccountsByClient() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(testData.person().getClient()));
        when(accountRepository.findByClient(any())).thenReturn(List.of(testData.account()));
        accountService.getAccountsByClient(UUID.randomUUID());
        verify(clientRepository).findById(any());
    }

    @Test
    public void shouldGetAccountStatus() throws JRException, IOException {
        when(accountRepository.findAccountsMovements(any(), any(), any(), any())).thenReturn(Optional.of(testData.account()));
        when(jasperReportService.getReportWithCollectionDataSource(anyString(), any(), anyCollection())).thenReturn(new byte[1]);
        AccountPresenter accountPresenter = accountService.getAccountStatus(UUID.randomUUID(), AccountType.AHORROS, new Date(), new Date());
        verify(accountRepository).findAccountsMovements(any(), any(), any(), any());
        Assertions.assertThat(accountPresenter.getPdf()).isNotEmpty();
    }
}