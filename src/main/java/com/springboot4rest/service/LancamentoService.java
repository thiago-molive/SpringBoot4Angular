package com.springboot4rest.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot4rest.controller.filter.LancamentoFilter;
import com.springboot4rest.controller.projections.LancamentoResumo;
import com.springboot4rest.model.Lancamento;
import com.springboot4rest.model.Pessoa;
import com.springboot4rest.repository.LancamentoRepository;
import com.springboot4rest.repository.PessoaRepository;
import com.springboot4rest.service.exception.PessoaInesxistenteOuInativaException;

@Service
public class LancamentoService 
{
	@Autowired
	private LancamentoRepository lancamentoRepository;
	@Autowired
	private PessoaRepository pessoaRepository;

	public Lancamento alterarLancamento(Long id, Lancamento lancamento) 
	{
		Lancamento lancamentoBanco = buscarLancamentoPorId(id);
		BeanUtils.copyProperties(lancamento, lancamentoBanco, "codigo");
		return lancamentoBanco;
	}

	public Lancamento buscarLancamentoPorId(Long id) 
	{
		Lancamento lancamentoBanco = lancamentoRepository.findOne(id);
		if(lancamentoBanco == null)
			throw new EmptyResultDataAccessException(1);
		return lancamentoBanco;
	}

	public Page<Lancamento> buscarTodos(LancamentoFilter filtro, Pageable pageAble) 
	{
		return lancamentoRepository.filtrar(filtro, pageAble);
	}

	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if(pessoa == null || !pessoa.isAtivo())
			throw new PessoaInesxistenteOuInativaException();
			
		return lancamentoRepository.save(lancamento);
	}

	
	public void deletarLancamento(Long id) 
	{
		Lancamento lancamento = buscarLancamentoPorId(id);
		lancamentoRepository.delete(lancamento);
	}

	public Page<LancamentoResumo> resumo(LancamentoFilter filtro, Pageable pageAble) {
		return lancamentoRepository.resumo(filtro, pageAble);
	}
	
}
