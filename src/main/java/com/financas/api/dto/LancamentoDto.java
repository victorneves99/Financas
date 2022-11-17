package com.financas.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LancamentoDto {

  private Integer id;

  private Integer mes;

  private Integer ano;

  private String descricao;

  private Integer usuario;

  private BigDecimal valor;

  private String tipo;

  private String status;

}
