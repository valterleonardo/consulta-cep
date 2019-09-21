package br.valter.leonardo.resource.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.valter.leonardo.resource.services.entities.Cep;

@ApplicationScoped
public class CepService {

	@Inject
	EntityManager em;
	
	@Transactional
	public void createNewCep() {
		Cep cep = new Cep();
		cep.setBairro("Bairro do Valter");
		cep.setCidade("Cidade do Valter");
		cep.setLogradouro("Rua do Valter");
		cep.setUf("VA");
		em.persist(cep);
	}
	
	public Cep search(Long id) {
		return (Cep) em.createNativeQuery(" SELECT * FROM CEP WHERE ID = ? ", Cep.class)
				.setParameter(1, id)
				.getSingleResult();
	}
}
