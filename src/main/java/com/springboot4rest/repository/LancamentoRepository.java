package com.springboot4rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot4rest.model.Lancamento;
import com.springboot4rest.repository.SuperRepository.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery
{

}
