package com.financas.api.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.models.User;
import com.financas.api.service.LancamentoService;
import com.financas.api.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/usuarios")
public class UserController {

  @Autowired
  private LancamentoService lancamentoService;

  @GetMapping("{id}/saldo")
  public ResponseEntity obterSaldo(@PathVariable("id") Integer id) {

    try {
      BigDecimal saldo = lancamentoService.obterSaldoUsuario(id);
      return ResponseEntity.status(HttpStatus.OK).body(saldo);
    } catch (RegraNegocioException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

  }

}
