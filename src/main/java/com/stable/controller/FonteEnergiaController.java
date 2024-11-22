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
    @Path("/{regiao}/{login}")
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
    @Path("/{regiao}/{login}")
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

    @GET
    @Path("/menor-emissor/{regiao}/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response menorEmissorCarbono(
            @PathParam("login") String login,
            @PathParam("regiao") String regiao
    ){
        Consumidor consumidor = null;
        try{
             consumidor = fonteEnergiaService.menorEmissorCarbono(regiao,login);
        }catch (Exception e){
            System.out.println("Erro ao pegar menor emissor de carbono");
        }
        return Response
                .status(Response.Status.OK)
                .entity(consumidor)
                .build();
    }

    @GET
    @Path("/soma-consumo-total/{regiao}/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularGastoEnergeticoTotal(
            @PathParam("login") String login,
            @PathParam("regiao") String regiao
    ){
        double totalEnergia = 0;
        try{
             totalEnergia = fonteEnergiaService.calcularGastoEnergeticoTotal(regiao,login);
        }catch (Exception e){
            System.out.println("Erro ao pegar soma dos niveis");
        }
        return Response
                .status(Response.Status.OK)
                .entity(totalEnergia)
                .build();
    }
}
