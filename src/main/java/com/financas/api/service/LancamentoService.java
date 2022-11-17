package com.financas.api.service;

import java.lang.StackWalker.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.models.Lancamento;
import com.financas.api.models.User;
import com.financas.api.models.enums.StatusLancamento;
import com.financas.api.models.enums.TipoLancamento;
import com.financas.api.repository.LancamentoRepository;

import io.jsonwebtoken.lang.Objects;

@Service
@Transactional
public class LancamentoService {

  @Autowired
  private LancamentoRepository lancamentoRepository;

  @Autowired
  private UserService userService;

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

  public List<Lancamento> buscar(Lancamento lancamento) {
    Example example = Example.of(lancamento,
        ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

    List findAll = lancamentoRepository.findAll(example);

    return findAll;

  }

  public void atualziarStatus(Lancamento lancamento, String status) {

    if (status == null) {
      throw new RegraNegocioException("Nao foi possivel atualizar  o status do lançamento, envie um status valido.");
    }
    StatusLancamento stausString = StatusLancamento.valueOf(status);
    lancamento.setStatus(stausString);

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

  public Lancamento obterPorId(Integer id) {
    try {
      Optional<Lancamento> findById = lancamentoRepository.findById(id);
      if (findById.isPresent()) {
        return findById.get();
      }
    } catch (RegraNegocioException e) {
      throw new RegraNegocioException("Lancamento não encontrado");
    }
    return null;

  }

  public BigDecimal obterSaldoUsuario(Integer id) {

    userService.obterPorId(id);

    BigDecimal receitas = lancamentoRepository.obterSaldo(id, TipoLancamento.RECEITA);
    BigDecimal despesas = lancamentoRepository.obterSaldo(id, TipoLancamento.DESPESA);

    if (receitas == null) {
      receitas = BigDecimal.ZERO;
    }

    if (despesas == null) {
      despesas = BigDecimal.ZERO;
    }

    return receitas.subtract(despesas);

  }

}
