package com.springboot4rest.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.springboot4rest.model.Pessoa;
import com.springboot4rest.repository.PessoaRepository;

@Service
public class PessoaService 
{
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualizarPessoa(Long id, Pessoa pessoa)
	{
		Pessoa pessoaBanco = buscarPessoaPorId(id);
		BeanUtils.copyProperties(pessoa, pessoaBanco, "codigo");
		return pessoaRepository.save(pessoaBanco);

	}


	public Pessoa atualizarPropriedadeAtivo(Long id, boolean ativo) 
	{
		Pessoa pessoaBanco = buscarPessoaPorId(id);
		pessoaBanco.setAtivo(ativo);
		return pessoaRepository.save(pessoaBanco);
	}
	
	private Pessoa buscarPessoaPorId(Long id) {
		Pessoa pessoaBanco = pessoaRepository.findOne(id);
		if(pessoaBanco == null)
			throw new EmptyResultDataAccessException(1);
		return pessoaBanco;
	}
}
