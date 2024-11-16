package com.stable.dominio;

public interface RepositorioConsumidor {
    void inserirConsumidor(Consumidor consumidor,FonteEnergia energia,String login);
    void fecharConexao();
}
