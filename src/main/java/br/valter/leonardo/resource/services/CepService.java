package br.valter.leonardo.resource.services;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import br.valter.leonardo.resource.services.entities.Bairros;
import br.valter.leonardo.resource.services.entities.CadBruto;
import br.valter.leonardo.resource.services.entities.Cep;
import br.valter.leonardo.resource.services.entities.Cidades;
import br.valter.leonardo.resource.services.entities.Logradouros;

@ApplicationScoped
public class CepService {

	@Inject
	EntityManagerFactory emf;
	
	@Transactional
	public void createNewCep() {
		EntityManager em = emf.createEntityManager();
		Cep cep = new Cep();
		cep.setBairro("Bairro do Valter");
		cep.setCidade("Cidade do Valter");
		cep.setLogradouro("Rua do Valter");
		cep.setUf("VA");
		em.persist(cep);
	}
	
	public Cep search(Long id) {
		EntityManager em = emf.createEntityManager();
		try {
			return (Cep) em.createNativeQuery(" SELECT * FROM CEP WHERE ID = ? ", Cep.class)
					.setParameter(1, id)
					.getSingleResult();			
		} catch (Exception e) {
			return null;
		} finally {
			em.close();
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Cep> searchAll() {
		EntityManager em = emf.createEntityManager();
		try {
			List<Cep> resultList = (List<Cep>) em.createNativeQuery(" SELECT * FROM CEP", Cep.class)
					.getResultList();
			return resultList;			
		} catch (Exception e) {
			return null;
		} finally {
			em.close();
		}
	}
		
	public void rearranjarCeps(String estado) {
		
		EntityManager em = emf.createEntityManager();
		
		try {
			
			List<CadBruto> cadBrutos = (List<CadBruto>) 
					em.createNativeQuery(
					" SELECT "
					+ " id, cep, uf, "
					+ " replace(replace(trim(cidade),'''',''),' ','') as cidade, "
					+ " replace(replace(trim(bairro),'''',''),' ','') as bairro, "
					+ " replace(replace(trim(logradouro),'''',''),' ','') as logradouro "
					+ " FROM cad_bruto where uf = ?1 ",
					CadBruto.class)
					.setParameter(1, estado)
					.getResultList();
			em.close();					
			
			System.out.println("Adicionando logradouros...."+estado);
			List<Logradouros> listLogradouros = new ArrayList<Logradouros>();
			
			for (CadBruto cadBruto : cadBrutos) {
				
				Integer cidade = getCidade(cadBruto.getCidade());
				Integer bairro = getBairro(cadBruto.getBairro(), cidade);
				
				Logradouros logradouro = new Logradouros(
						cadBruto.getLogradouro(),
						cidade,
						bairro,
						cadBruto.getCep());
				
				listLogradouros.add(logradouro);
			}
			
			System.out.println("Persistindo...."+estado);			

			persistLogradouro(listLogradouros);
			
			System.out.println("Terminou...."+estado);			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Deu zica "+estado);
		} finally {
			em.close();
		}
	}

	@Transactional(value = TxType.REQUIRED)
	private void persistLogradouro(List<Logradouros> logradouros) {
		EntityManager em = emf.createEntityManager();
		try {
			
			em.getTransaction().begin();
			int i=0;
			for (Logradouros logradouro : logradouros) {
				em.persist(logradouro);
				
				if ((i % 200) == 0) {
					em.flush();
					em.clear();
					System.out.println("flush");
				}
			}			
			
			em.getTransaction().commit();
			System.out.println("commit");
			
		} catch (Exception e) {
			
			em.getTransaction().rollback();
			System.out.println("rollback");
			
		} finally {
			
			em.close();
			System.out.println("close");
		}		
	}

	private Integer getBairro(String bairro, Integer cidade) {
		EntityManager em = emf.createEntityManager();
		try {
			Bairros bairroResult = (Bairros) em.createNamedQuery(
					"Bairros.findBairro", Bairros.class)
					.setParameter(1, bairro)
					.setParameter(2, cidade)
					.setHint("org.hibernate.cacheable", Boolean.TRUE)
					.getSingleResult();
			
			return bairroResult.getId();
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar bairro: " + bairro + "  cidade: " + cidade.toString());
			return null;
		} finally {
			em.close();
		}
	}

	private Integer getCidade(String cidade) {
		EntityManager em = emf.createEntityManager();
		try {
			Cidades cidadeResult = (Cidades) em.createNamedQuery(
					"Cidades.findCity", Cidades.class)
					.setParameter(1, cidade)
					.setHint("org.hibernate.cacheable", Boolean.TRUE)
					.getSingleResult();
			
			return cidadeResult.getId();
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar cidade: " + cidade);
			return null;
		} finally {
			em.close();
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Cidades> searchCidadePorEstado(String estado) {
		EntityManager em = emf.createEntityManager();
		try {
			List<Cidades> cidadeResult = (List<Cidades>) 
					em.createNativeQuery(
					" select c.* " + 
					" from cidades c " + 
					" join estados e on e.id = c.uf " + 
					" where e.uf = ?1 ", Cidades.class)
					.setParameter(1, estado)
					.setHint("org.hibernate.cacheable", Boolean.TRUE)
					.getResultList();
			
			return cidadeResult;
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar cidades do estado: " + estado);
			e.printStackTrace();
			return new ArrayList<Cidades>();
		} finally {
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Cidades> searchBairroPorEstado(String estado) {
		EntityManager em = emf.createEntityManager();
		try {
			List<Cidades> cidadeResult = (List<Cidades>) 
					em.createNativeQuery(
					" select c.* " + 
					" from cidades c " + 
					" join estados e on e.id = c.uf " + 
					" where e.uf = ?1 ", Cidades.class)
					.setParameter(1, estado)
					.setHint("org.hibernate.cacheable", Boolean.TRUE)
					.getResultList();
			
			return cidadeResult;
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar cidades do estado: " + estado);
			e.printStackTrace();
			return new ArrayList<Cidades>();
		} finally {
			em.close();
		}
	}
}
