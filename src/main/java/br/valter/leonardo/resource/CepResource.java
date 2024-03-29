package br.valter.leonardo.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.valter.leonardo.resource.entities.Cidades;
import br.valter.leonardo.resource.services.CepService;

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
    @Path(value = "/search/cidade/{estado}")
    public Response searchCidadePorEstado(@PathParam("estado") String estado) {
    	List<Cidades> cidades = cepService.searchCidadePorEstado(estado);
    	if(cidades.size() <= 0) {
    		return Response.noContent().build();
    	}
        return Response.ok(cidades).build();
    }
    
    @GET
    @Path(value = "/cadastrar")
    public Response criarEstrutura() {
    	cepService.criarEstrutura();
        return Response.ok("SUCESSO").build();
    }
    
    
}