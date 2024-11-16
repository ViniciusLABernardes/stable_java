package com.stable.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FonteEnergia {
    @JsonProperty
    private String tipoFonte;
    public FonteEnergia(){

    }
    public FonteEnergia(String tipoFonte){
        this.tipoFonte = tipoFonte;
    }

    public String getTipoFonte() {
        return tipoFonte;
    }


}
