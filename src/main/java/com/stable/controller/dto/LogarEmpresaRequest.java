package com.stable.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogarEmpresaRequest {
    @JsonProperty
    private String login;
    @JsonProperty
    private String senha;

    public String getSenha() {
        return senha;
    }

    public String getLogin() {
        return login;
    }
}
