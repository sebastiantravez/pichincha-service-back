package com.pichincha.service.entity;

import com.pichincha.service.enums.MovementType;
import com.pichincha.service.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movements {
    @Id
    @GeneratedValue
    private UUID movementId;
    private Date movementDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private MovementType movementType;
    private String observation;
    private BigDecimal movementAmount;
    private BigDecimal balanceAvailable;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
