package com.springboot4rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot4rest.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
