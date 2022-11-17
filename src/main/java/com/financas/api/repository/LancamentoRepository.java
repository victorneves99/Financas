package com.financas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.api.models.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Integer> {

}
