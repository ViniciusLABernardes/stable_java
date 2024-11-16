package com.stable.service;

import com.stable.dominio.Empresa;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioEmpresa;

public class EmpresaService {
    RepositorioEmpresa repositorioEmpresa;
    public EmpresaService(RepositorioEmpresa repositorioEmpresa){
        this.repositorioEmpresa = repositorioEmpresa;
    }

    public void inserirEmpresa(Empresa empresa){
        repositorioEmpresa.inserirEmpresa(empresa);
        repositorioEmpresa.fecharConexao();
    }
    public  boolean logarEmpresa(String login, String senha){
        if(repositorioEmpresa.logarEmpresa(login, senha)){
            repositorioEmpresa.fecharConexao();
            return true;
        }else{
            repositorioEmpresa.fecharConexao();
            return false;
        }
    }
    public void deletarFonteEnergiaEmpresa(String login, String energia){
        repositorioEmpresa.deletarFonteEnergiaEmpresa(login, energia);
        repositorioEmpresa.fecharConexao();
    }
}
