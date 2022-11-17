package com.financas.api.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financas.api.models.Lancamento;
import com.financas.api.models.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Integer> {

  @Query(value = "SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u WHERE u.id = :idUsuario AND l.tipo = :tipo GROUP BY u")
  BigDecimal obterSaldo(@Param("idUsuario") Integer idUsuario, @Param("tipo") TipoLancamento tipo);

}
