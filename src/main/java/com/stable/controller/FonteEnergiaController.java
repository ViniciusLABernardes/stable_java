package com.stable.controller;

import com.stable.infra.conn.FonteEnergiaDAO;
import com.stable.controller.dto.InserirFonteRequest;
import com.stable.dominio.Consumidor;
import com.stable.dominio.FonteEnergia;
import com.stable.dominio.RepositorioFonte;
import com.stable.service.FonteEnergiaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;


@Path("/fonte")
public class FonteEnergiaController {

    private FonteEnergiaService fonteEnergiaService;
    private RepositorioFonte fonteDAO;
    public FonteEnergiaController(){
        fonteDAO = new FonteEnergiaDAO();
        fonteEnergiaService = new FonteEnergiaService(fonteDAO);
    }

    @OPTIONS
    public Response handleCorsPreflight() {
        return Response
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization")
                .header("Access-Control-Allow-Methods", "POST, OPTIONS")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inserirFonte(InserirFonteRequest inserirFonteRequest){
        FonteEnergia fonteEnergia =  inserirFonteRequest.getFonteEnergia();
        String login = inserirFonteRequest.getLogin();
        try{
            fonteEnergiaService.inserirFonte(fonteEnergia,login);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    @Path("/{regiao}-{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response listarConsumidoresPorRegiao(
            @PathParam("login") String login,
            @PathParam("regiao") String regiao
    ){
        ArrayList<Consumidor> consumidores = null;
        try{
           consumidores = fonteEnergiaService.listarConsumidoresPorRegiao(regiao,login);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return Response
                .status(Response.Status.OK)
                .entity(consumidores)
                .build();
    }

    @PUT
    @Path("/{regiao}-{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response alocarConsumidoresPorEnergia(
            @PathParam("login") String login,
            @PathParam("regiao") String regiao
    ){
        try{
            fonteEnergiaService.alocarConsumidoresPorEnergia(regiao, login);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

}
