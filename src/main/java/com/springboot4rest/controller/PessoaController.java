package com.springboot4rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot4rest.Event.RecursoCriadoEvent;
import com.springboot4rest.model.Pessoa;
import com.springboot4rest.repository.PessoaRepository;
import com.springboot4rest.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController 
{
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private PessoaService pessoaService;
	
	@GetMapping
	public ResponseEntity<?> getPessoas()
	{
		List<Pessoa> pessoas = pessoaRepository.findAll();
		return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.noContent().build();
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<Pessoa> salvar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response)
	{
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> getPessoaPorId(@PathVariable Long id)
	{
		return ResponseEntity.ok(pessoaRepository.findOne(id));
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id)
	{
		pessoaRepository.delete(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa)
	{
		Pessoa pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoa);
		return ResponseEntity.ok(pessoaAtualizada);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAtivo(@PathVariable Long id, @RequestBody boolean ativo)
	{
		pessoaService.atualizarPropriedadeAtivo(id, ativo);
	}
}
