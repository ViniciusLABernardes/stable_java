package com.stable.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Empresa implements Verificador{
    @JsonProperty
    private String nome;
    @JsonProperty
    private String cnpj;
    @JsonProperty
    private String dataAbertura;
    @JsonProperty
    private String email;
    @JsonProperty
    private String telefone;
    @JsonProperty
    private String login;
    @JsonProperty
    private String senha;

    public Empresa(){

    }
    public Empresa(String nome, String cnpj, String dataAbertura,String email,String telefone, String login, String senha){
        this.nome = nome;
        this.cnpj = cnpj;
        this.dataAbertura = dataAbertura;
        this.email = email;
        this.telefone = telefone;
        this.login = login;
        this.senha= senha;
    }


    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public boolean login(boolean logado){
        if(logado){
            System.out.println("Logado");
            return true;
        }else{
            System.out.println("Credenciais incorretas");
            return false;
        }

    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }
}


