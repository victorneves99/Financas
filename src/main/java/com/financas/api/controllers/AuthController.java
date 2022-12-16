package com.financas.api.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.api.exceptions.RegraNegocioException;
import com.financas.api.models.Role;
import com.financas.api.models.User;
import com.financas.api.models.enums.ERole;
import com.financas.api.payload.request.LoginRequest;
import com.financas.api.payload.request.SingupRequest;
import com.financas.api.payload.response.JwtResponse;
import com.financas.api.payload.response.MessageResponse;
import com.financas.api.repository.RoleRepository;
import com.financas.api.repository.UserRepository;
import com.financas.api.security.jwt.JwtUtils;
import com.financas.api.security.services.UserDetailsImpl;
import com.financas.api.service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      JwtResponse autenticado = authService.autenticar(loginRequest);

      return ResponseEntity.status(HttpStatus.OK).body(autenticado);
    } catch (RegraNegocioException e) {

      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SingupRequest signUpRequest) {

    try {
      MessageResponse criarUsuario = authService.criarUsuario(signUpRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(criarUsuario);
    } catch (RegraNegocioException e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

  }

}
