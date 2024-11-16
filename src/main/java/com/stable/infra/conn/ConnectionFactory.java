package com.stable.infra.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public ConnectionFactory(){

    }
    private String urlConexao = "jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL";
    private String usuario = "RM554728";
    private String senha = "080703";

    public Connection pegarConexao(){
        try{
            Connection conexao = DriverManager.getConnection(urlConexao,usuario,senha);
            return conexao;
        }catch(
                SQLException e){
            throw new RuntimeException(e);
        }
    }

}
