package com.springboot4rest.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
public class Endereco 
{
	@NotBlank
	@Size(min=3, max=30)
	private String logradouro;
	
	@NotBlank
	@Size(min=1,max=6)
	private String numero;
	
	private String complemento;
	
	@NotBlank
	@Size(min=3, max=30)
	private String bairro;
	
	@NotBlank
	@Size(min=8, max=8)
	private String cep;
	
	@NotBlank
	@Size(min=3, max=20)
	private String cidade;
	
	@NotBlank
	@Size(min=2, max=2)
	private String estado;

	
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
