package com.stable.controller;

import com.stable.infra.conn.ConsumidorDAO;
import com.stable.controller.dto.InserirConsumidorRequest;
import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioConsumidor;
import com.stable.service.ConsumidorService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/consumidor")
public class ConsumidorController {
    private ConsumidorService consumidorService;
    private RepositorioConsumidor consumidorDAO;
    public ConsumidorController(){
        consumidorDAO = new ConsumidorDAO();
        consumidorService = new ConsumidorService(consumidorDAO);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inserirConsumidor(InserirConsumidorRequest inserirConsumidorRequest){
        Consumidor consumidor = inserirConsumidorRequest.getConsumidor();
        FonteEnergia energia = inserirConsumidorRequest.getEnergia();
        String login = inserirConsumidorRequest.getLogin();
        try{
            consumidorService.inserirConsumidor(consumidor,energia,login);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return Response
                .status(Response.Status.CREATED)
                .build();
    }
}
