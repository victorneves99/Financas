package com.financas.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

@Service
public class AuthService {
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

  public JwtResponse autenticar(LoginRequest loginRequest) {

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());

      JwtResponse response = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
          userDetails.getEmail(),
          roles);

      return response;

    } catch (BadCredentialsException e) {
      throw new RegraNegocioException("Usuario não autenticado, crie um usuario autenticado." + e.getMessage());
    }

  }

  public MessageResponse criarUsuario(SingupRequest singupRequest) {

    if (userRepository.existsByUsername(singupRequest.getUsername())) {
      throw new RegraNegocioException("Username ja existe");
    }

    if (userRepository.existsByEmail(singupRequest.getEmail())) {
      throw new RegraNegocioException("Email ja esta em uso!");
    }

    // Create new user's account
    User user = new User(singupRequest.getUsername(),
        singupRequest.getEmail(),
        encoder.encode(singupRequest.getPassword()));

    Set<String> strRoles = singupRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null || strRoles.isEmpty()) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {

      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return new MessageResponse("Usuario criado e autenticado com sucesso!");

  }

}
