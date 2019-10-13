package br.valter.leonardo.resource.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cidades {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_cidades")
	private Integer id;
	private String cidade;
	private Integer uf;
	
	public Cidades() {}
	public Cidades(String cidade, Integer uf) {
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
	public Integer getUf() {
		return uf;
	}
	public void setUf(Integer uf) {
		this.uf = uf;
	}
}