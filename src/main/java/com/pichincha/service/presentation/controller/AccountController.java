package com.pichincha.service.presentation.controller;

import com.pichincha.service.enums.AccountType;
import com.pichincha.service.presentation.presenter.AccountPresenter;
import com.pichincha.service.presentation.presenter.MovementPresenter;
import com.pichincha.service.presentation.presenter.PersonPresenter;
import com.pichincha.service.service.AccountService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/saveAccount")
    public void savePerson(@RequestBody AccountPresenter accountPresenter) {
        accountService.saveAccount(accountPresenter);
    }

    @PutMapping("/updateAccount")
    public void updatePerson(@RequestBody AccountPresenter accountPresenter) {
        accountService.updateAccount(accountPresenter);
    }

    @GetMapping("/getAllAccounts")
    public List<AccountPresenter> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @DeleteMapping("/deleteAccount")
    public void deleteAccount(@RequestParam UUID accountId) {
        accountService.deleteAccount(accountId);
    }

    @GetMapping("/searchAccount/{value}")
    public List<AccountPresenter> searchAccount(@PathVariable("value") String value) {
        return accountService.searchAccount(value);
    }

    @GetMapping("/getAccountsByClient")
    public List<AccountPresenter> getAccountsByClient(@RequestParam UUID clientId) {
        return accountService.getAccountsByClient(clientId);
    }

    @GetMapping("/getAccountStatusReport")
    public AccountPresenter getReportAccount(@RequestParam UUID clientId, AccountType accountType,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date initDate,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws JRException, IOException {
        return accountService.getAccountStatus(clientId, accountType, initDate, endDate);
    }

}
