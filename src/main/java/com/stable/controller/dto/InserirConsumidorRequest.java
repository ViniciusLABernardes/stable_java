package com.stable.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;

public class InserirConsumidorRequest {
    @JsonProperty
    private Consumidor consumidor;
    @JsonProperty
    private FonteEnergia energia;
    @JsonProperty
    private String login;

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public FonteEnergia getEnergia() {
        return energia;
    }

    public String getLogin() {
        return login;
    }
}
