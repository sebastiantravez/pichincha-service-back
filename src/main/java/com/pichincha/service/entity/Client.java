package com.pichincha.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue
    private UUID clientId;
    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @NotNull
    private String password;
    @Builder.Default
    private Boolean status = Boolean.TRUE;
}
