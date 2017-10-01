package com.springboot4rest.repository.SuperRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot4rest.controller.filter.LancamentoFilter;
import com.springboot4rest.controller.projections.LancamentoResumo;
import com.springboot4rest.model.Lancamento;

public interface LancamentoRepositoryQuery 
{
	public Page<Lancamento> filtrar(LancamentoFilter filtro, Pageable pageAble);
	public Page<LancamentoResumo> resumo(LancamentoFilter filter, Pageable pageable);
}
