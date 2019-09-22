package br.valter.leonardo.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.valter.leonardo.resource.services.CepService;
import br.valter.leonardo.resource.services.entities.Cep;

@Path("/cep")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CepResource {
	
	@Inject
	CepService cepService;
	
    @GET
    @Path(value = "/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String pingPong() {
        return "pong";
    }
    
    @GET
    @Path(value = "/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Cep search() {    	
    	Cep cep = cepService.search(2L);
    	return cep;
    }
    
    @GET
    @Path(value = "/response")
    public Response list() {
    	List<Cep> list = cepService.searchAll();
        return Response.ok(list).build();
    }
}