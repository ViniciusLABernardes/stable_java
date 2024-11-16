package com.stable.infra.conn;

import com.stable.dominio.Empresa;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioEmpresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpresaDAO implements RepositorioEmpresa {
    private Connection conexao;
    public EmpresaDAO(){
        conexao = new ConnectionFactory().pegarConexao();
    }

    public void inserirEmpresa(Empresa empresa) {

        String comandoInsercao = "INSERT INTO TB_EMPRESA(nome_empresa,cnpj,data_abertura) VALUES (?,?,?)";
        String comandoInsercao2 = "INSERT INTO TB_LOGIN_EMPRESA(id_empresa,login, senha) VALUES (?,?,?)";
        String comandoInsercao3 = "INSERT INTO TB_CONTATO_EMPRESA(id_empresa,telefone,email) VALUES(?,?,?)";
        try {
            PreparedStatement preparandoInsercao = conexao.prepareStatement(comandoInsercao, new String[]{"id_empresa"});
            preparandoInsercao.setString(1, empresa.getNome());
            preparandoInsercao.setString(2, empresa.getCnpj());
            preparandoInsercao.setString(3, empresa.getDataAbertura());

            preparandoInsercao.executeUpdate();

            ResultSet pegandoId = preparandoInsercao.getGeneratedKeys();

            if(pegandoId.next()){
                int idEmpresa = pegandoId.getInt(1);
                PreparedStatement preparandoInsercao2 = null;
                PreparedStatement preparandoInsercao3 = null;
                try {
                    preparandoInsercao2 = conexao.prepareStatement(comandoInsercao2);

                    preparandoInsercao2.setInt(1, idEmpresa);
                    preparandoInsercao2.setString(2, empresa.getLogin());
                    preparandoInsercao2.setString(3, empresa.getSenha());

                    preparandoInsercao2.executeUpdate();

                    preparandoInsercao3 = conexao.prepareStatement(comandoInsercao3);

                    preparandoInsercao3.setInt(1, idEmpresa);
                    preparandoInsercao3.setString(2, empresa.getTelefone());
                    preparandoInsercao3.setString(3, empresa.getEmail());

                    preparandoInsercao3.executeUpdate();


                    System.out.println("Cadastro realizado com sucesso!");
                }finally {
                    pegandoId.close();
                    if (preparandoInsercao2 != null) preparandoInsercao2.close();
                    if (preparandoInsercao3 != null) preparandoInsercao3.close();
                }
            }

            preparandoInsercao.close();

        }

        catch (SQLException e) {
            throw new RuntimeException("Não foi possivel inserir os dados" + e);
        }


    }


    public boolean logarEmpresa(String login, String senha){
        String comandoVerificacao = "SELECT * FROM TB_LOGIN_EMPRESA WHERE login = ? AND senha = ?";

        try {
            PreparedStatement  preparandoEstado = conexao.prepareStatement(comandoVerificacao);
            preparandoEstado.setString(1,login);
            preparandoEstado.setString(2,senha);

            ResultSet resultSet = preparandoEstado.executeQuery();

            if(resultSet.next()){
                System.out.println("Logado com sucesso!");
                preparandoEstado.close();
                resultSet.close();
                return true;

            }else{
                System.out.println("Empresa não encontrada");
                preparandoEstado.close();
                resultSet.close();
                return false;
            }


        } catch (SQLException e ){
            throw new RuntimeException("Erro ao realizar consulta de dados, por favor tente se logar novamente");
        }


    }


    public void fecharConexao(){
        try{
            conexao.close();
        }catch (SQLException e){
            System.out.println("Não foi possivel fechar a conexão com o banco: " + e.getMessage());
        }
    }


    public void deletarFonteEnergiaEmpresa(String login,String energia) {

        String comandoDelecao = "DELETE FROM TB_FONTES_EMPRESA WHERE id_empresa = ? AND id_energia = ?";
        String comandoBuscaEnergia = "SELECT id_energia FROM TB_FONTE_ENERGIA WHERE UPPER(tipo_fonte) = UPPER(?)";
        String comandoBuscaEmpresa = "SELECT id_empresa FROM TB_LOGIN_EMPRESA WHERE UPPER(login) LIKE UPPER(?)";
        try{
            PreparedStatement preparedStatement = conexao.prepareStatement(comandoDelecao);
            PreparedStatement preparandoBusca = conexao.prepareStatement(comandoBuscaEnergia);
            PreparedStatement preparandoBusca2 = conexao.prepareStatement(comandoBuscaEmpresa);


            preparandoBusca.setString(1, energia);
            ResultSet resultSetEnergia = preparandoBusca.executeQuery();

            if (resultSetEnergia.next()) {
                int idEnergia = resultSetEnergia.getInt(1);
                preparedStatement.setInt(2, idEnergia);
            } else {
                throw new RuntimeException("Tipo de fonte de energia não encontrado: " + energia);
            }

            preparandoBusca2.setString(1, login);
            ResultSet resultSetEmpresa = preparandoBusca2.executeQuery();

            if (resultSetEmpresa.next()) {
                int idEmpresa = resultSetEmpresa.getInt(1);
                preparedStatement.setInt(1, idEmpresa);
            } else {
                throw new RuntimeException("Empresa com login: " + login + " não encontrada");
            }

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Energia desvinculada da empresa com sucesso");
            } else {
                System.out.println("Nenhuma energia foi desvinculada, verifique os dados.");
            }

            resultSetEnergia.close();
            resultSetEmpresa.close();
            preparedStatement.close();

        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
