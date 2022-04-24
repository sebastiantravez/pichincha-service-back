package com.pichincha.service.presentation.presenter;

import com.pichincha.service.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountPresenter {
    private UUID accountId;
    @NotNull
    private String accountNumber;
    @NotNull
    private AccountType accountType;
    @NotNull
    private BigDecimal initialAmount;
    @Builder.Default
    private Boolean status = Boolean.FALSE;
    private PersonPresenter personPresenter;
}
