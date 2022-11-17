package com.financas.api.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.models.Lancamento;
import com.financas.api.models.enums.StatusLancamento;
import com.financas.api.repository.LancamentoRepository;

import io.jsonwebtoken.lang.Objects;

@Service
@Transactional
public class LancamentoService {

  @Autowired
  private LancamentoRepository lancamentoRepository;

  public Lancamento salvar(Lancamento lancamento) {
    validar(lancamento);
    lancamento.setStatus(StatusLancamento.PENDENTE);
    Lancamento save = lancamentoRepository.save(lancamento);

    return save;

  }

  public Lancamento atualizar(Lancamento lancamento) {
    validar(lancamento);

    java.util.Objects.requireNonNull(lancamento.getId());

    Lancamento atualizaLancamento = lancamentoRepository.save(lancamento);

    return atualizaLancamento;

  }

  public void deletar(Lancamento lancamento) {

    java.util.Objects.requireNonNull(lancamento.getId());

    lancamentoRepository.delete(lancamento);

  }

  List<Lancamento> buscar(Lancamento lancamento) {
    Example example = Example.of(lancamento,
        ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

    List findAll = lancamentoRepository.findAll(example);

    return findAll;

  }

  public void atualziarStatus(Lancamento lancamento, StatusLancamento status) {

    lancamento.setStatus(status);

    atualizar(lancamento);

  }

  public void validar(Lancamento lancamento) {

    if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
      throw new RegraNegocioException("Informe uma descrição valida.");
    }
    if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
      throw new RegraNegocioException("Informe um mes valido.");
    }
    if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
      throw new RegraNegocioException("Informe  um ano valido .");
    }
    if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
      throw new RegraNegocioException("Informe  um usuario.");
    }
    if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
      throw new RegraNegocioException("Informe um valor valido.");
    }
    if (lancamento.getTipo() == null) {
      throw new RegraNegocioException("Informe um tipo lançamento.");
    }

  }

}
