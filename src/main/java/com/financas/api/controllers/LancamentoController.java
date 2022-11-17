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

import com.financas.api.dto.LancamentoDto;
import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.mapper.Mapper;
import com.financas.api.models.Lancamento;
import com.financas.api.models.User;
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

    Lancamento lancamentoFiltro = new Lancamento();
    lancamentoFiltro.setDescricao(descricao);
    lancamentoFiltro.setAno(ano);
    lancamentoFiltro.setMes(mes);

    User user = userService.obterPorId(idUsuario);

    if (user.equals(Objects.nonNull(user))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não  encontrado para o ID informado.");
    } else {
      lancamentoFiltro.setUsuario(user);
    }

    List<Lancamento> buscarLancamentos = service.buscar(lancamentoFiltro);

    return ResponseEntity.ok(buscarLancamentos);
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

    return service.obterPorId(id).map(entity -> {
      try {
        Lancamento model = mapper.toModel(dto);
        model.setId(entity.getId());
        service.atualizar(model);
        return ResponseEntity.status(HttpStatus.OK).body(model);
      } catch (RegraNegocioException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

      }

    }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

  }

  @DeleteMapping("/{id}")
  public ResponseEntity deletar(@PathVariable("id") Integer id) {

    return service.obterPorId(id).map(entidade -> {
      service.deletar(entidade);
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

  }

}
