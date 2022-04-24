package com.pichincha.service.presentation.presenter;

import com.pichincha.service.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementPresenter {
    private UUID movementId;
    private Date movementDate;
    @NotNull
    private MovementType movementType;
    private String observation;
    private BigDecimal amount;
    private BigDecimal balanceAvailable;
    private AccountPresenter accountPresenter;
}
