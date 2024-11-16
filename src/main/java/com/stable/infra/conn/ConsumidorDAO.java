package com.stable.infra.conn;

import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioConsumidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsumidorDAO implements RepositorioConsumidor {
    private Connection conexao;

    public ConsumidorDAO(){
        conexao = new ConnectionFactory().pegarConexao();
    }

    public void inserirConsumidor(Consumidor consumidor, FonteEnergia energia,String login) {

        String comandoInsercao = "INSERT INTO TB_USO_FONTE(id_empresa,id_energia,id_regiao,nivel_consumo,nome_consumo,prioridade,status,emissao_carbono) VALUES (?,?,?,?,?,?,?,?)";
        String comandoInsercao2 = "SELECT id_energia FROM TB_FONTE_ENERGIA WHERE UPPER(tipo_fonte) LIKE UPPER(?)";
        String comandoInsercao3 = "SELECT id_regiao FROM TB_REGIOES WHERE UPPER(regiao) LIKE UPPER(?)";
        String comandoInsercao4 = "SELECT id_empresa FROM TB_LOGIN_EMPRESA WHERE UPPER(login) LIKE UPPER(?)";

        try {

            PreparedStatement preparandoInsercao = conexao.prepareStatement(comandoInsercao, new String[]{"id_uso_fonte"});
            PreparedStatement preparandoInsercao2 = conexao.prepareStatement(comandoInsercao2);
            PreparedStatement preparandoInsercao3 = conexao.prepareStatement(comandoInsercao3);
            PreparedStatement preparandoInsercao4 = conexao.prepareStatement(comandoInsercao4);


            preparandoInsercao4.setString(1, login);
            ResultSet resultSetLogin = preparandoInsercao4.executeQuery();

            if (resultSetLogin.next()) {
                int idEmpresa = resultSetLogin.getInt(1);
                preparandoInsercao.setInt(1, idEmpresa);
            } else {
                throw new RuntimeException("Empresa não encontrada: " + login);
            }



            preparandoInsercao2.setString(1, energia.getTipoFonte());
            ResultSet resultSetEnergia = preparandoInsercao2.executeQuery();

            if (resultSetEnergia.next()) {
                int idEnergia = resultSetEnergia.getInt(1);
                preparandoInsercao.setInt(2, idEnergia);
            } else {
                throw new RuntimeException("Tipo de fonte de energia não encontrado: " + energia.getTipoFonte());
            }

            preparandoInsercao3.setString(1,"%" + consumidor.getRegiao() + "%");
            ResultSet resultSetRegiao = preparandoInsercao3.executeQuery();

            if (resultSetRegiao.next()) {
                int idRegiao = resultSetRegiao.getInt(1);
                preparandoInsercao.setInt(3, idRegiao);
            } else {
                throw new RuntimeException("Região não encontrada: " + consumidor.getRegiao());
            }



            preparandoInsercao.setDouble(4, consumidor.getNivelConsumo());
            preparandoInsercao.setString(5, consumidor.getNomeConsumidor());
            preparandoInsercao.setInt(6, consumidor.getPrioridade());
            preparandoInsercao.setString(7, consumidor.getStatus());
            preparandoInsercao.setDouble(8,consumidor.getEmissaoCarbono());

            preparandoInsercao.executeUpdate();

            System.out.println("Cadastro de consumidor realizado com sucesso!");

            preparandoInsercao2.close();
            preparandoInsercao3.close();
            preparandoInsercao.close();
            resultSetRegiao.close();
            resultSetLogin.close();
            resultSetEnergia.close();

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível inserir os dados: " + e.getMessage(), e);
        }
    }

    public void fecharConexao(){
        try{
            conexao.close();
        }catch (SQLException e){
            System.out.println("Não foi possivel fechar a conexão com o banco: " + e.getMessage());
        }
    }
}
