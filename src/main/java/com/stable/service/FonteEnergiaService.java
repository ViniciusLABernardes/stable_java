package com.stable.service;


import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioFonte;

import java.util.ArrayList;

public class FonteEnergiaService {
    RepositorioFonte repositorioFonte;
    public FonteEnergiaService(RepositorioFonte repositorioFonte){
        this.repositorioFonte = repositorioFonte;
    }

    public void inserirFonte(FonteEnergia fonte,String login){
        repositorioFonte.inserirFonte(fonte,login);
        repositorioFonte.fecharConexao();
    }
    public ArrayList<Consumidor> listarConsumidoresPorRegiao(String regiao, String login){
        ArrayList<Consumidor> consumidores = repositorioFonte.listarConsumidoresPorRegiao(regiao, login);
        repositorioFonte.fecharConexao();
        return consumidores;

    }

    public void alocarConsumidoresPorEnergia(String regiao, String login){
        repositorioFonte.alocarConsumidoresPorEnergia(regiao, login);
        repositorioFonte.fecharConexao();
        //Nosso objetivo na verdade seria nunca fechar esta conexão, já que ela estaria conectada 24 horas as fontes de energia
    }

    public Consumidor menorEmissorCarbono(String regiao, String login) {
        ArrayList<Consumidor> consumidores = repositorioFonte.listarConsumidoresPorRegiao(regiao, login);
        repositorioFonte.fecharConexao();

        return FonteEnergia.menorEmissorCarbono(consumidores);
    }
    public double calcularGastoEnergeticoTotal(String regiao, String login){
        ArrayList<Consumidor> consumidores = repositorioFonte.listarConsumidoresPorRegiao(regiao, login);
        repositorioFonte.fecharConexao();

        return FonteEnergia.calcularGastoEnergeticoTotal(consumidores);
    }
}
