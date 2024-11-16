package com.stable.infra.conn;

import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioFonte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FonteEnergiaDAO implements RepositorioFonte {
    private Connection conexao;
    public FonteEnergiaDAO(){
        conexao = new ConnectionFactory().pegarConexao();
    }
    public void inserirFonte(FonteEnergia fonteEnergia,String login){

        String comandoinsercao1 = "INSERT INTO TB_FONTES_EMPRESA(id_empresa,id_energia) VALUES (?,?)";
        String comandoInsercao2 = "SELECT id_energia FROM TB_FONTE_ENERGIA WHERE UPPER(tipo_fonte) LIKE UPPER(?)";
        String comandoInsercao3 = "SELECT id_empresa FROM TB_LOGIN_EMPRESA WHERE UPPER(login) LIKE UPPER(?)";

        try {
            PreparedStatement preparandoInsercao = conexao.prepareStatement(comandoinsercao1);
            PreparedStatement preparandoInsercao2 = conexao.prepareStatement(comandoInsercao2);
            PreparedStatement preparandoInsercao3 = conexao.prepareStatement(comandoInsercao3);


            preparandoInsercao2.setString(1, fonteEnergia.getTipoFonte());
            ResultSet resultSetEnergia = preparandoInsercao2.executeQuery();

            if (resultSetEnergia.next()) {
                int idEnergia = resultSetEnergia.getInt(1);
                preparandoInsercao.setInt(2, idEnergia);
            } else {
                throw new RuntimeException("Tipo de fonte de energia não encontrado: " + fonteEnergia.getTipoFonte());
            }

            preparandoInsercao3.setString(1, login);
            ResultSet resultSetLogin = preparandoInsercao3.executeQuery();

            if (resultSetLogin.next()) {
                int idEmpresa = resultSetLogin.getInt(1);
                preparandoInsercao.setInt(1, idEmpresa);
            } else {
                throw new RuntimeException("Empresa não encontrada: " + login);
            }


            preparandoInsercao.execute();
            System.out.println("Fonte de energia associada a empresa com sucesso!");


            preparandoInsercao.close();
            preparandoInsercao2.close();
            preparandoInsercao3.close();
            resultSetEnergia.close();
            resultSetLogin.close();

        }catch (SQLException e){
            System.out.println("Houve um erro ao cadastrar a fonte");
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Consumidor> listarConsumidoresPorRegiao(String regiao, String login){
        ArrayList<Consumidor> consumidores = new ArrayList<>();

        String comandoBuscaConsumidores = "SELECT c.nome_consumo,c.nivel_consumo,c.prioridade,c.status," +
                "c.emissao_carbono,f.tipo_fonte,r.regiao " +
                "FROM TB_USO_FONTE c INNER JOIN TB_FONTE_ENERGIA f ON c.id_energia = f.id_energia " +
                "INNER JOIN TB_REGIOES r ON r.id_regiao = c.id_regiao" +
                " WHERE c.id_empresa = ? AND c.id_regiao = ?";
        String comandoInsercao2 = "SELECT id_regiao FROM TB_REGIOES WHERE regiao = ?";
        String comandoInsercao3 = "SELECT id_empresa FROM TB_LOGIN_EMPRESA WHERE UPPER(login) LIKE UPPER(?)";

        try{
            PreparedStatement preparandoBusca = conexao.prepareStatement(comandoBuscaConsumidores);
            PreparedStatement preparandoInsercao3 = conexao.prepareStatement(comandoInsercao3);
            PreparedStatement preparandoInsercao2 = conexao.prepareStatement(comandoInsercao2);


            preparandoInsercao2.setString(1, regiao);
            ResultSet resultSetRegiao = preparandoInsercao2.executeQuery();

            if (resultSetRegiao.next()) {
                int idRegiao = resultSetRegiao.getInt(1);
                preparandoBusca.setInt(2, idRegiao);
            } else {
                throw new RuntimeException("Região não encontrada: " + regiao);
            }


            preparandoInsercao3.setString(1, login);
            ResultSet resultSetLogin = preparandoInsercao3.executeQuery();

            if (resultSetLogin.next()) {
                int idEmpresa = resultSetLogin.getInt(1);
                preparandoBusca.setInt(1, idEmpresa);
            } else {
                throw new RuntimeException("Empresa não encontrada: " + login);
            }

            System.out.println("Região recebida: " + regiao);
            System.out.println("Login recebido: " + login);
            System.out.println("Query executada para id_regiao e id_empresa...");

            ResultSet resultSetBusca = preparandoBusca.executeQuery();
            while(resultSetBusca.next()){
                Consumidor consumidor = new Consumidor();
                consumidor.setNomeConsumidor(resultSetBusca.getString("nome_consumo"));
                consumidor.setNivelConsumo(resultSetBusca.getDouble("nivel_consumo"));
                consumidor.setPrioridade(resultSetBusca.getInt("prioridade"));
                consumidor.setRegiao(resultSetBusca.getString("regiao"));
                consumidor.setEnergia(resultSetBusca.getString("tipo_fonte"));
                consumidor.setStatus(resultSetBusca.getString("status"));
                consumidor.setEmissaoCarbono(resultSetBusca.getDouble("emissao_carbono"));

                consumidores.add(consumidor);
            }


            preparandoBusca.close();
            preparandoInsercao2.close();
            preparandoInsercao3.close();
            resultSetLogin.close();
            resultSetRegiao.close();
            resultSetBusca.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return consumidores;
    }

    public void alocarConsumidoresPorEnergia(String regiao, String login) {

        double somaEolica = 0;
        double somaHidreletrica = 0;
        double somaBiomassa = 0;
        double somaGeotermica = 0;
        double somaSolar = 0;

        ArrayList<Consumidor> consumidoresEolica = new ArrayList<>();
        ArrayList<Consumidor> consumidoresHidreletrica = new ArrayList<>();
        ArrayList<Consumidor> consumidoresBiomassa = new ArrayList<>();
        ArrayList<Consumidor> consumidoresGeotermica = new ArrayList<>();
        ArrayList<Consumidor> consumidoresSolar = new ArrayList<>();

        String comandoBuscaConsumidores = "SELECT c.nivel_consumo, f.tipo_fonte, c.prioridade " +
                "FROM TB_USO_FONTE c " +
                "INNER JOIN TB_FONTE_ENERGIA f ON c.id_energia = f.id_energia " +
                "INNER JOIN TB_REGIOES r ON r.id_regiao = c.id_regiao " +
                "INNER JOIN TB_LOGIN_EMPRESA le ON c.id_empresa = le.id_empresa " +
                "WHERE r.regiao = ? AND le.login = ?";

        String comandoBuscaId = "SELECT id_energia FROM TB_FONTE_ENERGIA WHERE tipo_fonte = ?";
        String comandoUpdate = "UPDATE TB_USO_FONTE SET id_energia = ? WHERE nivel_consumo = ? AND prioridade = ?";

        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(comandoBuscaConsumidores);
            preparedStatement.setString(1, regiao);
            preparedStatement.setString(2, login);

            System.out.println("Executando consulta de consumidores...");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                double nivelConsumo = resultSet.getDouble("nivel_consumo");
                String tipoFonte = resultSet.getString("tipo_fonte");
                int prioridade = resultSet.getInt("prioridade");

                if (tipoFonte.equalsIgnoreCase("Eólica")) {
                    somaEolica += nivelConsumo;
                    consumidoresEolica.add(new Consumidor(nivelConsumo, prioridade));
                } else if (tipoFonte.equalsIgnoreCase("Hidrelétrica")) {
                    somaHidreletrica += nivelConsumo;
                    consumidoresHidreletrica.add(new Consumidor(nivelConsumo, prioridade));
                } else if (tipoFonte.equalsIgnoreCase("Biomassa")) {
                    somaBiomassa += nivelConsumo;
                    consumidoresBiomassa.add(new Consumidor(nivelConsumo, prioridade));
                } else if (tipoFonte.equalsIgnoreCase("Geotérmica")) {
                    somaGeotermica += nivelConsumo;
                    consumidoresGeotermica.add(new Consumidor(nivelConsumo, prioridade));
                } else if (tipoFonte.equalsIgnoreCase("Solar")) {
                    somaSolar += nivelConsumo;
                    consumidoresSolar.add(new Consumidor(nivelConsumo, prioridade));
                }
            }

            resultSet.close();
            preparedStatement.close();

            System.out.println("Somas por fonte: ");
            System.out.println("Eólica: " + somaEolica);
            System.out.println("Hidrelétrica: " + somaHidreletrica);
            System.out.println("Biomassa: " + somaBiomassa);
            System.out.println("Geotérmica: " + somaGeotermica);
            System.out.println("Solar: " + somaSolar);

            if (somaEolica > somaHidreletrica * 1.3) {
                System.out.println("Realocando consumidores de Eólica para Hidrelétrica...");
                System.out.println("Tamanho consumidores Eólica: " + consumidoresEolica.size());
                int consumidoresParaRealocar = (int) Math.ceil(consumidoresEolica.size() * 0.1);
                System.out.println("Consumidores para realocar: " + consumidoresParaRealocar);


                ArrayList<Consumidor> consumidoresARealocar = new ArrayList<>();
                for (int i = consumidoresEolica.size() - 1; i >= consumidoresEolica.size() - consumidoresParaRealocar; i--) {
                    Consumidor consumidor = consumidoresEolica.get(i);
                    consumidoresARealocar.add(consumidor);
                }


                for (Consumidor consumidor : consumidoresARealocar) {
                    PreparedStatement preparedStatementBuscaId = conexao.prepareStatement(comandoBuscaId);
                    preparedStatementBuscaId.setString(1, "Hidrelétrica");

                    ResultSet rsBuscaId = preparedStatementBuscaId.executeQuery();
                    if (rsBuscaId.next()) {
                        int idEnergiaDestino = rsBuscaId.getInt("id_energia");
                        System.out.println("ID da fonte destino (Hidrelétrica): " + idEnergiaDestino);

                        PreparedStatement preparedStatementUpdate = conexao.prepareStatement(comandoUpdate);
                        preparedStatementUpdate.setInt(1, idEnergiaDestino);
                        preparedStatementUpdate.setDouble(2, consumidor.getNivelConsumo());
                        preparedStatementUpdate.setInt(3, consumidor.getPrioridade());

                        int linhasAfetadas = preparedStatementUpdate.executeUpdate();
                        System.out.println("Linhas afetadas no update: " + linhasAfetadas);
                        if (linhasAfetadas > 0) {
                            consumidoresHidreletrica.add(consumidor);  // Adicionar em Hidrelétrica
                            System.out.println("Consumidor adicionado a Hidrelétrica");
                            consumidoresEolica.remove(consumidor);  // Remover de Eólica
                        }

                        preparedStatementUpdate.close();
                    }

                    rsBuscaId.close();
                    preparedStatementBuscaId.close();
                }
            } else {
                System.out.println("Não é necessário a realocação de Eólica para Hidrelétrica");
            }

            if (somaHidreletrica > somaEolica * 1.3) {
                System.out.println("Realocando consumidores de Hidrelétrica para Eólica...");
                System.out.println("Tamanho consumidores Hidrelétrica: " + consumidoresHidreletrica.size());
                int consumidoresParaRealocar = (int) Math.ceil(consumidoresHidreletrica.size() * 0.1);
                System.out.println("Consumidores para realocar: " + consumidoresParaRealocar);


                ArrayList<Consumidor> consumidoresARealocar = new ArrayList<>();
                for (int i = consumidoresHidreletrica.size() - 1; i >= consumidoresHidreletrica.size() - consumidoresParaRealocar; i--) {
                    Consumidor consumidor = consumidoresHidreletrica.get(i);
                    consumidoresARealocar.add(consumidor);
                }

                for (Consumidor consumidor : consumidoresARealocar) {
                    PreparedStatement preparedStatementBuscaId = conexao.prepareStatement(comandoBuscaId);
                    preparedStatementBuscaId.setString(1, "Eólica");

                    ResultSet rsBuscaId = preparedStatementBuscaId.executeQuery();
                    if (rsBuscaId.next()) {
                        int idEnergiaDestino = rsBuscaId.getInt("id_energia");
                        System.out.println("ID da fonte destino (Eólica): " + idEnergiaDestino);

                        PreparedStatement preparedStatementUpdate = conexao.prepareStatement(comandoUpdate);
                        preparedStatementUpdate.setInt(1, idEnergiaDestino);
                        preparedStatementUpdate.setDouble(2, consumidor.getNivelConsumo());
                        preparedStatementUpdate.setInt(3, consumidor.getPrioridade());

                        int linhasAfetadas = preparedStatementUpdate.executeUpdate();
                        System.out.println("Linhas afetadas no update: " + linhasAfetadas);
                        if (linhasAfetadas > 0) {
                            consumidoresEolica.add(consumidor);
                            System.out.println("Consumidor adicionado a Eólica");
                            consumidoresHidreletrica.remove(consumidor);
                        }

                        preparedStatementUpdate.close();
                    }

                    rsBuscaId.close();
                    preparedStatementBuscaId.close();
                }
            } else {
                System.out.println("Não é necessário a realocação de Hidrelétrica para Eólica");
            }


            if (somaBiomassa > somaGeotermica * 1.3) {
                System.out.println("Realocando consumidores de Biomassa para Geotérmica...");
                System.out.println("Tamanho consumidores Biomassa: " + consumidoresBiomassa.size());
                int consumidoresParaRealocar = (int) Math.ceil(consumidoresBiomassa.size() * 0.1);
                System.out.println("Consumidores para realocar: " + consumidoresParaRealocar);


                ArrayList<Consumidor> consumidoresARealocar = new ArrayList<>();
                for (int i = consumidoresBiomassa.size() - 1; i >= consumidoresBiomassa.size() - consumidoresParaRealocar; i--) {
                    Consumidor consumidor = consumidoresBiomassa.get(i);
                    consumidoresARealocar.add(consumidor);
                }


                for (Consumidor consumidor : consumidoresARealocar) {
                    PreparedStatement preparedStatementBuscaId = conexao.prepareStatement(comandoBuscaId);
                    preparedStatementBuscaId.setString(1, "Geotérmica");

                    ResultSet rsBuscaId = preparedStatementBuscaId.executeQuery();
                    if (rsBuscaId.next()) {
                        int idEnergiaDestino = rsBuscaId.getInt("id_energia");
                        System.out.println("ID da fonte destino (Geotérmica): " + idEnergiaDestino);

                        PreparedStatement preparedStatementUpdate = conexao.prepareStatement(comandoUpdate);
                        preparedStatementUpdate.setInt(1, idEnergiaDestino);
                        preparedStatementUpdate.setDouble(2, consumidor.getNivelConsumo());
                        preparedStatementUpdate.setInt(3, consumidor.getPrioridade());

                        int linhasAfetadas = preparedStatementUpdate.executeUpdate();
                        System.out.println("Linhas afetadas no update: " + linhasAfetadas);
                        if (linhasAfetadas > 0) {
                            consumidoresGeotermica.add(consumidor);
                            System.out.println("Consumidor adicionado a Geotérmica");
                            consumidoresBiomassa.remove(consumidor);
                        }

                        preparedStatementUpdate.close();
                    }

                    rsBuscaId.close();
                    preparedStatementBuscaId.close();
                }
            } else {
                System.out.println("Não é necessário a realocação de Biomassa para Geotérmica");
            }


            if (somaGeotermica > somaBiomassa * 1.3) {
                System.out.println("Realocando consumidores de Geotérmica para Biomassa...");
                System.out.println("Tamanho consumidores Geotérmica: " + consumidoresGeotermica.size());
                int consumidoresParaRealocar = (int) Math.ceil(consumidoresGeotermica.size() * 0.1);
                System.out.println("Consumidores para realocar: " + consumidoresParaRealocar);


                ArrayList<Consumidor> consumidoresARealocar = new ArrayList<>();
                for (int i = consumidoresGeotermica.size() - 1; i >= consumidoresGeotermica.size() - consumidoresParaRealocar; i--) {
                    Consumidor consumidor = consumidoresGeotermica.get(i);
                    consumidoresARealocar.add(consumidor);
                }

                for (Consumidor consumidor : consumidoresARealocar) {
                    PreparedStatement preparedStatementBuscaId = conexao.prepareStatement(comandoBuscaId);
                    preparedStatementBuscaId.setString(1, "Biomassa");

                    ResultSet rsBuscaId = preparedStatementBuscaId.executeQuery();
                    if (rsBuscaId.next()) {
                        int idEnergiaDestino = rsBuscaId.getInt("id_energia");
                        System.out.println("ID da fonte destino (Biomassa): " + idEnergiaDestino);

                        PreparedStatement preparedStatementUpdate = conexao.prepareStatement(comandoUpdate);
                        preparedStatementUpdate.setInt(1, idEnergiaDestino);
                        preparedStatementUpdate.setDouble(2, consumidor.getNivelConsumo());
                        preparedStatementUpdate.setInt(3, consumidor.getPrioridade());

                        int linhasAfetadas = preparedStatementUpdate.executeUpdate();
                        System.out.println("Linhas afetadas no update: " + linhasAfetadas);
                        if (linhasAfetadas > 0) {
                            consumidoresBiomassa.add(consumidor);
                            System.out.println("Consumidor adicionado a Biomassa");
                            consumidoresGeotermica.remove(consumidor);
                        }

                        preparedStatementUpdate.close();
                    }

                    rsBuscaId.close();
                    preparedStatementBuscaId.close();
                }
            } else {
                System.out.println("Não é necessário a realocação de Geotérmica para Biomassa");
            }


            if (somaSolar > somaEolica * 1.3) {
                System.out.println("Realocando consumidores de Solar para Eólica...");
                System.out.println("Tamanho consumidores Solar: " + consumidoresSolar.size());
                int consumidoresParaRealocar = (int) Math.ceil(consumidoresSolar.size() * 0.1);
                System.out.println("Consumidores para realocar: " + consumidoresParaRealocar);


                ArrayList<Consumidor> consumidoresARealocar = new ArrayList<>();
                for (int i = consumidoresSolar.size() - 1; i >= consumidoresSolar.size() - consumidoresParaRealocar; i--) {
                    Consumidor consumidor = consumidoresSolar.get(i);
                    consumidoresARealocar.add(consumidor);
                }


                for (Consumidor consumidor : consumidoresARealocar) {
                    PreparedStatement preparedStatementBuscaId = conexao.prepareStatement(comandoBuscaId);
                    preparedStatementBuscaId.setString(1, "Eólica");

                    ResultSet rsBuscaId = preparedStatementBuscaId.executeQuery();
                    if (rsBuscaId.next()) {
                        int idEnergiaDestino = rsBuscaId.getInt("id_energia");
                        System.out.println("ID da fonte destino (Eólica): " + idEnergiaDestino);

                        PreparedStatement preparedStatementUpdate = conexao.prepareStatement(comandoUpdate);
                        preparedStatementUpdate.setInt(1, idEnergiaDestino);
                        preparedStatementUpdate.setDouble(2, consumidor.getNivelConsumo());
                        preparedStatementUpdate.setInt(3, consumidor.getPrioridade());

                        int linhasAfetadas = preparedStatementUpdate.executeUpdate();
                        System.out.println("Linhas afetadas no update: " + linhasAfetadas);
                        if (linhasAfetadas > 0) {
                            consumidoresEolica.add(consumidor);
                            System.out.println("Consumidor adicionado a Eólica");
                            consumidoresSolar.remove(consumidor);
                        }

                        preparedStatementUpdate.close();
                    }

                    rsBuscaId.close();
                    preparedStatementBuscaId.close();
                }
            } else {
                System.out.println("Não é necessário a realocação de Solar para Eólica");
            }


            if (somaEolica > somaSolar * 1.3) {
                System.out.println("Realocando consumidores de Eólica para Solar...");
                System.out.println("Tamanho consumidores Eólica: " + consumidoresEolica.size());
                int consumidoresParaRealocar = (int) Math.ceil(consumidoresEolica.size() * 0.1);
                System.out.println("Consumidores para realocar: " + consumidoresParaRealocar);

                ArrayList<Consumidor> consumidoresARealocar = new ArrayList<>();
                for (int i = consumidoresEolica.size() - 1; i >= consumidoresEolica.size() - consumidoresParaRealocar; i--) {
                    Consumidor consumidor = consumidoresEolica.get(i);
                    consumidoresARealocar.add(consumidor);
                }


                for (Consumidor consumidor : consumidoresARealocar) {
                    PreparedStatement preparedStatementBuscaId = conexao.prepareStatement(comandoBuscaId);
                    preparedStatementBuscaId.setString(1, "Solar");

                    ResultSet rsBuscaId = preparedStatementBuscaId.executeQuery();
                    if (rsBuscaId.next()) {
                        int idEnergiaDestino = rsBuscaId.getInt("id_energia");
                        System.out.println("ID da fonte destino (Solar): " + idEnergiaDestino);

                        PreparedStatement preparedStatementUpdate = conexao.prepareStatement(comandoUpdate);
                        preparedStatementUpdate.setInt(1, idEnergiaDestino);
                        preparedStatementUpdate.setDouble(2, consumidor.getNivelConsumo());
                        preparedStatementUpdate.setInt(3, consumidor.getPrioridade());

                        int linhasAfetadas = preparedStatementUpdate.executeUpdate();
                        System.out.println("Linhas afetadas no update: " + linhasAfetadas);
                        if (linhasAfetadas > 0) {
                            consumidoresSolar.add(consumidor);
                            System.out.println("Consumidor adicionado a Solar");
                            consumidoresEolica.remove(consumidor);
                        }

                        preparedStatementUpdate.close();
                    }

                    rsBuscaId.close();
                    preparedStatementBuscaId.close();
                }
            } else {
                System.out.println("Não é necessário a realocação de Eólica para Solar");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
