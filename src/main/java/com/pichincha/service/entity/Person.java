package com.pichincha.service.entity;

import com.pichincha.service.constraint.ClientValidDni;
import com.pichincha.service.enums.GenderPerson;
import com.pichincha.service.enums.IdentificationPattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ClientValidDni
@Builder
public class Person {
    @Id
    @GeneratedValue
    private UUID personId;
    @NotNull
    private String fullName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private GenderPerson genderPerson;
    @Positive
    private Integer age;
    @NotNull
    private String dni;
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private IdentificationPattern identificationPattern;
    @NotNull
    private String address;
    @NotNull
    private String phone;
    private Date createDate;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Client client;
}
