package com.springboot4rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot4rest.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> 
{

}
