package com.springboot4rest.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.springboot4rest.controller.filter.LancamentoFilter;
import com.springboot4rest.controller.projections.LancamentoResumo;
import com.springboot4rest.model.Lancamento;
import com.springboot4rest.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController 
{
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private LancamentoService lancamentoService;
	
	@GetMapping
	public ResponseEntity<?> buscarTodos(LancamentoFilter filtro, Pageable pageAble)
	{
		return ResponseEntity.ok(lancamentoService.buscarTodos(filtro, pageAble));
	}

	@GetMapping(params = "resumo")
	public Page<LancamentoResumo> resumo(LancamentoFilter filtro, Pageable pageAble)
	{
		return lancamentoService.resumo(filtro, pageAble);
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> salvarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response)
	{
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()) );
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> BuscarLancamentoPorId(@PathVariable Long id)
	{
		return ResponseEntity.ok(lancamentoService.buscarLancamentoPorId(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deletarLancamento(@PathVariable Long id)
	{
		lancamentoService.deletarLancamento(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Lancamento> alterarLancamento(@PathVariable Long id, @Valid @RequestBody Lancamento lancamento)
	{
		Lancamento lancamentoBanco = lancamentoService.alterarLancamento(id, lancamento);
		return ResponseEntity.ok(lancamentoBanco);
	}
}
