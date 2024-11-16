package com.stable.service;

import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioConsumidor;

public class ConsumidorService {
    RepositorioConsumidor repositorioConsumidor;
    public ConsumidorService(RepositorioConsumidor repositorioConsumidor){
        this.repositorioConsumidor = repositorioConsumidor;
    }

    public void inserirConsumidor(Consumidor consumidor, FonteEnergia energia,String login){
        repositorioConsumidor.inserirConsumidor(consumidor,energia,login);
        repositorioConsumidor.fecharConexao();
    }
}
