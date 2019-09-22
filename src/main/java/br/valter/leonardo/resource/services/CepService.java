package br.valter.leonardo.resource.services;

import java.util.List;

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
		try {
			return (Cep) em.createNativeQuery(" SELECT * FROM CEP WHERE ID = ? ", Cep.class)
					.setParameter(1, id)
					.getSingleResult();			
		} catch (Exception e) {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Cep> searchAll() {
		try {
			List<Cep> resultList = (List<Cep>) em.createNativeQuery(" SELECT * FROM CEP", Cep.class)
					.getResultList();
			return resultList;			
		} catch (Exception e) {
			return null;
		}
	}
}
