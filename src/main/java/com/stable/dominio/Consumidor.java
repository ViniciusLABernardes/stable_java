package com.stable.dominio;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Consumidor {
    @JsonProperty
    private String nomeConsumidor;
    @JsonProperty
    private double nivelConsumo;
    @JsonProperty
    private int prioridade;
    @JsonProperty
    private String regiao;
    @JsonProperty
    private String status;
    @JsonProperty
    private double emissaoCarbono;
    @JsonProperty
    private String energia;

    public Consumidor(){

    }

    public Consumidor(String nomeConsumidor, double nivelConsumo, int prioridade, String regiao, String status,double emissaoCarbono){
        this.nomeConsumidor = nomeConsumidor;
        this.nivelConsumo = nivelConsumo;
        this.prioridade = prioridade;
        this.regiao = regiao;
        this.status = status;
        this.emissaoCarbono = emissaoCarbono;

    }

    public Consumidor(double nivelConsumo, int prioridade) {
        this.nivelConsumo = nivelConsumo;
        this.prioridade = prioridade;
    }


    public String getNomeConsumidor() {
        return nomeConsumidor;
    }

    public void setNomeConsumidor(String nomeConsumidor) {
        this.nomeConsumidor = nomeConsumidor;
    }

    public double getNivelConsumo() {
        return nivelConsumo;
    }

    public void setNivelConsumo(double nivelConsumo) {
        this.nivelConsumo = nivelConsumo;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getEmissaoCarbono() {
        return emissaoCarbono;
    }

    public void setEmissaoCarbono(double emissaoCarbono) {
        this.emissaoCarbono = emissaoCarbono;
    }

    public String getEnergia() {
        return energia;
    }

    public void setEnergia(String energia) {
        this.energia = energia;
    }



}
