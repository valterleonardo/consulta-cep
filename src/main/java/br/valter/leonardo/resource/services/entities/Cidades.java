package br.valter.leonardo.resource.services.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

@Entity
@NamedQuery(name = "Cidades.findCity",
	query = "SELECT c FROM Cidades c WHERE replace(replace(trim(c.cidade),'''',''),' ','') = ?1",
	hints = @QueryHint(name = "org.hibernate.cacheable", value = "true") )
public class Cidades {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cidades")
	private Integer id;
	private String cidade;
	private String uf;
	
	public Cidades() {}
	public Cidades(String cidade, String uf) {
		this.cidade = cidade;
		this.setUf(uf); 
	}
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
}