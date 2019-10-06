package br.valter.leonardo.resource.services.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Logradouros {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "seq_logradouro")
	private Integer id;
	private String logradouro;
	private Integer cidade;
	private Integer bairro;
	private String cep;
	
	public Logradouros() {}
	public Logradouros(String logradouro, Integer cidade, Integer bairro, String cep) {
		super();
		this.logradouro = logradouro;
		this.cidade = cidade;
		this.bairro = bairro;
		this.cep = cep;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public Integer getCidade() {
		return cidade;
	}
	public void setCidade(Integer cidade) {
		this.cidade = cidade;
	}
	public Integer getBairro() {
		return bairro;
	}
	public void setBairro(Integer bairro) {
		this.bairro = bairro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
}