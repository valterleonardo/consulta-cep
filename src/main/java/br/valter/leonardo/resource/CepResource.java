package br.valter.leonardo.resource;

import java.util.Collections;
import java.util.LinkedHashSet;
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
import io.agroal.api.AgroalDataSource;

@Path("/cep")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CepResource {

	@Inject
	AgroalDataSource agroalDataSource;
	
	@Inject
	CepService cepService;
	
    private Set<Cep> ceps = Collections.synchronizedSet(new LinkedHashSet<>());
	
	public CepResource() {
        ceps.add(new Cep("M1", "Valter", "Valter", "Valter"));
        ceps.add(new Cep("M2", "Valter2", "Valter2", "Valter2"));
    }
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
    	
    	cepService.createNewCep();
        return "hello";
    }
    
    @GET
    @Path(value = "/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Cep search() {    	
        return cepService.search(1L);
    }
    
    @GET
    @Path(value = "/response")
    public Response list() {
        return Response.ok(ceps).build();
    }
}