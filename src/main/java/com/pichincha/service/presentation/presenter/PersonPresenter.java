package com.pichincha.service.presentation.presenter;

import com.pichincha.service.enums.GenderPerson;
import com.pichincha.service.enums.IdentificationPattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonPresenter {
    private UUID personId;
    @NotNull
    private String fullName;
    @NotNull
    private GenderPerson genderPerson;
    @Positive
    private Integer age;
    @NotNull
    private String dni;
    @NotNull
    private IdentificationPattern identificationPattern;
    @NotNull
    private String address;
    @NotNull
    private String phone;
    private ClientPresenter clientPresenter;
}
