package com.financas.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financas.api.models.Lancamento;
import com.financas.api.repository.LancamentoRepository;

@Service
public class LancamentoService {

  @Autowired
  private LancamentoRepository lancamentoRepository;

  public Lancamento salvar(Lancamento lancamento) {

    return null;

  }

  public Lancamento atualizar(Lancamento lancamento) {
    return null;

  }

}
