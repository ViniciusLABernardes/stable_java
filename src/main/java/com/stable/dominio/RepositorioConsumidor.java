package com.stable.dominio;

import java.util.ArrayList;

public interface RepositorioConsumidor {
    void inserirConsumidor(Consumidor consumidor,FonteEnergia energia,String login);
    void fecharConexao();
}
