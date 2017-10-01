package com.springboot4rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot4rest.Event.RecursoCriadoEvent;
import com.springboot4rest.model.Categoria;
import com.springboot4rest.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController 
{
	@Autowired
	private CategoriaRepository categoriaRepositoty;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping()
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<?> BuscarTodos()
	{
		List<Categoria> categorias = categoriaRepositoty.findAll();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
	}
	
	@PostMapping()
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> salvar(@Valid @RequestBody Categoria categoria, HttpServletResponse response)
	{
		//Salva a categoria no banco
		Categoria categoriaSalva = categoriaRepositoty.save(categoria);
		//seta o código da categoria salva da requisição atual no Header.Location
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<?> getCategoria(@PathVariable Long id)
	{
		Categoria categoria = categoriaRepositoty.findOne(id);
		return ResponseEntity.ok(categoria);
	}
	
}
