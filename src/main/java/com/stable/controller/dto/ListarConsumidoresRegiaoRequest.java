package com.stable.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListarConsumidoresRegiaoRequest {
    @JsonProperty
    private String regiao;
    @JsonProperty
    private String login;

    public String getLogin() {
        return login;
    }

    public String getRegiao() {
        return regiao;
    }
}
