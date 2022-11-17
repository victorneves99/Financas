package com.financas.api.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.financas.api.models.enums.StatusLancamento;
import com.financas.api.models.enums.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lancamento {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private Integer mes;

  private Integer ano;

  private String descricao;

  @ManyToOne
  @JoinColumn(name = "id_usuario")
  private User usuario;

  private BigDecimal valor;

  @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
  private LocalDate dataCadastro;

  @Enumerated(EnumType.STRING)
  private TipoLancamento tipo;

  @Enumerated(EnumType.STRING)
  private StatusLancamento status;

}
