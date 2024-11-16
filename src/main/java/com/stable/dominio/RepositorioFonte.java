package com.stable.dominio;

import java.util.ArrayList;

public interface RepositorioFonte {
    void inserirFonte(FonteEnergia fonteEnergia,String login);
    void fecharConexao();
    ArrayList<Consumidor> listarConsumidoresPorRegiao(String regiao, String login);
    void alocarConsumidoresPorEnergia(String regiao, String login);

}
