package com.financas.api.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.financas.api.dto.LancamentoDto;
import com.financas.api.models.Lancamento;
import com.financas.api.models.User;
import com.financas.api.models.enums.StatusLancamento;
import com.financas.api.models.enums.TipoLancamento;
import com.financas.api.service.UserService;

import io.jsonwebtoken.lang.Objects;
import net.bytebuddy.asm.Advice.Return;

@Component
public class Mapper {

  @Autowired
  private UserService service;

  public Lancamento toModel(LancamentoDto dto) {

    User user = service.obterPorId(dto.getUsuario());

    Lancamento lancamento2 = new Lancamento();
    lancamento2.setId(dto.getId());
    lancamento2.setAno(dto.getAno());
    lancamento2.setDescricao(dto.getDescricao());
    lancamento2.setMes(dto.getMes());
    lancamento2.setUsuario(user);
    lancamento2.setValor(dto.getValor());
    if (dto.getTipo() != null) {
      lancamento2.setTipo(TipoLancamento.valueOf(dto.getTipo()));
    }
    if (dto.getStatus() != null) {
      lancamento2.setStatus(StatusLancamento.valueOf(dto.getStatus()));
    }

    return lancamento2;

  }

}
