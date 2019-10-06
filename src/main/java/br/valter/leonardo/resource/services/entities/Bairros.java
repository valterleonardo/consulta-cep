package br.valter.leonardo.resource.services.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

@Entity
@NamedQuery(name = "Bairros.findBairro",
	query = "SELECT b FROM Bairros b WHERE replace(replace(trim(b.bairro),'''',''),' ','') = ?1 AND b.cidade = ?2",
	hints = @QueryHint(name = "org.hibernate.cacheable", value = "true") )
public class Bairros {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bairros")
	private Integer id;
	private Integer cidade;
	private String bairro;
	
	public Bairros() {}
	public Bairros(Integer cidade, String bairro) {
		this.cidade = cidade;
		this.bairro = bairro; 
	}
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCidade() {
		return cidade;
	}
	public void setCidade(Integer cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
}