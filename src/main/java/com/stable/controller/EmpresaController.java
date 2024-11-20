package com.stable.controller;

import com.stable.infra.conn.EmpresaDAO;
import com.stable.controller.dto.LogarEmpresaRequest;
import com.stable.dominio.Empresa;
import com.stable.dominio.RepositorioEmpresa;
import com.stable.service.EmpresaService;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/empresa")
public class EmpresaController {
    private EmpresaService empresaService;
    private RepositorioEmpresa empresaDAO;
    public EmpresaController(){
        empresaDAO = new EmpresaDAO();
        empresaService = new EmpresaService(empresaDAO);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inserirEmpresa(Empresa empresa){
        try{
            empresaService.inserirEmpresa(empresa);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @POST
    @Path("/logar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logarEmpresa(LogarEmpresaRequest logarEmpresaRequest){
        String login = logarEmpresaRequest.getLogin();
        String senha = logarEmpresaRequest.getSenha();
        try{
            empresaService.logarEmpresa(login,senha);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return Response
                .status(Response.Status.OK)
                .entity(login)
                .build();

    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletarFonteEnergiaEmpresa(
            @QueryParam("login") String login, @QueryParam("energia") String energia
    ){
        try {
            empresaService.deletarFonteEnergiaEmpresa(login, energia);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }


}
