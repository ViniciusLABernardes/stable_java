package com.stable.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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

    public static Consumidor menorEmissorCarbono(ArrayList<Consumidor> consumidores) {
        if (consumidores == null || consumidores.isEmpty()) {
            throw new IllegalArgumentException("A lista de consumidores não pode ser nula ou vazia.");
        }

        Consumidor menorConsumidor = null;
        double menorNivel = Double.MAX_VALUE;

        for (Consumidor c : consumidores) {
            double emissaoCarbono = c.getEmissaoCarbono();
            if (emissaoCarbono < menorNivel) {
                menorNivel = emissaoCarbono;
                menorConsumidor = c;
            }
        }

        return menorConsumidor;
    }

    public static double calcularGastoEnergeticoTotal(ArrayList<Consumidor> consumidores){
        double gastototal = 0;
        for(Consumidor c : consumidores){
            gastototal += c.getNivelConsumo();
        }
        return gastototal;
    }

}
