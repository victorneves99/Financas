package com.financas.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.models.User;
import com.financas.api.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  public User obterPorId(Integer id) {

    User findById = repository.findById(id).orElseThrow(() -> new RegraNegocioException("Usuario nao encontrado."));

    return findById;

  }

}
