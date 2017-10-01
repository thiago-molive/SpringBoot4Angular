package com.springboot4rest.repository.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.springboot4rest.controller.filter.LancamentoFilter;
import com.springboot4rest.controller.projections.LancamentoResumo;
import com.springboot4rest.model.Categoria_;
import com.springboot4rest.model.Lancamento;
import com.springboot4rest.model.Lancamento_;
import com.springboot4rest.model.Pessoa_;
import com.springboot4rest.repository.SuperRepository.LancamentoRepositoryQuery;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery
{
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter filtro, Pageable pageAble)
	{
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> from = criteria.from(Lancamento.class);
		
		//Restrições
		Predicate[] predicates = restricoes(filtro, builder, from);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionarRestricoesNaQuery(query, pageAble);
		
		
		return new PageImpl<>(query.getResultList(), pageAble, total(filtro));
	}
	//Seta onde começa o primeiro e o maximo de resultados por página
	private void adicionarRestricoesNaQuery(TypedQuery<?> query, Pageable pageAble) 
	{
		query.setFirstResult(pageAble.getPageNumber() * pageAble.getPageSize());
		query.setMaxResults(pageAble.getPageSize());
	}
	//Quantidade total de registros na consulta
	private Long total(LancamentoFilter filtro)
	{
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> from = criteria.from(Lancamento.class);
		
		Predicate[] predicates = restricoes(filtro, builder, from);
		criteria.where(predicates);
		
		criteria.select(builder.count(from));
		
		return manager.createQuery(criteria).getSingleResult();
		
	}
	
	@Override
	public Page<LancamentoResumo> resumo(LancamentoFilter filter, Pageable pageable) 
	{
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LancamentoResumo> criteria = builder.createQuery(LancamentoResumo.class);
		Root<Lancamento> from = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(LancamentoResumo.class, from.get(Lancamento_.codigo)
				, from.get(Lancamento_.descricao)
				, from.get(Lancamento_.dataLancamento)
				, from.get(Lancamento_.dataPagamento)
				, from.get(Lancamento_.valor)
				, from.get(Lancamento_.tipo)
				, from.get(Lancamento_.categoria).get(Categoria_.nome)
				, from.get(Lancamento_.pessoa).get(Pessoa_.nome) ));
		
		//Restrições
		Predicate[] predicates = restricoes(filter, builder, from);
		criteria.where(predicates);
		
		TypedQuery<LancamentoResumo> query = manager.createQuery(criteria);
		adicionarRestricoesNaQuery(query, pageable);
		
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	//Restrições para filtro de pesquisa Lancamentos
	private Predicate[] restricoes(LancamentoFilter filtro, CriteriaBuilder builder, Root<Lancamento> from)
	{
		List<Predicate> predicates = new ArrayList<>();
		if(!StringUtils.isEmpty(filtro.getDescricao()))
		{
			predicates.add(builder.like(builder.lower(from.get(Lancamento_.descricao)), "%" + filtro.getDescricao().toLowerCase() + "%"));
		}
		
		if(filtro.getDataVencimentoDe() != null)
		{
			predicates.add(builder.greaterThanOrEqualTo(from.get(Lancamento_.dataLancamento), filtro.getDataVencimentoDe()));
		}
		
		if(filtro.getDataVencimentoAte() != null)
		{
			predicates.add(builder.lessThanOrEqualTo(from.get(Lancamento_.dataLancamento), filtro.getDataVencimentoAte()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	
}
