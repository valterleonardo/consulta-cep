package br.valter.leonardo.resource.services;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import br.valter.leonardo.resource.entities.Bairros;
import br.valter.leonardo.resource.entities.Cidades;
import br.valter.leonardo.resource.entities.Estados;
import br.valter.leonardo.resource.entities.Logradouros;

@ApplicationScoped
public class CepService {

	@Inject
	EntityManagerFactory emf;

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
	private List<Cidades> buscarCidades() {
		EntityManager em = emf.createEntityManager();
		try {
			
			List<Cidades> cidades = em.createNativeQuery(
						"select row_number() OVER (ORDER BY est.id) AS id, cad.cidade, est.id as uf " + 
						"from cad_bruto cad " + 
						"join estados est on est.uf = cad.uf " + 
						"group by cad.cidade, cad.uf, est.uf, est.id " + 
						"order by cad.cidade",
						Cidades.class)
						.getResultList();
			
			return cidades;
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar cidades");
			return new ArrayList<Cidades>();
		} finally {
			em.close();
		}	
	}

	private Integer persistirCidades(List<Cidades> cidades) {
		EntityManager em = emf.createEntityManager();
		try {
			
			Integer quantidade = 0;
			em.getTransaction().begin();
			int i=1;
			for (Cidades cidade : cidades) {
				em.persist(new Cidades(cidade.getCidade(), cidade.getUf()));				
				if ((i % 100) == 0) {
					em.flush();
					em.clear();
					i=0;
				}
				
				i++;
				quantidade++;
			}			
			
			em.getTransaction().commit();
			return quantidade;
			
		} catch (Exception e) {
			
			em.getTransaction().rollback();
			System.out.println("rollback Cidades");
			return 0;
			
		} finally {			
			em.close();
		}		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> buscarEstados() {
		EntityManager em = emf.createEntityManager();
		try {
			
			List<String> estados = em.createNativeQuery(
					"select uf from cad_bruto group by uf order by uf")
					.getResultList();
						
			return estados;
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar estados");
			return new ArrayList<String>();
		} finally {
			em.close();
		}
	}
	
	private Integer persistirEstados(List<String> estados) {
		EntityManager em = emf.createEntityManager();
		try {
			
			Integer quantidade = 0;
			em.getTransaction().begin();
			int i=1;
			for (String estado : estados) {				
				em.persist(new Estados(estado));
				
				if ((i % 10) == 0) {
					em.flush();
					em.clear();
					i=0;
				}
				
				i++;
				quantidade++;
			}			
			
			em.getTransaction().commit();
			return quantidade;
			
		} catch (Exception e) {
			
			em.getTransaction().rollback();
			System.out.println("rollback Estados");
			return 0;
			
		} finally {			
			em.close();
		}		
		
	}
	
	@SuppressWarnings("unchecked")
	private List<Bairros> buscarBairros() {
		EntityManager em = emf.createEntityManager();
		try {
			
			List<Bairros> bairros = em.createNativeQuery(
					"select " + 
					"	row_number() OVER (ORDER BY cad.bairro) AS id, " + 
					"	cid.id as cidade, cad.bairro " + 
					"from cad_bruto cad " + 
					"left join cidades cid on cid.cidade = cad.cidade " + 
					"group by cad.cidade, cad.bairro, cid.id",
					Bairros.class)
					.getResultList();
						
			return bairros;
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar bairros");
			return new ArrayList<Bairros>();
		} finally {
			em.close();
		}
	}
	
	private Integer persistirBairros(List<Bairros> bairros) {
		EntityManager em = emf.createEntityManager();
		try {
			
			Integer quantidade = 0;
			em.getTransaction().begin();
			int i=1;
			for (Bairros bairro : bairros) {				
				em.persist(new Bairros(bairro.getCidade(), bairro.getBairro()));
				
				if ((i % 200) == 0) {
					em.flush();
					em.clear();
					i=0;
				}				
				i++;
				quantidade++;
			}			
			
			em.getTransaction().commit();
			return quantidade;
			
		} catch (Exception e) {
			
			em.getTransaction().rollback();
			System.out.println("rollback Bairros");
			return 0;
			
		} finally {			
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Logradouros> buscarLogradouros(String estado) {
		EntityManager em = emf.createEntityManager();
		try {
			
			List<Logradouros> logradouros = em.createNativeQuery(
					"select " + 
					"	row_number() OVER (ORDER BY cad.logradouro) AS id, " + 
					"	cad.logradouro, " + 
					"	(select id from cidades where cidade = cad.cidade and uf = (select id from estados where uf = cad.uf)) as cidade, " + 
					"	(select id from bairros where bairro = cad.bairro and cidade = (select id from cidades where cidade = cad.cidade and uf = (select id from estados where uf = cad.uf))) as bairro, " + 
					"	cad.cep " + 
					"from " + 
					"	cad_bruto cad " + 
					"where " + 
					"	cad.uf = ? " + 					
					"group by " + 
					"	cad.logradouro, " + 
					"	cad.cidade, " + 
					"	cad.bairro, " + 
					"	cad.cep, " + 
					"	cad.uf ",
					Logradouros.class)
					.setParameter(1, estado)
					.getResultList();
						
			return logradouros;
			
		} catch (Exception e) {
			System.out.println("Erro ao buscar bairros");
			return new ArrayList<Logradouros>();
		} finally {
			em.close();
		}
	}
	
	private Integer persistirLogradouros(List<Logradouros> logradouros) {
		EntityManager em = emf.createEntityManager();
		try {
			
			Integer quantidade = 0;
			em.getTransaction().begin();
			int i=1;
			for (Logradouros logradouro : logradouros) {				
				em.persist(new Logradouros(
						logradouro.getLogradouro(),
						logradouro.getCidade(),
						logradouro.getBairro(),
						logradouro.getCep()));
				
				if ((i % 300) == 0) {
					em.flush();
					em.clear();
					i=0;
				}				
				i++;
				quantidade++;
			}						
			em.getTransaction().commit();
			return quantidade;
			
		} catch (Exception e) {
			
			em.getTransaction().rollback();
			System.out.println("rollback Logradouros");
			return 0;
			
		} finally {			
			em.close();
		}
	}

	public void criarEstrutura() {
		
		Integer qtEstados, qtCidades, qtBairros, qtLogradouros;
		
		//Processar Estados
		List<String> estados = buscarEstados();
		qtEstados = persistirEstados(estados);
		System.out.println(qtEstados + "    Estados processados com sucesso");
		
		//Processar Cidades
		List<Cidades> cidades = buscarCidades();
		qtCidades = persistirCidades(cidades);
		System.out.println(qtCidades + "   Cidades processadas com sucesso");
		
		//Processar Bairros
		List<Bairros> bairros = buscarBairros();
		qtBairros = persistirBairros(bairros);
		System.out.println(qtBairros + " Bairros processados com sucesso");
		
		//Processar Logradouros
		for (String estado : estados) {
			List<Logradouros> logradouros = buscarLogradouros(estado);
			qtLogradouros = persistirLogradouros(logradouros);
			System.out.println(qtLogradouros + " Logradouros processados com sucesso para "+estado);			
		}
		
	}
}