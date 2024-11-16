package com.stable.dominio;

public interface RepositorioEmpresa{
    void inserirEmpresa(Empresa empresa);
    boolean logarEmpresa(String login, String senha);
    void fecharConexao();
    void deletarFonteEnergiaEmpresa(String login, String energia);
}
