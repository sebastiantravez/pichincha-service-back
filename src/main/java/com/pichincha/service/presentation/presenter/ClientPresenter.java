package com.pichincha.service.presentation.presenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientPresenter {
    private UUID clientId;
    private String password;
    @Builder.Default
    private Boolean status = Boolean.TRUE;
}
