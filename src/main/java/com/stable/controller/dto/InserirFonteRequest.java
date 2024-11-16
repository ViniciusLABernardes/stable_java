package com.stable.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stable.dominio.FonteEnergia;

public class InserirFonteRequest {
    @JsonProperty
    private FonteEnergia fonteEnergia;
    @JsonProperty
    private String login;

    public FonteEnergia getFonteEnergia() {
        return fonteEnergia;
    }

    public String getLogin() {
        return login;
    }
}
