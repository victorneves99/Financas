package com.financas.api.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.api.dto.AtualizaStatusDto;
import com.financas.api.dto.LancamentoDto;
import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.mapper.Mapper;
import com.financas.api.models.Lancamento;
import com.financas.api.models.User;
import com.financas.api.models.enums.StatusLancamento;
import com.financas.api.service.LancamentoService;
import com.financas.api.service.UserService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

  @Autowired
  private LancamentoService service;

  @Autowired
  private Mapper mapper;

  @Autowired
  private UserService userService;

  /**
   * @param descricao
   * @param mes
   * @param ano
   * @param idUsuario
   * @return
   */
  @GetMapping
  public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
      @RequestParam(value = "mes", required = false) Integer mes,
      @RequestParam(value = "ano", required = false) Integer ano, @RequestParam(value = "usuario") Integer idUsuario) {
    try {
      User user = userService.obterPorId(idUsuario);
      Lancamento lancamentoFiltro = new Lancamento();
      lancamentoFiltro.setDescricao(descricao);
      lancamentoFiltro.setAno(ano);
      lancamentoFiltro.setMes(mes);
      lancamentoFiltro.setUsuario(user);

      List<Lancamento> buscarLancamentos = service.buscar(lancamentoFiltro);

      return ResponseEntity.ok(buscarLancamentos);
    } catch (RegraNegocioException e) {

      return ResponseEntity.badRequest().body(e.getMessage());

    }

  }

  @PostMapping()
  public ResponseEntity salvar(@RequestBody LancamentoDto dto) {

    try {
      Lancamento lancamento = mapper.toModel(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(lancamento));

    } catch (RegraNegocioException e) {

      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @PutMapping("/{id}")
  public ResponseEntity atualizar(@RequestBody LancamentoDto dto, @PathVariable("id") Integer id) {

    try {
      Lancamento model = service.obterPorId(id);
      Lancamento lancamento = mapper.toModel(dto);
      model.setId(lancamento.getId());
      service.atualizar(model);
      return ResponseEntity.status(HttpStatus.OK).body(model);
    } catch (RegraNegocioException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

  }

  @DeleteMapping("/{id}")
  public ResponseEntity deletar(@PathVariable("id") Integer id) {

    try {
      Lancamento obterPorId = service.obterPorId(id);
      service.deletar(obterPorId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RegraNegocioException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

  }

  @PutMapping("{id}/atualizar-status")
  public ResponseEntity atualizarStatus(@RequestBody AtualizaStatusDto dto, @PathVariable("id") Integer id) {

    try {
      Lancamento lancamento = service.obterPorId(id);
      service.atualziarStatus(lancamento, dto.getStatus());
      return ResponseEntity.status(HttpStatus.OK).body(lancamento);
    } catch (RegraNegocioException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

}
