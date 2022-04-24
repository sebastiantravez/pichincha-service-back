package com.pichincha.service.entity;

import com.pichincha.service.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue
    private UUID bankAccountId;
    @NotNull
    @Column(unique = true)
    private String accountNumber;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @PositiveOrZero
    private BigDecimal initialAmount;
    @Builder.Default
    private Boolean status = Boolean.FALSE;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
